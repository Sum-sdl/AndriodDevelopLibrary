package com.sum.andrioddeveloplibrary.view_delegate.delegate;

import android.annotation.SuppressLint;
import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.sum.andrioddeveloplibrary.R;
import com.sum.andrioddeveloplibrary.view_delegate.ViewDelegateFragment;
import com.sum.library.app.delegate.BaseAppUiDelegate;
import com.sum.library.utils.LiveDataEventBus;
import com.sum.library.utils.TaskExecutor;
import com.sum.library_ui.image.photoAlbum.AlbumInfo;
import com.sum.library_ui.image.photoAlbum.PhotoAlbumActivity;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sdl on 2019-06-22.
 */
public class UiCopyUseDelegate extends BaseAppUiDelegate {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_view_delegate;
    }

    @Override
    protected boolean needPrintLifeLog() {
        return true;
    }

    private FrameLayout fl_content;
    int id;
    TextView tv_1;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initParams(View view) {
        fl_content = view.findViewById(R.id.fl_content);
        id = new Random().nextInt();
        fl_content.setId(id);

        tv_1 = view.findViewById(R.id.tv_1);
        String ids = Integer.toHexString(System.identityHashCode(this));
        tv_1.setText("Object->:" + ids + ",FrameLayout 随机id:" + id);

        view.findViewById(R.id.bt1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLong("UiDelegate-> replace :" + mContext.toString());
                replaceUi();
            }
        });

        view.findViewById(R.id.bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startA();
            }
        });
        view.findViewById(R.id.bt3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveDataEventBus.with("hello").postValue("hello");
            }
        });
        view.findViewById(R.id.bt4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                active();
            }
        });

        LiveDataEventBus.with("hello").observe(getLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tv_1.append(s);
            }
        });

        //打开关闭模拟 泄漏
        TaskExecutor.mainThread(new Runnable() {
            @Override
            public void run() {
                active();
            }
        }, 2000);

    }

    private void active() {
        getActivePresent().getLoadingView().showLoading("Hello:" + getObjectId());
    }

    private void replaceUi() {
//        getActivity().getSupportFragmentManager()
//                .beginTransaction().add(id, new ViewDkotlinFragment()).addToBackStack(null).commit();
        getActivity().getSupportFragmentManager()
                .beginTransaction().add(id, new ViewDelegateFragment()).addToBackStack(null).commit();
    }

    private void startA() {
        AlbumInfo info = new AlbumInfo();
        info.max_count = 12;
        info.take_photo_open = false;
        info.span_count = 3;
//        AppImageUtils.appImageAlbum(mActivity, info);
        Fragment fragment = getFragment();
        if (fragment != null) {
            info.take_photo_open = true;
            PhotoAlbumActivity.Companion.open(fragment, info);
        } else {
            PhotoAlbumActivity.Companion.open(getActivity(), info);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AlbumInfo.Request_choose_photo) {//图片选择返回
            if (data != null) {
                ArrayList<String> images = data.getStringArrayListExtra("images");
                tv_1.setText(images.toString());
            }

        }
    }
}
