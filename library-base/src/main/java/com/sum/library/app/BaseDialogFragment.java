package com.sum.library.app;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.sum.library.R;

import java.lang.ref.WeakReference;

/**
 * Created by sdl on 2018/8/6.
 * 通用对话框展示，默认底部进入动画
 * 1.根布局View是铺满屏幕
 * 2.内容必须在根布局里在添加一层View
 */
public abstract class BaseDialogFragment extends AppCompatDialogFragment {

    //缓存View对象
    private WeakReference<View> mWRView;

    protected Context mContext;
    //需要底部动画
    protected boolean mIsNeedBottomAnim = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getDialogNoBg()) {
            setStyle(DialogFragment.STYLE_NO_TITLE, com.sum.library.R.style.dialog_no_bg);
        }
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
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(cacheView);
            }
        }
        return cacheView;
    }

    //设置是否需要默认的地步弹框动画(getDialogShowAnimation优先生效)
    public void setNeedBottomAnim() {
        mIsNeedBottomAnim = true;
    }

    //弹框没有背景
    protected boolean getDialogNoBg() {
        return false;
    }

    //对话框展示动画,默认无动画
    //动画参考：R.style.dialog_anim_bottom
    protected int getDialogShowAnimation() {
        return 0;
    }

    //调整整体内容显示位置
    public void updateGravity(int gravity) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = getDialog().getWindow();
            if (window == null) {
                return;
            }
            //调整整体内容据显示位置
            window.setGravity(gravity);
        }
    }

    //默认对话框的初始话
    protected void defaultDialogSetting() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = getDialog().getWindow();
            if (window == null) {
                return;
            }
            boolean navBarVisible = true;
            //虚拟导航键显示出来(如果显示，需要将内容上移)
            FragmentActivity activity = getActivity();
            if (activity != null) {
                navBarVisible = isNavBarVisible(activity.getWindow());
            }
            //默认背景色透明
            window.setBackgroundDrawableResource(android.R.color.transparent);
            if (getDialogShowAnimation() != 0) {
                window.setWindowAnimations(getDialogShowAnimation());  //添加动画
            } else {
                if (mIsNeedBottomAnim) {
                    window.setWindowAnimations(R.style.dialog_anim_bottom);  //添加动画
                }
            }

            int height = -1;
            //需要减去导航栏高度
            if (navBarVisible) {
                //获取系统显示内容高度
                WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                Point point = new Point();
                if (wm != null) {
                    wm.getDefaultDisplay().getSize(point);
                }
                height = point.y;
                if (height == 0) {
                    height = -1;//全屏显示
                }
                //整体内容据上显示,避开导航栏高度
                window.setGravity(Gravity.TOP);
            }

            //影响整体内容大小 对话框的内容完全根据该大小在绘制一个View
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = -1;
            //该高度不包含导航栏高度
            lp.height = height;

            //全部透明，如果高度设置为-1，显示内容会在导航栏下面，整体高度需要移除导航栏高度
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            if (Build.VERSION.SDK_INT >= 21) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setAttributes(lp);
        }
    }

    private boolean isNavBarVisible(@NonNull final Window window) {
        boolean isVisible = false;
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        for (int i = 0, count = decorView.getChildCount(); i < count; i++) {
            final View child = decorView.getChildAt(i);
            final int id = child.getId();
            if (id != View.NO_ID) {
                String resourceEntryName = Resources.getSystem().getResourceEntryName(id);
                if ("navigationBarBackground".equals(resourceEntryName)
                        && child.getVisibility() == View.VISIBLE) {
                    isVisible = true;
                    break;
                }
            }
        }
        if (isVisible) {
            int visibility = decorView.getSystemUiVisibility();
            isVisible = (visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0;
        }
        return isVisible;
    }


    @Override
    public void onStart() {
        super.onStart();
        defaultDialogSetting();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initParams(view);
    }

    protected abstract int getLayoutId();

    protected abstract void initParams(View view);


    public void showFast(Context context) {
        if (context instanceof FragmentActivity) {
            if (!((FragmentActivity) context).isFinishing()) {
                show(((FragmentActivity) context).getSupportFragmentManager(), "dialog");
            }
        }
    }

    @Override
    public void show(@NonNull FragmentManager manager, String tag) {
//        try {
//            FragmentTransaction ft = manager.beginTransaction();
//            ft.add(this, tag);
//            ft.commitAllowingStateLoss();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        try {
            super.show(manager, tag);
        } catch (Exception ignored) {
        }
    }

}
