package com.example.user.navigation_calendar;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class count extends Fragment {

    private RecyclerView recyclerView;
    private cdAdapter adapter;
    private List<cdItem> trans;

    TextView first_event;
    TextView first_day;

    //存放要Get的訊息
    private String getUrl = "https://sd.jezrien.one/user/countdown";
    Http_Get HCG;
    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;

    public count() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_count, container, false);

        //set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = NsharedPreferences.getString("TOKEN", "");

        recyclerView = view.findViewById(R.id.cdRecyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        trans = new ArrayList<>();
        //get
        HCG = new Http_Get();
        HCG.Get(getUrl,token);
        resultJSON = HCG.getTt();
        parseJSON(resultJSON, trans);

        adapter = new cdAdapter(trans);
        //分隔線
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        first_event=view.findViewById(R.id.count_event);
        first_day=view.findViewById(R.id.count_day);

        if (trans.isEmpty()){
            first_day.setText("0");
            first_event.setText("Event");
        }else{
            first_day.setText(trans.get(0).getCd());
            first_event.setText(trans.get(0).getEvent());
        }
        return view;
    }

    public void parseJSON(String result, List<cdItem> trans) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i=0; i<array.length(); i++){
                JSONObject obj = array.getJSONObject(i);

                String belong = obj.getString("BelongsTo");
                String event = obj.getString("Event");
                String start_time = obj.getString("StartTime");
                String count_day= Integer.toString(obj.getInt("CD"));

                Log.d("JSON:",belong+"/"+event+"/"+start_time+"/"+count_day);
                trans.add(new cdItem(belong, event,start_time,count_day));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

class cdItem {
    private String belongsTo;
    private String event;
    private String startTime;
    private String cd;

    public cdItem(String belogsTo, String event, String startTime, String cd) {
        this.belongsTo = belogsTo;
        this.event = event;
        this.startTime = startTime;
        this.cd = cd;
    }

    public String getBelongsTo() {
        return belongsTo;
    }

    public void setBelongsTo(String belogsTo) {
        this.belongsTo = belogsTo;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getStartTime() {

        startTime=startTime.substring(0,10);
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }
}

class cdAdapter extends RecyclerView.Adapter<cdAdapter.ViewHolder> {
    private List<cdItem> data;

    public cdAdapter(List<cdItem> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView event;
        public TextView start;
        public TextView belongsTo;
        public TextView cd;

        public ViewHolder(View v) {
            super(v);

            event = v.findViewById(R.id.cd_event);
            start = v.findViewById(R.id.cd_starttime);
            belongsTo = v.findViewById(R.id.cd_group);
            cd = v.findViewById(R.id.cd_day);

        }
    }

    @Override
    public cdAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cd_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.event.setText(data.get(position).getEvent());
        holder.start.setText(data.get(position).getStartTime());
        holder.belongsTo.setText(data.get(position).getBelongsTo());
        holder.cd.setText(data.get(position).getCd());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}