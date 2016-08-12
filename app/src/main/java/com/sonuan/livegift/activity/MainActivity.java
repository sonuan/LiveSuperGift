package com.sonuan.livegift.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sonuan.livegift.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_ferrar).setOnClickListener(this);
        findViewById(R.id.tv_ferrar2).setOnClickListener(this);
        findViewById(R.id.tv_ship).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ship:
                ShipActivity.toActivity(this);
                break;
            case R.id.tv_ferrar:
                FerrariActivity.toActivity(this);
                break;
            case R.id.tv_ferrar2:
                FerrariActivity2.toActivity(this);
                break;
        }
    }
}
