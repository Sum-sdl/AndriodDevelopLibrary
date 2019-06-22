package com.sum.andrioddeveloplibrary.view_delegate.mvp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.sum.andrioddeveloplibrary.R;
import com.sum.andrioddeveloplibrary.view_delegate.ViewDelegateFragment;
import com.sum.library.domain.mvp.AppViewDelegate;
import com.sum.library_ui.image.AppImageUtils;
import com.sum.library_ui.image.photoAlbum.AlbumInfo;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by sdl on 2019-06-22.
 */
public class UiCopyUseDelegate extends AppViewDelegate {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_view_delegate;
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
    }

    private void replaceUi() {
        mContext.getSupportFragmentManager()
                .beginTransaction().add(id, new ViewDelegateFragment()).addToBackStack(null).commit();

//        mContext.getSupportFragmentManager().beginTransaction().add()
    }

    private void startA() {
        AppImageUtils.appImageAlbum(mContext, 2);
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
