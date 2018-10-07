package com.example.user.navigation_calendar;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonalBarChart extends AppCompatActivity implements View.OnClickListener {

    ImageButton back;
    ImageButton ppie;

    //存放要Get的訊息
    private String bar_getUrl = "https://sd.jezrien.one/user/analysis/1";
    Http_Get HPBG;

    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;

    List<BarEntry> barData = new ArrayList<>();
    ArrayList barstr = new ArrayList();

    String username;
    TextView NO1;
    TextView last;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_bar_chart);

        username = getIntent().getExtras().getString("username");
        TextView title = findViewById(R.id.textView30);
        title.setText(username + " Analysis (2/2)");

        back=findViewById(R.id.pbc_back);
        back.setOnClickListener(this);
        ppie=findViewById(R.id.imgbtn_ppiechart);
        ppie.setOnClickListener(this);

        //
        NO1=findViewById(R.id.bar_no1);
        last=findViewById(R.id.bar_no);

        //set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = NsharedPreferences.getString("TOKEN", "");

        //get_bar
        HPBG = new Http_Get();
        HPBG.Get(bar_getUrl,token);
        resultJSON = HPBG.getTt();
        bar_parseJSON(resultJSON);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.group1));
        colors.add(getResources().getColor(R.color.group2));
        colors.add(getResources().getColor(R.color.group3));
        colors.add(getResources().getColor(R.color.group4));
        colors.add(getResources().getColor(R.color.group5));

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

    public void bar_parseJSON(String result) {
        try {
            JSONArray array = new JSONArray(result);
            Integer no1=0;
            Integer no=0;
            for (int i=0; i<array.length() || i<5; i++){
                JSONObject obj = array.getJSONObject(i);

                String Bgroupname=obj.getString("Groupname");
                Integer Bcnt = obj.getInt("Cnt");

                barData.add(new BarEntry(i, Bcnt));
                barstr.add(Bgroupname);

                Log.d("JSON:",Bgroupname + "/" + Bcnt);

                if (Bcnt.intValue()>no1){
                    no1= Integer.valueOf(Bcnt.intValue());
                    NO1.setText(Bgroupname);
                }
                if(Bcnt.intValue() == no){
                    last.setText(Bgroupname);
                }else{
                    last.setText("You are a warm person");
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pbc_back:
                finish();
                break;
            case R.id.imgbtn_ppiechart:
                finish();
                break;
        }
    }
}
