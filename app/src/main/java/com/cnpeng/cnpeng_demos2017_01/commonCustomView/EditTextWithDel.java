package com.cnpeng.cnpeng_demos2017_01.commonCustomView;


import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cnpeng.cnpeng_demos2017_01.R;

/**
 * 作者：CnPeng
 * <p>
 * 时间：2017/6/20:下午4:49
 * <p>
 * 说明：
 * 内容为空时不显示删除图标
 * 焦点改变时不显示删除图标
 * 删除图标的点击事件
 * 内容变化时控制删除图标的展示
 */
public class EditTextWithDel extends LinearLayout {


    private Context      mContext;
    private LinearLayout ll_root;
    private EditText     editText;
    private ImageView    iv_del;

    public EditTextWithDel(Context paramContext) {
        this(paramContext, null);
    }

    public EditTextWithDel(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public EditTextWithDel(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        mContext = paramContext;
        initView();

        initAttr();
    }

    private void initAttr() {
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.custom_edittext_withdel, this);
        ll_root = (LinearLayout) findViewById(R.id.ll_root_ETwithDel);
        editText = (EditText) findViewById(R.id.et_input_ETwithDel);
        iv_del = (ImageView) findViewById(R.id.iv_del_ETwithDel);

        initEvent();
    }

    /**
     * 初始化事件，点击，删除，文字变化
     */
    private void initEvent() {
        iv_del.setOnClickListener(new View.OnClickListener() {   //清空内容
            @Override
            public void onClick(View v) {
                editText.setText(null);
            }
        });

        editText.addTextChangedListener(new TextWatcher() { //文字变化
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    iv_del.setVisibility(View.VISIBLE);
                } else {
                    iv_del.setVisibility(View.GONE);
                }

                if (onTextChangedListener != null) {
                    onTextChangedListener.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (editText.getText().length() > 0) {
                        iv_del.setVisibility(View.VISIBLE);
                    }
                } else {
                    iv_del.setVisibility(View.GONE);
                }
            }
        });
    }


    /**
     * 返回EditText中的文本数据
     */
    public Editable getText() {
        return editText.getText();
    }


    /**
     * 暴露文本变化监听给外部使用，
     */
    public interface OnTextChangedListener {
        void onTextChanged(CharSequence s, int start, int before, int count);
    }

    /**
     * 设置自定义的文本变化监听，
     */
    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener) {
        this.onTextChangedListener = onTextChangedListener;
    }

    OnTextChangedListener onTextChangedListener;
}
