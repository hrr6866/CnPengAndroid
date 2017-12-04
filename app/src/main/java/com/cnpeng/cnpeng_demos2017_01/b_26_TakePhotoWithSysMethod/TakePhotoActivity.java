package com.cnpeng.cnpeng_demos2017_01.b_26_TakePhotoWithSysMethod;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cnpeng.cnpeng_demos2017_01.BuildConfig;
import com.cnpeng.cnpeng_demos2017_01.R;
import com.cnpeng.cnpeng_demos2017_01.databinding.ActivityTakephotoBinding;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 作者：CnPeng
 * <p>
 * 时间：2017/12/1:上午10:36
 * <p>
 * 说明：调用系统相机执行拍照操作
 * 主要知识点：
 * 1、调用系统相机执行拍照
 * 2、动态权限申请
 * 3、刷新相册
 * 4、开启APP对应的设置界面
 * 5、使用 FileProvider 解决7.0及以后系统中使用 Uri.fromUri() 获取URI之后调用相机崩溃的情况
 * 6、getExternalCacheDir()获取当前APP对应的缓存目录，使用该方式不用申请读写SD的权限
 */
public class TakePhotoActivity extends AppCompatActivity {

    private ActivityTakephotoBinding binding;
    private File                     file;
    private       String takePhotoMode          = "";
    private final String MODE_TAKE_AND_SAVE     = "takePhotoAndSaveToLocal";
    private final String MODE_TAKE_AND_NOT_SAVE = "takePhotoAndNotSaveToLocal";
    private final String PERMISSION_CAMERA      = Manifest.permission.CAMERA;
    private final int    REQUEST_CODE           = 0;    //申请权限时的请求码

    @Override
    protected void onCreate(
            @Nullable
                    Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_takephoto);

        initTakePhotoBtEvent();
    }

    private void initTakePhotoBtEvent() {

        binding.btTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoMode = MODE_TAKE_AND_SAVE;
                takePhotoOrRequestPermission();
            }
        });
        binding.btTakePhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoMode = MODE_TAKE_AND_NOT_SAVE;
                takePhotoOrRequestPermission();
            }
        });
    }

    private void takePhotoOrRequestPermission() {
        boolean isPermissionGranted = isPermissionGranted(TakePhotoActivity.this, PERMISSION_CAMERA);
        if (isPermissionGranted) {
            //如果权限已经被允许
            openSysCameraView();
        } else {
            //如果权限未被允许，申请权限
            requestNecessaryPermission(TakePhotoActivity.this, new String[]{PERMISSION_CAMERA}, REQUEST_CODE);
        }
    }

    private void requestNecessaryPermission(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull
                                                   String[] permissions,
                                           @NonNull
                                                   int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                //如果有权限被允许了——因为此处值申请了一个拍照权限，所以只要被允许了就可以了
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openSysCameraView();
                } else {
                    showDeniedDialog(TakePhotoActivity.this);
                }
                break;
            default:
                break;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 展示被拒绝的弹窗
     */
    private void showDeniedDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("没有拍照权限将不能使用拍照功能");
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

    /**
     * 检查权限是否已经通过
     *
     * @param context    上下文
     * @param permission 申请的权限
     */
    private boolean isPermissionGranted(Context context, String permission) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context, permission);
    }

    /**
     * 调用系统相机执行拍照
     */
    private void openSysCameraView() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (MODE_TAKE_AND_SAVE.equals(takePhotoMode)) {
            //如果是拍照并存储，则需要指定在本地的存储路径，并需要获取拍照之后的结果
            //这里使用了 getExternalCacheDir 是系统为每个APP单独分配的缓存空间，不需要申请权限。
            file = new File(getExternalCacheDir(), System.currentTimeMillis() + "temp.jpg");
            Uri photoURI;
            photoURI = getPhotoUri(file);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        }
        startActivityForResult(intent, Activity.DEFAULT_KEYS_DIALER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK == resultCode) {
            //读取存储在本地的图片作为背景，这个图的清晰度会比较高
            if (MODE_TAKE_AND_SAVE.equals(takePhotoMode)) {
                Uri photoUri = getPhotoUri(file);
                try {
                    Drawable drawable = Drawable.createFromStream(getContentResolver().openInputStream(photoUri), "");
                    binding.parentLayout.setBackground(drawable);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                updateGallery(photoUri);
            } else {
                //相机拍照后会返回一个intent给onActivityResult。 intent的extra部分包含一个编码过的Bitmap,但这个Bitmap会比较模糊
                Bundle bundle = data.getExtras();
                if (null != bundle) {
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    Drawable drawable = new BitmapDrawable(bitmap);
                    binding.parentLayout.setBackground(drawable);
                }
            }
        }
        //模式使用完之后记得要重置
        takePhotoMode = "";
    }


    /**
     * 发送广播更新相册，不更新的话，在相册中将无法查看到截取的图片
     * ATTENTION  在6.0及以上手机系统中，使用该方法刷新相册并不是实时的。暂时未找到实时的方法。6.0以下可以实时刷新
     */
    private void updateGallery(Uri photoURI) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, photoURI);
        sendBroadcast(intent);
    }

    private Uri getPhotoUri(File file) {
        Uri photoURI;
        if (Build.VERSION.SDK_INT > 21) {
            photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + "" + "" + "" 
                    + ".fileprovider", file);
            //  Uri photoURI = FileProvider.getUriForFile(TakePhotoActivity.this,getPackageName()+ 
            // "" + ".fileprovider", file);     //这种方式也可以获取URI
        } else {
            photoURI = Uri.fromFile(file);
        }
        return photoURI;
    }
}
