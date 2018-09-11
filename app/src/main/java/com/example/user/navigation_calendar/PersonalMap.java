package com.example.user.navigation_calendar;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.anychart.anychart.DataEntry;
import com.anychart.anychart.ValueDataEntry;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonalMap extends AppCompatActivity implements View.OnClickListener, OnMarkerClickListener, OnMapReadyCallback {

    ImageButton back;
    //下拉式選單
    private String[] Pcategory_list = {"All","Business","Party","Dinner","Travel","Others"}; //宣告字串陣列
    private ArrayAdapter<String> Pcategory_listAdapter; //喧告listAdapter物件
    Spinner Pcategory;

    private String[] Pweek_list = {"1 week","2 week","3 week"}; //宣告字串陣列
    private ArrayAdapter<String> Pweek_listAdapter; //喧告listAdapter物件
    Spinner Pweek;

    //宣告字串陣列
    private String[] Pmonth_list = {"1 month","2 month","3 month","4 month","5 month","6 month",
            "7 month","8 month","9 month","10 month","11 month","12 month"};
    private ArrayAdapter<String> Pmonth_listAdapter; //喧告listAdapter物件
    Spinner Pmonth;

    //宣告字串陣列
    private String[] Pyear_list = {"1 year","2 year","3 year","4 year","5 year"};
    private ArrayAdapter<String> Pyear_listAdapter; //喧告listAdapter物件
    Spinner Pyear;

    // map
    private GoogleMap mMap;
    private ArrayList<LatLng> markers;

    //存放要Get的訊息
    private String PMap_getUrl = "https://sd.jezrien.one/user/map";
    Http_Get HMG;

    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;

    List<MapData> mapData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_map);

        back=findViewById(R.id.pm_back);
        back.setOnClickListener(this);
        category_menu();
        getPMSpinnerItem();

        //set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = NsharedPreferences.getString("TOKEN", "");

        //get_pie
        HMG = new Http_Get();
        HMG.Get(PMap_getUrl, token);
        resultJSON = HMG.getTt();
        map_parseJSON(resultJSON);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void category_menu(){
        //選擇"類別"的下拉式選單
        Pcategory=findViewById(R.id.pm_spinner);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Pcategory_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner,Pcategory_list);
        //設定下拉選單的樣式
        Pcategory_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Pcategory.setAdapter(Pcategory_listAdapter);
        //設定項目被選取之後的動作
        Pcategory.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(PersonalMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                if (adapterView.getSelectedItem().toString() == "Business"){
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block1));
                    //Pcategory.setBackground(getDrawable(R.drawable.rectangle118));
                }else if (adapterView.getSelectedItem().toString() == "Party"){
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block2));
                }else if (adapterView.getSelectedItem().toString() == "Dinner"){
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block3));
                }else if (adapterView.getSelectedItem().toString() == "Travel"){
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block4));
                }else if (adapterView.getSelectedItem().toString() == "Others"){
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block5));
                }else{
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
                }

            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(PersonalMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPMSpinnerItem(){

        //選擇"週"的下拉式選單
        Pweek=findViewById(R.id.pm_spinnerweek);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Pweek_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, Pweek_list);
        //設定下拉選單的樣式
        Pweek_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Pweek.setAdapter(Pweek_listAdapter);
        //設定項目被選取之後的動作
        Pweek.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(PersonalMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(PersonalMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });
        //選擇"月"的下拉式選單
        Pmonth=findViewById(R.id.pm_spinnermonth);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Pmonth_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, Pmonth_list);
        //設定下拉選單的樣式
        Pmonth_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Pmonth.setAdapter(Pmonth_listAdapter);
        //設定項目被選取之後的動作
        Pmonth.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(PersonalMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(PersonalMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
            }
        });

        //選擇"年"的下拉式選單
        Pyear=findViewById(R.id.pm_spinneryear);
        //建立一個ArrayAdapter物件，並放置下拉選單的內容
        Pyear_listAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, Pyear_list);
        //設定下拉選單的樣式
        Pyear_listAdapter.setDropDownViewResource(R.layout.myspinner_list);//android.R.layout.simple_spinner_dropdown_item
        Pyear.setAdapter(Pyear_listAdapter);
        //設定項目被選取之後的動作
        Pyear.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(PersonalMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(PersonalMap.this, "您沒有選擇任何項目", Toast.LENGTH_SHORT).show();
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
            case R.id.pm_back:
                finish();
                break;
        }

    }

    /** Called when the map is ready. */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        //"Business","Party","Dinner","Travel","Others"
        for (int i = 0; i < mapData.size(); i++) {
            if (mapData.get(i).getType().equals("Business")) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                        .title(mapData.get(i).getEvent())
                        .icon(BitmapDescriptorFactory.defaultMarker(213)));
            } else if (mapData.get(i).getType().equals("Party")) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                        .title(mapData.get(i).getEvent())
                        .icon(BitmapDescriptorFactory.defaultMarker(46)));
            } else if (mapData.get(i).getType().equals("Dinner")) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                        .title(mapData.get(i).getEvent())
                        .icon(BitmapDescriptorFactory.defaultMarker(32)));
            } else if (mapData.get(i).getType().equals("Travel")) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                        .title(mapData.get(i).getEvent())
                        .icon(BitmapDescriptorFactory.defaultMarker(0)));
            } else {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                        .title(mapData.get(i).getEvent())
                        .icon(BitmapDescriptorFactory.defaultMarker(110)));
            }
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

class MapData {
    String event;
    String type;
    double E;
    double N;

    public MapData(String event, String type, double e, double n) {
        this.event = event;
        this.type = type;
        E = e;
        N = n;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getE() {
        return E;
    }

    public void setE(double e) {
        E = e;
    }

    public double getN() {
        return N;
    }

    public void setN(double n) {
        N = n;
    }
}