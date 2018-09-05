package com.example.user.navigation_calendar;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class count extends Fragment {

    RecyclerView recyclerView;
    private cdAdapter adapter;

    List<cdItem> trans;

    public count() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_count, container, false);

        recyclerView = view.findViewById(R.id.cdRecyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        trans = new ArrayList<>();
        trans.add(new cdItem("Personal", "event1", "time1", "1"));
        trans.add(new cdItem("Group", "event2", "time2", "2"));
        trans.add(new cdItem("Group", "event3", "time3", "3"));
        trans.add(new cdItem("Group", "event4", "time4", "4"));

        adapter = new cdAdapter(trans);
        recyclerView.setAdapter(adapter);

        return view;
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

            event = v.findViewById(R.id.textView5);
            start = v.findViewById(R.id.textView8);
            belongsTo = v.findViewById(R.id.textView9);
            cd = v.findViewById(R.id.textView10);
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