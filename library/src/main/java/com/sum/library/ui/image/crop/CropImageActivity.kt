package com.sum.library.ui.image.crop

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.animation.AccelerateInterpolator
import com.sum.library.R
import com.sum.library.app.BaseActivity
import com.sum.library.view.widget.PubTitleView
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.model.AspectRatio
import com.yalantis.ucrop.view.CropImageView
import com.yalantis.ucrop.view.GestureCropImageView
import com.yalantis.ucrop.view.OverlayView
import com.yalantis.ucrop.view.TransformImageView
import kotlinx.android.synthetic.main.activity_image_crop.*
import java.util.*

/**
 * Created by sdl on 2018/1/15.
 * 图片剪裁
 */
class CropImageActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_image_crop

    private lateinit var mGestureCropImageView: GestureCropImageView
    private lateinit var mOverlayView: OverlayView

    private lateinit var mTitle: PubTitleView

    override fun statusBarColor(): Int {
        return Color.BLACK
    }

    override fun initParams() {
        mGestureCropImageView = cv_crop.cropImageView
        mOverlayView = cv_crop.overlayView

        mTitle = findViewById(R.id.pub_title_view)
        val textView = mTitle.addRightTextButton("完成", View.OnClickListener {
            cropImage()
        })
        mGestureCropImageView.setTransformImageListener(object : TransformImageView.TransformImageListener {
            override fun onRotate(currentAngle: Float) {
            }

            override fun onScale(currentScale: Float) {
            }

            override fun onLoadFailure(e: java.lang.Exception) {
            }

            override fun onLoadComplete() {
                cv_crop.animate()
                        .alpha(1f)
                        .setDuration(300)
                        .setInterpolator(AccelerateInterpolator())
                        .start()
            }
        })
        //setting
        mGestureCropImageView.isRotateEnabled = false
        mGestureCropImageView.isScaleEnabled = true

        setImageData(intent)
    }


    override fun onStop() {
        super.onStop()
        mGestureCropImageView.cancelAllAnimations()
    }

    private fun setImageData(intent: Intent) {
        val inputUri = intent.getParcelableExtra<Uri>(UCrop.EXTRA_INPUT_URI)
        val outputUri = intent.getParcelableExtra<Uri>(UCrop.EXTRA_OUTPUT_URI)
        processOptions(intent)
        if (inputUri != null && outputUri != null) {
            try {
                mGestureCropImageView.setImageUri(inputUri, outputUri)
            } catch (e: Exception) {
                finish()
            }
        } else {
            finish()
        }
    }


    private fun cropImage() {
        mGestureCropImageView.cropAndSaveImage(
                Bitmap.CompressFormat.JPEG,
                80,
                object : BitmapCropCallback {
                    override fun onBitmapCropped(resultUri: Uri, offsetX: Int, offsetY: Int, imageWidth: Int, imageHeight: Int) {
                        val intent = intent
                        val bundle = Bundle()
                        bundle.putString("path", resultUri.path)
                        intent.putExtras(bundle)
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                    override fun onCropFailure(t: Throwable) {

                    }
                })

    }

    private fun processOptions(intent: Intent) {
        // Crop image view options
        mGestureCropImageView.maxBitmapSize = intent.getIntExtra(UCrop.Options.EXTRA_MAX_BITMAP_SIZE, CropImageView.DEFAULT_MAX_BITMAP_SIZE)
        mGestureCropImageView.setMaxScaleMultiplier(intent.getFloatExtra(UCrop.Options.EXTRA_MAX_SCALE_MULTIPLIER, CropImageView.DEFAULT_MAX_SCALE_MULTIPLIER))
        mGestureCropImageView.setImageToWrapCropBoundsAnimDuration(intent.getIntExtra(UCrop.Options.EXTRA_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION).toLong())

        // Overlay view options
        mOverlayView.isFreestyleCropEnabled = intent.getBooleanExtra(UCrop.Options.EXTRA_FREE_STYLE_CROP, OverlayView.DEFAULT_FREESTYLE_CROP_ENABLED)

        mOverlayView.setDimmedColor(intent.getIntExtra(UCrop.Options.EXTRA_DIMMED_LAYER_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_default_dimmed)))
        mOverlayView.setCircleDimmedLayer(intent.getBooleanExtra(UCrop.Options.EXTRA_CIRCLE_DIMMED_LAYER, OverlayView.DEFAULT_CIRCLE_DIMMED_LAYER))

        mOverlayView.setShowCropFrame(intent.getBooleanExtra(UCrop.Options.EXTRA_SHOW_CROP_FRAME, OverlayView.DEFAULT_SHOW_CROP_FRAME))
        mOverlayView.setCropFrameColor(intent.getIntExtra(UCrop.Options.EXTRA_CROP_FRAME_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_default_crop_frame)))
        mOverlayView.setCropFrameStrokeWidth(intent.getIntExtra(UCrop.Options.EXTRA_CROP_FRAME_STROKE_WIDTH, resources.getDimensionPixelSize(R.dimen.ucrop_default_crop_frame_stoke_width)))

        mOverlayView.setShowCropGrid(intent.getBooleanExtra(UCrop.Options.EXTRA_SHOW_CROP_GRID, OverlayView.DEFAULT_SHOW_CROP_GRID))
        mOverlayView.setCropGridRowCount(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_ROW_COUNT, OverlayView.DEFAULT_CROP_GRID_ROW_COUNT))
        mOverlayView.setCropGridColumnCount(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_COLUMN_COUNT, OverlayView.DEFAULT_CROP_GRID_COLUMN_COUNT))
        mOverlayView.setCropGridColor(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_COLOR, ContextCompat.getColor(this, R.color.ucrop_color_default_crop_grid)))
        mOverlayView.setCropGridStrokeWidth(intent.getIntExtra(UCrop.Options.EXTRA_CROP_GRID_STROKE_WIDTH, resources.getDimensionPixelSize(R.dimen.ucrop_default_crop_grid_stoke_width)))

        // Aspect ratio options
        val aspectRatioX = intent.getFloatExtra(UCrop.EXTRA_ASPECT_RATIO_X, 0f)
        val aspectRatioY = intent.getFloatExtra(UCrop.EXTRA_ASPECT_RATIO_Y, 0f)

        val aspectRationSelectedByDefault = intent.getIntExtra(UCrop.Options.EXTRA_ASPECT_RATIO_SELECTED_BY_DEFAULT, 0)
        val aspectRatioList: ArrayList<AspectRatio>? = intent.getParcelableArrayListExtra(UCrop.Options.EXTRA_ASPECT_RATIO_OPTIONS)

        if (aspectRatioX > 0 && aspectRatioY > 0) {
            mGestureCropImageView.targetAspectRatio = aspectRatioX / aspectRatioY
        } else if (aspectRatioList != null && aspectRationSelectedByDefault < aspectRatioList.size) {
            mGestureCropImageView.targetAspectRatio = aspectRatioList[aspectRationSelectedByDefault].aspectRatioX /
                    aspectRatioList[aspectRationSelectedByDefault].aspectRatioY
        } else {
            mGestureCropImageView.targetAspectRatio = CropImageView.SOURCE_IMAGE_ASPECT_RATIO
        }
        // Result bitmap max size options
        val maxSizeX = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_X, 0)
        val maxSizeY = intent.getIntExtra(UCrop.EXTRA_MAX_SIZE_Y, 0)

        if (maxSizeX > 0 && maxSizeY > 0) {
            mGestureCropImageView.setMaxResultImageSizeX(maxSizeX)
            mGestureCropImageView.setMaxResultImageSizeY(maxSizeY)
        }
    }
}