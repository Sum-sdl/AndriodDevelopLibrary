package com.sum.andrioddeveloplibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.b1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTargetActivity(StickyActivity.class);
            }
        });
    }

    private void startTargetActivity(Class a) {
        Intent intent = new Intent(this, a);
        startActivity(intent);
    }

}
