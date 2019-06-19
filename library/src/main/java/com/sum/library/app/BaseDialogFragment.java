package com.sum.library.app;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;

import com.sum.library.R;
import com.sum.library.utils.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by sdl on 2018/8/6.
 * 通用对话框展示，默认底部进入动画
 */
public abstract class BaseDialogFragment extends AppCompatDialogFragment {

    //缓存View对象
    private WeakReference<View> mWRView;

    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View cacheView = null;
        if (mWRView != null) {
            cacheView = mWRView.get();
        }
        if (cacheView == null) {
            cacheView = inflater.inflate(getLayoutId(), container, false);
            mWRView = new WeakReference<>(cacheView);
        } else {
            ViewParent parent = cacheView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(cacheView);
            }
        }
        return cacheView;
    }

    //对话框展示动画
    protected int getDialogShowAnimation() {
        return R.style.dialog_anim_bottom;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = getDialog().getWindow();
            if (window == null) {
                return;
            }
            window.setBackgroundDrawableResource(android.R.color.transparent);
            if (getDialogShowAnimation() != 0) {
                window.setWindowAnimations(getDialogShowAnimation());  //添加动画
            }

            WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            Point point = new Point();
            //noinspection ConstantConditions
            wm.getDefaultDisplay().getRealSize(point);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = point.x;
            lp.height = point.y;
            //TODO
            Logger.e("x->" + point.x + ",y:" + point.y);
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            if (Build.VERSION.SDK_INT >= 21) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setAttributes(lp);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initParams(view);
    }

    protected abstract int getLayoutId();

    protected abstract void initParams(View view);
}
