package com.example.admineduecon.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.admineduecon.Adapter.VideoAdapter;
import com.example.admineduecon.Model.Video;
import com.example.admineduecon.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VideoViewActivity extends AppCompatActivity {

    //UI views
    FloatingActionButton addVideoBtn;
    private RecyclerView rvVideo;
    private ArrayList<Video> videoArrayList;
    private VideoAdapter adapterVideo;
    private static int REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, REQUEST_CODE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setTitle("Videos");

        addVideoBtn = findViewById(R.id.addVideoBtn);
        rvVideo = findViewById(R.id.rvVideo);


        loadVideoFromFirebase();

        addVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VideoViewActivity.this, UploadVideoActivity.class));
            }
        });
    }

    private void loadVideoFromFirebase() {
        videoArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Videos");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                videoArrayList.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Video video = dataSnapshot.getValue(Video.class);
                    videoArrayList.add(video);
                }
                adapterVideo = new VideoAdapter(VideoViewActivity.this, videoArrayList);
                rvVideo.setAdapter(adapterVideo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}