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
import android.widget.TextView;
import android.widget.Toast;

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
    private String[] Pcategory_list = {"所有類別","公事","聚會","聚餐","出遊","其他"}; //宣告字串陣列
    private ArrayAdapter<String> Pcategory_listAdapter; //喧告listAdapter物件
    Spinner Pcategory;

    private String[] Pweek_list = {"none", "1 week","2 week","3 week"}; //宣告字串陣列
    private ArrayAdapter<String> Pweek_listAdapter; //喧告listAdapter物件
    Spinner Pweek;

    //宣告字串陣列
    private String[] Pmonth_list = {"none", "1 month","2 month","3 month","4 month","5 month","6 month",
            "7 month","8 month","9 month","10 month","11 month"};
    private ArrayAdapter<String> Pmonth_listAdapter; //喧告listAdapter物件
    Spinner Pmonth;

    //宣告字串陣列
    private String[] Pyear_list = {"none", "1 year","2 year","3 year","4 year","5 year"};
    private ArrayAdapter<String> Pyear_listAdapter; //喧告listAdapter物件
    Spinner Pyear;

    // map
    private GoogleMap mMap;
    private ArrayList<LatLng> markers;

    //存放要Get的訊息
    private String PMap_getUrl = "https://sd.jezrien.one/user/map";
    private String PMap_getUrl_week = "https://sd.jezrien.one/user/map/weeks";
    private String PMap_getUrl_month = "https://sd.jezrien.one/user/map/months";
    private String PMap_getUrl_year = "https://sd.jezrien.one/user/map/years";

    Http_Get HMG;

    SharedPreferences NsharedPreferences;
    private String token;
    private String resultJSON;

    List<MapData> mapData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_map);
        String username = getIntent().getExtras().getString("username");
        TextView title = findViewById(R.id.textView17);
        title.setText(username + " Track");

        back=findViewById(R.id.pm_back);
        back.setOnClickListener(this);

        //set token
        NsharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = NsharedPreferences.getString("TOKEN", "");

        // get
        HMG = new Http_Get();
        HMG.Get(PMap_getUrl, token);
        resultJSON = HMG.getTt();
        map_parseJSON(resultJSON);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        category_menu();
        getPMSpinnerItem();
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
                if (adapterView.getSelectedItem().toString().equals("所有類別")) {
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("公事")) {
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block1));
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
                } else if (adapterView.getSelectedItem().toString().equals("聚會")) {
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block2));
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
                } else if (adapterView.getSelectedItem().toString().equals("聚餐")) {
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block3));
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
                } else if (adapterView.getSelectedItem().toString().equals("出遊")) {
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block4));
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
                } else if (adapterView.getSelectedItem().toString().equals("其他")) {
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.block5));
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
                if (adapterView.getSelectedItem().toString().equals("1 week")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_week, token, "", 1);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("2 week")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_week, token, "", 2);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("3 week")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_week, token, "", 3);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else {
                    mapData.clear();
                    HMG.Get(PMap_getUrl, token);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                }
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
                if (adapterView.getSelectedItem().toString().equals("1 month")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_month, token, "", 1);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("2 month")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_month, token, "", 2);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("3 month")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_month, token, "", 3);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("4 month")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_month, token, "", 4);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("5 month")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_month, token, "", 5);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("6 month")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_month, token, "", 6);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("7 month")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_month, token, "", 7);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("8 month")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_month, token, "", 8);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("9 month")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_month, token, "", 9);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("10 month")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_month, token, "", 10);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("11 month")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_month, token, "", 11);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else {
                    mapData.clear();
                    HMG.Get(PMap_getUrl, token);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                }
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
                if (adapterView.getSelectedItem().toString().equals("1 year")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_year, token, "", 1);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("2 year")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_year, token, "", 2);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("3 year")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_year, token, "", 3);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("4 year")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_year, token, "", 4);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else if (adapterView.getSelectedItem().toString().equals("5 year")) {
                    mapData.clear();
                    HMG.Get(PMap_getUrl_year, token, "", 5);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                } else {
                    mapData.clear();
                    HMG.Get(PMap_getUrl, token);
                    resultJSON = HMG.getTt();
                    map_parseJSON(resultJSON);
                    Pcategory.setSelection(0, false);
                    Pcategory.setBackgroundColor(getResources().getColor(R.color.white));
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
                }
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