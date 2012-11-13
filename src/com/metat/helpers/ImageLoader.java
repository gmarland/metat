package com.metat.helpers;

import java.util.HashMap;

import com.metat.dataaccess.ContactDataAccess;
import com.metat.models.Contact;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class ImageLoader {
	ContactDataAccess _contactDataAccess;
	
	private Context _context;
    private HashMap<String, Bitmap> _cache = new HashMap<String, Bitmap>();
    
    public ImageLoader(Context context){
    	_context = context;
    	_contactDataAccess = new ContactDataAccess(_context);
    }
    
    public void DisplayImage(final long contactId, final String meetupId, final ImageView imageView)
    {
        if(_cache.containsKey(meetupId))
        {
            if((imageView.getTag()!=null) && (imageView.getTag().equals(meetupId)))
            {
            	imageView.setImageBitmap(_cache.get(meetupId));
            }
        }
        
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                _cache.put(meetupId, (Bitmap) message.obj);
                imageView.setImageBitmap((Bitmap) message.obj);
            }
        };

        Thread thread = new Thread() {
            @Override
            public void run() {
                Contact contact = _contactDataAccess.getContact(contactId);;

                if (contact.getPhotoThumbnail().length > 0)
                {
	                Bitmap bitmap = ImagesHelper.ResampleImageFileToBitmap(contact.getPhotoThumbnail(), ImagesHelper.THUMBNAIL_SIZE);
	                
	                Message message = handler.obtainMessage(1, bitmap);
	                handler.sendMessage(message);
                }
            }
        };
        
        thread.start();
    }
}

