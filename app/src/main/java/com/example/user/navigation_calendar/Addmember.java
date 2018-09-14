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
import android.util.Log;
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
    //存放要Post的訊息
    private String AdduserID = null;
    private String groupname = null;
    private String postUrl = "https://sd.jezrien.one/user/group/member";
    static Handler handler; //宣告成static讓service可以直接使用
    Http_AddMemberPost HAMP;
    SharedPreferences NsharedPreferences;


    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_addmember, container);
        add = view.findViewById(R.id.add_member);
        add.setOnClickListener(this);
        userID = view.findViewById(R.id.add_memberID);
        groupname = getArguments().getString("groupname");
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = NsharedPreferences.getString("TOKEN", "");
        //取得group name，才能post
        // getGroupname=view.findViewById()


        HAMP = new Http_AddMemberPost();

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 11:
                        String ss = (String) msg.obj;
                        //Toast.makeText(Login.this, ss, Toast.LENGTH_LONG).show();
                        Log.d("Message",ss);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_member:
                if (groupname == null) {
                    Toast.makeText(getActivity(), "Your state is not in a group, please choose a group", Toast.LENGTH_SHORT).show();
                    return;
                }
                AdduserID = userID.getEditableText().toString();
                //抓到groupname
                HAMP.Post(AdduserID, groupname, postUrl, token);
                break;
        }
    }

}
