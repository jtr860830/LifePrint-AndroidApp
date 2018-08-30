package com.example.user.navigation_calendar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AddGroup extends AppCompatActivity implements View.OnClickListener {

    ImageButton group_save;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        group_save=findViewById(R.id.ag_save);
        back=findViewById(R.id.ag_back);
        group_save.setOnClickListener(this);
        back.setOnClickListener(this);


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

        }
    }
}
