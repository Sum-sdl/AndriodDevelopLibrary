package jetpack.demo.databindAdapter;

import android.databinding.ViewDataBinding;

import com.sum.adapter.RecyclerViewHolder;


/**
 * Created by sdl on 2018/7/18.
 */
public class ViewHolder extends RecyclerViewHolder {

    private ViewDataBinding mBind;

    public ViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        mBind = binding;
    }

    public void setBindData(int id, Object data) {
        mBind.setVariable(id, data);
//        mBind.executePendingBindings();
    }

    public ViewDataBinding getViewDataBinding() {
        return mBind;
    }
}
