package com.mindpin.art_demo.samples.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.view.WindowManager;

public class DecodeImageUtils {
	@SuppressWarnings("deprecation")
	public static Bitmap decodeImage(byte [] data,Context context,Matrix matrix){
        // 1.计算出来屏幕的宽高.
		WindowManager wm=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int windowWidth = wm.getDefaultDisplay().getWidth();
        int windowHeight = wm.getDefaultDisplay().getHeight();
        //2. 计算图片的宽高.
        BitmapFactory.Options opts = new Options();
        // 设置 不去真正的解析位图 不把他加载到内存 只是获取这个图片的宽高信息
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        int bitmapHeight = opts.outHeight;
        int bitmapWidth = opts.outWidth;
        if (bitmapHeight > windowHeight || bitmapWidth > windowWidth) {
            int scaleX = bitmapWidth/windowWidth;
            int scaleY = bitmapHeight/windowHeight;
            if(scaleX>scaleY){//按照水平方向的比例缩放
                opts.inSampleSize = scaleX;
            }else{//按照竖直方向的比例缩放
                opts.inSampleSize = scaleY;
            }
        }else{//如果图片比手机屏幕小 不去缩放了.
            opts.inSampleSize = 1;
        }
        //让位图工厂真正的去解析图片
        opts.inJustDecodeBounds = false;
        Bitmap oldBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        Bitmap newBitmap = Bitmap.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, true);
        return newBitmap;
	}
}
