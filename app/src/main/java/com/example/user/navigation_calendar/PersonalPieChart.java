package com.example.user.navigation_calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    ImageButton pbar;

    //存放要Get的訊息
    private String pie_getUrl = "https://sd.jezrien.one/user/analysis/2";
    Http_Get HPG;



    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;
    String username;

    List<PieEntry> pieData = new ArrayList<>();
    ArrayList barstr = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_pie_chart);

        username = getIntent().getExtras().getString("username");
        TextView title = findViewById(R.id.textView10);
        title.setText(username + " Analysis (2/1)");

        back=findViewById(R.id.ppc_back);
        back.setOnClickListener(this);
        pbar=findViewById(R.id.imgbtn_pbarchart);
        pbar.setOnClickListener(this);

        //set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = NsharedPreferences.getString("TOKEN", "");


        //get_pie
        HPG = new Http_Get();
        HPG.Get(pie_getUrl,token);
        resultJSON = HPG.getTt();
        pie_parseJSON(resultJSON);


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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ppc_back:
                finish();
                break;
            case R.id.imgbtn_pbarchart:
                Intent ittt = new Intent(this,PersonalBarChart.class);
                ittt.putExtra(username, "username");
                startActivity(ittt);
                break;
        }
    }
}
