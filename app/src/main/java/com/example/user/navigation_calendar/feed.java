package com.example.user.navigation_calendar;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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




/**
 * A simple {@link Fragment} subclass.
 */
public class feed extends Fragment implements View.OnClickListener {

    ImageButton goto_barchart;
    ImageButton goto_map;
    private String groupname = null;


    public feed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_feed, container, false);
        groupname = getArguments().getString("groupname");

        goto_barchart=view.findViewById(R.id.btn_barchart);
        goto_barchart.setOnClickListener(this);
        goto_map=view.findViewById(R.id.btn_groupmap);
        goto_map.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_barchart:
                //go to group bar chart
                Intent itchart = new Intent(getActivity(),GroupBarChart.class);
                itchart.putExtra("groupname", groupname);
                startActivity(itchart);

                break;
            case R.id.btn_groupmap:
                //go to group map
                Intent itmap = new Intent(getActivity(),PersonalPieChart.class);
                itmap.putExtra("groupname", groupname);
                startActivity(itmap);

                break;
        }
    }
}
