package com.example.user.navigation_calendar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class NewEvent extends AppCompatActivity implements View.OnClickListener {

    private ImageButton doSetDateFrom;
    private ImageButton doSetTimeFrom;
    private TextView textDateFrom;
    private TextView textTimeFrom;
    private DatePickerDialog datePickerDialogFrom;
    private TimePickerDialog timePickerDialogFrom;

    private ImageButton doSetDateTo;
    private ImageButton doSetTimeTo;
    private TextView textDateTo;
    private TextView textTimeTo;
    private DatePickerDialog datePickerDialogTo;
    private TimePickerDialog timePickerDialogTo;

    //post取得元件
    EditText new_title;
    TextView start_date;
    TextView start_time;
    TextView end_date;
    TextView end_time;
    EditText new_alert;
    EditText new_notes;
    EditText new_location;
    ImageButton save;
    ImageButton close;

    //存放要Post的訊息
    private String Stitle = null;
    private String Sstart = null;
    private String Send=null;
    private String Salert=null;
    private String Snotes=null;
    private String Slocation=null;
    private String token;
    SharedPreferences sharedPreferences;

    private String postUrl = "https://sd.jezrien.one/user/schedules";
    static Handler handler; //宣告成static讓service可以直接使用
    Http_NewEventPost HNEP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        HNEP=new Http_NewEventPost();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = sharedPreferences.getString("TOKEN", "");

        getelement();
        doFindView();
        DateFrom();
        DateTo();
    }

    public void getelement(){
        new_title=findViewById(R.id.ne_title);
        start_date=findViewById(R.id.datetextFrom);
        start_time=findViewById(R.id.timetextFrom);
        end_date=findViewById(R.id.datetextTo);
        end_time=findViewById(R.id.timetextTo);
        new_alert=findViewById(R.id.ne_alert);
        new_notes=findViewById(R.id.ne_note);
        new_location=findViewById(R.id.ne_location);
        save=findViewById(R.id.new_save);
        close=findViewById(R.id.new_back);

        save.setOnClickListener(this);
        close.setOnClickListener(this);
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 6:
                        String ss = (String) msg.obj;
                        Toast.makeText(NewEvent.this, ss, Toast.LENGTH_LONG).show();
                        Intent itCalendar = new Intent(NewEvent.this,MainActivity.class);
                        startActivity(itCalendar);
                        break;
                    case 7:
                        String ss2 = (String) msg.obj;
                        Toast.makeText(NewEvent.this, ss2, Toast.LENGTH_LONG).show();
                        break;
                }
            }

        };
    }




    public void DateFrom(){
        GregorianCalendar calendar= new GregorianCalendar();
        //實作DatePickerDialog的onDateSet方法，設定日期後將所有設定的日期show在textDate上
        datePickerDialogFrom= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear=monthOfYear+1;
                textDateFrom.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        //實作TimePickerDialog的onTimeSet方法，設定日期後將所有設定的日期show在textTime上
        timePickerDialogFrom= new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                textTimeFrom.setText(hourOfDay+":"+minute);
                //textTimeFrom.setText((hourOfDay>12 ? hourOfDay-12:hourOfDay)+":"+minute+" "+(hourOfDay>12?"PM" : "AM"));
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
    }
    public void DateTo(){
        GregorianCalendar calendar= new GregorianCalendar();
        //實作DatePickerDialog的onDateSet方法，設定日期後將所有設定的日期show在textDate上
        datePickerDialogTo= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear=monthOfYear+1;
                textDateTo.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        //實作TimePickerDialog的onTimeSet方法，設定日期後將所有設定的日期show在textTime上
        timePickerDialogTo= new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                textTimeTo.setText(hourOfDay+":"+minute);
                //textTimeTo.setText((hourOfDay>12 ? hourOfDay-12:hourOfDay)+":"+minute+" "+(hourOfDay>12?"PM" : "AM"));
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false);
    }
    //連上layout物件
    @SuppressLint("WrongViewCast")
    public void doFindView(){
        doSetDateFrom=(ImageButton)findViewById(R.id.dateButtonFrom);
        doSetTimeFrom=(ImageButton) findViewById(R.id.timeButtonFrom);
        textDateFrom=(TextView)findViewById(R.id.datetextFrom);
        textTimeFrom=(TextView)findViewById(R.id.timetextFrom);

        doSetDateTo=(ImageButton)findViewById(R.id.dateButtonTo);
        doSetTimeTo=(ImageButton) findViewById(R.id.timeButtonTo);
        textDateTo=(TextView)findViewById(R.id.datetextTo);
        textTimeTo=(TextView)findViewById(R.id.timetextTo);
    }
    public void setDateFrom(View v){
        datePickerDialogFrom.show();
    }
    public void setTimeFrom(View v){
        timePickerDialogFrom.show();
    }
    public void setDateTo(View v){
        datePickerDialogTo.show();
    }
    public void setTimeTo(View v){
        timePickerDialogTo.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.new_save:
                if (new_title!=null && start_date!=null &&
                        start_time!=null && end_date!=null && end_time!=null){

                    Stitle=new_title.getEditableText().toString();
                    Sstart=start_date.getText().toString()+"T"+start_time.getText().toString()+":00Z";
                    Send=end_date.getText().toString()+"T"+end_time.getText().toString()+":00Z";
                    Salert=new_alert.getEditableText().toString();
                    Slocation=new_location.getEditableText().toString();
                    Snotes=new_notes.getEditableText().toString();

                    HNEP.Post(Stitle,Sstart,Send,Salert,Slocation,Snotes,postUrl,token);
                    Log.d("Time",Sstart);
                }
                break;

            case R.id.new_back:
                Intent itcreateaccount = new Intent(NewEvent.this,MainActivity.class);
                startActivity(itcreateaccount);

                break;
        }
    }
}


