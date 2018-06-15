package com.cnpeng.cnpeng_demos2017_01.b_33_BaseRvAdapter;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.cnpeng.cnpeng_demos2017_01.R;
import com.cnpeng.cnpeng_demos2017_01.databinding.ActivityBaseRvAdapterBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 作者：CnPeng
 * 时间：2018/6/8
 * 功用：
 * 其他：
 */
public class BaseRvAdapterActivity extends FragmentActivity implements RvAdapter.OnLoadingMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private ActivityBaseRvAdapterBinding mBinding;
    private BaseRvAdapterActivity        mActivity;
    private RvAdapter                    mRvAdapter;

    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_rv_adapter);
        mActivity = this;
        initRv();
        initRefreshLayout();
    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(this);
    }

    private void initRv() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mBinding.rv.setLayoutManager(layoutManager);

        addDataToList(1);

        mRvAdapter = new RvAdapter(mActivity, mList, mBinding.rv);
        mBinding.rv.setAdapter(mRvAdapter);

        mRvAdapter.setLoadingMoreListener(this);
    }

    private void addDataToList(int start) {
        int end = start + 15;
        for (int i = start; i < end; i++) {
            mList.add(i + "");
        }
    }

    @Override
    public void onLoadingMore() {

        if (mList.size() >= 150) {
            //不再执行加载操作
            // TODO: CnPeng 2018/6/14 下午4:20 没有更多数据了
            mRvAdapter.setLoadingStatus(mRvAdapter.STATUS_NO_MORE);
            return;
        } else {
            mRvAdapter.setLoadingStatus(mRvAdapter.STATUS_LOADING);
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                addDataToList(mList.size());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRvAdapter.setData(mList);
                        mRvAdapter.setLoadingStatus(mRvAdapter.STATUS_OVER);
                        Toast.makeText(mActivity, "上拉完成", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, 1000);

    }

    @Override
    public void onRefresh() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for(int i= 0;i<10 ;i++){
                    mList.add(0,i+"下拉");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRvAdapter.setData(mList);
                        mRvAdapter.setLoadingStatus(mRvAdapter.STATUS_OVER);
                        Toast.makeText(mActivity, "下拉完成", Toast.LENGTH_SHORT).show();
                        mBinding.refreshLayout.setRefreshing(false);
                    }
                });
            }
        }, 1000);
    }
}
