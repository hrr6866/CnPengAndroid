package com.cnpeng.cnpeng_demos2017_01;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作者：CnPeng
 * <p>
 * 时间：2017/8/3:下午12:54
 * <p>
 * 说明：用户配置信息的sp
 */

public class SpHelper_config {

    private final Context           context;
    private final SharedPreferences sp;

    public SpHelper_config(Context mContext) {
        context = mContext;
        sp = context.getSharedPreferences("UserConfig", Context.MODE_PRIVATE);
    }


    /**
     * 存储自定义的switchBT是否被选中
     */
    public void saveCusSwitchBtStatus(boolean isChecked) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isCusSwitchBtChecked", isChecked);
        editor.commit();
    }

    /**
     * 获取自定义的SwitchBt 的选中状态
     */
    public boolean getCusSwitchBtStatus() {
        return sp.getBoolean("isCusSwitchBtChecked", false);
    }
}
