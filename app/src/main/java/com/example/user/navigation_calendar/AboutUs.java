package com.example.user.navigation_calendar;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class AboutUs extends AppCompatActivity implements View.OnClickListener {

    ImageButton aboutus_back;
    Button link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        aboutus_back=findViewById(R.id.aboutus_back);
        aboutus_back.setOnClickListener(this);
        link=findViewById(R.id.btn_link);
        link.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aboutus_back:
                finish();
                break;
            case R.id.btn_link:
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jtr860830/SD-AndroidApp"));
                startActivity(intent);
                break;
        }
    }
}
