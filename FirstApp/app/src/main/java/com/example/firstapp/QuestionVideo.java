package com.example.firstapp;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.VideoView;

import com.example.firstapp.databinding.ActivityQuestionVideoBinding;

/*
 * QuestionVideo.java
 *
 * Class Description: Displays QuestionSet's video after completing questions,
 *                    changeable audio level setting,
 *                    flippable to portrait or landscape orientation.
 * Class Invariant: Video must exist, else crash.
 *
 */

public class QuestionVideo extends AppCompatActivity {

    private VideoView questionVideo;
    private SeekBar volumeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int video = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            video = extras.getInt("video");
        else
            Log.e("QuestionVideo", "Video is not parsed");
        com.example.firstapp.databinding.ActivityQuestionVideoBinding binding = ActivityQuestionVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Romanisation r = new Romanisation(MyApplication.getAppContext());
        volumeBar = findViewById(R.id.volume_bar);
        questionVideo = findViewById(R.id.video_view);
        Button nextQuestionSetBtn = findViewById(R.id.next_question);
        nextQuestionSetBtn.setText(r.input(getString(R.string.video_close), this));
        volumeBar.setMax(100);
        volumeBar.setProgress(50);
        String videoPath = "android.resource://" + this.getPackageName() + "/" + video;
        Uri uri = Uri.parse(videoPath);
        questionVideo.setVideoURI(uri);
        questionVideo.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            questionVideo.start();
            mp.setVolume((float)volumeBar.getProgress()/100, (float)volumeBar.getProgress()/100);
            volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mp.setVolume((float)progress/100, (float)progress/100);
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        });

        nextQuestionSetBtn.setOnClickListener(v -> finish());
    }
}