package edu.illinois.cs.cs125.finalproject1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Log;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.mukesh.image_processing.ImageProcessor;

import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    ImageView ivCamera, ivGallery, ivImage;
    CameraPhoto cameraPhoto;
    GalleryPhoto galleryPhoto;

    final int CAMERA_REQUEST = 12332;
    final int GALLERY_REQUEST = 2200;

    ImageView imageView;
    Bitmap original;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //original = BitmapFactory.decodeResource(getResources(), R.drawable.panda);
        final ImageProcessor imageProcessor = new ImageProcessor();

        //final Bitmap original1 =((BitmapDrawable)ivImage.getDrawable()).getBitmap();

        final Button brightness = (Button) findViewById(R.id.doBrightness);
        brightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button clicked");
                    original =((BitmapDrawable)ivImage.getDrawable()).getBitmap();
                    original = imageProcessor.doBrightness(original, 50);
                ivImage.setImageBitmap(original);
                Toast.makeText(MainActivity.this, "You increased the brightness~", Toast.LENGTH_LONG).show();
                }
        });

        final Button black = (Button) findViewById(R.id.applyBlackFilter);
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button clicked");
                original =((BitmapDrawable)ivImage.getDrawable()).getBitmap();
                original = imageProcessor.applyBlackFilter(original);
                ivImage.setImageBitmap(original);
                Toast.makeText(MainActivity.this, "You applied a black filter~", Toast.LENGTH_LONG).show();
            }
        });

        final Button saturation = (Button) findViewById(R.id.saturation);
        saturation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button clicked");
                original =((BitmapDrawable)ivImage.getDrawable()).getBitmap();
                original = imageProcessor.applySaturationFilter(original,50);
                ivImage.setImageBitmap(original);
                Toast.makeText(MainActivity.this, "You increased saturation~", Toast.LENGTH_LONG).show();
            }
        });

        final Button invert = (Button) findViewById(R.id.invert);
        invert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button clicked");
                original =((BitmapDrawable)ivImage.getDrawable()).getBitmap();
                original = imageProcessor.doInvert(original);
                ivImage.setImageBitmap(original);
                Toast.makeText(MainActivity.this, "You have inverted the image~", Toast.LENGTH_LONG).show();
            }
        });

        final Button rotate = (Button) findViewById(R.id.rotate);
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button clicked");
                original =((BitmapDrawable)ivImage.getDrawable()).getBitmap();
                original = imageProcessor.rotate(original, 90);
                ivImage.setImageBitmap(original);
                Toast.makeText(MainActivity.this, "You have rotated the image by 90 degree~", Toast.LENGTH_LONG).show();
            }
        });

        final Button contrast = (Button) findViewById(R.id.contrast);
        contrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"button clicked");
                original =((BitmapDrawable)ivImage.getDrawable()).getBitmap();
                original = imageProcessor.createContrast(original, 90);
                ivImage.setImageBitmap(original);
                Toast.makeText(MainActivity.this, "You have increased contrast", Toast.LENGTH_LONG).show();
            }
        });

        /*final Button revert = (Button) findViewById(R.id.revert);
                revert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG,"button clicked");
                        original =((BitmapDrawable)ivImage.getDrawable()).getBitmap();
                        ivImage.setImageBitmap(original);
                        Toast.makeText(MainActivity.this, "Back to the original image~", Toast.LENGTH_LONG).show();
                    }
                });*/

        Boolean canWriteToPublicStorage = false;
        final int REQUEST_WRITE_STORAGE = 112;
        canWriteToPublicStorage = (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        Log.d(TAG, "Do we have permission to write to external storage: "
                + canWriteToPublicStorage);
        if (!canWriteToPublicStorage) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }

        cameraPhoto = new CameraPhoto(getApplicationContext());
        galleryPhoto = new GalleryPhoto(getApplicationContext());

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
                    System.err.println(e.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == CAMERA_REQUEST) {
                String photoPath = cameraPhoto.getPhotoPath();
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    ivImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
                }

            }
            else if(requestCode == GALLERY_REQUEST) {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    ivImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while choosing photos", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
