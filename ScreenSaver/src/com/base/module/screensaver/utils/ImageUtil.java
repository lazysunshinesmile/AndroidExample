/****************************************************************************
 *
 * FILENAME:        com.learn.urlpic.ImageUtil.java
 *
 * LAST REVISION:   $Revision: 1.0
 * LAST MODIFIED:   $Date: 2012-1-30
 *
 * DESCRIPTION:     The class encapsulates the music ring tone operations.
 *
 * vi: set ts=4:
 *
 * Copyright (c) 2009-2011 by Grandstream Networks, Inc.
 * All rights reserved.
 *
 * This material is proprietary to Grandstream Networks, Inc. and,
 * in addition to the above mentioned Copyright, may be
 * subject to protection under other intellectual property
 * regimes, including patents, trade secrets, designs and/or
 * trademarks.
 *
 * Any use of this material for any purpose, except with an
 * express license from Grandstream Networks, Inc. is strictly
 * prohibited.
 *
 ***************************************************************************/
package com.base.module.screensaver.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * ClassName:ImageUtil
 * @Description: 
 */

public class ImageUtil {
	public static Bitmap parse2Bitmap(byte[] data) {
		if (data.length != 0) {
		    Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
		    float sh=bm.getHeight()/272.0f;
		    float sw=bm.getWidth()/480.0f;
		    if(sh<1e-6&&sw<1e-6) {
		        return bm;
		    }
		    float s=Math.max(sh, sw);
			Bitmap bitmap = Bitmap.createScaledBitmap(bm, (int)(bm.getWidth()/s),
			        (int)(bm.getHeight()/s), false);
			bm.recycle();
			return bitmap;
		//    return BitmapFactory.decodeByteArray(data, 0, data.length);
		} else {
			return null;
		}
	}
	
	public static Bitmap scaleForGXP2200(Bitmap bm) {
	    if(bm==null){
	        return bm;
	    }
	    float sh=bm.getHeight()/272.0f;
        float sw=bm.getWidth()/480.0f;
        if(sh<1e-6&&sw<1e-6) {
            return bm;
        }
        float s=Math.max(sh, sw);
        Bitmap bitmap = Bitmap.createScaledBitmap(bm, (int)(bm.getWidth()/s),
                (int)(bm.getHeight()/s), false);
        bm.recycle();
        return bitmap;
	}
	
	public static boolean isImage(String fileName) {
	    File file=new File(fileName);
	    if(!file.exists()||file.isDirectory()) {
	        return false;
	    }
	    int lastDot = fileName.lastIndexOf(".");
        if (lastDot < 0)
            return false;
        String extension = fileName.substring(lastDot + 1).toLowerCase();
        
        
	    String mimeType=MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
	    if(mimeType==null || -1==mimeType.indexOf("image")) {
	        return false;
	    }
	    
	    return true;
	}
	
	public static boolean isValidImage(String fileName) {
	    if(!isImage(fileName)) {
	        return false;
	    }
	    File file = new File(fileName);
	    long fileLen = file.length();
	    if(fileLen>5*1024*1024) {
	        return false;
	    }
	    return true;
	}
	
	public static Bitmap getImageThumb(String filePath){
        File image = new File(filePath) ;
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getPath(), bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
                return null;
        int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
                : bounds.outWidth;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = originalSize / 64;
        Bitmap bmp =BitmapFactory.decodeFile(image.getPath(), opts);

        return bmp ;
	}
	
	public static Bitmap getSizedImage(String filePath, int width, int height){
        File image = new File(filePath);
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image.getPath(), bounds);
        if ((bounds.outWidth == -1) || (bounds.outHeight == -1))
                return null;
        int inSize = (bounds.outHeight*width > bounds.outWidth*height) ? bounds.outHeight/height : bounds.outWidth/width;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = inSize;
        Bitmap bmp =BitmapFactory.decodeFile(image.getPath(), opts);

        return bmp ;
    }
	
	public static Bitmap getImageGXP2200(String filePath) {
	    return getSizedImage(filePath, 480, 272);
	}

	public static Bitmap getImageFitScreen(Context context, String filePath) {
		return getSizedImage(filePath, getDisplaySize(context).widthPixels, getDisplaySize(context).widthPixels);
	}

	public static DisplayMetrics getDisplaySize(Context context) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService("window");
		wm.getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics;
	}
}

