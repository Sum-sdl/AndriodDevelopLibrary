package jetpack.demo.databindAdapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sum.andrioddeveloplibrary.BR;
import com.sum.andrioddeveloplibrary.R;
import com.sum.lib.rvadapter.RecyclerDataHolder;
import com.sum.lib.rvadapter.RecyclerViewHolder;

/**
 * Created by sdl on 2018/7/18.
 */
public class InfoDataHolder extends RecyclerDataHolder<Object> {

    public InfoDataHolder(Object data) {
        super(data);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.db_item_info_data_holder;
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

    //TODO BR.viewModel 每一个layout中variable的name属性必须为viewModel
    @Override
    public void onBindViewHolder(int i, RecyclerView.ViewHolder viewHolder, Object o) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.setBindData(BR.viewModel, o);
        }
    }
}
