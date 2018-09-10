package com.example.user.navigation_calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_pie_chart);

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

        pie.setData(data);
        anyChartView_Pie.setChart(pie);


        //user-->bar chart
        AnyChartView anyChartView_Bar = findViewById(R.id.bar_anychart_view);
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> Gdata = new ArrayList<>();
        Gdata.add(new ValueDataEntry("Rouge", 80540));
        Gdata.add(new ValueDataEntry("Foundation", 94190));

        cartesian.column(Gdata);
        anyChartView_Bar.setChart(cartesian);

    }
}
