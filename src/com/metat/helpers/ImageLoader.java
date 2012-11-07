package com.metat.helpers;

import java.io.File;
import java.util.HashMap;
import java.util.Stack;

import com.example.metat.R;
import com.metat.dataaccess.ContactDataAccess;
import com.metat.models.Contact;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class ImageLoader {
	ContactDataAccess _contactDataAccess;
	
	private Context _context;
    private HashMap<String, Bitmap> _cache = new HashMap<String, Bitmap>();
    
    private File _cacheDir;
    
    public ImageLoader(Context context){
    	_context = context;
    	_contactDataAccess = new ContactDataAccess(_context);
    	
        //Make the background thread low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
        
        _cacheDir = context.getCacheDir();
        
        if(!_cacheDir.exists())
            _cacheDir.mkdirs();
    }

    final int stub_id = R.drawable.profile_default;
    
    public void DisplayImage(long contactId, String meetupId, ImageView imageView)
    {
        if(_cache.containsKey(meetupId))
            imageView.setImageBitmap(_cache.get(meetupId));
        else
        {
            queuePhoto(contactId, meetupId, (Activity)_context, imageView);
            imageView.setImageResource(stub_id);
        }    
    }
        
    private void queuePhoto(long contactId, String meetupId, Activity activity, ImageView imageView)
    {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them. 
        photosQueue.Clean(imageView);
        
        PhotoToLoad p = new PhotoToLoad(contactId, meetupId, imageView);
        
        synchronized(photosQueue.photosToLoad){
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }
        
        //start thread if it's not started yet
        if(photoLoaderThread.getState()==Thread.State.NEW)
            photoLoaderThread.start();
    }
    
    private Contact getContact(long contactId) 
    {
		return _contactDataAccess.getContact(contactId);
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeImage(Contact contact){
    	return ImagesHelper.ResampleImageFileToBitmap(contact.getPhotoThumbnail(), ImagesHelper.THUMBNAIL_SIZE);
    }

    //Task for the queue
    private class PhotoToLoad
    {
        public long ContactId;
        public String MeetupId;
        public ImageView ImageView;
        
        public PhotoToLoad(long id, String meetupId, ImageView i){
        	ContactId = id; 
        	MeetupId = meetupId; 
        	ImageView = i;
        }
    }
    
    PhotosQueue photosQueue=new PhotosQueue();
    
    public void stopThread()
    {
        photoLoaderThread.interrupt();
    }
    
    //stores list of photos to download
    class PhotosQueue
    {
        private Stack<PhotoToLoad> photosToLoad=new Stack<PhotoToLoad>();
        
        public void Clean(ImageView image)
        {
            for(int j=0 ;j<photosToLoad.size();){
                if(photosToLoad.get(j).ImageView == image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
    }
    
    class PhotosLoader extends Thread {
        public void run() {
            try {
                while(true)
                {
                    if(photosQueue.photosToLoad.size()==0)
                    {
                        synchronized(photosQueue.photosToLoad){
                            photosQueue.photosToLoad.wait();
                        }
                    }
                    
                    if(photosQueue.photosToLoad.size()!=0)
                    {
                        PhotoToLoad photoToLoad;
                        
                        synchronized(photosQueue.photosToLoad){
                            photoToLoad=photosQueue.photosToLoad.pop();
                        }
                        
                        Contact contact = getContact(photoToLoad.ContactId);
                        
                        if ((contact != null) && (contact.getPhotoThumbnail().length > 0))
                        {
	                        Bitmap bmp = decodeImage(contact);
	                        _cache.put(photoToLoad.MeetupId, bmp);
	                        Object tag = photoToLoad.ImageView.getTag();
	
	                        if(tag!=null && ((String)tag).equals(photoToLoad.MeetupId)){
	                            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad.ImageView);
	                            Activity a = (Activity)_context;
	                            a.runOnUiThread(bd);
	                        }
                        }
                    }
                    
                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
            }
        }
    }
    
    PhotosLoader photoLoaderThread = new PhotosLoader();
    
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        ImageView imageView;
        public BitmapDisplayer(Bitmap b, ImageView i){bitmap=b;imageView=i;}
        public void run()
        {
            if(bitmap!=null)
                imageView.setImageBitmap(bitmap);
            else
                imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        _cache.clear();
        
        File[] files=_cacheDir.listFiles();
        
        for(File f:files)
            f.delete();
    }
}

