package com.cnpeng.cnpeng_demos2017_01.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：CnPeng
 * <p>
 * 时间：2017/12/5:下午2:11
 * <p>
 * 说明：动态权限申请工具类
 */

public class DynamicPermissionTool {
    private Context context;

    public String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public String[] deniedHints = {"没有相机权限将不能拍照", "没有存储设备的读写权限将不能存储照片到本地"};

    public DynamicPermissionTool(Context context) {
        this.context = context;
    }

//    public DynamicPermissionTool() {
//    }

    /**
     * 检查单个权限是否已经被允许
     *
     * @param permission 要申请的权限
     */
    public boolean checkCurPermissionStatus(String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission);
    }

    /**
     * 检查一组权限的授权状态。
     *
     * @param permissions 权限数组
     * @return 权限的状态数组
     */
    public boolean[] checkCurPermissionsStatus(String[] permissions) {
        if (null != permissions && permissions.length > 0) {
            boolean[] permissionsStatus = new boolean[permissions.length];

            for (int i = 0; i < permissions.length; i++) {
                boolean permissionStatus = checkCurPermissionStatus(permissions[i]);
                permissionsStatus[i] = permissionStatus;
            }
            return permissionsStatus;
        } else {
            throw new IllegalArgumentException("参数不能为空，且必须有元素");
        }
    }

    /**
     * 获取被拒绝的权限
     *
     * @param permissions 要申请的全部权限
     */
    public String[] getDeniedPermissions(String[] permissions) {
        if (null != permissions && permissions.length > 0) {
            List<String> deniedPermissionList = new ArrayList<>();

            boolean[] permissionsStatus = checkCurPermissionsStatus(permissions);
            for (int i = 0; i < permissions.length; i++) {
                if (!permissionsStatus[i]) {
                    deniedPermissionList.add(permissions[i]);
                }
            }

            String[] deniedPermissions = new String[deniedPermissionList.size()];
            return deniedPermissionList.toArray(deniedPermissions);
        } else {
            throw new IllegalArgumentException("参数不能为空，且必须有元素");
        }
    }

    /**
     * 获取被拒绝的权限对应的提示文本数组
     *
     * @param permissions 要申请的全部权限
     * @param hints       权限被拒绝时的提示文本
     */
    public String[] getDeniedHint(String[] permissions, String[] hints) {
        if (null == permissions || null == hints || permissions.length == 0 || hints.length == 0 || permissions
                .length != hints.length) {
            throw new IllegalArgumentException("参数不能为空、必须有元素，且两个参数的长度必须一致");
        } else {
            List<String> deniedHintList = new ArrayList<>();

            boolean[] permissionsStatus = checkCurPermissionsStatus(permissions);
            for (int i = 0; i < permissions.length; i++) {
                if (!permissionsStatus[i]) {
                    deniedHintList.add(hints[i]);
                }
            }

            String[] deniedPermissions = new String[deniedHintList.size()];
            return deniedHintList.toArray(deniedPermissions);
        }
    }

    /**
     * 获取被拒绝的权限对应的提示文本数组
     *
     * @param grantResults 权限申请的结果
     * @param hints        权限被拒绝时的提示文本
     */
    public String getDeniedHintStr(int[] grantResults, String[] hints) {
        if (null == grantResults || null == hints || grantResults.length == 0 || hints.length == 0 || grantResults
                .length != hints.length) {
            throw new IllegalArgumentException("参数不能为空、必须有元素，且两个参数的长度必须一致");
        } else {
            StringBuilder hintStr = new StringBuilder();

            for (int i = 0; i < grantResults.length; i++) {
                if (PackageManager.PERMISSION_DENIED == grantResults[i]) {
                    hintStr.append(hints[i]).append("\n");
                }
            }

            return hintStr.toString();
        }
    }

    /**
     * 获取被拒绝的权限对应的提示文本字符串
     *
     * @param permissions 要申请的全部权限
     * @param hints       权限被拒绝时的提示文本
     */
    public String getDeniedHintStr(String[] permissions, String[] hints) {
        if (null == permissions || null == hints || permissions.length == 0 || hints.length == 0 || permissions
                .length != hints.length) {
            throw new IllegalArgumentException("参数不能为空、必须有元素，且两个参数的长度必须一致");
        } else {
            StringBuilder hintStr = new StringBuilder();
            boolean[] permissionsStatus = checkCurPermissionsStatus(permissions);
            for (int i = 0; i < permissions.length; i++) {
                if (!permissionsStatus[i]) {
                    hintStr.append(hints[i]).append("\n");
                }
            }
            return hintStr.toString();
        }
    }

    /**
     * 是否所有权限都已经被允许
     *
     * @param permissions 申请的权限
     * @return true 全被允许，false 有没有被允许的权限
     */
    public boolean isAllPermissionGranted(String[] permissions) {
        return getDeniedPermissions(permissions).length == 0;
    }

    /**
     * 是否所有权限都已经被允许
     *
     * @param grantResults 权限申请的结果
     * @return true 全被允许，false 有没有被允许的权限
     */
    public boolean isAllPermissionGranted(int[] grantResults) {
        int isAllGranted = PackageManager.PERMISSION_GRANTED;
        for (int grantResult : grantResults) {
            isAllGranted = isAllGranted | grantResult;
        }
        return isAllGranted == 0;
    }

    /**
     * 请求权限
     *
     * @param activity    Activity
     * @param permissions 权限
     * @param requestCode 请求码
     */
    public void requestNecessaryPermissions(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }


    /**
     * 展示被拒绝的弹窗
     */
    public void showDeniedDialog(final Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setNegativeButton("就不给你权限", null);
        builder.setPositiveButton("我要重新开启权限", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openSysSettingPage(context);
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 打开系统设置界面
     */
    private void openSysSettingPage(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
