package com.example.user.navigation_calendar;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class month extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private eventAdapter adapter;
    private ImageButton addevent;
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

        recyclerView = view.findViewById(R.id.recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        List<eventCard> Event = new ArrayList<>();
        Event.add(new eventCard("title1", "event1", "location1"));
        Event.add(new eventCard("title2", "event2", "location2"));

        adapter = new eventAdapter(Event);
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

}

class eventCard {
    private String title;
    private String event;
    private String location;

    public eventCard(String title, String event, String location) {
        this.title = title;
        this.event = event;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        public TextView title;
        public TextView event;
        public TextView location;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            event = v.findViewById(R.id.event_time);
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
        holder.title.setText(data.get(position).getTitle());
        holder.event.setText(data.get(position).getEvent());
        holder.location.setText(data.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
