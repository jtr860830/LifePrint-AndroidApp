package com.example.user.navigation_calendar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class Addmember extends DialogFragment implements View.OnClickListener {

    ImageButton add;
    EditText userID;
    String token;

    String getGroupname;

    //存放要Post的訊息
    private String AdduserID = null;
    private String groupname = null;
    private String postUrl = "https://sd.jezrien.one/user/group/member";
    static Handler handler; //宣告成static讓service可以直接使用
    Http_AddMemberPost HAMP;


    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_addmember, container);
        add = view.findViewById(R.id.add_member);
        add.setOnClickListener(this);
        userID=view.findViewById(R.id.add_memberID);
        //get groupname
        //getGroupname=view.findViewById(R.id.);

        HAMP=new Http_AddMemberPost();

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 11:
                        String ss = (String) msg.obj;
                        //Toast.makeText(Login.this, ss, Toast.LENGTH_LONG).show();
                        getToken(ss);
                        Fragment prev = getFragmentManager().findFragmentByTag("fragment_dialog");
                        if (prev != null) {
                            DialogFragment df = (DialogFragment) prev;
                            df.dismiss();
                        }
                        break;
                }
            }

        };
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }



    public void getToken(String s){
        try {
            JSONObject jsonObject= new JSONObject(s);
            token=jsonObject.getString("token");
            Toast.makeText(MainActivity.this, token, Toast.LENGTH_LONG).show();

            //寫入token
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(this);
            sharedPreferences.edit().putString("TOKEN", token).apply();

        }catch (JSONException e){
            e.printStackTrace();
        }



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_member:
                if (userID!=null){
                    AdduserID = userID.getEditableText().toString();
                    //抓到groupname
                    groupname=getGroupname;
                    HAMP.Post(AdduserID,groupname,postUrl);
                }



                break;
        }
    }

}
