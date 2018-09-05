package com.example.user.navigation_calendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

public class AddGroup extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    ImageButton group_save;
    ImageButton back;

    ImageButton ag_clean;
    ImageView group_pic;
    EditText ad_groupname;
    ToggleButton ag_notification;
    SharedPreferences sharedPreferences;

    //驗證username
    String token;
    //存放要Post的訊息
    private String picture_path = null;
    private String GroupName = null;

    private String postUrl = "https://sd.jezrien.one/user/group/";
    static Handler handler; //宣告成static讓service可以直接使用
    Http_AddGroupPost HAGP;


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = sharedPreferences.getString("TOKEN", "");

        HAGP=new Http_AddGroupPost();

        group_save=findViewById(R.id.ag_save);
        group_save.setOnClickListener(this);

        back=findViewById(R.id.ag_back);
        back.setOnClickListener(this);

        ag_clean=findViewById(R.id.ag_buttonclean);
        ag_clean.setOnClickListener(this);

        ad_groupname=findViewById(R.id.ag_name);

        group_pic=findViewById(R.id.group_pic);

        ag_notification=findViewById(R.id.ag_toggleButton);
        ag_notification.setTextOn("");
        ag_notification.setTextOff("");
        ag_notification.setChecked(false);
        ag_notification.setOnCheckedChangeListener(this);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 12:
                        String ss = (String) msg.obj;
                        //Toast.makeText(Login.this, ss, Toast.LENGTH_LONG).show();
                        //getToken(ss);
                        finish();
                        //Intent itCalendar = new Intent(AddGroup.this,MainActivity.class);
                        //startActivity(itCalendar);
                        break;
                    case 13:
                        String ss2 = (String) msg.obj;
                        Toast.makeText(AddGroup.this, ss2, Toast.LENGTH_LONG).show();
                        //ad_groupname.setText("");

                        break;
                }
            }

        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ag_back:
                finish();
                break;
            case R.id.ag_save:
                //post group info
                if (ad_groupname!=null){
                    GroupName =ad_groupname.getEditableText().toString();
                    picture_path=group_pic.toString();
                    HAGP.Post(GroupName, picture_path, postUrl, token);
                }
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
