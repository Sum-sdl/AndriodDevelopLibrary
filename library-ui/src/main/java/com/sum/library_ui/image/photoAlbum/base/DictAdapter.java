package com.sum.library_ui.image.photoAlbum.base;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.sum.library_ui.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Clevo on 2016/9/1.
 */
public class DictAdapter extends BaseAdapter {

    Context context;
    ArrayList<PhotoDirectory> models;

    public DictAdapter(Context context, ArrayList<PhotoDirectory> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DictHolder holder;
        if (convertView == null) {
            holder = new DictHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.vh_image_dict, parent, false);
            holder.adapter_dict_cover = convertView.findViewById(R.id.adapter_dict_cover);
            holder.adapter_dict_name = convertView.findViewById(R.id.adapter_dict_name);
            holder.adapter_dict_count = convertView.findViewById(R.id.adapter_dict_count);
            convertView.setTag(holder);
        } else {
            holder = (DictHolder) convertView.getTag();
        }
        String path = models.get(position).getCoverPath();
        if (!TextUtils.isEmpty(path)) {
            Glide.with(context).asDrawable().load(new File(path)).into(holder.adapter_dict_cover);
        }

        holder.adapter_dict_name.setText(models.get(position).getName());
        holder.adapter_dict_count.setText(models.get(position).getAllPhotos().size() + "张");
        return convertView;
    }

    class DictHolder {
        ImageView adapter_dict_cover;
        TextView adapter_dict_name;
        TextView adapter_dict_count;
    }
}
