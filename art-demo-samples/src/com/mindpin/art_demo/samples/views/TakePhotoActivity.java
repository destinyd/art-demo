package com.mindpin.art_demo.samples.views;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.mindpin.art_demo.samples.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.io.*;
import java.util.Iterator;
import java.util.List;

public class TakePhotoActivity extends RoboActivity implements SurfaceHolder.Callback, Camera.PictureCallback {
    private static final String TAG = "TakePhotoActivity";
    @InjectView(R.id.sv_preview)
    SurfaceView sv_preview;
    @InjectView(R.id.btn_capture)
    Button btn_capture;

    private Camera camera; //这个是hardare的Camera对象
    private boolean isPreview;
    private Bitmap mBitmap;
    private int max_width;
    private int max_height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /* 使应用程序全屏运行，不使用title bar */
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.take_phone);

        sv_preview.setFocusable(true);
        sv_preview.setFocusableInTouchMode(true);
        sv_preview.setClickable(true);
        btn_capture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                camera.takePicture(null, null, null, TakePhotoActivity.this);

            }
        });
        //SurfaceView中的getHolder方法可以获取到一个SurfaceHolder实例
        SurfaceHolder holder = sv_preview.getHolder();
        //为了实现照片预览功能，需要将SurfaceHolder的类型设置为PUSH
        //这样，画图缓存就由Camera类来管理，画图缓存是独立于Surface的
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 当Surface被创建的时候，该方法被调用，可以在这里实例化Camera对象
        //同时可以对Camera进行定制
        camera = Camera.open(); //获取Camera实例


        /**
         * Camera对象中含有一个内部类Camera.Parameters.该类可以对Camera的特性进行定制
         * 在Parameters中设置完成后，需要调用Camera.setParameters()方法，相应的设置才会生效
         * 由于不同的设备，Camera的特性是不同的，所以在设置时，需要首先判断设备对应的特性，再加以设置
         * 比如在调用setEffects之前最好先调用getSupportedColorEffects。如果设备不支持颜色特性，那么该方法将
         * 返回一个null
         */
        try {

            Camera.Parameters param = camera.getParameters();
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                //如果是竖屏
                param.set("orientation", "portrait");
                //在2.2以上可以使用
                camera.setDisplayOrientation(90);
            } else {
                param.set("orientation", "landscape");
                //在2.2以上可以使用
                camera.setDisplayOrientation(0);
            }
            //首先获取系统设备支持的所有颜色特效，有复合我们的，则设置；否则不设置
