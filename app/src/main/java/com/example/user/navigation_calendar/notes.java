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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class notes extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private eventAdapter adapter;

    public notes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notes, container, false);
        ImageButton add_notes=(ImageButton)view.findViewById(R.id.btn_addNotes);
        add_notes.setOnClickListener(this);

        recyclerView=view.findViewById(R.id.recyclerview);
        final LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);



        recyclerView.setAdapter(adapter);

        return view;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_addNotes:
                Intent itadd=new Intent(getActivity(),AddNotes.class);
                startActivity(itadd);
                break;
        }
    }
}
