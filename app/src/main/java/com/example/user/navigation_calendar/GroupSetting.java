package com.example.user.navigation_calendar;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.FileNotFoundException;

public class GroupSetting extends AppCompatActivity implements View.OnClickListener  {

    ImageButton gs_back;
    ImageButton gs_save;
    ImageButton change_pic;
    ImageView group_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_setting);

        gs_back=findViewById(R.id.gs_back);
        gs_back.setOnClickListener(this);
        gs_save=findViewById(R.id.gs_save);
        gs_save.setOnClickListener(this);

        //找Change Picture Button按鈕
        change_pic=findViewById(R.id.btn_changeGP1);
        group_pic=findViewById(R.id.group_pic);
        //設定按鈕監聽
        change_pic.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.gs_back:
                finish();
                break;
            case R.id.gs_save:
                //post
                finish();
                break;

            case R.id.btn_changeGP1  :
                Intent intent = new Intent();
                //開啟Pictures畫面Type設定為image
                intent.setType("image/*");
                //使用Intent.ACTION_GET_CONTENT這個Action                                            //會開啟選取圖檔視窗讓您選取手機內圖檔
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //取得相片後返回本畫面
                startActivityForResult(intent, 1);
                break;
        }
    }
    //取得相片後返回的監聽式
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //當使用者按下確定後
        if (resultCode == RESULT_OK){
            //取得圖檔的路徑位置
            Uri uri = data.getData();
            //寫log
            Log.e("uri", uri.toString());
            //抽象資料的接口
            ContentResolver cr = this.getContentResolver();
            try {
                //由抽象資料接口轉換圖檔路徑為Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                //取得圖片控制項ImageView
                ImageView imageView = (ImageView) findViewById(R.id.group_pic);
                // 將Bitmap設定到ImageView
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.e("Exception", e.getMessage(),e);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    protected void doCropPhoto(Uri uri){
        //進行照片裁剪
        Intent intent = getCropImageIntent(uri);
        startActivityForResult(intent, 1);
    }
    //裁剪圖片的Intent設定
    public static Intent getCropImageIntent(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        intent.putExtra("crop", "true");// crop=true 有這句才能叫出裁剪頁面.
        intent.putExtra("scale", true); //讓裁剪框支援縮放
        intent.putExtra("aspectX", 1);// 这兩項為裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
        intent.putExtra("outputX", 500);//回傳照片比例X
        intent.putExtra("outputY", 500);//回傳照片比例Y
        intent.putExtra("return-data", true);
        return intent;
    }

}
