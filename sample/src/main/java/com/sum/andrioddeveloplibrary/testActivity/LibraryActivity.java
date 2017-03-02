package com.sum.andrioddeveloplibrary.testActivity;

import android.view.View;

import com.sum.andrioddeveloplibrary.R;
import com.sum.library.app.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

@ContentView(R.layout.activity_library)
public class LibraryActivity extends BaseActivity {

    @Event(R.id.b1)
    private void b1(View view) {
        startActivity(BottomTabActivity.class);
    }

    @Event(R.id.b2)
    private void b2(View view) {
        startActivity(TopTabActivity.class);
    }


    @Override
    protected void initParams() {

    }

}
