package com.example.user.navigation_calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Pie;
import com.anychart.anychart.ValueDataEntry;
import com.anychart.anychart.chart.common.Event;
import com.anychart.anychart.chart.common.ListenersInterface;

import java.util.ArrayList;
import java.util.List;

public class PersonalPieChart extends AppCompatActivity {

    private String[] week_list = {"1 week","2 week","3 week"}; //宣告字串陣列
    private ArrayAdapter<String> week_listAdapter; //喧告listAdapter物件
    Spinner week;

    //宣告字串陣列
    private String[]month_list = {"1 month","2 month","3 month","4 month","5 month","6 month",
                                    "7 month","8 month","9 month","10 month","11 month","12 month"};
    private ArrayAdapter<String> month_listAdapter; //喧告listAdapter物件
    Spinner month;

    //宣告字串陣列
    private String[] year_list = {"1 year","2 year","3 year","4 year","5 year"};
    private ArrayAdapter<String> year_listAdapter; //喧告listAdapter物件
    Spinner year;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_pie_chart);

        //選擇"週"的下拉式選單
        week=findViewById(R.id.spinner2);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        week_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, week_list);
        //設定下拉選單的樣式
        week_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        week.setAdapter(week_listAdapter);
        //設定項目被選取之後的動作
        week.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(PersonalPieChart.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(PersonalPieChart.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });
        //選擇"月"的下拉式選單
        month=findViewById(R.id.spinner3);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        month_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, month_list);
        //設定下拉選單的樣式
        month_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        month.setAdapter(month_listAdapter);
        //設定項目被選取之後的動作
        month.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(PersonalPieChart.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(PersonalPieChart.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });

        //選擇"年"的下拉式選單
        year=findViewById(R.id.spinner4);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        year_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, year_list);
        //設定下拉選單的樣式
        year_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        year.setAdapter(year_listAdapter);
        //設定項目被選取之後的動作
        year.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(PersonalPieChart.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(PersonalPieChart.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });

        AnyChartView anyChartView_Pie = findViewById(R.id.Pie_anychart_view);

        //user-->pie chart
        Pie pie = AnyChart.pie();
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(PersonalPieChart.this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("John", 10000));
        data.add(new ValueDataEntry("Jake", 12000));
        data.add(new ValueDataEntry("Peter", 18000));

        //pie.setFill("FFFFFF");
        pie.fill("FFFFFF",1);

        pie.setStroke("#00FFFF");//圓餅圖周圍顏色
        pie.setData(data);
        anyChartView_Pie.setChart(pie);


        //user-->bar chart
        AnyChartView anyChartView_Bar = findViewById(R.id.bar_anychart_view);
        Cartesian cartesian = AnyChart.column();

        cartesian.setPalette("#FFFFFF");
        List<DataEntry> Gdata = new ArrayList<>();
        Gdata.add(new ValueDataEntry("Rouge", 80540));
        Gdata.add(new ValueDataEntry("Foundation", 94190));

        cartesian.column(Gdata);

        anyChartView_Bar.setChart(cartesian);





    }
}
