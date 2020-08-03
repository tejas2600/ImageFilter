package com.example.imagefilter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestOptions;

import java.io.FileDescriptor;
import java.io.IOException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.gpu.SepiaFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.SketchFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;

public class MainActivity extends AppCompatActivity {
    Button choose_image;
    ImageView image_view;
    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        choose_image=findViewById(R.id.choose_image);
        image_view=findViewById(R.id.image_view);
    }
    public void choosephoto(View view){
      Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
      intent.setType("image/*");
      startActivityForResult(intent,1);
    }
    public void applyfilter(Transformation<Bitmap> filter){
        Glide.with(this)
                .load(image)
                .apply(RequestOptions.bitmapTransform(filter))
                .into(image_view);
    }
    public void applyblur(View v){
        applyfilter(new BlurTransformation(25,3));
    }
    public void applysepia(View v){
        applyfilter(new SepiaFilterTransformation());
    }
    public void applytoon(View v){
        applyfilter(new ToonFilterTransformation());
    }
    public void applysketch(View v){
        applyfilter(new SketchFilterTransformation());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null && resultCode== Activity.RESULT_OK){
            try{
                Uri uri=data.getData();
                ParcelFileDescriptor parcelFileDescriptor=getContentResolver().openFileDescriptor(uri,"r");
                FileDescriptor fileDescriptor=parcelFileDescriptor.getFileDescriptor();
                image= BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                image_view.setImageBitmap(image);
            }
            catch(IOException e){

            }

        }
    }
}
