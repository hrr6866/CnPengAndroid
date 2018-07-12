package com.cnpeng.b_28_collspaningToolBar;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cnpeng.cnpeng_demos2017_01.R;
import com.cnpeng.cnpeng_demos2017_01.databinding.ActivityCollapsingBinding;
import com.cnpeng.utils.CommonUtils;

/**
 * 作者：CnPeng
 * <p>
 * 时间：2017/12/25:上午10:13
 * <p>
 * 说明：
 */

public class CollapsingToolBarActivity extends AppCompatActivity {

    private ActivityCollapsingBinding mBinding;

    @Override
    protected void onCreate(
            @Nullable
                    Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_collapsing);

        CommonUtils.setImmersionStatusBar(CollapsingToolBarActivity.this, 0);

    }
}
