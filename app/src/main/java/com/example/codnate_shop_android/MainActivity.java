package com.example.codnate_shop_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences data = getSharedPreferences("DATA",MODE_PRIVATE);
        int user_id = data.getInt("user_id",0);
        if (user_id == 0){
            Intent intent = new Intent(getApplication(),Opening_pager.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);
    }
}
