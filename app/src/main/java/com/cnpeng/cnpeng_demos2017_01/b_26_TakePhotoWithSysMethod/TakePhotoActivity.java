package com.cnpeng.cnpeng_demos2017_01.b_26_TakePhotoWithSysMethod;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cnpeng.cnpeng_demos2017_01.BuildConfig;
import com.cnpeng.cnpeng_demos2017_01.R;
import com.cnpeng.cnpeng_demos2017_01.databinding.ActivityTakephotoBinding;
import com.cnpeng.cnpeng_demos2017_01.utils_stu.PermissionUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 作者：CnPeng
 * <p>
 * 时间：2017/12/1:上午10:36
 * <p>
 * 说明：调用系统相机执行拍照操作
 */
public class TakePhotoActivity extends AppCompatActivity {

    private ActivityTakephotoBinding binding;
    private File                     file;
    private       String takePhotoMode          = "";
    private final String MODE_TAKE_AND_SAVE     = "takePhotoAndSaveToLocal";
    private final String MODE_TAKE_AND_NOT_SAVE = "takePhotoAndNotSaveToLocal";

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
                PermissionUtils.requestPermission(TakePhotoActivity.this, PermissionUtils.CODE_CAMERA, 
                        permissionGrant, false);
            }
        });
        binding.btTakePhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoMode = MODE_TAKE_AND_NOT_SAVE;
                PermissionUtils.requestPermission(TakePhotoActivity.this, PermissionUtils.CODE_CAMERA, 
                        permissionGrant, false);
            }
        });
    }

    PermissionUtils.PermissionGrant permissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int permissionCode) {

            if (PermissionUtils.CODE_CAMERA == permissionCode) {
                if (MODE_TAKE_AND_SAVE.equals(takePhotoMode)) {
                    //如果模式是拍照并存储到本地，获得拍照权限之后继续申请存储设备的写入权限，成功之后再去执行拍照
                    PermissionUtils.requestPermission(TakePhotoActivity.this, PermissionUtils
                            .CODE_WRITE_EXTERNAL_STORAGE, permissionGrant, false);
                } else {
                    //如果模式是只拍照不存储则直接调用拍照
                    openSysCameraView();
                }
            } else if (PermissionUtils.CODE_WRITE_EXTERNAL_STORAGE == permissionCode) {
                openSysCameraView();
            }
        }
    };


    /**
     * 调用系统相机执行拍照
     */
    private void openSysCameraView() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (MODE_TAKE_AND_SAVE.equals(takePhotoMode)) {
            //如果是拍照并存储，则需要指定在本地的存储路径，并需要获取拍照之后的结果
            file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + "temp.jpg");
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
