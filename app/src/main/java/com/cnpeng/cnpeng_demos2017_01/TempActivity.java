package com.cnpeng.cnpeng_demos2017_01;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cnpeng.cnpeng_demos2017_01.databinding.ActivityTempBinding;
import com.cnpeng.cnpeng_demos2017_01.utils.LogUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class TempActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityTempBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_temp);

        String str = getJsonStr();
        binding.tv.setText(str);

    }

    public String getJsonStr() {
        String str = "";
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            productList.add(new Product("ID_00" + i, i));
        }

        Gson gson = new Gson();
        str = gson.toJson(productList);
        LogUtils.i("Json字符串", str);
        return str;
    }

    class Product {
        String id;
        int    num;

        public Product(String id, int num) {
            this.id = id;
            this.num = num;
        }
    }

}