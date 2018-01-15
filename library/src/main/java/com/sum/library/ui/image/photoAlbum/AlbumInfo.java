package com.sum.library.ui.image.photoAlbum;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.Utils;
import com.sum.library.R;

import java.io.Serializable;

/**
 * Created by sdl on 2018/1/15.
 */

public class AlbumInfo implements Serializable {

    public int request_code = 10;

    public boolean take_photo_open = false;

    public int max_count = 9;

    public int default_space = SizeUtils.dp2px(2);

    public int span_count = 4;

    public int choose_tint_nor_res_id = -1;//未选中的资源图片
    public int choose_tint_nor = Color.parseColor("#F5F0F0");

    public int choose_tint_sel_res_id = -1;//选中的资源图片
    public int choose_tint_sel = ContextCompat.getColor(Utils.getApp(), R.color.colorPrimary);

}
