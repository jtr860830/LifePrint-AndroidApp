package com.example.user.navigation_calendar;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

public class AddNotes extends AppCompatActivity implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    EditText note_title;
    RatingBar note_star;
    EditText note_content;
    ImageButton save;
    ImageButton back;
    private String postUrl = "https://sd.jezrien.one/user/backups";

    //存放要Post的訊息
    private String Snotetitle = null;
    private String star;
    private String Snotecontent = null;

    Http_NotePost HNP;
    static Handler handler; //宣告成static讓service可以直接使用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        HNP=new Http_NotePost();

        note_title=findViewById(R.id.ne_title);
        note_star=findViewById(R.id.ratingBar);
        note_content=findViewById(R.id.edt_content);
        save=findViewById(R.id.btn_save);
        back=findViewById(R.id.btn_back);

        //設定星星數量
        note_star.setMax(5);//設定最大值
        note_star.setNumStars(5);//設定最大星型數量
        note_star.setStepSize(1);//一次增加一顆星
        note_star.setRating(3);//設定目前顯示的星星數量
        note_star.setIsIndicator(false);//設定是否可被使用者修改評分
        note_star.setOnRatingBarChangeListener(this);//設定監聽器



        //讓多個Button共用一個Listener，在Listener中再去設定各按鈕要做的事
        save.setOnClickListener(this);
        back.setOnClickListener(this);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 5:
                        String ss = (String) msg.obj;
                        Toast.makeText(AddNotes.this, ss, Toast.LENGTH_LONG).show();
                        Intent it = new Intent(AddNotes.this,notes.class);
                        startActivity(it);
                        break;
                    case 6:
                        String ss2 = (String) msg.obj;
                        Toast.makeText(AddNotes.this, ss2, Toast.LENGTH_LONG).show();
                        break;
                }
            }

        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                finish();
            break;

            case R.id.btn_save:
            if (note_title!=null && note_content!=null){
                Snotetitle=note_title.getEditableText().toString();
                star=note_star.toString();
                Snotecontent=note_content.getEditableText().toString();
                HNP.Post(Snotetitle,star,Snotecontent,postUrl);
            }
            break;
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromuser) {
        Toast.makeText(getApplicationContext(), "important: " + rating, Toast.LENGTH_SHORT).show();
    }
}
