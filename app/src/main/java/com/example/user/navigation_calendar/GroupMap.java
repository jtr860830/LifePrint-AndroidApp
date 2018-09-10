package com.example.user.navigation_calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class GroupMap extends AppCompatActivity implements View.OnClickListener {

    ImageButton GMback;
    //下拉式選單
    private String[] Gcategory_list = {"All","Business","Gathering","Dinner","Travel","Others"}; //宣告字串陣列
    private ArrayAdapter<String> Gcategory_listAdapter; //喧告listAdapter物件
    Spinner Gcategory;

    private String[] Gweek_list = {"1 week","2 week","3 week"}; //宣告字串陣列
    private ArrayAdapter<String> Gweek_listAdapter; //喧告listAdapter物件
    Spinner Gweek;

    //宣告字串陣列
    private String[] Gmonth_list = {"1 month","2 month","3 month","4 month","5 month","6 month",
            "7 month","8 month","9 month","10 month","11 month","12 month"};
    private ArrayAdapter<String> Gmonth_listAdapter; //喧告listAdapter物件
    Spinner Gmonth;

    //宣告字串陣列
    private String[] Gyear_list = {"1 year","2 year","3 year","4 year","5 year"};
    private ArrayAdapter<String> Gyear_listAdapter; //喧告listAdapter物件
    Spinner Gyear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_map);

        GMback=findViewById(R.id.gm_back);
        GMback.setOnClickListener(this);
        category_menu();
        getGMSpinnerItem();

    }

    public void category_menu(){
        //選擇"類別"的下拉式選單
        Gcategory=findViewById(R.id.pm_spinner);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Gcategory_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner,Gcategory_list);
        //設定下拉選單的樣式
        Gcategory_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Gcategory.setAdapter(Gcategory_listAdapter);
        //設定項目被選取之後的動作
        Gcategory.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(GroupMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                if (adapterView.getSelectedItem().toString() == "Business"){
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block1));
                }else if (adapterView.getSelectedItem().toString() == "Gathering"){
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block2));
                }else if (adapterView.getSelectedItem().toString() == "Dinner"){
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block3));
                }else if (adapterView.getSelectedItem().toString() == "Travel"){
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block4));
                }else if (adapterView.getSelectedItem().toString() == "Others"){
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block5));
                }

            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(GroupMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getGMSpinnerItem(){

        //選擇"週"的下拉式選單
        Gweek=findViewById(R.id.pm_spinnerweek);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Gweek_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, Gweek_list);
        //設定下拉選單的樣式
        Gweek_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Gweek.setAdapter(Gweek_listAdapter);
        //設定項目被選取之後的動作
        Gweek.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(GroupMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(GroupMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });
        //選擇"月"的下拉式選單
        Gmonth=findViewById(R.id.pm_spinnermonth);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Gmonth_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, Gmonth_list);
        //設定下拉選單的樣式
        Gmonth_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Gmonth.setAdapter(Gmonth_listAdapter);
        //設定項目被選取之後的動作
        Gmonth.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(GroupMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(GroupMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });

        //選擇"年"的下拉式選單
        Gyear=findViewById(R.id.pm_spinneryear);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Gyear_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, Gyear_list);
        //設定下拉選單的樣式
        Gyear_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Gyear.setAdapter(Gyear_listAdapter);
        //設定項目被選取之後的動作
        Gyear.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(GroupMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(GroupMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gm_back:
                finish();
                break;
        }
    }
}
