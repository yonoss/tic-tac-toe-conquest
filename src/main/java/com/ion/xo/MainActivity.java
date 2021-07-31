package com.ion.xo;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import static com.ion.xo.game.Session.*;

public class MainActivity extends Activity  {

    int offsetX = 0;
    int offsetYStart1PlayerButton = 0;
    int offsetYStart2PlayersButton = 0;
    int offsetYHightScoresButton = 0;
    int offsetYQuitButton = 0;
    int offsetYTextView = 0;

    private Button start1PlayerButton;
    private Button start2PlayersButton;
    private Button hightScoresButton;
    private Button quitButton;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        //start service and play music
        startService(new Intent(MainActivity.this, SoundService.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }

    protected void onDestroy() {
        //stop service and stop music
        stopService(new Intent(MainActivity.this, SoundService.class));
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        arrangeButtons();
    }

    public void start1Player(View v) {
        level = Level.START_1_PLAYER;
        USER_1 = this.getString(R.string.player_1_name);
        USER_2 = this.getString(R.string.computer_name);
        aiEnabled = true;
        Intent i = new Intent(this, FullScreenAdMob.class);
        startActivity(i);
    }

    public void start2Players(View v) {
        level = Level.START_2_PLAYERS;
        USER_1 = this.getString(R.string.player_1_name);
        USER_2 = this.getString(R.string.player_2_name);
        aiEnabled = false;
        Intent i = new Intent(this, FullScreenAdMob.class);
        startActivity(i);
    }

    public void highScores(View v) {
        level = Level.HIGH_SCORES;
        Intent i = new Intent(this, FullScreenAdMob.class);
        startActivity(i);
    }

    public void quit(View v) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        finishAffinity();
    }

    private void arrangeButtons() {
        start1PlayerButton = (findViewById(R.id.player1));
        start2PlayersButton = (findViewById(R.id.players2));
        hightScoresButton = (findViewById(R.id.highScores));
        quitButton = (findViewById(R.id.quit));
        textView = (findViewById(R.id.textView));

        calculatePositions();
        positionButtons();
    }

    private void positionButtons() {
        start1PlayerButton.setX(offsetX);
        start1PlayerButton.setY(offsetYStart1PlayerButton);

        start2PlayersButton.setX(offsetX);
        start2PlayersButton.setY(offsetYStart2PlayersButton);

        hightScoresButton.setX(offsetX);
        hightScoresButton.setY(offsetYHightScoresButton);
        hightScoresButton.setVisibility(View.GONE);

        quitButton.setX(offsetX);
        quitButton.setY(offsetYQuitButton);

        textView.setX(offsetX);
        textView.setY(offsetYTextView);
    }

    private void calculatePositions() {
        int height = this.findViewById(android.R.id.content).getHeight();
        int width = this.findViewById(android.R.id.content).getWidth();
        viewHeight = height;
        viewWidth = width;

        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        dpi = dm.densityDpi;

        int buttonWidth = quitButton.getWidth();

        int buttonSpacer = height / 11;
        offsetX = (width - buttonWidth) / 2;
        offsetYStart1PlayerButton = buttonSpacer * 2;
        offsetYStart2PlayersButton = buttonSpacer * 4;
        offsetYHightScoresButton = buttonSpacer * 5;
        offsetYQuitButton = buttonSpacer * 6;
        offsetYTextView = buttonSpacer * 10;
    }
}
