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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.location.Address;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.DecimalFormat;
import java.util.List;

import com.anychart.anychart.StockRangeType;
import com.schibstedspain.leku.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewEvent extends AppCompatActivity implements View.OnClickListener {

    private ImageButton doSetDateFrom;
    private ImageButton doSetTimeFrom;
    private TextView textDateFrom;
    private TextView textTimeFrom;
    private DatePickerDialog datePickerDialogFrom;
    private TimePickerDialog timePickerDialogFrom;
    final DecimalFormat df = new DecimalFormat("00");

    private ImageButton doSetDateTo;
    private ImageButton doSetTimeTo;
    private TextView textDateTo;
    private TextView textTimeTo;
    private DatePickerDialog datePickerDialogTo;
    private TimePickerDialog timePickerDialogTo;
    private ImageView lc;

    //post取得元件
    EditText new_title;
    TextView start_date;
    TextView start_time;
    TextView end_date;
    TextView end_time;
    Spinner group;
    Spinner category;
    private String[] category_list = {"Party","Dinner","Travel","Business","Others"}; //宣告字串陣列
    private ArrayAdapter<String> category_listAdapter; //喧告listAdapter物件
    ArrayList<String> groupname_list = new ArrayList<>();
    private ArrayAdapter<String> groupname_listAdapter;


    EditText new_location;
    ImageButton save;
    ImageButton close;

    //存放要Post的訊息
    private String Stitle = null;
    private String Sstart = null;
    private String Send=null;
    private String Sgroup=null;
    private String Scategory=null;
    private String Slocation=null;
    private String token;
    SharedPreferences sharedPreferences;
    double latitude;
    double longitude;
    String address;

    //存放要Get的訊息
    private String getUrl = "https://sd.jezrien.one/user/group/";
    Http_Get HNEG;
    private String resultJSON;




    private String postUrl = "https://sd.jezrien.one/user/schedules";
    static Handler handler; //宣告成static讓service可以直接使用
    Http_NewEventPost HNEP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        HNEP=new Http_NewEventPost();
        //set token
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = sharedPreferences.getString("TOKEN", "");

        //get
        HNEG = new Http_Get();
        HNEG.Get(getUrl,token);
        resultJSON = HNEG.getTt();
        parseJSON(resultJSON);

        getelement();
        doFindView();
        DateFrom();
        DateTo();
    }

    public void parseJSON(String result) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i=0; i<array.length(); i++){
                JSONObject obj = array.getJSONObject(i);

                String GN = obj.getString("Name");
                Log.d("JSON:", GN);
                groupname_list.add(GN);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }
    public void getelement(){
        new_title=findViewById(R.id.ne_title);
        start_date=findViewById(R.id.datetextFrom);
        start_time=findViewById(R.id.timetextFrom);
        end_date=findViewById(R.id.datetextTo);
        end_time=findViewById(R.id.timetextTo);
        new_location=findViewById(R.id.ne_location);
        save=findViewById(R.id.new_save);
        close=findViewById(R.id.new_back);
        lc = findViewById(R.id.imageView4);

        //群組選單
        group=findViewById(R.id.group_spinner);
        groupname_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, groupname_list);
        groupname_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        group.setAdapter(groupname_listAdapter);
        //設定項目被選取之後的動作
        group.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(NewEvent.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(NewEvent.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });


        //類別選單
        category=findViewById(R.id.category_spinner);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        category_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, category_list);//預設android.R.layout.simple_list_item_1
        //設定下拉選單的樣式
        category_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        category.setAdapter(category_listAdapter);
        //設定項目被選取之後的動作
        category.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(NewEvent.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(NewEvent.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });

        lc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new LocationPickerActivity.Builder()
                        .withLocation(25.163711623792803, 121.44841454923154)
                        .withGeolocApiKey("AIzaSyCg2QiaDfiy3cGzlp3DIJeZbEaK82N9OhM")
                        .withSearchZone("zh_TW")
                        .shouldReturnOkOnBackPressed()
                        .withGooglePlacesEnabled()
                        .build(getApplicationContext());

                startActivityForResult(intent, 1);
            }
        });



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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                latitude = data.getDoubleExtra("latitude", 0);
                Log.d("LATITUDE****", String.valueOf(latitude));
                longitude = data.getDoubleExtra("longitude", 0);
                Log.d("LONGITUDE****", String.valueOf(longitude));
                address = data.getStringExtra("location_address");
                Log.d("ADDRESS****", String.valueOf(address));
                //String postalcode = data.getStringExtra("zipcode");
                //Log.d("POSTALCODE****", String.valueOf(postalcode));
                //Bundle bundle = data.getBundleExtra("transition_bundle");
                //Log.d("BUNDLE TEXT****", bundle.getString("test"));
                //Address fullAddress = data.getParcelableExtra("address");
                //if(fullAddress != null)
                    //Log.d("FULL ADDRESS****", fullAddress.toString());
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    public void DateFrom(){
        GregorianCalendar calendar= new GregorianCalendar();
        //實作DatePickerDialog的onDateSet方法，設定日期後將所有設定的日期show在textDate上
        datePickerDialogFrom= new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear=monthOfYear+1;
                textDateFrom.setText(year + "-" + df.format(monthOfYear) + "-" + df.format(dayOfMonth));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        //實作TimePickerDialog的onTimeSet方法，設定日期後將所有設定的日期show在textTime上
        timePickerDialogFrom= new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                textTimeFrom.setText(df.format(hourOfDay)+":"+df.format(minute));
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
                textDateTo.setText(year + "-" + df.format(monthOfYear) + "-" + df.format(dayOfMonth));
            }
        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        //實作TimePickerDialog的onTimeSet方法，設定日期後將所有設定的日期show在textTime上
        timePickerDialogTo= new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                textTimeTo.setText(df.format(hourOfDay)+":"+df.format(minute));
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
                    Scategory=category.getSelectedItem().toString();
                    Sgroup=group.getSelectedItem().toString();
                    Slocation=new_location.getEditableText().toString();

                    HNEP.Post(Stitle,Sstart,Send,Scategory,Sgroup,Slocation,longitude,latitude,postUrl,token);



                    Log.d("Time",Sstart);
                }
                break;

            case R.id.new_back:
                Intent itcreateaccount = new Intent(NewEvent.this, MainActivity.class);
                startActivity(itcreateaccount);

                break;
        }
    }
}


