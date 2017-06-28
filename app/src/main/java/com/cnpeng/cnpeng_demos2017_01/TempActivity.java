package com.cnpeng.cnpeng_demos2017_01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import java.lang.reflect.Method;

import static com.cnpeng.cnpeng_demos2017_01.R.drawable.a;
import static com.cnpeng.cnpeng_demos2017_01.R.drawable.i;


public class TempActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_temp);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();

        int a = 2;
        int b = 3;


        b = a;

    }


} 

