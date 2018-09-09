package com.example.user.navigation_calendar;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Http_NewEventPost extends Service{

    String title=null;
    String start=null;
    String end=null;
    String group;
    double n;//緯度
    double e;//經度
    String category;
    String postUrl=null;
    String strResult=null;
    String token;
    String location;



    public void Post(String Stitle,String Sstart,String Send,
                     String Scategory,String Sgroup,String lc,double Sn,double Se ,String Url,String T) {
        title=Stitle;
        start=Sstart;
        end=Send;
        category=Scategory;
        n=Sn;
        e=Se;
        group=Sgroup;
        postUrl=Url;
        token = T;
        location = lc;

        new Thread(new Runnable() {

            @Override
            public void run() {
                //建立HttpClient物件
                HttpClient httpClient = new DefaultHttpClient();
                //建立一個Post物件，並給予要連線的Url
                if (group != null) {
                    postUrl = "https://sd.jezrien.one/user/group/schedules";
                }

                if (group.equals("Personal")) {
                    postUrl = "https://sd.jezrien.one/user/schedules";
                }

                if (group.equals("All")) {
                    postUrl = "https://sd.jezrien.one/user/group/schedules/all";
                }
                HttpPost httpPost = new HttpPost(postUrl);

                //send token to backend
                httpPost.setHeader("Authorization","Bearer "+token);

                //建立一個ArrayList且需是NameValuePair，此ArrayList是用來傳送給Http server端的訊息
                List params = new ArrayList();
                if (group != null) {
                    params.add(new BasicNameValuePair("name", group.toString()));
                }
                params.add(new BasicNameValuePair("event", title.toString()));
                params.add(new BasicNameValuePair("start", start.toString()));
                params.add(new BasicNameValuePair("end", end.toString()));
                params.add(new BasicNameValuePair("location", location.toString()));
                //經度緯度
                params.add(new BasicNameValuePair("n", String.valueOf(n)));
                params.add(new BasicNameValuePair("e", String.valueOf(e)));
                //事件類別
                params.add(new BasicNameValuePair("type", category.toString()));


                //發送Http Request，內容為params，且為UTF8格式
                UrlEncodedFormEntity ent = null;
                try {
                    ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                httpPost.setEntity(ent);
                //接收Http Server的回應
                HttpResponse httpResponse = null;
                try {
                    httpResponse = httpClient.execute(httpPost);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //判斷Http Server是否回傳OK(200)
                if(httpResponse.getStatusLine().getStatusCode() == 200){

                    //將Post回傳的值轉為String，將轉回來的值轉為UTF8，否則若是中文會亂碼
                    try {
                        strResult = EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Message msg = Message.obtain();
                    //設定Message的內容
                    msg.what = 6;
                    msg.obj=strResult;
                    //使用MainActivity的static handler來丟Message
                    NewEvent.handler.sendMessage(msg);
                }
                if(httpResponse.getStatusLine().getStatusCode() == 401){
                    //將Post回傳的值轉為String，將轉回來的值轉為UTF8，否則若是中文會亂碼
                    try {
                        strResult = EntityUtils.toString(httpResponse.getEntity(),HTTP.UTF_8);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Message msg = Message.obtain();
                    //設定Message的內容
                    msg.what = 7;
                    msg.obj=strResult;
                    //使用MainActivity的static handler來丟Message
                    NewEvent.handler.sendMessage(msg);
                }

            }}).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
