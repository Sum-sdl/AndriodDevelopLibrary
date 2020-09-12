package com.sum.library_ui.image.photoAlbum;

import android.content.Intent;
import android.graphics.Color;

import com.sum.library_ui.utils.LibUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sdl on 2018/1/15.
 */

public class AlbumInfo implements Serializable {

    //默认相册请求处理
    public static int Request_choose_photo = 0x1233;

    public boolean take_photo_open = true;//拍照开关
    public boolean customer_camera = false;//自定义相机
    public boolean need_item_fast_preview = false;//图片快速预览
    public int max_count = 9;//最大勾选数量
    //内部处理
    int request_code = Request_choose_photo;//请求码

    public int default_space = LibUtils.dp2px(2);//行间距
    public int span_count = 4;//列数

    public int choose_tint_nor_res_id = -1;//未选中的资源图片
    public int choose_tint_nor = Color.parseColor("#F5F0F0");
    public int choose_tint_sel = Color.parseColor("#2b6cd4");
    public int choose_tint_sel_res_id = -1;//选中的资源图片


    //默认相册请求返回
    public static boolean isAlbumResult(int requestCode) {
        return requestCode == Request_choose_photo;
    }

    //获取图片
    public static ArrayList<String>  getAlbumSelectedImages(Intent data) {
        ArrayList<String> list = new ArrayList<>();
        if (data != null) {
            ArrayList<String> images = data.getStringArrayListExtra("images");
            list.addAll(images);
        }
        return list;
    }
}
