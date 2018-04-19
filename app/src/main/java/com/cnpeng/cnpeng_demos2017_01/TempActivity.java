package com.cnpeng.cnpeng_demos2017_01;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.cnpeng.cnpeng_demos2017_01.databinding.ActivityTempBinding;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


public class TempActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      ActivityTempBinding  binding = DataBindingUtil.setContentView(this, R.layout.activity_temp);

    }

  
}