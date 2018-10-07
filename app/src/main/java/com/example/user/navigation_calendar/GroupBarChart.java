package com.example.user.navigation_calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GroupBarChart extends AppCompatActivity implements View.OnClickListener {

    ImageButton back;
    ImageButton gline;

    private StringBuffer stringBuffer;
    private String groupname = null;

    List<BarEntry> GroupbarData = new ArrayList<>();


    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;

    private String groupbar_getUrl = "https://sd.jezrien.one/user/group/analysis/1";

    Http_Get HGBG;

    ArrayList barstr = new ArrayList();

    TextView leader;
    TextView nextone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_bar_chart);

        groupname = getIntent().getExtras().getString("groupname");
        TextView title = findViewById(R.id.textView9);
        title.setText(groupname + " Analysis (1/2)");
        back=findViewById(R.id.bc_back);
        back.setOnClickListener(this);
        gline=findViewById(R.id.imgbtn_glinechart);
        gline.setOnClickListener(this);

        //
        leader=findViewById(R.id.textView39);
        nextone=findViewById(R.id.textView41);

        //set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = NsharedPreferences.getString("TOKEN", "");

        //get_bar
        HGBG = new Http_Get();
        HGBG.Get(groupbar_getUrl, token, groupname);
        resultJSON = HGBG.getTt();
        bar_parseJSON(resultJSON);



        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.group1));
        colors.add(getResources().getColor(R.color.group2));
        colors.add(getResources().getColor(R.color.group3));
        colors.add(getResources().getColor(R.color.group4));
        colors.add(getResources().getColor(R.color.group5));

        // Bar
        BarChart barChart = findViewById(R.id.chart_bar2);
        barChart.setDrawValueAboveBar(true);
        BarDataSet barDataSet = new BarDataSet(GroupbarData, "Member");
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

                String groupname=obj.getString("Username");
                Integer cnt = obj.getInt("Cnt");

                GroupbarData.add(new BarEntry(i, cnt));
                barstr.add(groupname);

                Log.d("JSON:",groupname+"/"+cnt);

                if (cnt.intValue()>no1){
                    no1=Integer.valueOf(cnt.intValue());
                    leader.setText(groupname);
                }
                if(cnt.intValue() == no){
                    nextone.setText(groupname);
                }else{
                    nextone.setText("大家都不是邊緣人");
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bc_back:
                finish();
                break;
            case R.id.imgbtn_glinechart:
                Intent itlinechart = new Intent(GroupBarChart.this,GroupLineChart.class);
                itlinechart.putExtra("groupname", groupname);
                startActivity(itlinechart);
                break;
        }
    }

}