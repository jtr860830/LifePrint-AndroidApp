package com.example.user.navigation_calendar;

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
    private String[] category_list = {"Business","Gathering","Dinner","Travel","Others"}; //宣告字串陣列
    private ArrayAdapter<String> category_listAdapter; //喧告listAdapter物件
    Spinner category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_map);

        back=findViewById(R.id.pm_back);
        back.setOnClickListener(this);
        category_menu();


    }

    public void category_menu(){
        //選擇"週"的下拉式選單
        category=findViewById(R.id.pm_spinner);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        category_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner,category_list);
        //設定下拉選單的樣式
        category_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        category.setAdapter(category_listAdapter);
        //設定項目被選取之後的動作
        category.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(PersonalMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                if (adapterView.getSelectedItem().toString() == "Business"){
                    category.setBackgroundColor(getResources().getColor(R.color.block1));
                }else if (adapterView.getSelectedItem().toString() == "Gathering"){
                    category.setBackgroundColor(getResources().getColor(R.color.block2));
                }else if (adapterView.getSelectedItem().toString() == "Dinner"){
                    category.setBackgroundColor(getResources().getColor(R.color.block3));
                }else if (adapterView.getSelectedItem().toString() == "Travel"){
                    category.setBackgroundColor(getResources().getColor(R.color.block4));
                }else if (adapterView.getSelectedItem().toString() == "Others"){
                    category.setBackgroundColor(getResources().getColor(R.color.block5));
                }

            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(PersonalMap.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
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
