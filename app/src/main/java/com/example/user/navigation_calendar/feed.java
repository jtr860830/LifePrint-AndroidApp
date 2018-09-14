package com.example.user.navigation_calendar;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;




import java.util.ArrayList;
import java.util.List;




/**
 * A simple {@link Fragment} subclass.
 */
public class feed extends Fragment implements View.OnClickListener {

    ImageButton goto_barchart;
    ImageButton goto_map;
    private String groupname = null;
    private String username = null;

    public feed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_feed, container, false);
        groupname = getArguments().getString("groupname");
        username = getArguments().getString("username");

        goto_barchart=view.findViewById(R.id.btn_barchart);
        goto_map=view.findViewById(R.id.btn_groupmap);

        if (groupname == null) {
            goto_barchart.setImageResource(android.R.color.transparent);
            goto_barchart.setBackgroundResource(R.drawable.group76);
            goto_map.setImageResource(android.R.color.transparent);
            goto_map.setBackgroundResource(R.drawable.group77);
        }

        goto_barchart.setOnClickListener(this);
        goto_map.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_barchart:
                //go to group bar chart
                if (groupname == null) {
                    Intent itmap = new Intent(getActivity(),PersonalPieChart.class);
                    itmap.putExtra("username", username);
                    startActivity(itmap);
                } else {
                    Intent itchart = new Intent(getActivity(),GroupBarChart.class);
                    itchart.putExtra("groupname", groupname);
                    startActivity(itchart);
                }
                break;
            case R.id.btn_groupmap:
                //go to group map
                if (groupname == null) {
                    Intent itPmap = new Intent(getActivity(),PersonalMap.class);
                    itPmap.putExtra("username", username);
                    startActivity(itPmap);
                } else {
                    Intent itGchart = new Intent(getActivity(),GroupMap.class);
                    itGchart.putExtra("groupname", groupname);
                    startActivity(itGchart);
                }



                break;
        }
    }
}
