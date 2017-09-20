package com.cnpeng.cnpeng_demos2017_01.b_09_TextureView;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import com.cnpeng.cnpeng_demos2017_01.R;

import java.io.IOException;

import static com.cnpeng.cnpeng_demos2017_01.R.drawable.f;

/**
 * 作者：CnPeng
 * <p>
 * 时间：2017/4/28:下午3:13
 * <p>
 * 说明：拍照预览界面
 */

public class TextureViewActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //先初始化TextureView ,然后将其作为view设置给该界面
        TextureView textureView = new TextureView(this);
        textureView.setSurfaceTextureListener(this);    //设置监听

        textureView.setAlpha(1.0f);     //控制透明度
        textureView.setRotation(90.0f); //控制旋转

        setContentView(textureView);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        camera = Camera.open();
        try {
            camera.setPreviewTexture(surface);  //设置预览监听
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();  //开启预览

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        camera.stopPreview();      //关闭预览
        camera.release();       //释放对象
        return true;
        //        return false;     //此处返回true ,表示事件已经处理
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
