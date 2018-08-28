package com.example.user.navigation_calendar;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class PersonalSettings extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private PersonalSettingsAdapter adapter;
    ImageButton ps_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_settings);

        recyclerView = findViewById(R.id.ps_recyclerview);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        List<settingCard> psc = new ArrayList<>();

        psc.add(new settingCard("Notification","Accept schedule nitifications",R.id.toggleButton));
        adapter = new PersonalSettingsAdapter(psc);
        recyclerView.setAdapter(adapter);

        ps_back=findViewById(R.id.ps_back);
        ps_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ps_back:
                finish();
        }
    }
}

class settingCard{
    String title;
    String word;
    int button;

    public settingCard(String title, String word, int button) {
        this.title = title;
        this.word = word;
        this.button = button;
    }
    public void setTitle(String title){
        this.title=title;
    }
    public String getTitle() {
        return title;
    }
    public void setButton(int button) {
        this.button = button;
    }
    public int getButton() {
        return button;
    }
    public void setWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}

class PersonalSettingsAdapter extends RecyclerView.Adapter<PersonalSettingsAdapter.ViewHolder> {
    private List<settingCard> data;

    public PersonalSettingsAdapter(List<settingCard> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView PStitle;
        public TextView PSword;
        public ToggleButton PStoggleButton;


        public ViewHolder(View v) {
            super(v);
            PStitle=v.findViewById(R.id.ps_title);
            PSword=v.findViewById(R.id.ps_word);
            PStoggleButton=v.findViewById(R.id.toggleButton);

        }
    }

    @Override
    public PersonalSettingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.per_set_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.PStitle.setText(data.get(position).getTitle());
        holder.PSword.setText(data.get(position).getWord());
        holder.PStoggleButton.setTextOff(" ");
        holder.PStoggleButton.setTextOn(" ");
        holder.PStoggleButton.setChecked(false);
        holder.PStoggleButton.setBackgroundResource(R.drawable.group15);
        holder.PStoggleButton.setOnCheckedChangeListener(new toggleButton_OnCheckedChangeListener());


    }
    private class toggleButton_OnCheckedChangeListener
            implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(b){
                compoundButton.setBackgroundResource(R.drawable.group14);
            }else{
                compoundButton.setBackgroundResource(R.drawable.group15);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

