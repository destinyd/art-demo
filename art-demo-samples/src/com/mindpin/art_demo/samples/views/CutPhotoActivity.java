package com.mindpin.art_demo.samples.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.*;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.mindpin.art_demo.samples.R;

import java.io.ByteArrayOutputStream;

public class CutPhotoActivity extends Activity implements OnTouchListener,OnClickListener {
	private ImageView imageview;
	private Button ok_button,cancel_button;
	private Bitmap bitmap;
	private ClipView clipview;
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
    /** 动作标志：无 */
    private static final int NONE = 0;
    /** 动作标志：拖动 */
    private static final int DRAG = 1;
    /** 动作标志：缩放 */
    private static final int ZOOM = 2;
    /** 初始化动作标志 */
    private int mode = NONE;

    /** 记录起始坐标 */
    private PointF start = new PointF();
    /** 记录缩放时两指中间点坐标 */
    private PointF mid = new PointF();
    private float oldDist = 1f;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cut_photo_layout);
		String imgPath=getIntent().getStringExtra("imgPath");
		bitmap= BitmapFactory.decodeFile(imgPath);
		imageview=(ImageView) findViewById(R.id.imageView);
		ok_button=(Button) findViewById(R.id.ok);
		cancel_button=(Button) findViewById(R.id.cancle);
		imageview.setOnTouchListener(this);
		ok_button.setOnClickListener(this);
		cancel_button.setOnClickListener(this);
		initClipView(imageview.getTop(), bitmap);
	}
    private void initClipView(int top,final Bitmap bitmap) {
        clipview = new ClipView(CutPhotoActivity.this);
        clipview.setCustomTopBarHeight(top);
        clipview.addOnDrawCompleteListener(new ClipView.OnDrawListenerComplete() {
            public void onDrawCompelete() {
                clipview.removeOnDrawCompleteListener();
                int clipHeight = clipview.getClipHeight();
                int clipWidth = clipview.getClipWidth();
                int midX = clipview.getClipLeftMargin() + (clipWidth / 2);
                int midY = clipview.getClipTopMargin() + (clipHeight / 2);

                int imageWidth = bitmap.getWidth();
                int imageHeight = bitmap.getHeight();
                // 按裁剪框求缩放比例
                float scale = (clipWidth * 1.0f) / imageWidth;
                if (imageWidth > imageHeight) {
                    scale = (clipHeight * 1.0f) / imageHeight;
                }

                // 起始中心点
                float imageMidX = imageWidth * scale / 2;
                float imageMidY = clipview.getCustomTopBarHeight()
                        + imageHeight * scale / 2;
                imageview.setScaleType(ScaleType.MATRIX);

                // 缩放
                matrix.postScale(scale, scale);
                // 平移
                matrix.postTranslate(midX - imageMidX, midY - imageMidY);

                imageview.setImageMatrix(matrix);
                imageview.setImageBitmap(bitmap);
            }
        });
        this.addContentView(clipview, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_DOWN:
            savedMatrix.set(matrix);
            // 设置开始点位置
            start.set(event.getX(), event.getY());
            mode = DRAG;
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            oldDist = spacing(event);
            if (oldDist > 10f) {
                savedMatrix.set(matrix);
                midPoint(mid, event);
                mode = ZOOM;
            }
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            mode = NONE;
            break;
        case MotionEvent.ACTION_MOVE:
            if (mode == DRAG) {
                matrix.set(savedMatrix);
                matrix.postTranslate(event.getX() - start.x, event.getY()
                        - start.y);
            } else if (mode == ZOOM) {
                float newDist = spacing(event);
                if (newDist > 10f) {
                    matrix.set(savedMatrix);
                    float scale = newDist / oldDist;
                    matrix.postScale(scale, scale, mid.x, mid.y);
                }
            }
            break;
        }
        view.setImageMatrix(matrix);
        return true;
	}
    /**
     * 多点触控时，计算最先放下的两指距离
     * 
     * @param event
     * @return
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
    
    /**
     * 多点触控时，计算最先放下的两指中心坐标
     * 
     * @param point
     * @param event
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.ok:
			Bitmap clipBitmap = getBitmap();
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        clipBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	        byte[] bitmapByte = baos.toByteArray();
	        if(clipBitmap!=null && !clipBitmap.isRecycled()){
	        	clipBitmap.recycle();
	        	System.gc();
	        	System.out.println("图片资源已回收，控制内存占用");
	        }
	        Intent intent = new Intent();
	        intent.setClass(getApplicationContext(), PreviewActivity.class);
	        intent.putExtra("bitmap", bitmapByte);
	        startActivity(intent);
	        break;
		case R.id.cancle:
	        if(bitmap!=null && !bitmap.isRecycled()){
	        	bitmap.recycle();
	        	System.gc();
	        }
			finish();
		}
		
	}
	   /**
     * 获取裁剪框内截图
     * 
     * @return
     */
    private Bitmap getBitmap() {
        // 获取截屏
        View view = this.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
         
        // 获取状态栏高度
        Rect frame = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        Bitmap finalBitmap = Bitmap.createBitmap(view.getDrawingCache(),
                clipview.getClipLeftMargin(), clipview.getClipTopMargin()
                        + statusBarHeight, clipview.getClipWidth(),
                clipview.getClipHeight());

        // 释放资源
        view.destroyDrawingCache();
        return finalBitmap;
    }
}
