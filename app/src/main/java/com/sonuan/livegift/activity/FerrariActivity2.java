package com.sonuan.livegift.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.sonuan.livegift.R;
import com.sonuan.livegift.widget.LiveFerrariGiftBuilder;
import com.sonuan.livegift.widget.LiveGiftBaseBuilder;

public class FerrariActivity2 extends AppCompatActivity {

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, FerrariActivity2.class);
        context.startActivity(intent);
    }

    private FrameLayout mLiveGiftBaseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ferrari2);
        mLiveGiftBaseLayout = (FrameLayout) findViewById(R.id.fl_live_gift);


        final LiveFerrariGiftBuilder liveGiftBaseBuilder = new LiveFerrariGiftBuilder(mLiveGiftBaseLayout);
        mLiveGiftBaseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveGiftBaseBuilder.start();
            }
        });
        liveGiftBaseBuilder.setGiftAnimatorListener(new LiveGiftBaseBuilder.OnGiftAnimatorListener() {
            @Override
            public void onAnimatorStart() {

            }

            @Override
            public void onAnimatorEnd() {

            }
        });
    }
}
