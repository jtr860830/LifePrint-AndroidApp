package com.example.user.navigation_calendar;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.DialogFragment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class members extends Fragment implements View.OnClickListener {

    //private static final int RESULT_OK = 1;
    private RecyclerView recyclerView;
    private memberAdapter adapter;
    private ImageButton add;


    //存放要Get的訊息
    private String getUrl = "https://sd.jezrien.one/user/group/member";
    Http_Get HMG;
    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;
    private String groupname;
    List<memberCard> trans;
    SwipeRefreshLayout mRefreshLayout;

    public members() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_members, container, false);
        add = view.findViewById(R.id.Add);

        mRefreshLayout = view.findViewById(R.id.layout_swipe_refresh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                trans.clear();
                HMG.Get(getUrl, token, groupname);
                resultJSON = HMG.getTt();
                parseJSON(resultJSON, trans);
                mRefreshLayout.setRefreshing(false);
            }
        });

        //set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        token = NsharedPreferences.getString("TOKEN", "");

        //add member dialog
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                Addmember dialog = new Addmember();
                args.putString("groupname", groupname);
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), "fragment_dialog");
            }
        });

        //recyclerView
        recyclerView = view.findViewById(R.id.member_recycler);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        trans = new ArrayList<>();

        //get
        groupname = getArguments().getString("groupname");
        HMG = new Http_Get();

        if (groupname != null) {
            getUrl = "https://sd.jezrien.one/user/group/member";
            HMG.Get(getUrl, token, groupname);
            resultJSON = HMG.getTt();
            parseJSON(resultJSON, trans);
        } else {
            Toast.makeText(getActivity(), "Your state is not in a group, please choose a group", Toast.LENGTH_SHORT).show();
        }

        adapter = new memberAdapter(trans);
        recyclerView.setAdapter(adapter);
/*
        List<memberCard> Member = new ArrayList<>();
        Member.add(new memberCard(R.mipmap.ic_launcher ,"isabel"));
        Member.add(new memberCard(R.mipmap.ic_launcher ,"leah"));
*/
        //Change Group Background


        return view;


    }

    public void parseJSON(String result, List<memberCard> trans) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i=0; i<array.length(); i++){
                JSONObject obj = array.getJSONObject(i);

                String mem_photo = obj.getString("Sticker");
                String mem_username = obj.getString("Username");


                Log.d("JSON:",mem_username+"/"+mem_photo);
                trans.add(new memberCard(mem_photo, mem_username));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //變更群組照片
    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}
class memberCard {
    private String photo;
    private String username;

    public memberCard(String photo, String username) {
        this.photo = photo;
        this.username = username;
    }
    /*
    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    */
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

}


class memberAdapter extends RecyclerView.Adapter<memberAdapter.ViewHolder> {
    private List<memberCard> data;

    public memberAdapter(List<memberCard> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public ImageView photo;
        public TextView username;


        public ViewHolder(View v) {
            super(v);
            //photo = v.findViewById(R.id.userimage);
            username = v.findViewById(R.id.username);
        }
    }

    @Override
    public memberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.members_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.photo.setImageResource(data.get(position).getPhoto());
        holder.username.setText(data.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
