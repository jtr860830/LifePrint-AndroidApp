package com.example.user.navigation_calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class GroupList extends AppCompatActivity {

    RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        recyclerview=findViewById(R.id.gl_recyclerview);
        // Grid型態，第二個參數控制一列顯示幾項
        recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        /*
        // 第二個參數VERTICAL或HORIZONTAL控制垂直或水平
        recyclerview.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        */


    }
}
