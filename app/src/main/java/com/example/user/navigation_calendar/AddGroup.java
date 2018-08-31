package com.example.user.navigation_calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ToggleButton;

public class AddGroup extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    ImageButton group_save;
    ImageButton back;

    ImageButton ag_clean;
    EditText ad_groupname;
    ToggleButton ag_notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        group_save=findViewById(R.id.ag_save);
        back=findViewById(R.id.ag_back);
        group_save.setOnClickListener(this);
        back.setOnClickListener(this);

        ag_clean=findViewById(R.id.ag_buttonclean);
        ag_clean.setOnClickListener(this);

        ag_notification=findViewById(R.id.ag_toggleButton);
        ag_notification.setTextOn("");
        ag_notification.setTextOff("");
        ag_notification.setChecked(false);
        ag_notification.setOnCheckedChangeListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ag_back:
                finish();
                break;
            case R.id.ag_save:
                //post group info

                //go back
                finish();
                break;
            case R.id.ag_buttonclean:
                ad_groupname=findViewById(R.id.ag_name);
                ad_groupname.setText("");
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            compoundButton.setBackgroundResource(R.drawable.group14);
        }else{
            compoundButton.setBackgroundResource(R.drawable.group15);
        }
    }
}
