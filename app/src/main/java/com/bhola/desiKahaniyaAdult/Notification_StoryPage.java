package com.bhola.desiKahaniyaAdult;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

public class Notification_StoryPage extends AppCompatActivity {
    ImageView back, play_audio;
    TextView storyText, textsize, title_textview;
    String date, heading, title, title_category;
    AlertDialog dialog;
    private TextToSpeech mTTS;
    Animation rotate_openAnim, rotate_closeAnim, fromBottom, toBottom;
    boolean clicked = false;
    FloatingActionButton add, share, copy, textsixe, favourite_button;
    String collection;
    String DB_TABLENUMBER;
    int _id;
    RewardedInterstitialAd mRewardedVideoAd;
    SeekBar seekBar;
    Button button;
    AdView mAdView;

    com.facebook.ads.InterstitialAd facebook_IntertitialAds;
    com.facebook.ads.AdView facebook_adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_page);

        try {
            if (SplashScreen.Ads_State.equals("active")) {
                showAds();
            }
        } catch (Exception ignored) {

        }


        Intents_and_InitViews();
        actionBar();


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Vibrator vibe = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(80);//80 represents the milliseconds (the duration of the vibration)
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, storyText.getText().toString());
                intent.setType("text/plain");
                intent = Intent.createChooser(intent, "Share By");

                v.getContext().startActivity(intent);
            }
        });


        favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Vibrator vibe = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(80);//80 represents the milliseconds (the duration of the vibration)
                Toast.makeText(Notification_StoryPage.this, "You Can noy Save Notification", Toast.LENGTH_SHORT).show();


            }
        });


        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Vibrator vibe = (Vibrator) v.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(80);//80 represents the milliseconds (the duration of the vibration)
                ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", storyText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(v.getContext(), "COPIED", Toast.LENGTH_SHORT).show();

            }
        });


        textsixe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loadAlertdialog();

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        storyText.setTextSize(progress);
                        textsize.setText(String.valueOf(progress));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOnbuttonClicked();

            }
        });


    }

    private void showAds() {


        if (SplashScreen.Ad_Network_Name.equals("admob")) {
            mAdView = findViewById(R.id.adView);
            ADS_ADMOB.BannerAd(this, mAdView);

            ADS_ADMOB rewarded_ads = new ADS_ADMOB(mRewardedVideoAd, this, getString(R.string.Rewarded_ADS_Unit_ID));
            rewarded_ads.RewardedVideoAds();
        } else {
            LinearLayout facebook_bannerAd_layput;
            facebook_bannerAd_layput = findViewById(R.id.banner_container);
            ADS_FACEBOOK.interstitialAd(this, facebook_IntertitialAds, getString(R.string.Facebbok_InterstitialAdUnit));
            ADS_FACEBOOK.bannerAds(this, facebook_adView, facebook_bannerAd_layput, getString(R.string.Facebbok_BannerAdUnit_1));
        }


    }


    @SuppressLint("SetTextI18n")
    private void Intents_and_InitViews() {

//Intents
        title = getIntent().getStringExtra("title");
        heading = getIntent().getStringExtra("Story");
        date = getIntent().getStringExtra("date");
        DB_TABLENUMBER = getIntent().getStringExtra("DB_TABLENUMBER");
        _id = getIntent().getIntExtra("_id", 0);

//InitViews
        storyText = findViewById(R.id.story_text);
        title_textview = findViewById(R.id.storyPage_Title_textview);
        textsixe = findViewById(R.id.floatingActionButtonText);
        copy = findViewById(R.id.floatingActionButtonCopy);
        share = findViewById(R.id.floatingActionButtonShare);
        favourite_button = findViewById(R.id.floatingActionFavouriteButton);
        favourite_button.setVisibility(View.GONE);

        add = findViewById(R.id.floatingActionButtonMain);


        rotate_openAnim = AnimationUtils.loadAnimation(this, R.anim.floatingbar_open_anim);
        rotate_closeAnim = AnimationUtils.loadAnimation(this, R.anim.floatingbar_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_floatingbar);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_floatingbar);


