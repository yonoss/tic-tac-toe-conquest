package com.ion.xo;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import static com.ion.xo.game.Session.*;


public class FullScreenAdMob extends AppCompatActivity {

    private int offsetX;
    private int offsetXMessageView;
    private int offsetYStartButton;
    private int offsetYResumeButton;
    private int offsetYRestart;
    private int offsetYBackToMenuButton;
    private int offsetYPlayAgainButton;
    private int offsetYTextInputLayout1;
    private int offsetYTextInputLayout2;
    private int offsetYMessageView;

    private Button startButton;
    private Button resumeButton;
    private Button restartButton;
    private Button playAgainButton;
    private Button backToMenuButton;
    private InterstitialAd mInterstitialAd;
    private TextView messageView;
    private TextInputLayout textInputLayout1;
    private TextInputLayout textInputLayout2;
    private int buttonClicked= -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        super.onCreate(savedInstanceState);
        buttonClicked = -1;

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_full_screen_ad_mob);
        startButton = (findViewById(R.id.start));
        startButton.setVisibility(View.GONE);
        resumeButton = (findViewById(R.id.resume));
        resumeButton.setVisibility(View.GONE);
        restartButton = (findViewById(R.id.restart));
        restartButton.setVisibility(View.GONE);
        playAgainButton = (findViewById(R.id.play_again));
        playAgainButton.setVisibility(View.GONE);
        messageView = (findViewById(R.id.message_view));
        messageView.setVisibility(View.GONE);
        textInputLayout1 = (findViewById(R.id.text_input_1));
        textInputLayout1.setVisibility(View.GONE);
        textInputLayout2 = (findViewById(R.id.text_input_2));
        textInputLayout2.setVisibility(View.GONE);

        backToMenuButton = (findViewById(R.id.back_to_menu));
        backToMenuButton.setVisibility(View.VISIBLE);
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClicked = R.id.back_to_menu;
                showInterstitial();
            }
        });

        if (level.equals(Level.START_1_PLAYER) || level.equals(Level.START_2_PLAYERS)) {
            startButton.setVisibility(View.VISIBLE);
            textInputLayout1.setVisibility(View.VISIBLE);
            if (aiEnabled) {
                textInputLayout2.setVisibility(View.GONE);
            } else {
                textInputLayout2.setVisibility(View.VISIBLE);
            }
            backToMenuButton.setVisibility(View.GONE);
            restartButton.setVisibility(View.GONE);
            resumeButton.setVisibility(View.GONE);
            playAgainButton.setVisibility(View.GONE);
            messageView.setVisibility(View.GONE);
            // Create the start button, which tries to show an interstitial when clicked.
            startButton.setEnabled(true);
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonClicked = R.id.start;
                    showInterstitial();
                }
            });
        }

        if (level.equals(Level.PAUSE)) {
            startButton.setVisibility(View.GONE);
            textInputLayout1.setVisibility(View.GONE);
            textInputLayout2.setVisibility(View.GONE);
            backToMenuButton.setVisibility(View.VISIBLE);
            resumeButton.setVisibility(View.VISIBLE);
            restartButton.setVisibility(View.VISIBLE);
            playAgainButton.setVisibility(View.GONE);
            messageView.setVisibility(View.GONE);
            // Create the back to menu button.
            backToMenuButton.setEnabled(true);
            backToMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonClicked = R.id.back_to_menu;
                    showInterstitial();
                }
            });

            // Create the back to game button.
            resumeButton.setEnabled(true);
            resumeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonClicked = R.id.resume;
                    showInterstitial();
                }
            });

            // Create the restart game button.
            restartButton.setEnabled(true);
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonClicked = R.id.restart;
                    showInterstitial();
                }
            });
        }

        if (level.equals(Level.GAME_OVER)) {
            setGameEndMessage();

            startButton.setVisibility(View.GONE);
            textInputLayout1.setVisibility(View.GONE);
            textInputLayout2.setVisibility(View.GONE);
            backToMenuButton.setVisibility(View.VISIBLE);
            resumeButton.setVisibility(View.GONE);
            restartButton.setVisibility(View.GONE);
            playAgainButton.setVisibility(View.VISIBLE);
            messageView.setVisibility(View.VISIBLE);

            // Create the back to menu button.
            backToMenuButton.setEnabled(true);
            backToMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonClicked = R.id.back_to_menu;
                    showInterstitial();
                }
            });

            // Create the back to menu button.
            playAgainButton.setEnabled(true);
            playAgainButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    buttonClicked = R.id.play_again;
                    showInterstitial();
                }
            });
        }

        // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
        mInterstitialAd = newInterstitialAd();
        loadInterstitial();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        arrangeButtons();
    }

    private InterstitialAd newInterstitialAd() {
        InterstitialAd interstitialAd = new InterstitialAd(this);

        interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        //interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                startButton.setEnabled(true);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                startButton.setEnabled(true);
            }

            @Override
            public void onAdClosed() {
                // Proceed to the next level.
                goToNextLevel();
            }
        });
        return interstitialAd;
    }

    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise toast and reload the ad.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            // Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show();
            goToNextLevel();
        }
    }

    private void loadInterstitial() {
        // Disable the start button and load the ad.
        startButton.setEnabled(true);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void goToNextLevel() {
        if (buttonClicked == R.id.start) {
            clickStart();
        } else if (buttonClicked == R.id.restart || buttonClicked == R.id.play_again) {
            clickRestart();
        }
        else if (buttonClicked == R.id.resume) {
            clickResume();
        } else if (buttonClicked == R.id.back_to_menu) {
            clickBackToMenu();
        }
    }

    private void clickStart() {
        level = Level.GAME;
        initialized = false;
        background_id = -1;
        orientation = -1;
        USER_1 = textInputLayout1.getEditText().getText().toString();
        if (USER_1 == null || USER_1.equals("")) {
            USER_1 = this.getString(R.string.player_1_name);
        }

        if (!aiEnabled) {
            USER_2 = textInputLayout2.getEditText().getText().toString();
            if (USER_2 == null || USER_2.equals("")) {
                USER_2 = this.getString(R.string.player_2_name);
            }
        }

        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    private void clickResume() {
        level = Level.GAME;
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    private void clickRestart() {
        level = Level.GAME;
        initialized = false;
        background_id = -1;
        orientation = -1;
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
    }

    private void clickBackToMenu() {
        level = Level.MENU;
        initialized = false;
        boardCells = null;
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void arrangeButtons() {
        calculatePositions();
        positionButtons();
    }

    private void positionButtons() {
        startButton.setX(offsetX);
        startButton.setY(offsetYStartButton);

        resumeButton.setX(offsetX);
        resumeButton.setY(offsetYResumeButton);

        restartButton.setX(offsetX);
        restartButton.setY(offsetYRestart);

        playAgainButton.setX(offsetX);
        playAgainButton.setY(offsetYPlayAgainButton);

        backToMenuButton.setX(offsetX);
        backToMenuButton.setY(offsetYBackToMenuButton);

        messageView.setX(offsetXMessageView);
        messageView.setY(offsetYMessageView);

        textInputLayout1.setX(offsetX);
        textInputLayout1.setY(offsetYTextInputLayout1);

        textInputLayout2.setX(offsetX);
        textInputLayout2.setY(offsetYTextInputLayout2);
    }

    private void calculatePositions() {
        int height = this.findViewById(android.R.id.content).getHeight();
        int width = this.findViewById(android.R.id.content).getWidth();
        viewHeight = height;
        viewWidth = width;

        int buttonWidth = startButton.getWidth();
        if (buttonWidth == 0) {
            buttonWidth = backToMenuButton.getWidth();
        }

        int buttonSpacer = height / 11;
        offsetX = (width - buttonWidth) / 2;

        offsetYTextInputLayout1 = buttonSpacer * 4;
        offsetYTextInputLayout2 =  buttonSpacer * 2;
        offsetYStartButton = buttonSpacer * 6;

        offsetYResumeButton = buttonSpacer * 2;
        offsetYRestart = buttonSpacer * 4;
        offsetYBackToMenuButton = buttonSpacer * 6;

        offsetYMessageView = buttonSpacer * 2;
        offsetYPlayAgainButton = buttonSpacer * 4;

        int messageViewWidth = messageView.getWidth();
        offsetXMessageView = (width - messageViewWidth) / 2;
    }

    private void setGameEndMessage() {
        String message = "";
        if (USER_1_SCORE > USER_2_SCORE) {
            message = USER_1 + " " + getString(R.string.win_message);
        } else if (USER_2_SCORE > USER_1_SCORE) {
            message = USER_2 + " " + getString(R.string.win_message);
        } else {
            message = getString(R.string.draw);
        }
        messageView.setText(message);
    }
}
