package com.example.user.navigation_calendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
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

    //驗證username
    private String token;
    SharedPreferences sharedPreferences;
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

        //addGroup();

        HAGP=new Http_AddGroupPost();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = sharedPreferences.getString("TOKEN", "");

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
                        Toast.makeText(AddGroup.this, ss, Toast.LENGTH_LONG).show();
                        getToken(ss);
                        finish();
                        //Intent itCalendar = new Intent(AddGroup.this,MainActivity.class);
                        //startActivity(itCalendar);

                        //add group name in drawer menu
                        //addGroup(GroupName);

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
/*
    private void addGroup() {

        NavigationView drawer_navigationView = findViewById(R.id.nav_view);
        final Menu menu = drawer_navigationView.getMenu();
        Menu submenu = menu.addSubMenu("New Super SubMenu");

        submenu.add("Super Item1");
        submenu.add("Super Item2");
        submenu.add("Super Item3");

    }
*/
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ag_back:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.ag_save:
                //post group info

                if (ad_groupname!=null){
                    GroupName =ad_groupname.getEditableText().toString();
                    picture_path=group_pic.toString();

                    HAGP.Post(GroupName,picture_path,postUrl,token);
                }
                //go back
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.ag_buttonclean:
                ad_groupname=findViewById(R.id.ag_name);
                ad_groupname.setText("");
                break;
        }
    }
    public void getToken(String s){
        try {
            JSONObject jsonObject= new JSONObject(s);
            token=jsonObject.getString("token");
            Toast.makeText(AddGroup.this, token, Toast.LENGTH_LONG).show();

            //寫入token
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(this);
            sharedPreferences.edit().putString("TOKEN", token).apply();

        }catch (JSONException e){
            e.printStackTrace();
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
