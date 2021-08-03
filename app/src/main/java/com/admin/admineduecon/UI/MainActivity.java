package com.admin.admineduecon.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.admin.admineduecon.R;
import com.admin.admineduecon.SharedPref.SharedPrefManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    CardView addEbook, addVideo;
    ImageView ebook, video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        addEbook = findViewById(R.id.addEbook);
        addVideo = findViewById(R.id.addVideo);
        ebook = findViewById(R.id.ebook);
        video = findViewById(R.id.videoView);
        TextView logout = findViewById(R.id.tvLogout);

        final SharedPrefManager sharedPrefManager = new SharedPrefManager(this);

        addEbook.setOnClickListener(this);
        addVideo.setOnClickListener(this);
        ebook.setOnClickListener(this);
        video.setOnClickListener(this);

        logout.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            sharedPrefManager.saveIsLogin(false);
            finishAffinity();
            startActivity(i);
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.addEbook:
                intent = new Intent(MainActivity.this, UploadEbookActivity.class);
                startActivity(intent);
                break;
            case R.id.addVideo:
                intent = new Intent(MainActivity.this, UploadVideoActivity.class);
                startActivity(intent);
                break;
            case R.id.ebook:
                intent = new Intent(MainActivity.this, EbookActivity.class);
                startActivity(intent);
                break;
            case R.id.videoView:
                intent = new Intent(MainActivity.this, VideoViewActivity.class);
                startActivity(intent);
                break;
        }

    }
}