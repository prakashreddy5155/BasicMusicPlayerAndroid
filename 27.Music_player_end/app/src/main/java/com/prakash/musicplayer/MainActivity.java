package com.prakash.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {



    Handler handler = new Handler();
    TextView timing_textview,song_name;
    Button play_button,forward_button,pause_button,backward_button;


   SeekBar seekBar;

    // variables
    int startTime = 0;
    int finalTime = 0;
    MediaPlayer mediaplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // views
        timing_textview = findViewById(R.id.timing_textview);
        song_name = findViewById(R.id.song_name);
        play_button = findViewById(R.id.play_button);
        pause_button = findViewById(R.id.pause_button);
        forward_button = findViewById(R.id.forward_button);
        backward_button = findViewById(R.id.backward_button);
       seekBar = findViewById(R.id.seekBar);


        // setting song name
        song_name.setText(getResources().getIdentifier(
                "lord_shiva","raw",getPackageName()
        ));

        // MediaPlayer
        mediaplayer = MediaPlayer.create(getApplication(), R.raw.lord_shiva);

        startTime = mediaplayer.getCurrentPosition();
        finalTime = mediaplayer.getDuration();

        //Handlers


        // clickables

        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                playMusic();

            }
        });


        pause_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaplayer.pause();
            }
        });


        forward_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTime = mediaplayer.getCurrentPosition();
                if (startTime + 10000 <= finalTime) {
                    startTime += 10000;
                    Toast.makeText(MainActivity.this,">>> 10sec >>>",Toast.LENGTH_SHORT).show();
                    mediaplayer.seekTo(startTime);

                } else {
                    Toast.makeText(MainActivity.this, "Cannot jump forward", Toast.LENGTH_SHORT).show();
                }

            }
        });


        backward_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startTime = mediaplayer.getCurrentPosition();

                if (startTime - 10000 >= 0) {
                    startTime -= 10000;
                    Toast.makeText(MainActivity.this, "<<< 10sec <<<",Toast.LENGTH_SHORT).show();
                    mediaplayer.seekTo(startTime);

                } else {

                    startTime= 0;
                    mediaplayer.seekTo(startTime);
                    Toast.makeText(MainActivity.this, "Started", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        seekBar.setMax(finalTime);
    }

    private void playMusic() {

        if(!mediaplayer.isPlaying()) {

            mediaplayer.start();

            handler.postDelayed(UpdateSongTime, 100);
        }
        else
        {
            Toast.makeText(this, "Song is already Playing", Toast.LENGTH_SHORT).show();
        }

    }

    Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {

            startTime = mediaplayer.getCurrentPosition();

            timing_textview.setText(String.format("%d min %d sec ",TimeUnit.MILLISECONDS.toMinutes(startTime),TimeUnit.MILLISECONDS.toSeconds(startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime))));

           seekBar.setProgress(startTime);

            handler.postDelayed(UpdateSongTime,1000);
        }
    };
}