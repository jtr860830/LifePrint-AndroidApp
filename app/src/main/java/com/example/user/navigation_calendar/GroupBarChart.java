package com.example.user.navigation_calendar;

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
import android.widget.ImageButton;
import android.widget.TextView;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Mapping;
import com.anychart.anychart.MarkerType;
import com.anychart.anychart.Stroke;
import com.anychart.anychart.TooltipPositionMode;
import com.anychart.anychart.ValueDataEntry;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GroupBarChart extends AppCompatActivity implements View.OnClickListener {

    ImageButton back;


    private StringBuffer stringBuffer;
    private String groupname = null;
    private RecyclerView recyclerView;
    private BCmemberAdapter adapter;
    private List<BCMemberCard> trans;
    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_bar_chart);
        groupname = getIntent().getExtras().getString("groupname");
        back=findViewById(R.id.bc_back);
        back.setOnClickListener(this);

        //set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = NsharedPreferences.getString("TOKEN", "");

        recyclerView = findViewById(R.id.bc_recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        trans = new ArrayList<>();

        Http_Get HMG = new Http_Get();
        if (groupname != null) {
            HMG.Get("https://sd.jezrien.one/user/group/analysis", token, groupname);
            resultJSON = HMG.getTt();
            parseJSON(resultJSON, trans);
        }

        adapter = new BCmemberAdapter(trans);
        recyclerView.setAdapter(adapter);


        AnyChartView anyChartView = findViewById(R.id.group_barchart);
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







    }

    public void parseJSON(String result, List<BCMemberCard> trans) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i=0; i<array.length(); i++){
                JSONObject obj = array.getJSONObject(i);

                String mem_username = obj.getString("Username");
                int mem_cnt = obj.getInt("Cnt");


                Log.d("JSON:",mem_username+"/"+mem_cnt);
                trans.add(new BCMemberCard(mem_username, mem_cnt));
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
        }
    }

}

//JSON-->data
class BCMemberCard {

    private String username;
    private int data;

    public BCMemberCard(String username, int data) {
        this.data = data;
        this.username = username;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

}

class BCmemberAdapter extends RecyclerView.Adapter<BCmemberAdapter.ViewHolder> {
    private List<BCMemberCard> data;

    public BCmemberAdapter(List<BCMemberCard> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;

        public ViewHolder(View v) {
            super(v);
            username = v.findViewById(R.id.BC_username);
        }
    }

    @Override
    public BCmemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.barchart_member_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(data.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}