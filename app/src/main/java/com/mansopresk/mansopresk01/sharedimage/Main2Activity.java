package com.mansopresk.mansopresk01.sharedimage;

import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {
ImageView iv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        iv2 = (ImageView) findViewById(R.id.displayimage);
        SharedPreferences preferences = getSharedPreferences("myprefs",MODE_PRIVATE);




        String img_str = preferences.getString("userphoto", "");
        if (!img_str.equals("")) {
            //decode string to image
            String base = img_str;
            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
            iv2.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }}
}
