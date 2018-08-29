package com.example.user.navigation_calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class GroupSetting extends AppCompatActivity implements View.OnClickListener  {

    ImageButton gs_back;
    ImageButton gs_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_setting);

        gs_back=findViewById(R.id.gs_back);
        gs_back.setOnClickListener(this);
        gs_save=findViewById(R.id.gs_save);
        gs_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.gs_back:
                finish();
                break;
            case R.id.gs_save:
                //post

                finish();
                break;
        }
    }
}
