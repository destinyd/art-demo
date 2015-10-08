package com.mindpin.art_demo.samples.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.Camera.*;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.mindpin.art_demo.samples.Constants;
import com.mindpin.art_demo.samples.R;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("deprecation")
public class TakePhotoActivity extends Activity {// implements OnTouchListener {
    private static final String TAG = "TakePhotoActivity";
    private Button take_photo_button;
    private SurfaceHolder mHolder;
    private String path;
    private static Camera mCamera;
    public File photoFile;
    public Handler mHandler = new Handler();
    private CameraPreview mPreview;
    private FrameLayout preview;
    int displayRotation;
    int picRotate = 0;
    private DrawCaptureRect mDraw;
    private int width = 0;
    private int height = 0;
    private boolean isfocusing = false;
    private boolean isfocuseed = false;
    private int x = 0;
    private int y = 0;
    private int draw_width, draw_height;
    private int fix_top;
    private int mid_x, mid_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo_layout);

        //旋转监视器
        new OrientationListener(TakePhotoActivity.this).enable();

        // CameraPreview 重写 SurfaceView
        mPreview = new CameraPreview(this, mCamera);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
//        preview.setOnTouchListener(this);
        // 将SurfaceView 添加到 FrameLayout 下
        preview.addView(mPreview);

        init();

        // DrawCaptureRect 重写 View
        mDraw = new DrawCaptureRect(TakePhotoActivity.this,
                width / 2 - draw_width / 2, height / 2 - draw_height / 2 + fix_top, draw_width, draw_height,
                getResources().getColor(R.color.red));
        preview.addView(mDraw);

        take_photo_button = (Button) findViewById(R.id.take_photo_button);
        take_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 已经成功对焦
//                if (isfocuseed) {
                mCamera.takePicture(shutterCallback, null, mPicture);
                isfocusing = false;
                isfocuseed = false;
//                } else if (isfocusing) {
//                    Toast.makeText(TakePhotoActivity.this, "正在聚焦，请稍等", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(TakePhotoActivity.this, "请触摸屏幕对焦后再拍照", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }

    private void focus() {
        // 自动聚焦
        isfocusing = true;
        Rect focusRect = calculateTapArea(mid_x, mid_y, 1f);
        Rect meteringRect = calculateTapArea(mid_x, mid_y, 1.5f);
        Parameters parameters = mCamera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        System.out.println("支持的变焦模式" + focusModes);
        parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);
        if (parameters.getMaxNumFocusAreas() > 0) {
            List<Area> focusAreas = new ArrayList<Area>();
            focusAreas.add(new Area(focusRect, 600));
            parameters.setFocusAreas(focusAreas);
        }
        if (parameters.getMaxNumMeteringAreas() > 0) {
            List<Area> meteringAreas = new ArrayList<Area>();
            meteringAreas.add(new Area(meteringRect, 1000));
            parameters.setMeteringAreas(meteringAreas);
        }
        mCamera.cancelAutoFocus();
        mCamera.setParameters(parameters);
        mCamera.autoFocus(new AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                Log.d(TAG, "onAutoFocus success:" + success);
                // 对焦成功
                if (success) {
                    mDraw = new DrawCaptureRect(TakePhotoActivity.this, mid_x, mid_y, draw_width, draw_height, getResources().getColor(R.color.green));
                    preview.addView(mDraw);
                    isfocuseed = true;
                    isfocusing = false;
                } else {
                    mDraw = new DrawCaptureRect(TakePhotoActivity.this, mid_x, mid_y, draw_width, draw_height, getResources().getColor(R.color.red));
                    preview.addView(mDraw);
                    isfocuseed = false;
                    isfocusing = true;
                }
            }
        });
    }

    private void init() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        width = dm.widthPixels;
        height = dm.heightPixels;
        draw_width = width - 200;
        draw_height = (int) (draw_width * 0.75);
        fix_top = -200;

        mid_x = width / 2;
        mid_y = height / 2 + fix_top;
    }

    private Rect calculateTapArea(float x, float y, float coefficient) {
        float focusAreaSize = 200;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) ((x / width) * 2000 - 1000);
        int centerY = (int) ((y / height) * 2000 - 1000);
        int left = clamp(centerX - (areaSize / 2), -1000, 1000);
        int top = clamp(centerY - (areaSize / 2), -1000, 1000);
        RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top),
                Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }

    //快门声音
    ShutterCallback shutterCallback = new ShutterCallback() {
        @Override
        public void onShutter() {
            // TODO Auto-generated method stub
//			mCamera.enableShutterSound(true);
        }
    };

    public void getCameraInstance() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                mCamera = Camera.open(0);
                // i=0 表示后置相机
            } else
                mCamera = Camera.open();
        } catch (Exception e) {
        }
    }

    //设置预览参数
    public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        public CameraPreview(Context context, Camera camera) {
            super(context);
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                Log.d("TakePhoto", "Error setting camera preview: " + e.getMessage());
            }
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            resetCamera();
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (mHolder.getSurface() == null) {
                return;
            }
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
            }
            try {
                mCamera.setDisplayOrientation(getCameraDisplayOrientation(0));
                Camera.Parameters params = mCamera.getParameters();
                Size PreviewSize = getBestSupportedSize(params.getSupportedPreviewSizes());
                Size PictureSize = getBestSupportedSize(params.getSupportedPictureSizes());
                params.setPictureFormat(ImageFormat.JPEG);
                params.setPreviewSize(PreviewSize.width, PreviewSize.height);
                params.setPictureSize(PictureSize.width, PictureSize.height);
                params.setJpegQuality(100);
                mCamera.setParameters(params);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (Exception e) {
                resetCamera();
                Log.d("takePhoto", "Error starting camera preview: " + e.getMessage());
            }
        }
    }

    //获取图片数据
    private PictureCallback mPicture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 原a part 1储存，切换
            // 得全图，传出
            Matrix matrix = new Matrix();
            matrix.setRotate(picRotate);
            Bitmap bitmap = DecodeImageUtils.decodeImage(data, TakePhotoActivity.this, matrix);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_kk-mm-ss");// 转换格式
            String picName = sdf.format(new Date()) + ".jpg";
            path = getPicPath() + picName;
            File pictureFile = new File(getPicPath());
            if (!pictureFile.exists()) {
                pictureFile.mkdirs();
            }
            pictureFile = new File(path);

            // 原b 储存，切换
            // 获取截屏
