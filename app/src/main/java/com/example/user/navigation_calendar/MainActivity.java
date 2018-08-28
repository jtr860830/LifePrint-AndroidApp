package com.example.user.navigation_calendar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private static final String TAG = "fri_sug";
    public ArrayList<String> username;
    public  ArrayList<Bitmap> userimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Member 的array資料
        username=new ArrayList<>();
        userimage=new ArrayList<>();


        //滑開頁面
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView drawer_navigationView = findViewById(R.id.nav_view);
        drawer_navigationView.setNavigationItemSelectedListener(drawer_navigationViewListener);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //主頁面下方選單
        BottomNavigationView navigationView = findViewById(R.id.Navbar);
        navigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            navigationView.setSelectedItemId(R.id.nav_month); // change to whichever id should be default
        }
        //setTitle("Fragment title month");
        month fragment_month = new month();
        FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
        transaction3.replace(R.id.fragment_space, fragment_month, "Fragment_month");
        transaction3.commit();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    private NavigationView.OnNavigationItemSelectedListener drawer_navigationViewListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.personal_settings:
                            Intent itSche1=new Intent(MainActivity.this,PersonalSettings.class);
                            startActivity(itSche1);
                            break;


                        case R.id.exit:
                            Toast.makeText(MainActivity.this,"exit",Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            };

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.nav_notes:
                            //setTitle("fragment notes");
                            notes fragment_notes = new notes();
                            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                            transaction1.replace(R.id.fragment_space, fragment_notes, "FragmentNotes");
                            transaction1.commit();
                            break;
                        //return true;
                        case R.id.nav_count:
                            //setTitle("fragment count");
                            count fragment_count = new count();
                            FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                            transaction2.replace(R.id.fragment_space, fragment_count, "Fragment_count");
                            transaction2.commit();
                            break;
                        //return true;
                        case R.id.nav_month:
                            //setTitle("fragment month");
                            month fragment_month = new month();
                            FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                            transaction3.replace(R.id.fragment_space, fragment_month, "Fragment_month");
                            transaction3.commit();
                            break;
                        //return true;
                        case R.id.nav_feed:
                            // setTitle("fragment feed");
                            feed fragment_feed = new feed();
                            FragmentTransaction transaction4 = getSupportFragmentManager().beginTransaction();
                            transaction4.replace(R.id.fragment_space, fragment_feed, "Fragment_feed");
                            transaction4.commit();
                            break;
                        //return true;
                        case R.id.nav_member:
                            //setTitle("fragment setting");
                            members fragment_setting = new members();
                            FragmentTransaction transaction5 = getSupportFragmentManager().beginTransaction();
                            transaction5.replace(R.id.fragment_space, fragment_setting, "Fragment_members");
                            transaction5.commit();
                            break;
                        //return true;
                    }
                    return true;
                }
            };



}


