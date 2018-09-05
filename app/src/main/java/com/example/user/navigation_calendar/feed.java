package com.example.user.navigation_calendar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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




/**
 * A simple {@link Fragment} subclass.
 */
public class feed extends Fragment {


    public feed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_feed, container, false);

        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        //anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        /*
        //user-->pie
        Pie pie = AnyChart.pie();
        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getActivity(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("John", 10000));
        data.add(new ValueDataEntry("Jake", 12000));
        data.add(new ValueDataEntry("Peter", 18000));

        pie.setData(data);
        anyChartView.setChart(pie);
        */


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



        return view;
    }

}
