package com.metat.helpers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.apache.http.util.ByteArrayBuffer;

import com.metat.dataaccess.ContactDataAccess;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ImageDownloader {
	ContactDataAccess _contactDataAccess;
	
	private Context _context;
    private HashMap<String, Bitmap> _cache = new HashMap<String, Bitmap>();
    
    public ImageDownloader(Context context){
    	_context = context;
    	_contactDataAccess = new ContactDataAccess(_context);
    }
    
    public void DisplayImage(final String meetupId, final String imageLocation, final ImageView imageView)
    {
        if(_cache.containsKey(meetupId))
        {
            if((imageView.getTag()!=null) && (imageView.getTag().equals(meetupId)))
            {
                if (imageView.getTag().equals(meetupId))
                {
                	imageView.setImageBitmap(_cache.get(meetupId));
                }
            }
        }
        
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                _cache.put(meetupId, (Bitmap) message.obj);
                
                if (imageView.getTag().equals(meetupId))
                {
                	imageView.setImageBitmap((Bitmap) message.obj);
                }
            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {
        		byte[] downloadedImage = new byte[0];
    		
    			URL imageUrl;
    			URLConnection imageConnection;
    			InputStream inputStream;
    			
    			String[] urlParts = imageLocation.split("/");
    			
    			if (urlParts.length == 0)
    			{
    				return;
    			}
    			
    			if (!urlParts[urlParts.length-1].contains("."))
    			{
    				return;
    			}
    			
    			try
    			{
    				imageUrl = new URL(imageLocation);
    			}
    			catch (Exception ex)
    			{
    				return;
    			}
    			
    			try
    			{
    				imageConnection = imageUrl.openConnection();
    				inputStream = imageConnection.getInputStream();
    			}
    			catch (IOException ex)
    			{
    				return;
    			}
    			
    			BufferedInputStream bis = new BufferedInputStream(inputStream);
    			ByteArrayBuffer baf = new ByteArrayBuffer(50);
    		
    			int current = 0;

    			try
    			{
                    while ((current = bis.read()) != -1) 
                    {
                        baf.append((byte) current);
                    }
    			}
    			catch (IOException ex)
    			{
    				return;
    			}
    			
    			if (baf.isEmpty())
    			{
    				return;
    			}
    			
    			downloadedImage = baf.toByteArray();
    			
    			try
    			{
    				bis.close();
    			}
    			catch (Exception ex)
    			{
    				bis = null;
    			}
    			
    			try
    			{
    				inputStream.close();
    			}
    			catch (Exception ex)
    			{
    				inputStream = null;
    			}
    			
    			baf.clear();
    			imageConnection = null;
    			baf = null;
    			
                Bitmap bitmap = ImagesHelper.ResampleImageFileToBitmap(downloadedImage, ImagesHelper.THUMBNAIL_SIZE);
                
                Message message = handler.obtainMessage(1, bitmap);
                handler.sendMessage(message);
            }
        };
        
        thread.start();
    }
}

