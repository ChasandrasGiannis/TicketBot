package com.example.ticketbot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class FaqActivity extends  BaseActivity{
    ImageButton back,askdisc,askloc,askch,askcancel;
    TextView textDisc,textLoc,textChange,textCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        textDisc=(TextView)findViewById(R.id.textDisc);
        textLoc=(TextView) findViewById(R.id.textLoc);
        textChange=(TextView) findViewById(R.id.textChange);
        textCancel=(TextView) findViewById(R.id.textCancel);

        //change font size
        float savedFontSize = FontSizeUtil.loadFontSize(this);
        textDisc.setTextSize(savedFontSize);
        textLoc.setTextSize(savedFontSize);
        textChange.setTextSize(savedFontSize);
        textCancel.setTextSize(savedFontSize);



        back=(ImageButton) findViewById(R.id.leftFaqArrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        askdisc=(ImageButton) findViewById(R.id.buttonDisc);
        askdisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FaqActivity.this, ChatBotActivity.class);
                intent.putExtra("messageFaqDisc",textDisc.getText() );
                startActivity(intent);
            }
        });

        askloc=(ImageButton) findViewById(R.id.buttonLoc);
        askloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FaqActivity.this, ChatBotActivity.class);
                intent.putExtra("messageFaqLoc",textLoc.getText() );
                startActivity(intent);
            }
        });

        askch=(ImageButton) findViewById(R.id.buttonChange);
        askch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FaqActivity.this, ChatBotActivity.class);
                intent.putExtra("messageFaqChange",textChange.getText() );
                startActivity(intent);
            }
        });

        askcancel=(ImageButton) findViewById(R.id.buttonCancel);
        askcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FaqActivity.this, ChatBotActivity.class);
                intent.putExtra("messageFaqCancel",textCancel.getText() );
                startActivity(intent);
            }
        });

        Button accessibilityButton = findViewById(R.id.accessibility_button);
        accessibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FaqActivity.this, AccessibilityMenu.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");
        //change font size
        float savedFontSize = FontSizeUtil.loadFontSize(this);
        textDisc.setTextSize(savedFontSize);
        textLoc.setTextSize(savedFontSize);
        textChange.setTextSize(savedFontSize);
        textCancel.setTextSize(savedFontSize);

    }
}
