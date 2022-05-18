package com.example.player;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {
    List<Integer> listResId = new ArrayList<>();
    List<String> listNameFile = new ArrayList<>();
    MediaPlayer player;
    SeekBar progress;
    Handler handler;
    Runnable runnable;
    boolean isPlay = false, isPause = false, isStop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        progress = (SeekBar) findViewById(R.id.progress);


        progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressBar, boolean input) {
                if (input) {
                    player.seekTo(progressBar);
                    player.start();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        ImageView btnPlay = findViewById(R.id.btnPlay);
        ImageView btnPause = findViewById(R.id.btnPause);
        ImageView btnStop = findViewById(R.id.btnStop);
        SeekBar progress = findViewById(R.id.progress);

        listResId.add(R.raw.alex_kozin_dreams);
        listResId.add(R.raw.marshmallow_alone);
        listResId.add(R.raw.rammstein_reisereise);
        listResId.add(R.raw.imagine_iragons_radioactive);

        listNameFile.add("Alex Kozin - Dreams");
        listNameFile.add("Marshmallow - Alone");
        listNameFile.add("Rammstein - Reise, Reise");
        listNameFile.add("Imagine Dragons - Radioactive");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, listNameFile);

        ListView listAudio = findViewById(R.id.listAudio);
        listAudio.setAdapter(adapter);
        listAudio.setOnItemClickListener((adapterView, view, i, l) -> {
            if (player != null)
                if (player.isPlaying())
                    player.stop();
            player = MediaPlayer.create(MainActivity.this, listResId.get(i));
            player.start();
            isPause = isStop = true;
            btnPause.setAlpha(1f);
            btnStop.setAlpha(1f);
            progress.setMax(player.getDuration());
            playCycle();

        });
        btnPlay.setAlpha(0.5F);
        btnPause.setAlpha(0.5F);
        btnStop.setAlpha(0.5F);
        btnPlay.setOnClickListener(v -> {
            if (isPlay) {
                player.start();
                isPause = true;
                isPlay = false;
                btnPause.setAlpha(1f);
                btnPlay.setAlpha(0.5f);
            }
        });
        btnPause.setOnClickListener(v -> {
            if (isPause) {
                player.pause();
                isPause = false;
                isPlay = true;
                btnPause.setAlpha(0.5f);
                btnPlay.setAlpha(1f);
            }
        });
        btnStop.setOnClickListener(v -> {
            if (isStop) {
                player.stop();
                isPause = isPlay = isStop = false;
                btnPause.setAlpha(0.5f);
                btnPlay.setAlpha(0.5f);
                btnStop.setAlpha(0.5f);
            }

        });
    }

    public void playCycle() {
        progress.setProgress(player.getCurrentPosition());
        if (player.isPlaying()){
            runnable = new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            handler.postDelayed(runnable, 500);
        }
    }

}