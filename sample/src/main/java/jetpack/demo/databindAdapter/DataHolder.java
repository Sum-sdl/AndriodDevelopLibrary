package jetpack.demo.databindAdapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sum.andrioddeveloplibrary.BR;
import com.sum.lib.rvadapter.RecyclerDataHolder;
import com.sum.lib.rvadapter.RecyclerViewHolder;

/**
 * Created by sdl on 2018/7/18.
 */
public abstract class DataHolder<T> extends RecyclerDataHolder<T> {

    public DataHolder(T data) {
        super(data);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(View view, int i) {
        ViewDataBinding bind = DataBindingUtil.bind(view);
        if (bind != null) {
            return new ViewHolder(bind);
        } else {
            return new RecyclerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(int i, RecyclerView.ViewHolder viewHolder, T o) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.setBindData(BR.viewModel, o);
        }
    }
}