//            List<String> colorEffects = param.getSupportedColorEffects();
//            Iterator<String> colorItor = colorEffects.iterator();
//            while(colorItor.hasNext()){
//                String currColor = colorItor.next();
//                if(currColor.equals(Camera.Parameters.EFFECT_SOLARIZE)){
//                    param.setColorEffect(Camera.Parameters.EFFECT_SOLARIZE);
//                    break;
//                }
//            }
//            //设置完成需要再次调用setParameter方法才能生效
//            camera.setParameters(param);

            camera.setPreviewDisplay(holder);

            /**
             * 在显示了预览后，我们有时候希望限制预览的Size
             * 我们并不是自己指定一个SIze而是指定一个Size，然后
             * 获取系统支持的SIZE，然后选择一个比指定SIZE小且最接近所指定SIZE的一个
             * Camera.Size对象就是该SIZE。
             *
             */

            max_width = sv_preview.getWidth();
            max_height = sv_preview.getHeight();
            int bestWidth = 0;
            int bestHeight = 0;

            List<Camera.Size> sizeList = param.getSupportedPreviewSizes();
            //如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
            if(sizeList != null && sizeList.size() > 1){
                Iterator<Camera.Size> itor = sizeList.iterator();
                while(itor.hasNext()){
                    Camera.Size cur = itor.next();
                    if(cur.width > bestWidth && cur.height>bestHeight && cur.width < max_width && cur.height < max_height){
                        bestWidth = cur.width;
                        bestHeight = cur.height;
                    }
                }
                if(bestWidth != 0 && bestHeight != 0){
                    param.setPreviewSize(bestWidth, bestHeight);
                    //这里改变了SIze后，我们还要告诉SurfaceView，否则，Surface将不会改变大小，进入Camera的图像将质量很差
                    sv_preview.setLayoutParams(new RelativeLayout.LayoutParams(bestWidth, bestHeight));
                }
            }
            camera.setParameters(param);
        } catch (Exception e) {
            Log.d("surfaceCreated", "catch Exception");
//            Log.d("surfaceCreated", e.toString());
            // 如果出现异常，则释放Camera对象
            camera.release();
        }

        //启动预览功能
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // 当Surface被销毁的时候，该方法被调用
        //在这里需要释放Camera资源
        camera.stopPreview();
        camera.release();
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
//        // bytes是一个原始的JPEG图像数据，
//        //在这里我们可以存储图片，很显然可以采用MediaStore
//        //注意保存图片后，再次调用startPreview()回到预览
//        Uri imageUri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
//        try {
//            OutputStream os = this.getContentResolver().openOutputStream(imageUri);
//            os.write(bytes);
//            os.flush();
//            os.close();
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//
//        camera.startPreview();
        Log.i(TAG, "myJpegCallback:onPictureTaken...");
        if (null != bytes) {
            mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);//bytes是字节数据，将其解析成位图
            camera.stopPreview();
            isPreview = false;
        }
        //设置FOCUS_MODE_CONTINUOUS_VIDEO)之后，myParam.set("rotation", 90)失效。图片竟然不能旋转了，故这里要旋转下
        Matrix matrix = new Matrix();
        matrix.postRotate((float) 90.0);
        // width 640 height 480 摄像头
        Bitmap rotaBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, false);
        Log.d(TAG, "mBitmap.getWidth():" + mBitmap.getWidth());
        Log.d(TAG, "mBitmap.getHeight():" + mBitmap.getHeight());

//        sv_preview.getWidth():768
//        sv_preview.getHeight():1038
        Log.d(TAG, "sv_preview.getWidth():" + sv_preview.getWidth());
        Log.d(TAG, "sv_preview.getHeight():" + sv_preview.getHeight());


        //旋转后rotaBitmap是960×1280.预览surfaview的大小是540×800
        //将960×1280缩放到540×800
        Bitmap sizeBitmap = Bitmap.createScaledBitmap(rotaBitmap, sv_preview.getWidth(), sv_preview.getHeight(), true);
        Log.d(TAG, "sizeBitmap.getWidth():" + sizeBitmap.getWidth());
        Log.d(TAG, "sizeBitmap.getHeight():" + sizeBitmap.getHeight());

        int left = dp2px(this, 50);
//        int top = dp2px(this, 50);
        int top = dp2px(this, 10);
        int width = sv_preview.getWidth() - dp2px(this, 100);
        int height = dp2px(this, 200);
        Bitmap rectBitmap = Bitmap.createBitmap(sizeBitmap, left, 0, width, height);//截取
//        //保存图片到sdcard
        if(null != rectBitmap)
        {
            saveJpeg(sizeBitmap);
            saveJpeg(rectBitmap);
        }

        //再次进入预览
        camera.startPreview();
        isPreview = true;
    }

    /*给定一个Bitmap，进行保存*/
    public void saveJpeg(Bitmap bm) {
        String savePath = "/mnt/sdcard/rectPhoto/";
        File folder = new File(savePath);
        if (!folder.exists()) //如果文件夹不存在则创建
        {
            folder.mkdir();
        }
        long dataTake = System.currentTimeMillis();
        String jpegName = savePath + dataTake + ".jpg";
        Log.i(TAG, "saveJpeg:jpegName--" + jpegName);
        //File jpegFile = new File(jpegName);  
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);

            //          //如果需要改变大小(默认的是宽960×高1280),如改成宽600×高800  
            //          Bitmap newBM = bm.createScaledBitmap(bm, 600, 800, false);  

            bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            Log.i(TAG, "saveJpeg：存储完毕！");
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            Log.i(TAG, "saveJpeg:存储失败！");
            e.printStackTrace();
        }
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}