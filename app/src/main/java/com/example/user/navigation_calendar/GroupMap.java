package com.example.user.navigation_calendar;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupMap extends AppCompatActivity implements View.OnClickListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    ImageButton GMback;
    //下拉式選單
    private String[] Gcategory_list = {"All","Business","Gathering","Dinner","Travel","Others"}; //宣告字串陣列
    private ArrayAdapter<String> Gcategory_listAdapter; //喧告listAdapter物件
    Spinner Gcategory;

    private String[] Gweek_list = {"1 week","2 week","3 week"}; //宣告字串陣列
    private ArrayAdapter<String> Gweek_listAdapter; //喧告listAdapter物件
    Spinner Gweek;

    //宣告字串陣列
    private String[] Gmonth_list = {"1 month","2 month","3 month","4 month","5 month","6 month",
            "7 month","8 month","9 month","10 month","11 month","12 month"};
    private ArrayAdapter<String> Gmonth_listAdapter; //喧告listAdapter物件
    Spinner Gmonth;

    //宣告字串陣列
    private String[] Gyear_list = {"1 year","2 year","3 year","4 year","5 year"};
    private ArrayAdapter<String> Gyear_listAdapter; //喧告listAdapter物件
    Spinner Gyear;

    private String groupname;

    // map
    private GoogleMap mMap;
    private ArrayList<LatLng> markers;

    //存放要Get的訊息
    private String Map_getUrl = "https://sd.jezrien.one/user/map";
    Http_Get HMG;

    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;

    List<MapData> mapData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_map);

        groupname = getIntent().getExtras().getString("groupname");
        GMback=findViewById(R.id.gm_back);
        GMback.setOnClickListener(this);
        category_menu();
        getGMSpinnerItem();

        // set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = NsharedPreferences.getString("TOKEN", "");

        // get
        HMG = new Http_Get();
        HMG.Get(Map_getUrl, token, groupname);
        resultJSON = HMG.getTt();
        map_parseJSON(resultJSON);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void category_menu(){
        //選擇"類別"的下拉式選單
        Gcategory=findViewById(R.id.gm_spinner);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Gcategory_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner,Gcategory_list);
        //設定下拉選單的樣式
        Gcategory_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Gcategory.setAdapter(Gcategory_listAdapter);
        //設定項目被選取之後的動作
        Gcategory.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(GroupMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                if (adapterView.getSelectedItem().toString() == "Business"){
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block1));
                }else if (adapterView.getSelectedItem().toString() == "Gathering"){
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block2));
                }else if (adapterView.getSelectedItem().toString() == "Dinner"){
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block3));
                }else if (adapterView.getSelectedItem().toString() == "Travel"){
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block4));
                }else if (adapterView.getSelectedItem().toString() == "Others"){
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block5));
                }

            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(GroupMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getGMSpinnerItem(){

        //選擇"週"的下拉式選單
        Gweek=findViewById(R.id.gm_spinnerweek);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Gweek_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, Gweek_list);
        //設定下拉選單的樣式
        Gweek_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Gweek.setAdapter(Gweek_listAdapter);
        //設定項目被選取之後的動作
        Gweek.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(GroupMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(GroupMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });
        //選擇"月"的下拉式選單
        Gmonth=findViewById(R.id.gm_spinnermonth);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Gmonth_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, Gmonth_list);
        //設定下拉選單的樣式
        Gmonth_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Gmonth.setAdapter(Gmonth_listAdapter);
        //設定項目被選取之後的動作
        Gmonth.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(GroupMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(GroupMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });

        //選擇"年"的下拉式選單
        Gyear=findViewById(R.id.gm_spinneryear);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Gyear_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, Gyear_list);
        //設定下拉選單的樣式
        Gyear_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Gyear.setAdapter(Gyear_listAdapter);
        //設定項目被選取之後的動作
        Gyear.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(GroupMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(GroupMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void map_parseJSON(String result) {
        try {
            JSONArray array = new JSONArray(result);
            for (int i=0; i<array.length(); i++){
                JSONObject obj = array.getJSONObject(i);

                String map_event = obj.getString("Event");
                String map_category = obj.getString("Type");
                Double map_E = obj.getDouble("E");
                Double map_N = obj.getDouble("N");


                mapData.add(new MapData(map_event, map_category, map_E, map_N));

                Log.d("JSON:",map_event+"/"+map_category+"/"+map_E+"/"+map_N);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.gm_back:
                finish();
                break;
        }
    }

    /** Called when the map is ready. */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        for (int i = 0; i < mapData.size(); i++) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN())).title(mapData.get(i).getEvent()));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()), 4.0f));
        }

        mMap.setOnMarkerClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}