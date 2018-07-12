package com.cnpeng.b_30_RichTextEditor;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cnpeng.cnpeng_demos2017_01.R;
import com.cnpeng.cnpeng_demos2017_01.databinding.ActivityRichEditorBinding;

/**
 * 作者：CnPeng
 * <p>
 * 时间：2018/3/7:下午8:05
 * <p>
 * 说明：富文本编辑器界面
 */
public class RichTextEditorActivity extends AppCompatActivity {

    private ActivityRichEditorBinding mBinding;

    @Override
    protected void onCreate(
            @Nullable
                    Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rich_editor);

        RichEditorHandler handler = new RichEditorHandler(this, mBinding);
        mBinding.setHandler(handler);
    }
}