//Set Story and Title
        storyText.setText(heading.toString().trim().replaceAll("  ","\n\n").replaceAll("\\/",""));
        Log.d("STORY", "Intents_and_InitViews: " + heading);
        title_textview.setText(title);

    }


    private void addOnbuttonClicked() {
//setVisivility
        if (!clicked) {
            share.setVisibility(View.VISIBLE);
            copy.setVisibility(View.VISIBLE);
            textsixe.setVisibility(View.VISIBLE);

        } else {
            share.setVisibility(View.INVISIBLE);
            copy.setVisibility(View.INVISIBLE);
            textsixe.setVisibility(View.INVISIBLE);

        }

//setAnimation
        if (!clicked) {

            share.setAnimation(fromBottom);
            copy.setAnimation(fromBottom);
            textsixe.setAnimation(fromBottom);
            add.setAnimation(rotate_openAnim);
        } else {

            share.setAnimation(toBottom);
            copy.setAnimation(toBottom);
            textsixe.setAnimation(toBottom);
            add.setAnimation(rotate_closeAnim);

        }

        clicked = !clicked;
    }


    private void actionBar() {

        text2Speech();
        title_category = getIntent().getStringExtra("bhola2");
        collection = getIntent().getStringExtra("Collection");
        if (collection == null) {
            TextView title = findViewById(R.id.title_collection);
            title.setText("Notification Story");
        }

        back = findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (SplashScreen.Ads_State.equals("active")) {
            if (SplashScreen.Ad_Network_Name.equals("admob")) {
                ADS_ADMOB rewarded_ads = new ADS_ADMOB(mRewardedVideoAd, this, getString(R.string.Rewarded_ADS_Unit_ID));
                rewarded_ads.RewardedVideoAds();
            } else {
                ADS_FACEBOOK.interstitialAd(this, facebook_IntertitialAds, getString(R.string.Facebbok_InterstitialAdUnit));

            }
        }

    }

    private void text2Speech() {
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                mTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            mTTS.setLanguage(new Locale("hin")); // Setting Hindi language
                        }
                    }
                });
            }
        });

    }

    private void speak(SeekBar seek_bar_pitch, SeekBar seek_bar_speed) {

        String text = heading;

        float pitch = (float) seek_bar_pitch.getProgress() / 50;
        if (pitch < 0.1) pitch = 0.1f;
        float speed = (float) seek_bar_speed.getProgress() / 50;
        if (speed < 0.1) speed = 0.1f;
        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);


        int dividerLimit = 3900;
        if (text.length() >= dividerLimit) {
            int textLength = text.length();
            ArrayList<String> texts = new ArrayList<String>();
            int count = textLength / dividerLimit + ((textLength % dividerLimit == 0) ? 0 : 1);
            int start = 0;
            int end = text.indexOf(" ", dividerLimit);
            for (int i = 1; i <= count; i++) {
                texts.add(text.substring(start, end));
                start = end;
                if ((start + dividerLimit) < textLength) {
                    end = text.indexOf(" ", start + dividerLimit);
                } else {
                    end = textLength;
                }
            }
            for (int i = 0; i < texts.size(); i++) {
                mTTS.speak(texts.get(i), TextToSpeech.QUEUE_ADD, null);
            }
        } else {
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
//        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);

    }


    void loadAlertdialog() {

        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View promptView = inflater.inflate(R.layout.seekbar, null);
        builder.setView(promptView);
        builder.setCancelable(false);
        textsize = promptView.findViewById(R.id.textSize);

        dialog = builder.create();
        dialog.show();
        seekBar = promptView.findViewById(R.id.your_dialog_seekbar);
        button = promptView.findViewById(R.id.your_dialog_button);
    }


    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();

        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mTTS != null) {
            mTTS.stop();
        }
        super.onPause();
    }

    @Override
    protected void onStop() {
        if (mTTS != null) {
            mTTS.stop();
        }
        super.onStop();
    }


}