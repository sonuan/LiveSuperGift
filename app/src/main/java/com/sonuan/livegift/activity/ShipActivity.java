package com.sonuan.livegift.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.sonuan.livegift.R;
import com.sonuan.livegift.widget.LiveGiftBaseBuilder;
import com.sonuan.livegift.widget.LiveShipGiftBuilder;

public class ShipActivity extends AppCompatActivity {

    private LiveShipGiftBuilder mShipGiftBuilder;

    public static void toActivity(Context context) {
        Intent intent = new Intent(context, ShipActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ship);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        mShipGiftBuilder = new LiveShipGiftBuilder(frameLayout);
        mShipGiftBuilder.setGiftAnimatorListener(new LiveGiftBaseBuilder.OnGiftAnimatorListener() {
            @Override
            public void onAnimatorStart() {

            }

            @Override
            public void onAnimatorEnd() {

            }
        });
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShipGiftBuilder.start();
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShipGiftBuilder.release();
    }
}
