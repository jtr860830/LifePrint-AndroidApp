package com.example.user.navigation_calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;


public class member_Adapter extends  RecyclerView.Adapter<member_Adapter.ViewHolder>{

    private static final String TAG = "member_Adapter";

    private ArrayList<Bitmap> userimage=new ArrayList<>();
    private ArrayList<String> username=new ArrayList<>();
    private Context mcontext;


    public member_Adapter(ArrayList<String> username, ArrayList<Bitmap> userimage, Context mcontext) {
        this.userimage = userimage;
        this.username = username;
        this.mcontext = mcontext;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.members_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder:  called.");

        Glide.with(mcontext).asBitmap().load(userimage.get(position)).into(holder.Muserimage);
        holder.Musername.setText(username.get(position));
        /*holder.mem_re.setOnClickListener((view)->{
            Log.d(TAG, "onclick: click on : "+mFuserimage.get(position));
            Intent intent=new Intent(mFcontext,personal.class);
            intent.putExtra("fuserid",mFuserId.get(position));
            mcontext.startActivity(intent);
        });*/

    }

    @Override
    public int getItemCount() {
        return username.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView Muserimage;
        TextView Musername;
        RelativeLayout mem_re ;


        public ViewHolder(View root) {
            super(root);
            Muserimage=itemView.findViewById(R.id.userimage);
            Musername=itemView.findViewById(R.id.username);
            mem_re=itemView.findViewById(R.id.mem_re);
        }
    }
}
