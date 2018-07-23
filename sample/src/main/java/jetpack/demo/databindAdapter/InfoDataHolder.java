package jetpack.demo.databindAdapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sum.andrioddeveloplibrary.BR;
import com.sum.andrioddeveloplibrary.R;
import com.sum.lib.rvadapter.RecyclerDataHolder;
import com.sum.lib.rvadapter.RecyclerViewHolder;
import com.sum.library.utils.Logger;

/**
 * Created by sdl on 2018/7/18.
 */
public class InfoDataHolder extends RecyclerDataHolder<Object> {

    public InfoDataHolder(Object data) {
        super(data);
    }

    @Override
    protected int getItemViewLayoutId() {
        return R.layout.db_item_info_data_holder;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(View view, int i) {
        ViewDataBinding bind = DataBindingUtil.bind(view);
        Logger.e("onCreateViewHolder->"+i);
        if (bind != null) {
            return new ViewHolder(bind);
        } else {
            return new RecyclerViewHolder(view);
        }
    }

    //TODO BR.viewModel 每一个layout中variable的name属性必须为viewModel
    @Override
    protected void onBindViewHolder(int i, RecyclerView.ViewHolder viewHolder, Object o) {
        Logger.e("onBindViewHolder->"+i);
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            holder.setBindData(BR.viewModel, o);
        }
    }
}
