package com.example.user.navigation_calendar;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class PersonalMap extends AppCompatActivity implements View.OnClickListener {

    ImageButton back;
    //下拉式選單
    private String[] Pcategory_list = {"All","Business","Gathering","Dinner","Travel","Others"}; //宣告字串陣列
    private ArrayAdapter<String> Pcategory_listAdapter; //喧告listAdapter物件
    Spinner Pcategory;

    private String[] Pweek_list = {"1 week","2 week","3 week"}; //宣告字串陣列
    private ArrayAdapter<String> Pweek_listAdapter; //喧告listAdapter物件
    Spinner Pweek;

    //宣告字串陣列
    private String[] Pmonth_list = {"1 month","2 month","3 month","4 month","5 month","6 month",
            "7 month","8 month","9 month","10 month","11 month","12 month"};
    private ArrayAdapter<String> Pmonth_listAdapter; //喧告listAdapter物件
    Spinner Pmonth;

    //宣告字串陣列
    private String[] Pyear_list = {"1 year","2 year","3 year","4 year","5 year"};
    private ArrayAdapter<String> Pyear_listAdapter; //喧告listAdapter物件
    Spinner Pyear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_map);

        back=findViewById(R.id.pm_back);
        back.setOnClickListener(this);
        category_menu();
        getPMSpinnerItem();

    }

    public void category_menu(){
        //選擇"類別"的下拉式選單
        Pcategory=findViewById(R.id.pm_spinner);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Pcategory_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner,Pcategory_list);
        //設定下拉選單的樣式
        Pcategory_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Pcategory.setAdapter(Pcategory_listAdapter);
        //設定項目被選取之後的動作
        Pcategory.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(PersonalMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                if (adapterView.getSelectedItem().toString() == "Business"){
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block1));
                }else if (adapterView.getSelectedItem().toString() == "Gathering"){
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block2));
                }else if (adapterView.getSelectedItem().toString() == "Dinner"){
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block3));
                }else if (adapterView.getSelectedItem().toString() == "Travel"){
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block4));
                }else if (adapterView.getSelectedItem().toString() == "Others"){
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block5));
                }

            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(PersonalMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPMSpinnerItem(){

        //選擇"週"的下拉式選單
        Pweek=findViewById(R.id.pm_spinnerweek);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Pweek_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, Pweek_list);
        //設定下拉選單的樣式
        Pweek_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Pweek.setAdapter(Pweek_listAdapter);
        //設定項目被選取之後的動作
        Pweek.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(PersonalMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(PersonalMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });
        //選擇"月"的下拉式選單
        Pmonth=findViewById(R.id.pm_spinnermonth);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Pmonth_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, Pmonth_list);
        //設定下拉選單的樣式
        Pmonth_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Pmonth.setAdapter(Pmonth_listAdapter);
        //設定項目被選取之後的動作
        Pmonth.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(PersonalMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(PersonalMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });

        //選擇"年"的下拉式選單
        Pyear=findViewById(R.id.pm_spinneryear);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Pyear_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, Pyear_list);
        //設定下拉選單的樣式
        Pyear_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Pyear.setAdapter(Pyear_listAdapter);
        //設定項目被選取之後的動作
        Pyear.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(PersonalMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(PersonalMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pm_back:
                finish();
                break;
        }

    }
}
