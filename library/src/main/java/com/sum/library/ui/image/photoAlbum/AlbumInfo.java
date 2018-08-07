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

    public static final int Request_choose_photo = 10;

    public boolean take_photo_open = true;//拍照开关
    public boolean need_item_fast_preview = false;//图片快速预览
    public int request_code = Request_choose_photo;//请求码
    public int max_count = 9;//最大勾选数量

    public int default_space = SizeUtils.dp2px(2);//行间距
    public int span_count = 4;//列数

    public int choose_tint_nor_res_id = -1;//未选中的资源图片
    public int choose_tint_nor = Color.parseColor("#F5F0F0");
    public int choose_tint_sel_res_id = -1;//选中的资源图片
    public int choose_tint_sel = ContextCompat.getColor(Utils.getApp(), R.color.colorPrimary);

}
