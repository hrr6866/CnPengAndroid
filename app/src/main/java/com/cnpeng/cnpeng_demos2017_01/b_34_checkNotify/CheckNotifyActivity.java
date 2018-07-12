package com.cnpeng.cnpeng_demos2017_01.b_34_checkNotify;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cnpeng.cnpeng_demos2017_01.R;
import com.cnpeng.cnpeng_demos2017_01.databinding.ActivityCheckNotifyBinding;

import static android.provider.Settings.EXTRA_APP_PACKAGE;
import static android.provider.Settings.EXTRA_CHANNEL_ID;

/**
 * 作者：CnPeng
 * 时间：2018/7/11
 * 功用：检测在设置中是否开启了APP的推送
 * 其他：
 *
 * https://stackoverflow.com/questions/32366649/any-way-to-link-to-the-android-notification-settings-for-my-app
 * https://blog.csdn.net/ysy950803/article/details/71910806
 * https://juejin.im/post/5a2508656fb9a0450407b638
 */
public class CheckNotifyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCheckNotifyBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_check_notify);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        // areNotificationsEnabled方法的有效性官方只最低支持到API 19，低于19的仍可调用此方法不过只会返回true，即默认为用户已经开启了通知。
        boolean isOpened = manager.areNotificationsEnabled();

        if (isOpened) {
            binding.tvMsg.setText("通知权限已经被打开" +
                    "\n手机型号:" + android.os.Build.MODEL +
                    "\nSDK版本:" + android.os.Build.VERSION.SDK +
                    "\n系统版本:" + android.os.Build.VERSION.RELEASE +
                    "\n软件包名:" + getPackageName());

        } else {
            binding.tvMsg.setText("还没有开启通知权限，点击去开启");
        }

        //CnPeng 2018/7/12 上午7:08 跳转到通知设置界面
        binding.tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                Intent intent = new Intent();


                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.putExtra(EXTRA_APP_PACKAGE, getPackageName());
                intent.putExtra(EXTRA_CHANNEL_ID, getApplicationInfo().uid);

                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", getPackageName());
                intent.putExtra("app_uid", getApplicationInfo().uid);

                //下面这种方案是直接跳转到当前应用的设置界面。
                //https://blog.csdn.net/ysy950803/article/details/71910806
                // intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                // Uri uri = Uri.fromParts("package", getPackageName(), null);
                // intent.setData(uri);
                startActivity(intent);
            }
        });
    }
}
