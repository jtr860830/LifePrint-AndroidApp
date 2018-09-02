package com.example.user.navigation_calendar;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class Addmember extends DialogFragment {

    ImageButton add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invite_dialog, container);
        add = view.findViewById(R.id.imageButton);
        return view;
    }

}