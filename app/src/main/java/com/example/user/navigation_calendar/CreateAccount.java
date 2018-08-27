package com.example.user.navigation_calendar;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity implements View.OnClickListener {

    EditText username;
    EditText birthday;
    EditText password;
    EditText email;

    ImageButton postBtn;
    ImageButton btn_close;
    private String postUrl = "https://sd.jezrien.one/register";

    //存放要Post的訊息
    private String Susername = null;
    private String Sbirthday = null;
    private String Spassword = null;
    private String Semail = null;

    Http_AccountPost HAP;
    static Handler handler; //宣告成static讓service可以直接使用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        HAP= new Http_AccountPost();

        username = findViewById(R.id.edt_username);
        birthday = findViewById(R.id.edt_birthday);
        password = findViewById(R.id.login_password);
        email = findViewById(R.id.edt_email);
        postBtn = findViewById(R.id.btn_create);
        btn_close=findViewById(R.id.account_close);

        //讓多個Button共用一個Listener，在Listener中再去設定各按鈕要做的事
        postBtn.setOnClickListener(this);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        String ss = (String) msg.obj;
                        Toast.makeText(CreateAccount.this, ss, Toast.LENGTH_LONG).show();
                        Intent it = new Intent(CreateAccount.this,Login.class);
                        startActivity(it);
                        break;
                }
            }

        };

    }

    @Override
    public void onClick(View view) {
        if (username != null && password !=null) {
            //取得EditText的內容
            Susername = username.getEditableText().toString();
            Sbirthday= birthday.getEditableText().toString();
            Spassword= password.getEditableText().toString();
            Semail= email.getEditableText().toString();
            HAP.Post(Susername,Sbirthday,Spassword,Semail,postUrl);
        }

    }

    public void back(View v){
        Intent it = new Intent(this, Login.class);
        startActivity(it);
    }

}
