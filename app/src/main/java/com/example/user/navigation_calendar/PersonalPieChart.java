package com.example.user.navigation_calendar;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonalPieChart extends AppCompatActivity implements View.OnClickListener {

    ImageButton back;

    //宣告字串陣列
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

    //存放要Get的訊息
    private String pie_getUrl = "https://sd.jezrien.one/user/analysis/2";
    Http_Get HPG;

    private String bar_getUrl = "https://sd.jezrien.one/user/analysis/1";
    Http_Get HBG;


    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;

    List<PieEntry> pieData = new ArrayList<>();
    List<BarEntry> barData = new ArrayList<>();
    ArrayList barstr = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_pie_chart);
        String username = getIntent().getExtras().getString("username");
        TextView title = findViewById(R.id.textView9);
        title.setText(username + " Chart");

        back=findViewById(R.id.ppc_back);
        back.setOnClickListener(this);
        getSpinnerItem();

        //set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = NsharedPreferences.getString("TOKEN", "");


        //get_pie
        HPG = new Http_Get();
        HPG.Get(pie_getUrl,token);
        resultJSON = HPG.getTt();
        pie_parseJSON(resultJSON);


        //get_bar
        HBG = new Http_Get();
        HBG.Get(bar_getUrl,token);
        resultJSON = HBG.getTt();
        bar_parseJSON(resultJSON);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.group1));
        colors.add(getResources().getColor(R.color.group2));
        colors.add(getResources().getColor(R.color.group3));
        colors.add(getResources().getColor(R.color.group4));
        colors.add(getResources().getColor(R.color.group5));

        // Pie
        PieChart pieChart = findViewById(R.id.chart_pie);
        PieDataSet dataSet = new PieDataSet(pieData, "Group");
        dataSet.setColors(colors);
        PieData piedata = new PieData(dataSet);
        piedata.setDrawValues(true);
        pieChart.setHoleRadius(0);
        pieChart.setTransparentCircleRadius(0);
        pieChart.setData(piedata);
        pieChart.invalidate();

        // Bar
        BarChart barChart = findViewById(R.id.chart_bar);
        barChart.setDrawValueAboveBar(true);
        BarDataSet barDataSet = new BarDataSet(barData, "Group");
        barDataSet.setColors(colors);
        BarData bardata = new BarData(barDataSet);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.setDrawGridBackground(false);
        barChart.setData(bardata);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(barstr));
        barChart.invalidate();
    }


    public void getSpinnerItem(){

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


    }

    public void pie_parseJSON(String result) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i=0; i<array.length(); i++){
                JSONObject obj = array.getJSONObject(i);

                String Pgroupname=obj.getString("Groupname");
                Double Pcnt = obj.getDouble("Cnt");

                pieData.add(new PieEntry(Pcnt.floatValue(), Pgroupname));

                Log.d("JSON:",Pgroupname+"/"+Pcnt);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void bar_parseJSON(String result) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i=0; i<array.length() || i<5; i++){
                JSONObject obj = array.getJSONObject(i);

                String Bgroupname=obj.getString("Groupname");
                Integer Bcnt = obj.getInt("Cnt");

                barData.add(new BarEntry(i, Bcnt));
                barstr.add(Bgroupname);

                Log.d("JSON:",Bgroupname + "/" + Bcnt);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ppc_back:
                finish();
                break;
        }
    }
}
