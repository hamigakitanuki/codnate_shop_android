package com.example.codnate_shop_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.LinearLayout;

public class Opening_pager extends AppCompatActivity {

    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_activity);
        ViewPager viewPager = findViewById(R.id.opening_pager);
        viewPager.setAdapter(new Opening_pager_manager(getSupportFragmentManager(),0));
    }

}
