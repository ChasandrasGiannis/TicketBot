package com.example.ticketbot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class TermsConditions2 extends BaseActivity {
    private ImageButton gobacktohelp;
    private ImageButton gobacktofirst,accmenu;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms2);
        textView = (TextView) findViewById(R.id.textTermsB);

        float savedFontSize = FontSizeUtil.loadFontSize(this);
        textView.setTextSize(savedFontSize);


        gobacktohelp = findViewById(R.id.leftTerms2Arrow);
        gobacktohelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermsConditions2.this, HelpMenu.class);
                startActivity(intent);
            }
        });

        gobacktofirst=findViewById(R.id.previousTerms2Arrow);
        gobacktofirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermsConditions2.this, TermsConditions.class);
                startActivity(intent);
            }
        });

        accmenu=findViewById(R.id.imageAc);
        accmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),AccessibilityMenu.class);
                startActivity(i);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");

        float savedFontSize = FontSizeUtil.loadFontSize(this);
        textView.setTextSize(savedFontSize);
    }
}
