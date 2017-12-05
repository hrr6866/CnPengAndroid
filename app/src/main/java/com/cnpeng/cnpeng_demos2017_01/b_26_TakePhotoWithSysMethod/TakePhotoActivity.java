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
import android.os.Environment;
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

import static android.os.Environment.DIRECTORY_PICTURES;

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
 * <p>
 * 注意：
 * 不同品牌的手机对拍照处理不一样，个别手机中即便我们没有指定存储路径，也会存储照片到默认的地址中。如：Galaxy Note4 调用系统相机执行拍照时，
 * 如果么有指定照片存储路径，则会存储在 DCIM/Camera 目录下，此时，即便不调用 刷新相册的方法，也会执行刷新操作。
 */
public class TakePhotoActivity extends AppCompatActivity {

    private ActivityTakephotoBinding binding;
    private File                     file;
    private       String   takePhotoMode          = "";
    private final String   MODE_TAKE_AND_SAVE     = "takePhotoAndSaveToLocal";
    private final String   MODE_TAKE_AND_NOT_SAVE = "takePhotoAndNotSaveToLocal";
    private final int      REQUEST_CODE1          = 0;    //申请权限时的请求码
    private final int      REQUEST_CODE2          = 1;    //申请权限时的请求码
    private final int      REQUEST_CODE3          = 2;    //申请权限时的请求码
    private final String[] DENIED_DESC            = {"没有拍照权限将不能使用拍照功能", "没有存储权限将不能存储照片到本地"};  //权限被拒绝的描述文本

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
        String PERMISSION_CAMERA = Manifest.permission.CAMERA;
        boolean isCamearPermissionGranted = isPermissionGranted(TakePhotoActivity.this, PERMISSION_CAMERA);
        String PERMISSION_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        boolean isExternalStoragePermissionGranted = isPermissionGranted(TakePhotoActivity.this, 
                PERMISSION_EXTERNAL_STORAGE);

        if (isCamearPermissionGranted && isExternalStoragePermissionGranted) {
            //如果权限已经被允许
            openSysCameraView();
        } else if (isExternalStoragePermissionGranted && !isCamearPermissionGranted) {
            //如果拍照权限未被允许，申请拍照权限
            requestNecessaryPermission(TakePhotoActivity.this, new String[]{PERMISSION_CAMERA}, REQUEST_CODE1);
        } else if (isCamearPermissionGranted && !isExternalStoragePermissionGranted) {
            //存储权限未被允许
            requestNecessaryPermission(TakePhotoActivity.this, new String[]{PERMISSION_EXTERNAL_STORAGE}, 
                    REQUEST_CODE2);
        } else {
            //啥子权限都没有
            requestNecessaryPermission(TakePhotoActivity.this, new String[]{PERMISSION_EXTERNAL_STORAGE, 
                    PERMISSION_CAMERA}, REQUEST_CODE3);
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
            case REQUEST_CODE1:
                //如果有权限被允许了——因为此处值申请了一个拍照权限，所以只要被允许了就可以了
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openSysCameraView();
                } else {
                    showDeniedDialog(TakePhotoActivity.this, DENIED_DESC[0]);
                }
                break;
            case REQUEST_CODE2:
                //如果有权限被允许了——因为此处值申请了一个拍照权限，所以只要被允许了就可以了
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openSysCameraView();
                } else {
                    showDeniedDialog(TakePhotoActivity.this, DENIED_DESC[1]);
                }
                break;
            case REQUEST_CODE3:
                if (grantResults.length > 0) {
                    boolean isAllGranted = true;
                    StringBuilder deniedHitDesc = new StringBuilder();    //权限被拒绝是的提示文本
                    for (int i = 0; i < grantResults.length; i++) {
                        boolean isCurGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                        if (!isCurGranted) {
                            deniedHitDesc.append(DENIED_DESC[i]).append("\n");
                        }
                        isAllGranted = isAllGranted && isCurGranted;
                    }

                    if (isAllGranted) {
                        openSysCameraView();
                    } else {
                        showDeniedDialog(TakePhotoActivity.this, deniedHitDesc.toString());
                    }
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
    private void showDeniedDialog(final Context context, String message) {
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
     * <p>
     * 这里使用的 getExternalCacheDir() 是系统为每个APP单独分配的缓存空间，返回一个绝对路径，API19以后使用该路径不需要申请权限，API19之前需
     * 要申请。该路径只对当前APP可用，其他APP不可访问，也就是说，如果我们将照片存储在这个路径，系统的媒体扫描器也无法检测到该路径的内容，也就无法
     * 实现 相册/图库 内容的刷新。APP卸载则该目录清空。getExternalFilesDir() 与此相同
     * <p>
     * If you saved your photo to the directory provided by getExternalFilesDir(), the media scanner cannot access the
     * files because they are private to your app.
     */
    private void openSysCameraView() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (MODE_TAKE_AND_SAVE.equals(takePhotoMode)) {
            //如果是拍照并存储，则需要指定在本地的存储路径，并需要获取拍照之后的结果
            //file = new File(getExternalCacheDir(), System.currentTimeMillis() + "temp.jpg");
            // file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), System.currentTimeMillis() + "temp" 
            //         + ".jpg");

            file = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES), System
                    .currentTimeMillis() + "temp.jpg");
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
     * If you saved your photo to the directory provided by getExternalFilesDir(), the media scanner cannot access the
     * files because they are private to your app.
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