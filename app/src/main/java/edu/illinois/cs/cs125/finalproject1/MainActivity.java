package edu.illinois.cs.cs125.finalproject1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.mukesh.image_processing.ImageProcessor;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    ImageView ivCamera, ivGallery, ivImage;
    CameraPhoto cameraPhoto;

    final int CAMERA_REQUEST = 12332;


    ImageView imageView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.panda);

        final ImageProcessor imageProcessor = new ImageProcessor();
       // bitmap = imageProcessor.roundCorner(bitmap, 10);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);

        Button btn = (Button) findViewById(R.id.doBrightness);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId())
                {
                    case R.id.doBrightness:
                        bitmap = imageProcessor.doBrightness(bitmap, 100);
                    break;
                }

                
            }
        });

        cameraPhoto = new CameraPhoto(getApplicationContext());

        ivImage = (ImageView)findViewById(R.id.ivImage);
        ivCamera = (ImageView)findViewById(R.id.ivCamera);
        ivGallery = (ImageView)findViewById(R.id.ivGallery);

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(), CAMERA_REQUEST);
                    cameraPhoto.addToGallery();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(resultCode == CAMERA_REQUEST) {
                cameraPhoto.getPhotoPath();
            }
        }
    }
}
