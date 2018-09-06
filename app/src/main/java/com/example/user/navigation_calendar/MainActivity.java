package com.example.user.navigation_calendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.os.StrictMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private static final String TAG = "fri_sug";
    public ArrayList<String> username;
    public  ArrayList<Bitmap> userimage;
    private TextView title;
    NavigationView drawer_navigationView;

    String person_name;
    String person_email;

    //存放要Get的訊息
    private String getUrl = "https://sd.jezrien.one/user/profile";
    Http_Get HUG;
    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;
    private static final int REFRESH = 1;

    //Get Group Name
    String groupname = null;
    private String getGNUrl = "https://sd.jezrien.one/user/group/";
    Http_Get HGNG;
    public ArrayList<String> groupnameList;//存group name

    //menu group
    Menu  mySchedual;
    Menu  settings;
    Menu  other;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //Member 的array資料
        username = new ArrayList<>();
        userimage = new ArrayList<>();
        title = findViewById(R.id.toolbar_title);

        //滑開頁面
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        drawer = findViewById(R.id.drawer_layout);

        drawer_navigationView = findViewById(R.id.nav_view);
        drawer_navigationView.setNavigationItemSelectedListener(this);

        View header = drawer_navigationView.inflateHeaderView(R.layout.drawer_header);


        drawer_navigationView.invalidate();

        //set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = NsharedPreferences.getString("TOKEN", "");

        //get group name
        groupnameList = new ArrayList<>();
        groupnameList.add("Personal");
        HGNG = new Http_Get();
        HGNG.Get(getGNUrl, token);
        resultJSON=HGNG.getTt();
        ArrayList<String> GN = GNparseJSON(resultJSON, groupnameList);

        //動態產生群組
        addGroup(GN);

        //get username & email
        List<PerInfoCard> trans = new ArrayList<>();
        HUG = new Http_Get();
        HUG.Get(getUrl, token);
        resultJSON = HUG.getTt();
        parseJSON(resultJSON, trans);
        getUserInfo(header);


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
    }

    public void addGroup(ArrayList<String> GN) {

        NavigationView drawer_navigationView = findViewById(R.id.nav_view);
        final Menu menu = drawer_navigationView.getMenu();

        //groupID 1 "My Schedual"
        //ItemID i=1
        //Menu  mySchedual
        mySchedual= menu.addSubMenu("My Schedual");
        for(int i=0;i<GN.size();i++){
            mySchedual.add(1,i+5,Menu.FIRST,GN.get(i)).setIcon(R.drawable.menu_schedule);
        }

        //groupID 2 "Settings"
        //ItemID 1,2
        //Menu  settings
        settings = menu.addSubMenu("Settings");
        settings.add(2,1,Menu.FIRST,"Personal Settings").setIcon(R.drawable.user);
        settings.add(2,2,Menu.FIRST,"Group Settings").setIcon(R.drawable.member);

        //Menu  other
        other = menu.addSubMenu("Others");
        other.add(3,3,Menu.FIRST,"Your Track").setIcon(R.drawable.place);
        other.add(3,4,Menu.FIRST,"Exit").setIcon(R.drawable.exit);

    }



    public void parseJSON(String result, List<PerInfoCard> trans) {
        try {
            JSONObject array = new JSONObject(result);
            person_name = array.getString("Username");
            person_email = array.getString("Email");

            Log.d("JSON:", person_name + "/" + person_email);
            trans.add(new PerInfoCard(person_name, person_email));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> GNparseJSON(String result, ArrayList<String> stringArrayList) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i=0; i<array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                String group_name=obj.getString("Name");

                Log.d("JSON:",group_name);
                stringArrayList.add(group_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stringArrayList;
    }

    public void getUserInfo(View header){
        TextView username = header.findViewById(R.id.per_name);
        TextView useremail = header.findViewById(R.id.per_email);

        username.setText(person_name);
        useremail.setText(person_email);

        ImageButton addgroup = header.findViewById(R.id.btn_addgroup);
        addgroup.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
    //滑開頁面上方選單-->AddGroup
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_addgroup:
                Intent itAddgroup = new Intent(MainActivity.this, AddGroup.class);
                startActivityForResult(itAddgroup, REFRESH);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case RESULT_OK:
                this.recreate();
                break;
            case RESULT_CANCELED:
                break;
        }
    }

    //滑開頁面選單
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        //Settings
        if (id == settings.getItem(0).getItemId()){
            //Personal Settings
            Intent itPS=new Intent(MainActivity.this,PersonalSettings.class);
            startActivity(itPS);
            return true;
        } else if (id == settings.getItem(1).getItemId()) {
            //Group Settings
            Intent itGS = new Intent(MainActivity.this, GroupSetting.class);
            startActivity(itGS);
            return true;
        }
        //Other
        if (id == other.getItem(0).getItemId()) {
            //Map
            Intent itmap = new Intent(MainActivity.this, maps.class);
            startActivity(itmap);
            return true;
        } else if (id == other.getItem(1).getItemId()) {
            //Exit
            Toast.makeText(MainActivity.this,"exit",Toast.LENGTH_SHORT).show();
            return true;
        }

        //Schedual
        if (id-5 < groupnameList.size()) {
            //group calendar
            groupname = groupnameList.get(id-5);
            if (groupname.equals("Personal")) groupname = null;
            Toast.makeText(MainActivity.this, groupname, Toast.LENGTH_SHORT).show();
            Bundle data =  new Bundle();
            data.putString("groupname", groupname);
            month fragment_month = new month();
            fragment_month.setArguments(data);
            FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
            transaction3.replace(R.id.fragment_space, fragment_month, "Fragment_month");
            transaction3.commit();
        }

        return true;
    }

    //下方選單
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.nav_notes:
                            title.setText("Notes");
                            notes fragment_notes = new notes();
                            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                            transaction1.replace(R.id.fragment_space, fragment_notes, "FragmentNotes");
                            transaction1.commit();
                            break;
                        //return true;
                        case R.id.nav_count:
                            title.setText("Countdown");
                            count fragment_count = new count();
                            FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                            transaction2.replace(R.id.fragment_space, fragment_count, "Fragment_count");
                            transaction2.commit();
                            break;
                        //return true;
                        case R.id.nav_month:
                            title.setText("Month");
                            Bundle data =  new Bundle();
                            data.putString("groupname", groupname);
                            month fragment_month = new month();
                            fragment_month.setArguments(data);
                            FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                            transaction3.replace(R.id.fragment_space, fragment_month, "Fragment_month");
                            transaction3.commit();
                            break;
                        //return true;
                        case R.id.nav_feed:
                            title.setText("Analysis");
                            feed fragment_feed = new feed();
                            FragmentTransaction transaction4 = getSupportFragmentManager().beginTransaction();
                            transaction4.replace(R.id.fragment_space, fragment_feed, "Fragment_feed");
                            transaction4.commit();
                            break;
                        //return true;
                        case R.id.nav_member:
                            title.setText("Member");
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

//JSON-->data
class PerInfoCard {
    private String username;
    private String email;

    public PerInfoCard(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String title) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}


