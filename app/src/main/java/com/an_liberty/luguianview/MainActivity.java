package com.an_liberty.luguianview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.an_liberty.luguianview.view.BeautyView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{




    //左
    private BeautyView BeautyView_Left;
    //右
    private BeautyView BeautyView_Right;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BeautyView_Left = (BeautyView)findViewById(R.id.BeautyView_Left);
        BeautyView_Right = (BeautyView)findViewById(R.id.BeautyView_Right);
        BeautyView_Left.setOnClickListener(this);
        BeautyView_Right.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.BeautyView_Left:
                BeautyView_Left.isSelect(true,false);
                BeautyView_Right.isSelect(true,false);
               // BeautyView_Left.isSelect(0);
                break;
            case R.id.BeautyView_Right:
                BeautyView_Left.isSelect(false,true);
                BeautyView_Right.isSelect(false,true);
             //   BeautyView_Left.isSelect(1);
                break;
        }
    }
}
