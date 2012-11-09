package com.metat.helpers;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImagesHelper {
	public static final int THUMBNAIL_SIZE = 80;
	
    public static Bitmap ImageFileToBitmap(byte[] originalFile)
    {
	    return BitmapFactory.decodeByteArray(originalFile, 0, originalFile.length);
    }
	
    public static Bitmap ResampleImageFileToBitmap(byte[] originalFile, int maxSize)
    {
    	BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options(); 
	    onlyBoundsOptions.inJustDecodeBounds = true; 
	    onlyBoundsOptions.inDither=true;
	    onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;
	    BitmapFactory.decodeByteArray(originalFile, 0, originalFile.length, onlyBoundsOptions);
	
	    int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth; 
	
	    double ratio = (originalSize > maxSize) ? (originalSize / maxSize) : 1.0; 
	
	    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options(); 
	    bitmapOptions.inSampleSize = ImagesHelper.getPowerOfTwoForSampleRatio(ratio); 
	    bitmapOptions.inDither=true;
	    bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;

	    return BitmapFactory.decodeByteArray(originalFile, 0, originalFile.length, bitmapOptions);
    }
    
    public static byte[] ResampleImageFileToData(byte[] originalFile, int maxSize)
    {
    	BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options(); 
	    onlyBoundsOptions.inJustDecodeBounds = true; 
	    onlyBoundsOptions.inDither=true;
	    onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;
	    BitmapFactory.decodeByteArray(originalFile, 0, originalFile.length, onlyBoundsOptions);

		if ((onlyBoundsOptions.outHeight == -1) || (onlyBoundsOptions.outHeight == -1))
			return null;
		
	    int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth; 
	
	    double ratio = (originalSize > maxSize) ? (originalSize / maxSize) : 1.0; 
	
	    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options(); 
	    bitmapOptions.inSampleSize = ImagesHelper.getPowerOfTwoForSampleRatio(ratio); 
	    bitmapOptions.inDither=true;
	    bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;
	    
	    Bitmap bitmap = BitmapFactory.decodeByteArray(originalFile, 0, originalFile.length, bitmapOptions);
	    ByteArrayOutputStream stream = new ByteArrayOutputStream(); 
	    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); 
	    
	    return stream.toByteArray(); 
    }
 
    public static int getPowerOfTwoForSampleRatio(double ratio){ 
        int k = Integer.highestOneBit((int)Math.floor(ratio)); 
        if(k==0) return 1; 
        else return k; 
    }
}