//            View view = TakePhotoActivity.this.getWindow().getDecorView();
//            view.setDrawingCacheEnabled(true);
//            view.buildDrawingCache();
//
//            // 获取状态栏高度
//            Rect frame = new Rect();
//            TakePhotoActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//            int statusBarHeight = frame.top;
            // 计算
//            int bitmap_2_preview_fit_width = (int) (preview.getWidth() * (bitmap.getHeight() / (float)preview.getHeight() ));

            Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap,
                    preview.getHeight(), preview.getHeight(),
                    false
            );
            int left_fix = Math.abs(preview.getHeight() - mDraw.getMwidth()) / 2;

            Bitmap clipBitmap = Bitmap.createBitmap(scaleBitmap,
//                    mDraw.getMleft(),
                    left_fix, mDraw.getMtop(), mDraw.getMwidth(), mDraw.getMheight()
            );

            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(pictureFile));
                clipBitmap.compress(CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();

                if (bitmap != null || clipBitmap != null) {
                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                    if (clipBitmap != null && !clipBitmap.isRecycled()) {
                        clipBitmap.recycle();
                    }
                    System.gc();
                    System.out.println("图片资源已回收，控制内存占用");
                }
                camera.stopPreview();
                Log.d(TAG, "跳转到裁剪页面");
                Log.d(TAG, "imgPath: " + path);
                Intent intent = new Intent(TakePhotoActivity.this, ResultActivity.class);
                intent.putExtra(Constants.Extra.IMAGE_PATH, path);
                startActivity(intent);
            } catch (FileNotFoundException e) {
                Log.d("TakePhoto", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("TakePhoto", "Error accessing file: " + e.getMessage());
            }

            // 原a part 2储存，切换
//            try {
//                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(pictureFile));
//                bitmap.compress(CompressFormat.JPEG, 100, bos);
//                bos.flush();
//                bos.close();
//                if (bitmap != null && !bitmap.isRecycled()) {
//                    bitmap.recycle();
//                    System.gc();
//                    System.out.println("图片资源已回收，控制内存占用");
//                }
//                camera.stopPreview();
//                Log.d(TAG, "跳转到裁剪页面");
//                Log.d(TAG, "imgPath: " + path);
//                Intent intent = new Intent(TakePhotoActivity.this, CutPhotoActivity.class);
//                intent.putExtra("imgPath", path);
//                startActivity(intent);
//            } catch (FileNotFoundException e) {
//                Log.d("TakePhoto", "File not found: " + e.getMessage());
//            } catch (IOException e) {
//                Log.d("TakePhoto", "Error accessing file: " + e.getMessage());
//            }

        }
    };

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
                mDraw.getLeft(), mDraw.getTop()
                        + statusBarHeight, mDraw.getWidth(),
                mDraw.getHeight());

        // 释放资源
        view.destroyDrawingCache();
        return finalBitmap;
    }

    private class OrientationListener extends OrientationEventListener {
        public OrientationListener(Context context) {
            super(context);
        }

        public OrientationListener(Context context, int rate) {
            super(context, rate);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation == ORIENTATION_UNKNOWN) return;
            android.hardware.Camera.CameraInfo info =
                    new android.hardware.Camera.CameraInfo();
            android.hardware.Camera.getCameraInfo(0, info);
            orientation = (orientation + 45) / 90 * 90;
            int rotation = 0;
            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
                rotation = (info.orientation - orientation + 360) % 360;
            } else {  // back-facing camera
                rotation = (info.orientation + orientation) % 360;
            }
            picRotate = rotation;
        }
    }

    public int getCameraDisplayOrientation(int cameraId) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    private Size getBestSupportedSize(List<Size> sizes) {
        // 取能适用的最大的SIZE
        Size largestSize = sizes.get(0);
        int largestArea = sizes.get(0).height * sizes.get(0).width;
        for (Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                largestArea = area;
                largestSize = s;
            }
        }
        return largestSize;
    }

    private void resetCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public static String getPicPath() {
        File defaultDir = Environment.getExternalStorageDirectory();
        String path = defaultDir.getAbsolutePath() + File.separator + "DXM" + File.separator;
        return path;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        resetCamera();
        getCameraInstance();
        focus();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        resetCamera();
        getCameraInstance();
        focus();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        resetCamera();
    }
}
