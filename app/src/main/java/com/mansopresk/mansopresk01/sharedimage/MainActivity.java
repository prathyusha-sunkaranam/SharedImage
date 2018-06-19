package com.mansopresk.mansopresk01.sharedimage;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    String choice[] = {"CAMERA","GALLERY"};
    public static final int CAM_REQ_CODE = 123;
    public static final int GAL_REQ_CODE = 321;

    public static final int CAM_PERMISSION_ACCESS_CODE = 111;
    public static final String CAM_PERMISSION_NAME[] = {android.Manifest.permission.CAMERA};
    public static final int GAL_PERMISSION_ACCESS_CODE = 222;
    public static final String GAL_PERMISSION_NAME[] = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
    ImageView image;
    Bitmap bit = null;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = findViewById(R.id.image);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(i);
            }
        });
    }

    public void cam(View v){

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setIcon(R.drawable.ic_camera_black_24dp);
        adb.setTitle(" Select One ");
        adb.setItems(choice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        int res = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA);
                        if (res == PackageManager.PERMISSION_GRANTED) {
                            Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cam, CAM_REQ_CODE);
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, CAM_PERMISSION_NAME, CAM_PERMISSION_ACCESS_CODE);
                        }
                        break;
                    case 1:
                        int res1 = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

                        if (res1 == PackageManager.PERMISSION_GRANTED) {
                            Intent gal = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(gal, GAL_REQ_CODE);
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, GAL_PERMISSION_NAME, GAL_PERMISSION_ACCESS_CODE);
                        }

                        break;
                }
            }
        });
        adb.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAM_PERMISSION_ACCESS_CODE:
                if (CAM_PERMISSION_NAME.equals(permissions[0]) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent cam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cam, CAM_REQ_CODE);
                }
                break;

            case GAL_PERMISSION_ACCESS_CODE:
                if (GAL_PERMISSION_NAME.equals(permissions[0]) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent gal = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gal, GAL_REQ_CODE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAM_REQ_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    bit = (Bitmap) b.get("data");
                    image.setImageBitmap(bit);
                }
                break;

            case GAL_REQ_CODE:
                if (resultCode == RESULT_OK) {
                    Uri img = data.getData();
                    try {
                        bit = MediaStore.Images.Media.getBitmap(this.getContentResolver(), img);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image.setImageBitmap(bit);
                }
                break;
        }

        image.buildDrawingCache();
        Bitmap bitmap = image.getDrawingCache();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();

        String img_str = Base64.encodeToString(image, 0);
        //decode string to image
        String base=img_str;
        byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
        //        ImageView ivsavedphoto = (ImageView)this.findViewById(R.id.iv2);
//        ivsavedphoto.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes,0, imageAsBytes.length)
//        );
        SharedPreferences preferences = getSharedPreferences("myprefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userphoto",img_str);
        editor.commit();
    }



}
