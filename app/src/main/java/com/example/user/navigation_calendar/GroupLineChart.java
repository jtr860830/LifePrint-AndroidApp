package com.example.user.navigation_calendar;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupLineChart extends AppCompatActivity implements View.OnClickListener {

    ImageButton back;

    //宣告字串陣列
    private String[] lineweek_list = {"1 week","2 week","3 week"}; //宣告字串陣列
    private ArrayAdapter<String> barweek_listAdapter; //喧告listAdapter物件
    Spinner barweek;

    //宣告字串陣列
    private String[] linemonth_list = {"1 month","2 month","3 month","4 month","5 month","6 month",
            "7 month","8 month","9 month","10 month","11 month","12 month"};
    private ArrayAdapter<String> barmonth_listAdapter; //喧告listAdapter物件
    Spinner barmonth;

    //宣告字串陣列
    private String[] lineyear_list = {"1 year","2 year","3 year","4 year","5 year"};
    private ArrayAdapter<String> baryear_listAdapter; //喧告listAdapter物件
    Spinner baryear;

    private StringBuffer stringBuffer;
    private String groupname = null;

    List<Entry> GrouplineData = new ArrayList<>();

    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;

    private String groupline_getUrl = "https://sd.jezrien.one/user/group/analysis/2";
    Http_Get HGBG;

    ArrayList barstr = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_line_chart);

        back=findViewById(R.id.lc_back);
        back.setOnClickListener(this);
        getSpinnerItem();

        //set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = NsharedPreferences.getString("TOKEN", "");

        // get_line
        HGBG.Get(groupline_getUrl, token, groupname);
        resultJSON = HGBG.getTt();
        line_parseJSON(resultJSON);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.group1));
        colors.add(getResources().getColor(R.color.group2));
        colors.add(getResources().getColor(R.color.group3));
        colors.add(getResources().getColor(R.color.group4));
        colors.add(getResources().getColor(R.color.group5));


        // Line
        LineChart lineChart = findViewById(R.id.chart_line);
        LineDataSet dataSet = new LineDataSet(GrouplineData, "Month");
        dataSet.setColor(R.color.darkRed);
        LineData lineData = new LineData(dataSet);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setData(lineData);
        lineChart.invalidate();

    }

    public void line_parseJSON(String result) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i=0; i<array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                Integer cnt = obj.getInt("Cnt");
                GrouplineData.add(new Entry(i, cnt));
                Log.d("JSON:",groupname+"/"+cnt);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getSpinnerItem(){

        //選擇"週"的下拉式選單
        barweek=findViewById(R.id.groupbar_spinner1);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        barweek_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, lineweek_list);
        //設定下拉選單的樣式
        barweek_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        barweek.setAdapter(barweek_listAdapter);
        //設定項目被選取之後的動作
        barweek.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(GroupLineChart.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(GroupLineChart.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });
        //選擇"月"的下拉式選單
        barmonth=findViewById(R.id.groupbar_spinner2);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        barmonth_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, linemonth_list);
        //設定下拉選單的樣式
        barmonth_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        barmonth.setAdapter(barmonth_listAdapter);
        //設定項目被選取之後的動作
        barmonth.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(GroupLineChart.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(GroupLineChart.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });

        //選擇"年"的下拉式選單
        baryear=findViewById(R.id.groupbar_spinner3);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        baryear_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, lineyear_list);
        //設定下拉選單的樣式
        baryear_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        baryear.setAdapter(baryear_listAdapter);
        //設定項目被選取之後的動作
        baryear.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(GroupLineChart.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(GroupLineChart.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lc_back:
                finish();
                break;
        }
    }
}
