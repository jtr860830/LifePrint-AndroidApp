package com.example.user.navigation_calendar;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class notes extends Fragment implements View.OnClickListener, NoteItemTouchHelperListener {

    private RecyclerView recyclerView;
    private noteAdapter adapter;
    private List<noteCard> trans;

    //存放要Get的訊息
    private String getUrl = "https://sd.jezrien.one/user/backups";
    Http_Get HNG;
    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;

    public notes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notes, container, false);
        ImageButton add_notes = view.findViewById(R.id.btn_addNotes);
        add_notes.setOnClickListener(this);

        //set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = NsharedPreferences.getString("TOKEN", "");

        recyclerView = view.findViewById(R.id.recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        trans = new ArrayList<>();
        //get
        HNG = new Http_Get();
        HNG.Get(getUrl,token);
        resultJSON = HNG.getTt();
        parseJSON(resultJSON, trans);


        adapter = new noteAdapter(trans);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new NoteItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        return view;

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof noteAdapter.ViewHolder) {
            int delIndex = viewHolder.getAdapterPosition();
            adapter.removeItem(delIndex);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_addNotes:
                Intent itadd = new Intent(getActivity(),AddNotes.class);
                startActivity(itadd);
                break;
        }
    }

    public void parseJSON(String result, List<noteCard> trans) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i=0; i<array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                String title = obj.getString("Title");
                String content = obj.getString("Info");
                String importance = obj.getString("Importance");

                Log.d("JSON:",title+"/"+content+"/"+importance);
                trans.add(new noteCard(title, content,importance));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


//JSON-->data
class noteCard {
    private String title;
    private String content;
    private String importance;


    public noteCard(String title, String content,String importance) {
        this.title = title;
        this.content = content;
        this.importance=importance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String star){this.importance=importance;    }
}

class noteAdapter extends RecyclerView.Adapter<noteAdapter.ViewHolder> {
    private List<noteCard> data;

    public noteAdapter(List<noteCard> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView content;
        public TextView star;
        public RelativeLayout viewBackground;
        public ConstraintLayout viewForeground;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.note_title);
            content = v.findViewById(R.id.note_content);
            star=v.findViewById(R.id.note_star);
            viewBackground = v.findViewById(R.id.view_background);
            viewForeground = v.findViewById(R.id.view_foreground);
        }
    }

    @Override
    public noteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(data.get(position).getTitle());
        holder.content.setText(data.get(position).getContent());
        holder.star.setText(data.get(position).getImportance());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void removeItem(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }
}


