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
import android.widget.TextClock;
import android.widget.TextView;
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
    ImageButton gbar;

    private StringBuffer stringBuffer;
    private String groupname = null;

    List<Entry> GrouplineData = new ArrayList<>();

    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;

    private String groupline_getUrl = "https://sd.jezrien.one/user/group/analysis/2";
    Http_Get HGLG;

    ArrayList barstr = new ArrayList();

    TextView high;
    TextView middle;
    TextView low;

    String[] heartBeat = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_line_chart);

        groupname = getIntent().getExtras().getString("groupname");
        TextView title = findViewById(R.id.textView40);
        title.setText(groupname + " Analysis (2/2)");

        back=findViewById(R.id.lc_back);
        back.setOnClickListener(this);
        gbar=findViewById(R.id.imgbtn_gbarchart);
        gbar.setOnClickListener(this);

        //
        heartBeat[0] = "";
        heartBeat[1] = "";
        heartBeat[2] = "";
        high=findViewById(R.id.textView45);
        middle=findViewById(R.id.textView46);
        low=findViewById(R.id.textView47);

        //set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = NsharedPreferences.getString("TOKEN", "");

        // get_line
        groupname = getIntent().getExtras().getString("groupname");
        HGLG = new Http_Get();
        HGLG.Get(groupline_getUrl, token, groupname);
        resultJSON = HGLG.getTt();
        line_parseJSON(resultJSON);

        high.setText(heartBeat[0]);
        middle.setText(heartBeat[1]);
        low.setText(heartBeat[2]);

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
            Integer max=0;
            Integer min=0;

            for (int i=0; i<array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                int cnt = obj.getInt("Cnt");
                GrouplineData.add(new Entry(i, cnt));
                Log.d("JSON:",groupname+"/"+cnt);
                if (cnt == 0) {
                    heartBeat[2] += " " + (i+1) + "月";
                } else if (cnt >= 5 && cnt < 10) {
                    heartBeat[1] += " " + (i+1) + "月";
                } else if (cnt >= 10) {
                    heartBeat[0] += " " + (i+1) + "月";
                }





            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lc_back:
                finish();
                break;
            case R.id.imgbtn_gbarchart:
                finish();
                break;
        }
    }
}