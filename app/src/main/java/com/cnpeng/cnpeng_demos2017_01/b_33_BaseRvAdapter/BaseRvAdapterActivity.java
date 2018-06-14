package com.cnpeng.cnpeng_demos2017_01.b_33_BaseRvAdapter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.cnpeng.cnpeng_demos2017_01.R;
import com.cnpeng.cnpeng_demos2017_01.databinding.ActivityBaseRvAdapterBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：CnPeng
 * 时间：2018/6/8
 * 功用：
 * 其他：
 */
public class BaseRvAdapterActivity extends FragmentActivity {

    private ActivityBaseRvAdapterBinding mBinding;
    private BaseRvAdapterActivity        mActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_rv_adapter);
        mActivity = this;
        initRv();
    }

    private void initRv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mBinding.rv.setLayoutManager(layoutManager);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i + "");
        }

        RvAdapter rvAdapter = new RvAdapter(mActivity, list);
        mBinding.rv.setAdapter(rvAdapter);
    }
}
