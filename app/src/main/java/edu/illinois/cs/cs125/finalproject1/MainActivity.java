package edu.illinois.cs.cs125.finalproject1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.mukesh.image_processing.ImageProcessor;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.panda);

        final ImageProcessor imageProcessor = new ImageProcessor();
        bitmap = imageProcessor.roundCorner(bitmap, 10);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);

        Button btn = (Button) findViewById(R.id.doBrightness);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = imageProcessor.doBrightness(bitmap, 100);
                
            }
        });
    }
}
