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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    private String[] Gcategory_list = {"All","Party","Business","Dinner","Travel","Others"}; //宣告字串陣列
    private ArrayAdapter<String> Gcategory_listAdapter; //喧告listAdapter物件
    Spinner Gcategory;

    private String[] Gweek_list = {"none", "1 week","2 week","3 week"}; //宣告字串陣列
    private ArrayAdapter<String> Gweek_listAdapter; //喧告listAdapter物件
    Spinner Gweek;

    //宣告字串陣列
    private String[] Gmonth_list = {"none", "1 month","2 month","3 month","4 month","5 month","6 month",
            "7 month","8 month","9 month","10 month","11 month"};
    private ArrayAdapter<String> Gmonth_listAdapter; //喧告listAdapter物件
    Spinner Gmonth;

    //宣告字串陣列
    private String[] Gyear_list = {"none", "1 year","2 year","3 year","4 year","5 year"};
    private ArrayAdapter<String> Gyear_listAdapter; //喧告listAdapter物件
    Spinner Gyear;

    private String groupname;

    // map
    private GoogleMap mMap;
    private ArrayList<LatLng> markers;

    //存放要Get的訊息
    private String Map_getUrl = "https://sd.jezrien.one/user/map";
    private String Map_getUrl_week = "https://sd.jezrien.one/user/map/weeks";
    private String Map_getUrl_month = "https://sd.jezrien.one/user/map/months";
    private String Map_getUrl_year = "https://sd.jezrien.one/user/map/years";
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
        category_menu();
        getGMSpinnerItem();
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
        Gcategory.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){
                Toast.makeText(GroupMap.this, "您選擇"+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                if (adapterView.getSelectedItem().toString().equals("All")) {
                    mMap.clear();
                    for (int i = 0; i < mapData.size(); i++) {
                        if (mapData.get(i).getType().equals("Business")) {
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                                    .title(mapData.get(i).getEvent())
                                    .icon(BitmapDescriptorFactory.defaultMarker(213)));
                            marker.setTag(mapData.get(i).getType());
                        } else if (mapData.get(i).getType().equals("Party")) {
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                                    .title(mapData.get(i).getEvent())
                                    .icon(BitmapDescriptorFactory.defaultMarker(46)));
                            marker.setTag(mapData.get(i).getType());
                        } else if (mapData.get(i).getType().equals("Dinner")) {
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                                    .title(mapData.get(i).getEvent())
                                    .icon(BitmapDescriptorFactory.defaultMarker(32)));
                            marker.setTag(mapData.get(i).getType());
                        } else if (mapData.get(i).getType().equals("Travel")) {
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                                    .title(mapData.get(i).getEvent())
                                    .icon(BitmapDescriptorFactory.defaultMarker(0)));
                            marker.setTag(mapData.get(i).getType());
                        } else {
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                                    .title(mapData.get(i).getEvent())
                                    .icon(BitmapDescriptorFactory.defaultMarker(110)));
                            marker.setTag(mapData.get(i).getType());
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()), 10.0f));
                    }
                } else if (adapterView.getSelectedItem().toString().equals("Business")) {
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block1));
                    mMap.clear();
                    for (int i = 0; i < mapData.size(); i++) {
                        if (mapData.get(i).getType().equals("Business")) {
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                                    .title(mapData.get(i).getEvent())
                                    .icon(BitmapDescriptorFactory.defaultMarker(213)));
                            marker.setTag(mapData.get(i).getType());
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()), 10.0f));
                    }
                } else if (adapterView.getSelectedItem().toString().equals("Party")) {
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block2));
                    mMap.clear();
                    for (int i = 0; i < mapData.size(); i++) {
                        if (mapData.get(i).getType().equals("Party")) {
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                                    .title(mapData.get(i).getEvent())
                                    .icon(BitmapDescriptorFactory.defaultMarker(46)));
                            marker.setTag(mapData.get(i).getType());
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()), 10.0f));
                    }
                } else if (adapterView.getSelectedItem().toString().equals("Dinner")) {
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block3));
                    mMap.clear();
                    for (int i = 0; i < mapData.size(); i++) {
                        if (mapData.get(i).getType().equals("Dinner")) {
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                                    .title(mapData.get(i).getEvent())
                                    .icon(BitmapDescriptorFactory.defaultMarker(46)));
                            marker.setTag(mapData.get(i).getType());
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()), 10.0f));
                    }
                } else if (adapterView.getSelectedItem().toString().equals("Travel")) {
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block4));
                    mMap.clear();
                    for (int i = 0; i < mapData.size(); i++) {
                        if (mapData.get(i).getType().equals("Travel")) {
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                                    .title(mapData.get(i).getEvent())
                                    .icon(BitmapDescriptorFactory.defaultMarker(0)));
                            marker.setTag(mapData.get(i).getType());
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()), 10.0f));
                    }
                } else if (adapterView.getSelectedItem().toString().equals("Others")) {
                    Gcategory.setBackgroundColor(getResources().getColor(R.color.block5));
                    mMap.clear();
                    for (int i = 0; i < mapData.size(); i++) {
                        if (mapData.get(i).getType().equals("Others")) {
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()))
                                    .title(mapData.get(i).getEvent())
                                    .icon(BitmapDescriptorFactory.defaultMarker(110)));
                            marker.setTag(mapData.get(i).getType());
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mapData.get(i).getE(), mapData.get(i).getN()), 10.0f));
                    }
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
                if (adapterView.getSelectedItem().toString().equals("1 week")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_week, token, groupname, 1);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("2 week")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_week, token, groupname, 2);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("3 week")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_week, token, groupname, 3);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl, token, groupname);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                }
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
                if (adapterView.getSelectedItem().toString().equals("1 month")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_month, token, groupname, 1);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("2 month")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_month, token, groupname, 2);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("3 month")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_month, token, groupname, 3);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("4 month")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_month, token, groupname, 4);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("5 month")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_month, token, groupname, 5);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("6 month")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_month, token, groupname, 6);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("7 month")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_month, token, groupname, 7);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("8 month")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_month, token, groupname, 8);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("9 month")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_month, token, groupname, 9);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("10 month")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_month, token, groupname, 10);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("11 month")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_month, token, groupname, 11);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl, token, groupname);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                }
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
                if (adapterView.getSelectedItem().toString().equals("1 year")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_year, token, groupname, 1);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("2 year")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_year, token, groupname, 2);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("3 year")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_year, token, groupname, 3);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("4 year")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_year, token, groupname, 4);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else if (adapterView.getSelectedItem().toString().equals("5 year")) {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl_year, token, groupname, 5);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                } else {
                    HMG = new Http_Get();
                    HMG.Get(Map_getUrl, token, groupname);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Gcategory.setSelection(0);
                }
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

        mMap.setOnMarkerClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        String type = (String) marker.getTag();
        Toast.makeText(this, type, Toast.LENGTH_SHORT).show();
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}