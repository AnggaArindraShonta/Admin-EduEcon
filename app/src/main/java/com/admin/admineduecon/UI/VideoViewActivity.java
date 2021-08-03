package com.admin.admineduecon.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.os.Bundle;
import android.view.WindowManager;

import com.admin.admineduecon.Adapter.VideoAdapter;
import com.admin.admineduecon.Model.Video;
import com.admin.admineduecon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VideoViewActivity extends AppCompatActivity {

    //UI views
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

        rvVideo = findViewById(R.id.rvVideo);

        loadVideoFromFirebase();

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