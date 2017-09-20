package com.cnpeng.cnpeng_demos2017_01;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.EditText;
import android.widget.TextView;

import static android.support.v4.widget.ViewDragHelper.INVALID_POINTER;


public class TempActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_temp);
        super.onCreate(savedInstanceState);
        
    }

    public void onClick(View view){
        init();
    }
    private void init() {

        EditText et= (EditText) findViewById(R.id.et_temp);
        String content=et.getEditableText().toString();
        String content_trim=et.getEditableText().toString().trim();

        TextView tv1= (TextView) findViewById(R.id.tv_temp1);
        tv1.setText(content);
        
        TextView tv2= (TextView) findViewById(R.id.tv_temp2);
        tv2.setText(content_trim);
    }


}