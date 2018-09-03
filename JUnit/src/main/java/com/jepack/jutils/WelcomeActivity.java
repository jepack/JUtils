package com.jepack.jutils;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

import com.jepack.lib.widget.ColorFillProgressView;

public class WelcomeActivity extends AppCompatActivity {
    ColorFillProgressView colorFillProgressView;
    int progress = 0;
    Handler handler;
    ProgressRunnable runnable;
    int direction = 1;
    int step = 5;
    int interval = step / 100 * 10000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        colorFillProgressView = findViewById(R.id.fill_color_progress);
        SeekBar seekBar = findViewById(R.id.progress_ctr);
        seekBar.setMax(colorFillProgressView.getMax());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                colorFillProgressView.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        handler = new Handler();
        runnable = new ProgressRunnable();
        //handler.postDelayed(runnable, interval );
    }

    class ProgressRunnable implements Runnable{
        @Override
        public void run() {
            progress += step * direction;
            if(progress > colorFillProgressView.getMax()){
                direction = -1;
                progress = colorFillProgressView.getMax() - step;
            }else if(progress < 0){
                direction = 1;
                progress = step;
            }
            colorFillProgressView.setProgress(progress);
            if(handler != null) handler.postDelayed(runnable, interval);
        }
    }

}
