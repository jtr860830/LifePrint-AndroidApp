package com.example.user.navigation_calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText username;
    EditText password;
    ImageButton btn_login;
    Button btn_forgotpassword;
    Button btn_createAccount;
    //
    String token;


    //存放要Post的訊息
    private String Susername = null;
    private String Spassword = null;

    private String postUrl = "https://sd.jezrien.one/login";
    static Handler handler; //宣告成static讓service可以直接使用
    Http_LoginPost HLP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        HLP= new Http_LoginPost();

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        btn_createAccount=findViewById(R.id.btn_createAccount);
        btn_login=findViewById(R.id.btn_login);
        btn_forgotpassword=findViewById(R.id.btn_forgetpassword);

        btn_forgotpassword.setOnClickListener(this);
        btn_createAccount.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 2:
                        String ss = (String) msg.obj;
                        //Toast.makeText(Login.this, ss, Toast.LENGTH_LONG).show();
                        getToken(ss);
                        Intent itCalendar = new Intent(Login.this,MainActivity.class);
                        startActivity(itCalendar);
                        break;
                    case 3:
                        String ss2 = (String) msg.obj;
                        Toast.makeText(Login.this, ss2, Toast.LENGTH_LONG).show();
                        username.setText("");
                        password.setText("");
                        break;
                }
            }

        };

    }

    public void getToken(String s){
        try {
            JSONObject jsonObject= new JSONObject(s);
            token=jsonObject.getString("token");
            Toast.makeText(Login.this, token, Toast.LENGTH_LONG).show();

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
            case R.id.btn_createAccount:
                Intent itcreateaccount = new Intent(Login.this,CreateAccount.class);
                startActivity(itcreateaccount);
                break;
            case R.id.btn_login:
                if (username!=null && password!=null){
                    Susername = username.getEditableText().toString();
                    Spassword= password.getEditableText().toString();
                    HLP.Post(Susername,Spassword,postUrl);
                }
                break;
            case R.id.btn_forgetpassword:
                Intent itfp = new Intent(Login.this,ForgotPassword.class);
                startActivity(itfp);
                break;
        }

    }
}
