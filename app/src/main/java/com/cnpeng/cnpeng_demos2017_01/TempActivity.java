package com.cnpeng.cnpeng_demos2017_01;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cnpeng.cnpeng_demos2017_01.utils.LogUtils;

import java.util.HashSet;
import java.util.Set;


public class TempActivity extends AppCompatActivity {

    private String          tempStr;
    private SpannableString spannableString;
    private TextView        tv1;
    int preEndIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_temp);
        super.onCreate(savedInstanceState);

        setKeyStrClickEvent();
    }

    //    public void onClick(View view) {
    //        init();
    //    }
    //
    //    private void init() {
    //
    //        EditText et = (EditText) findViewById(R.id.et_temp);
    //        String content = et.getEditableText().toString();
    //        String content_trim = et.getEditableText().toString().trim();
    //
    //        tv1 = (TextView) findViewById(R.id.tv_temp1);
    //        tv1.setText(content);
    //
    //        TextView tv2 = (TextView) findViewById(R.id.tv_temp2);
    //        tv2.setText(content_trim);
    //
    //
    //    }


    public void setKeyStrClickEvent() {
        Set<String> keySet = new HashSet<>();
        keySet.add("出版社");
        keySet.add("商务");

        String str = "长江出版社，商务书局，黄河出版社，啥子东东呢出版社？";
        spannableString = new SpannableString(str);     //全局变量
        tempStr = str;      //全局变量

        for (String keyStr : keySet) {
            preEndIndex = 0;
            findKeyAndSetEvent(tempStr, keyStr);
        }

        tv1 = (TextView) findViewById(R.id.tv_temp1);
        tv1.setText(spannableString);
        //设置该句使文本的超连接起作用,不设置该句代码，点击事件不生效！！！
        tv1.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void findKeyAndSetEvent(String tempStr, String keyStr) {
        final int startIndex = tempStr.indexOf(keyStr);     //起始索引
        if (startIndex != -1) {
            final int endIndex = startIndex + keyStr.length() - 1;    //终止索引,
            int startIndexInOgirinal = startIndex + preEndIndex;

            if (preEndIndex == 0) {
                preEndIndex = startIndexInOgirinal + keyStr.length();      //在原始字符串中的结束位置
            } else {
                startIndexInOgirinal = startIndex + preEndIndex-1;
                preEndIndex = startIndexInOgirinal + keyStr.length();      //在原始字符串中的结束位置
            }
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    //点击事件弹窗+请求服务器数据
                    Toast.makeText(TempActivity.this, "点我干嘛？" + startIndex + "/" + endIndex, Toast.LENGTH_SHORT).show();
                }
            }, startIndexInOgirinal, preEndIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

            LogUtils.e("本地关键字的索引位置：" + keyStr, startIndex + "/" + endIndex);

            tempStr = tempStr.substring(endIndex);  //截取字符串
            findKeyAndSetEvent(tempStr, keyStr);     //递归调用
        }
    }
}