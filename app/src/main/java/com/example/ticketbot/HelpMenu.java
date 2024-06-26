package com.example.ticketbot;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HelpMenu extends BaseActivity {

    private TextView terms,contact,theater,accmenu,rating,walkthrough;

    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpmenu);

        ImageButton buttonHome = findViewById(R.id.button_home);
        ImageButton buttonSearch = findViewById(R.id.button_search);
        ImageButton buttonFaq = findViewById(R.id.button_faq);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpMenu.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpMenu.this, ChatBotActivity.class);
                startActivity(intent);
            }
        });

        buttonFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpMenu.this, HelpMenu.class);
                startActivity(intent);
            }
        });



        terms=findViewById(R.id.buttonTerms);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HelpMenu.this,TermsConditions.class);
                startActivity(intent);
            }
        });

        contact=findViewById(R.id.button);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HelpMenu.this,ContactPage.class);
                startActivity(intent);
            }
        });

        theater=findViewById(R.id.buttonDisc);
        theater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://www.megaron.gr/");
            }
        });

        accmenu=findViewById(R.id.buttonAcc);
        accmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HelpMenu.this,AccessibilityMenu.class);
                startActivity(intent);
            }
        });

        rating=findViewById(R.id.buttonRating);
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HelpMenu.this,Rating.class);
                startActivity(intent);
                }
        });

        walkthrough=(TextView)findViewById(R.id.buttonWalkthrough);
        walkthrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        back = findViewById(R.id.leftHelpArrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelpMenu.this, MainActivity.class);
                startActivity(intent);
            }
        });

        float savedFontSize = FontSizeUtil.loadFontSize(this);
        terms.setTextSize(savedFontSize);
        contact.setTextSize(savedFontSize);
        theater.setTextSize(savedFontSize);
        accmenu.setTextSize(savedFontSize);
        rating.setTextSize(savedFontSize);
        walkthrough.setTextSize(savedFontSize);

    }

    void gotoUrl(String s){
        try {
            Uri uri=Uri.parse(s);
            startActivity(new Intent(Intent.ACTION_VIEW,uri));
        }catch(Exception e){
            Toast.makeText(getApplicationContext(),"No website linked",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle","onStart invoked");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");
        float savedFontSize = FontSizeUtil.loadFontSize(this);
        terms.setTextSize(savedFontSize);
        contact.setTextSize(savedFontSize);
        theater.setTextSize(savedFontSize);
        accmenu.setTextSize(savedFontSize);
        rating.setTextSize(savedFontSize);
        walkthrough.setTextSize(savedFontSize);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause invoked");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle","onStop invoked");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle","onDestroy invoked");
    }
}