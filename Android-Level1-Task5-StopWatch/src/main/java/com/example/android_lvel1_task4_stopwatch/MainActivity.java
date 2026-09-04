package com.example.android_lvel1_task4_stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvTime;
    private Button btnStart;
    private Button btnPause;
    private Button btnReset;
    private Button btnLap;
    private LinearLayout lapContainer;

    private final Handler handler =
            new Handler(Looper.getMainLooper());

    private long elapsedTime = 0;
    private long startTime = 0;

    private boolean isRunning = false;

    private static final long UPDATE_INTERVAL = 10;

    // Stopwatch update
    private final Runnable stopwatchRunnable = new Runnable() {

        @Override
        public void run() {

            if (isRunning) {

                elapsedTime =
                        System.currentTimeMillis() - startTime;

                updateTimeDisplay();

                handler.postDelayed(
                        this,
                        UPDATE_INTERVAL
                );
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        tvTime = findViewById(R.id.tvTime);
        btnStart = findViewById(R.id.btnStart);
        btnPause = findViewById(R.id.btnPause);
        btnReset = findViewById(R.id.btnReset);
        btnLap = findViewById(R.id.btnLap);
        lapContainer = findViewById(R.id.lapContainer);

        setupButtons();

        updateTimeDisplay();
        updateButtonState();
    }

    private void setupButtons() {

        btnStart.setOnClickListener(v ->
                startStopwatch()
        );

        btnPause.setOnClickListener(v ->
                pauseStopwatch()
        );

        btnReset.setOnClickListener(v ->
                resetStopwatch()
        );

        btnLap.setOnClickListener(v ->
                addLap()
        );
    }

    // START
    private void startStopwatch() {

        if (isRunning) {
            return;
        }

        isRunning = true;

        startTime =
                System.currentTimeMillis() - elapsedTime;

        handler.removeCallbacks(stopwatchRunnable);

        handler.post(stopwatchRunnable);

        updateButtonState();
    }

    // PAUSE
    private void pauseStopwatch() {

        if (!isRunning) {
            return;
        }

        elapsedTime =
                System.currentTimeMillis() - startTime;

        isRunning = false;

        handler.removeCallbacks(stopwatchRunnable);

        updateTimeDisplay();
        updateButtonState();
    }

    // RESET
    private void resetStopwatch() {

        isRunning = false;

        handler.removeCallbacks(stopwatchRunnable);

        elapsedTime = 0;
        startTime = 0;

        tvTime.setText("00:00:00.00");

        lapContainer.removeAllViews();

        updateButtonState();
    }

    // DISPLAY TIME
    private void updateTimeDisplay() {

        long totalMilliseconds = elapsedTime;

        long hours =
                totalMilliseconds / 3600000;

        long minutes =
                (totalMilliseconds % 3600000) / 60000;

        long seconds =
                (totalMilliseconds % 60000) / 1000;

        long milliseconds =
                (totalMilliseconds % 1000) / 10;

        String time = String.format(
                "%02d:%02d:%02d.%02d",
                hours,
                minutes,
                seconds,
                milliseconds
        );

        tvTime.setText(time);
    }

    // BUTTON STATES
    private void updateButtonState() {

        if (isRunning) {

            btnStart.setEnabled(false);
            btnPause.setEnabled(true);
            btnLap.setEnabled(true);

        } else {

            btnStart.setEnabled(true);
            btnPause.setEnabled(false);
            btnLap.setEnabled(false);
        }
    }

    // LAP
    private void addLap() {

        if (!isRunning) {
            return;
        }

        long totalMilliseconds = elapsedTime;

        long hours =
                totalMilliseconds / 3600000;

        long minutes =
                (totalMilliseconds % 3600000) / 60000;

        long seconds =
                (totalMilliseconds % 60000) / 1000;

        long milliseconds =
                (totalMilliseconds % 1000) / 10;

        String lapTime = String.format(
                "%02d:%02d:%02d.%02d",
                hours,
                minutes,
                seconds,
                milliseconds
        );

        int lapNumber =
                lapContainer.getChildCount() + 1;

        TextView lapText =
                new TextView(this);

        lapText.setText(
                "Lap " + lapNumber +
                        "     " +
                        lapTime
        );

        lapText.setTextSize(18);

        lapText.setTextColor(
                android.graphics.Color.BLACK
        );

        lapText.setPadding(
                10,
                10,
                10,
                10
        );

        lapContainer.addView(lapText);
    }

    // ACTIVITY PAUSE
    @Override
    protected void onPause() {

        super.onPause();

        if (isRunning) {

            elapsedTime =
                    System.currentTimeMillis() - startTime;

            handler.removeCallbacks(
                    stopwatchRunnable
            );
        }
    }

    // ACTIVITY RESUME
    @Override
    protected void onResume() {

        super.onResume();

        if (isRunning) {

            startTime =
                    System.currentTimeMillis() - elapsedTime;

            handler.removeCallbacks(
                    stopwatchRunnable
            );

            handler.post(stopwatchRunnable);
        }

        updateTimeDisplay();
        updateButtonState();
    }

    // DESTROY
    @Override
    protected void onDestroy() {

        handler.removeCallbacks(
                stopwatchRunnable
        );

        super.onDestroy();
    }
}

