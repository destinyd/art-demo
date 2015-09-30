package com.mindpin.art_demo.samples.views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class PreviewActivity extends Activity {
	Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		byte [] data=getIntent().getByteArrayExtra("bitmap");
		ImageView iv=new ImageView(PreviewActivity.this);
		bitmap= BitmapFactory.decodeByteArray(data, 0, data.length);
		iv.setImageBitmap(bitmap);
		setContentView(iv);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		bitmap.recycle();
		System.out.println("图片资源已回收，控制内存占用");
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		bitmap.recycle();
		System.out.println("图片资源已回收，控制内存占用");
	}
}
