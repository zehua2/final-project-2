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
import android.util.Log;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.mukesh.image_processing.ImageProcessor;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    ImageView ivCamera, ivGallery, ivImage;
    CameraPhoto cameraPhoto;

    final int CAMERA_REQUEST = 12332;


    ImageView imageView;
    Bitmap original;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        original = BitmapFactory.decodeResource(getResources(), R.drawable.panda);
        final ImageProcessor imageProcessor = new ImageProcessor();
        imageView = (ImageView) findViewById(R.id.imageView);

        final Button brightness = (Button) findViewById(R.id.doBrightness);
        brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button clicked");
                    original = imageProcessor.doBrightness(original, 100);
                    imageView.setImageBitmap(original);
                Toast.makeText(MainActivity.this, "You increased the brightness~", Toast.LENGTH_LONG).show();
                }
        });

        final Button black = (Button) findViewById(R.id.applyBlackFilter);
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button clicked");
                original = imageProcessor.applyBlackFilter(original);
                imageView.setImageBitmap(original);
                Toast.makeText(MainActivity.this, "You applied a black filter~", Toast.LENGTH_LONG).show();
            }
        });

        final Button saturation = (Button) findViewById(R.id.saturation);
        saturation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button clicked");
                original = imageProcessor.applySaturationFilter(original,50);
                imageView.setImageBitmap(original);
                Toast.makeText(MainActivity.this, "You increased saturation~", Toast.LENGTH_LONG).show();
            }
        });

        final Button invert = (Button) findViewById(R.id.invert);
        invert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button clicked");
                original = imageProcessor.doInvert(original);
                imageView.setImageBitmap(original);
                Toast.makeText(MainActivity.this, "You have inverted the image~", Toast.LENGTH_LONG).show();
            }
        });

        final Button rotate = (Button) findViewById(R.id.rotate);
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button clicked");
                original = imageProcessor.rotate(original, 90);
                imageView.setImageBitmap(original);
                Toast.makeText(MainActivity.this, "You have rotated the image by 90 degree~", Toast.LENGTH_LONG).show();
            }
        });

        final Button contrast = (Button) findViewById(R.id.contrast);
        contrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button clicked");
                original = imageProcessor.createContrast(original, 90);
                imageView.setImageBitmap(original);
                Toast.makeText(MainActivity.this, "You have increased contrast", Toast.LENGTH_LONG).show();
            }
        });

        final Button revert = (Button) findViewById(R.id.revert);
        revert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button clicked");
                original = BitmapFactory.decodeResource(getResources(), R.drawable.panda);
                imageView.setImageBitmap(original);
                Toast.makeText(MainActivity.this, "Back to the original image~", Toast.LENGTH_LONG).show();
            }
        });


        cameraPhoto = new CameraPhoto(getApplicationContext());

        ivImage = (ImageView)findViewById(R.id.ivImage);
        ivCamera = (ImageView)findViewById(R.id.ivCamera);
        ivGallery = (ImageView)findViewById(R.id.ivGallery);

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"button clicked");
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
