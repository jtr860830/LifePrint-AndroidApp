package com.example.user.navigation_calendar;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Http_Get extends Service {

    private String getUrl;
    private String token;
    private String tt;


    public void Get(String Url, String T) {
        getUrl = Url;
        token = T;
        //建立HttpClient物件
        HttpClient httpClient = new DefaultHttpClient();
        //建立Http Get，並給予要連線的Url
        HttpGet get = new HttpGet(getUrl);
        //send token to backend
        get.setHeader("Authorization","Bearer "+token);

        //透過Get跟Http Server連線並取回傳值，並將傳值透過Log顯示出來
        try {
            HttpResponse response = httpClient.execute(get);
            HttpEntity resEntity = response.getEntity();
            tt = EntityUtils.toString(resEntity, HTTP.UTF_8);
            Log.d("Response of GET request", tt);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void Get(String Url, String T, String name) {
        getUrl = Url + "?name=" + name;
        token = T;
        //建立HttpClient物件
        HttpClient httpClient = new DefaultHttpClient();
        //建立Http Get，並給予要連線的Url
        HttpGet get = new HttpGet(getUrl);
        //send token to backend
        get.setHeader("Authorization","Bearer "+token);

        //透過Get跟Http Server連線並取回傳值，並將傳值透過Log顯示出來
        try {
            HttpResponse response = httpClient.execute(get);
            HttpEntity resEntity = response.getEntity();
            tt = EntityUtils.toString(resEntity, HTTP.UTF_8);
            Log.d("Response of GET request", tt);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void Get(String Url, String T, String name, int size) {
        getUrl = Url + "?name=" + name + "&&size=" + size;
        token = T;
        //建立HttpClient物件
        HttpClient httpClient = new DefaultHttpClient();
        //建立Http Get，並給予要連線的Url
        HttpGet get = new HttpGet(getUrl);
        //send token to backend
        get.setHeader("Authorization","Bearer "+token);

        //透過Get跟Http Server連線並取回傳值，並將傳值透過Log顯示出來
        try {
            HttpResponse response = httpClient.execute(get);
            HttpEntity resEntity = response.getEntity();
            tt = EntityUtils.toString(resEntity, HTTP.UTF_8);
            Log.d("Response of GET request", tt);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String getTt() {
        return tt;
    }
}
