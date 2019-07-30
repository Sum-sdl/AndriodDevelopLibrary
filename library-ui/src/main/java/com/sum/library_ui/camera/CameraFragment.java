package com.sum.library_ui.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.hardware.SensorManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sum.library.AppFileConfig;
import com.sum.library.app.BaseFragment;
import com.sum.library.utils.TaskExecutor;
import com.sum.library_ui.R;
import com.sum.library_ui.utils.LibUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by sdl on 2019-07-11.
 */
public class CameraFragment extends BaseFragment implements SurfaceHolder.Callback, Camera.PictureCallback {
    private String TAG = "CameraFragment";

    private static final String CAMERA_ID_KEY = "camera_id";
    private static final String CAMERA_FLASH_KEY = "flash_mode";

    private int mCameraID;
    private String mFlashMode;
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    public SquareCameraPreview mPreviewView;
    private ProgressBar progress = null;
    private ImageView takePhotoBtn = null;

    private boolean isSurfaceDestroy = false;

    // 拍摄成功之后回调
    private TakeCompleteListener mTakeCompleteListener = null;
    private CameraOrientationListener mOrientationListener;
    //目标存储图片
    private File dirPath;

    public interface TakeCompleteListener {
        void onTakeFinish(String filePath);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOrientationListener = new CameraOrientationListener(context);
        mTakeCompleteListener = (TakeCompleteListener) context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ui_activity_camera;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        //默认存储的文件地址
        createNewFile();
        //相机配置
        if (savedInstanceState == null) {
            mCameraID = getBackCameraID();
            mFlashMode = Camera.Parameters.FLASH_MODE_AUTO;
        } else {
            mCameraID = savedInstanceState.getInt(CAMERA_ID_KEY);
            mFlashMode = savedInstanceState.getString(CAMERA_FLASH_KEY);
        }
        Log.i(TAG, "start camera:" + mCameraID + "," + mFlashMode);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initParams(View view) {
        mPreviewView = findViewById(R.id.camera_preview_view);
        mPreviewView.getHolder().addCallback(this);
        progress = findViewById(R.id.progress);
        findViewById(R.id.pub_title_back).setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });
        takePhotoBtn = findViewById(R.id.capture_image_button);
        takePhotoBtn = view.findViewById(R.id.capture_image_button);
        takePhotoBtn.setOnClickListener(v -> takePicture());
        initChangeCamera(view);
        mOrientationListener.enable();
    }

    private void initChangeCamera(View view) {
        final ImageView swapCameraBtn = view.findViewById(R.id.change_camera);
        PackageManager pm = mContext.getPackageManager();
        //同时拥有前后置摄像头才可以切换
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) && pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            swapCameraBtn.setVisibility(View.VISIBLE);
        } else {
            swapCameraBtn.setVisibility(View.GONE);
        }
        swapCameraBtn.setOnClickListener(v -> {
            if (mCameraID == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mCameraID = getBackCameraID();
            } else {
                mCameraID = getFrontCameraID();
            }
            restartPreview();
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CAMERA_ID_KEY, mCameraID);
        outState.putString(CAMERA_FLASH_KEY, mFlashMode);
        super.onSaveInstanceState(outState);
    }

    private boolean getCamera(int cameraID) {
        Log.d(TAG, "get camera with id " + cameraID);
        try {
            mCamera = Camera.open(cameraID);
            mPreviewView.setCamera(mCamera);
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Can't open camera with id " + cameraID);
            e.printStackTrace();
        }
        return false;
    }

    private void createNewFile() {
        deleteFile();
        Bundle arguments = getArguments();
        if (arguments != null) {
            String targetFile = arguments.getString("targetFile");
            if (!TextUtils.isEmpty(targetFile)) {
                dirPath = new File(targetFile);
            }
        }
        if (dirPath == null) {
            dirPath = new File(AppFileConfig.getAppCacheImageDirectory() + "/" + System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * Start the camera preview
     */
    private void startCameraPreview() {
        determineDisplayOrientation();
        setupCamera();
        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Can't start camera preview due to IOException " + e);
        }
    }

    /**
     * Stop the camera preview
     */
    private void stopCameraPreview() {
        // Nulls out callbacks, stops face detection
        try {
            if (mCamera != null) {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();
            }
            mCamera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPreviewView.setCamera(null);
    }

    /**
     * Determine the current display orientation and rotate the camera preview
     * accordingly
     */
    private void determineDisplayOrientation() {
        CameraInfo cameraInfo = new CameraInfo();
        Camera.getCameraInfo(mCameraID, cameraInfo);

        int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: {
                degrees = 0;
                break;
            }
            case Surface.ROTATION_90: {
                degrees = 90;
                break;
            }
            case Surface.ROTATION_180: {
                degrees = 180;
                break;
            }
            case Surface.ROTATION_270: {
                degrees = 270;
                break;
            }
        }

        int displayOrientation;

        // Camera direction
        if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
            // Orientation is angle of rotation when facing the camera for
            // the camera image to match the natural orientation of the device
            displayOrientation = (cameraInfo.orientation + degrees) % 360;
            displayOrientation = (360 - displayOrientation) % 360;
        } else {
            displayOrientation = (cameraInfo.orientation - degrees + 360) % 360;
        }

        mCamera.setDisplayOrientation(displayOrientation);
    }

    /**
     * Setup the camera parameters
     */
    private void setupCamera() {
        // Never keep a global parameters
        Camera.Parameters parameters = mCamera.getParameters();

        Size bestPreviewSize = determineBestPreviewSize(parameters);
        Size bestPictureSize = determineBestPictureSize(parameters);

        parameters.setPreviewSize(bestPreviewSize.width, bestPreviewSize.height);
        parameters.setPictureSize(bestPictureSize.width, bestPictureSize.height);

        // Set continuous picture focus, if it's supported
        if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes != null && flashModes.contains(mFlashMode)) {
            parameters.setFlashMode(mFlashMode);
        }
        if (isSupportedPictureFormats(parameters.getSupportedPictureFormats(), ImageFormat.JPEG)) {
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.setJpegQuality(100);
        }

        // Lock in the changes
        mCamera.setParameters(parameters);
    }

    private Camera.Size determineBestPreviewSize(Camera.Parameters parameters) {
        return LibUtils.getCurrentScreenSize(getContext(), parameters.getSupportedPreviewSizes());
    }

    private Camera.Size determineBestPictureSize(Camera.Parameters parameters) {
        return LibUtils.getCurrentScreenSize(getContext(), parameters.getSupportedPictureSizes());
    }

    private void restartPreview() {
        stopCameraPreview();
        if (getCamera(mCameraID)) {
            startCameraPreview();
        } else {
            Log.e(TAG, "Can't get camera");
        }
    }

    private int getFrontCameraID() {
        PackageManager pm = mContext.getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            return CameraInfo.CAMERA_FACING_FRONT;
        }
        return getBackCameraID();
    }

    private int getBackCameraID() {
        return CameraInfo.CAMERA_FACING_BACK;
    }

    /**
     * Take a picture
     */
    private void takePicture() {
        progress.setVisibility(View.VISIBLE);
        takePhotoBtn.setVisibility(View.GONE);
        mOrientationListener.rememberOrientation();
        // Shutter callback occurs after the image is captured. This can
        // be used to trigger a sound to let the user know that image is taken
        Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        };

        // Raw callback occurs when the raw image data is available
        Camera.PictureCallback raw = null;

        // postView callback occurs when a scaled, fully processed
        // postView image is available.
        Camera.PictureCallback postView = null;

        // jpeg callback occurs when the compressed image is available
        mCamera.takePicture(shutterCallback, raw, postView, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopCameraPreview();
        mOrientationListener.disable();
        mCamera = null;
        // 没有拍照，直接删除
        if (dirPath != null && dirPath.length() == 0) {
            deleteFile();
        }
    }

    public boolean deleteFile() {
        try {
            if (dirPath != null && dirPath.exists()) {
                return dirPath.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        if (isSurfaceDestroy) {
            restartPreview();
            isSurfaceDestroy = false;
        } else {
            if (getCamera(mCameraID))
                startCameraPreview();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isSurfaceDestroy = true;
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        camera.startPreview();
        mPreviewView.onPictureTaken();

        progress.setVisibility(View.GONE);
        takePhotoBtn.setVisibility(View.VISIBLE);

        TaskExecutor.ioThread(() -> {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, options);
            //旋转情况
            int orientation = mOrientationListener.getRememberedNormalOrientation();
            options.inJustDecodeBounds = false;
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, options);
            //照片旋转检测
            Matrix matrix = new Matrix();
            if (orientation != ExifInterface.ORIENTATION_UNDEFINED) {
                matrix.setRotate(orientation);
                if (mCameraID == CameraInfo.CAMERA_FACING_FRONT) {
                    matrix.postScale(-1, 1);
                }
            }
            bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            deleteFile();
            LibUtils.save(bmp, dirPath, Bitmap.CompressFormat.JPEG, true);
            Log.d(TAG, "save bitmap finish:" + dirPath.getPath());
            //通知显示预览
            TaskExecutor.mainThread(() -> mTakeCompleteListener.onTakeFinish(dirPath.getPath()));
        });
    }

    /**
     * When orientation changes, onOrientationChanged(int) of the listener will be called
     */
    private class CameraOrientationListener extends OrientationEventListener {

        private int mCurrentNormalizedOrientation;
        private int mRememberedNormalOrientation;

        CameraOrientationListener(Context context) {
            super(context, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation != ORIENTATION_UNKNOWN) {
                mCurrentNormalizedOrientation = getCameraPictureRotation(orientation);
            }
        }

        void rememberOrientation() {
            mRememberedNormalOrientation = mCurrentNormalizedOrientation;
        }

        int getRememberedNormalOrientation() {
            return mRememberedNormalOrientation;
        }
    }

    private int getCameraPictureRotation(int orientation) {
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(mCameraID, info);
        int rotation;

        orientation = (orientation + 45) / 90 * 90;

        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            rotation = (info.orientation - orientation + 360) % 360;
        } else { // back-facing camera
            rotation = (info.orientation + orientation) % 360;
        }

        return rotation;
    }

    private boolean isSupportedPictureFormats(List<Integer> supportedPictureFormats, int jpeg) {
        for (int i = 0; i < supportedPictureFormats.size(); i++) {
            if (jpeg == supportedPictureFormats.get(i)) {
                return true;
            }
        }
        return false;
    }
}
