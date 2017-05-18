package com.cnpeng.cnpeng_demos2017_01.b_10_AndroidDrawables;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.cnpeng.cnpeng_demos2017_01.R;
import com.cnpeng.cnpeng_demos2017_01.utils.CustomRectRoundDrawable;
import com.cnpeng.cnpeng_demos2017_01.utils.CustomRoundDrawable;

/**
 * 作者：CnPeng
 * <p>
 * 时间：2017/5/18:下午5:56
 * <p>
 * 说明：用来展示自定义的圆角和圆形drawble
 */

public class CustomDrawableActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customdrawable);

        ImageView iv_cusDrawable = (ImageView) findViewById(R.id.iv_cusDrawable);
        iv_cusDrawable.setImageDrawable(new CustomRoundDrawable(this, R.drawable.daomeixiong));

        ImageView iv_cusDrawable2 = (ImageView) findViewById(R.id.iv_cusDrawable2);
        iv_cusDrawable2.setImageDrawable(new CustomRectRoundDrawable(this, R.drawable.daomeixiong));
    }
}
