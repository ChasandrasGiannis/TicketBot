package com.example.ticketbot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TermsConditions extends AppCompatActivity {

    private ImageButton gobacktoHelp,gotonextpage;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        textView = (TextView) findViewById(R.id.textTermsA);
        float savedFontSize = FontSizeUtil.loadFontSize(this);
        textView.setTextSize(savedFontSize);

        gobacktoHelp=findViewById(R.id.leftTermsArrow);
        gobacktoHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TermsConditions.this,HelpMenu.class);
                startActivity(intent);
            }
        });

        gotonextpage=findViewById(R.id.RightTermsArrow);
        gotonextpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TermsConditions.this,TermsConditions2.class);
                startActivity(intent);
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
