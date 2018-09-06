package com.example.user.navigation_calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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

public class GroupBarChart extends AppCompatActivity implements View.OnClickListener {

    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_bar_chart);

        back=findViewById(R.id.bc_back);
        back.setOnClickListener(this);

        AnyChartView anyChartView = findViewById(R.id.Bar_anychart_view);
        //anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));


        //groupName -->bar
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> Gdata = new ArrayList<>();
        Gdata.add(new ValueDataEntry("Rouge", 80540));
        Gdata.add(new ValueDataEntry("Foundation", 94190));
        Gdata.add(new ValueDataEntry("Mascara", 102610));
        Gdata.add(new ValueDataEntry("Lip gloss", 110430));
        Gdata.add(new ValueDataEntry("Lipstick", 128000));

        cartesian.column(Gdata);
        //Column column = cartesian.column(data);
        anyChartView.setChart(cartesian);


        /*
        //user-->pie
        Pie pie = AnyChart.pie();
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(this, event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("John", 10000));
        data.add(new ValueDataEntry("Jake", 12000));
        data.add(new ValueDataEntry("Peter", 18000));

        pie.setData(data);
        anyChartView.setChart(pie);
        */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bc_back:
                finish();
                break;
        }
    }
}
