package com.example.user.navigation_calendar;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class month extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private eventAdapter adapter;
    private ImageButton addevent;

    //存放要Get的訊息
    private String getUrl = "https://sd.jezrien.one/user/schedules";
    Http_NewEventGet HNEG;
    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;

    public month() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_month, container, false);

        addevent=view.findViewById(R.id.btn_addevent);
        addevent.setOnClickListener(this);

        //set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = NsharedPreferences.getString("TOKEN", "");

        recyclerView = view.findViewById(R.id.recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        List<eventCard> trans = new ArrayList<>();
        //Event.add(new eventCard("title1", "event1", "location1"));
        //Event.add(new eventCard("title2", "event2", "location2"));

        //get
        HNEG = new Http_NewEventGet();
        HNEG.Get(getUrl,token);
        resultJSON = HNEG.getTt();
        parseJSON(resultJSON, trans);

        adapter = new eventAdapter(trans);
        recyclerView.setAdapter(adapter);

        return view;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_addevent:
                Intent itaddevent = new Intent(getActivity(),NewEvent.class);
                startActivity(itaddevent);
                break;
        }
    }

    public void parseJSON(String result, List<eventCard> trans) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i=0; i<array.length(); i++){
                JSONObject obj = array.getJSONObject(i);

                String event = obj.getString("Event");
                String startTime = obj.getString("Start");
                String location = obj.getString("Location");

                Log.d("JSON:",event+"/"+startTime+"/"+location);
                trans.add(new eventCard(event, startTime,location));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
//JSON-->data
class eventCard {
    private String starttime;
    private String event;
    private String location;

    public eventCard(String event, String starttime, String location) {
        this.event = event;
        this.starttime = starttime;
        this.location = location;
    }

    public String getStartTime() {
        return starttime;
    }

    public void setStartTime(String starttime) {
        this.starttime = starttime;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

class eventAdapter extends RecyclerView.Adapter<eventAdapter.ViewHolder> {
    private List<eventCard> data;

    public eventAdapter(List<eventCard> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView starttime;
        public TextView event;
        public TextView location;

        public ViewHolder(View v) {
            super(v);

            event = v.findViewById(R.id.event_title);
            starttime = v.findViewById(R.id.event_time);
            location = v.findViewById(R.id.location);
        }
    }

    @Override
    public eventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.starttime.setText(data.get(position).getStartTime());
        holder.event.setText(data.get(position).getEvent());
        holder.location.setText(data.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
