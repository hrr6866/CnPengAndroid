package com.cnpeng.cnpeng_demos2017_01.b_18_customSwitchButton;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cnpeng.cnpeng_demos2017_01.R;

/**
 * 作者：CnPeng
 * <p>
 * 时间：2017/8/3:上午8:58
 * <p>
 * 说明：自定义开关按钮的使用
 */

public class CustomSwitchButtonActivity extends AppCompatActivity {
    @Override
    protected void onCreate(
            @Nullable
                    Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this,R.layout.activity_customswitchbutton);
    }
}
