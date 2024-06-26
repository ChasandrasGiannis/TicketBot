package com.example.ticketbot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;


public class AccessibilityMenu extends BaseActivity {

    TextView text,big,textPalette;
    SeekBar seekbar;
    TextView vocal,voice;
    SwitchCompat grey;
    private ConstraintLayout parentLayout;
    Boolean greymode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int seekValue;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibilitymenu);

        text=(TextView) findViewById(R.id.titleAc);
        big=(TextView) findViewById(R.id.titleBig);
        textPalette=(TextView) findViewById(R.id.textPalette);

        vocal=(TextView) findViewById(R.id.textVocal);
        grey=findViewById(R.id.switchMode);
        voice=(TextView) findViewById(R.id.voiceControl);

        back=(ImageButton) findViewById(R.id.leftAccArrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //change theme
        sharedPreferences=getSharedPreferences("MODE", Context.MODE_PRIVATE);
        greymode=sharedPreferences.getBoolean("greymode",false);
        if(greymode) {
            grey.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        grey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(greymode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor=sharedPreferences.edit();
                    editor.putBoolean("greymode",false);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor=sharedPreferences.edit();
                    editor.putBoolean("greymode",true);
                }
                editor.apply();
            }
        });


        seekbar=(SeekBar) findViewById(R.id.seekBar2);
        parentLayout = findViewById(R.id.parentlayout);



        // Load saved font size
        float savedFontSize = FontSizeUtil.loadFontSize(this);
        text.setTextSize(savedFontSize);
        big.setTextSize(savedFontSize);
        vocal.setTextSize(savedFontSize);
        grey.setTextSize(savedFontSize);
        voice.setTextSize(savedFontSize);
        textPalette.setTextSize(savedFontSize);
        seekbar.setProgress((int) savedFontSize);

//        adjustParentLayoutSize(text);
//        adjustParentLayoutSize(big);
//        adjustParentLayoutSize(vocal);
//        adjustParentLayoutSize(grey);
//        adjustParentLayoutSize(voice);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekValue=progress;
                text.setTextSize(seekValue);
                big.setTextSize(seekValue);
                vocal.setTextSize(seekValue);
                grey.setTextSize(seekValue);
                textPalette.setTextSize(seekValue);
                voice.setTextSize(seekValue);

                // Save the new font size
                FontSizeUtil.saveFontSize(AccessibilityMenu.this, seekValue);

                // Apply font size to all views
                applyFontSizeToViews(getWindow().getDecorView());

                // Adjust parent layout size
//                adjustParentLayoutSize(text);
//                adjustParentLayoutSize(big);
//                adjustParentLayoutSize(vocal);
//                adjustParentLayoutSize(grey);
//                adjustParentLayoutSize(voice);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    private void adjustParentLayoutSize(TextView textView) {
        int[] textSize = ViewUtil.measureTextView(textView);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) parentLayout.getLayoutParams();
        params.width = textSize[0] + 20; // Add padding if needed
        params.height = textSize[1] + 20; // Add padding if needed
        parentLayout.setLayoutParams(params);
    }



}
