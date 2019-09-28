package jetpack.demo.databindAdapter;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.sum.adapter.RecyclerDataHolder;
import com.sum.adapter.RecyclerViewHolder;
import com.sum.andrioddeveloplibrary.BR;

/**
 * Created by sdl on 2018/7/18.
 */
public abstract class DataHolder<T> extends RecyclerDataHolder<T> {

    public DataHolder(T data) {
        super(data);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(View view, int i) {
        ViewDataBinding bind = DataBindingUtil.bind(view);
        if (bind != null) {
            return new ViewHolder(bind);
        } else {
            return new RecyclerViewHolder(view);
        }
    }

    @Override
    protected void onBindViewHolder(int i, RecyclerView.ViewHolder viewHolder, T o) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.setBindData(BR.viewModel, o);
        }
    }
}
