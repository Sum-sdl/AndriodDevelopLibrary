package jetpack.demo.databindAdapter;

import com.sum.andrioddeveloplibrary.R;

/**
 * Created by sdl on 2018/7/18.
 */
public class Info2DataHolder extends DataHolder {

    public Info2DataHolder(Object data) {
        super(data);
    }

    @Override
    protected int getItemViewLayoutId() {
        return R.layout.db_item_info2_data_holder;
    }
}
