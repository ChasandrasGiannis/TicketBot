package com.example.ticketbot;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;


public class MainActivity extends BaseActivity {

    TextView mytick,call,buy,through,voice,faq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mytick=(TextView)findViewById(R.id.textMytick);
        call=(TextView) findViewById(R.id.textCall);
        buy=(TextView) findViewById(R.id.textBuy);
        through=(TextView) findViewById(R.id.textTh);
        voice=(TextView) findViewById(R.id.textVoice);
        faq=(TextView) findViewById(R.id.textFaq);

        //change font size
        float savedFontSize = FontSizeUtil.loadFontSize(this);
        mytick.setTextSize(savedFontSize);
        call.setTextSize(savedFontSize);
        buy.setTextSize(savedFontSize);
        through.setTextSize(savedFontSize);
        voice.setTextSize(savedFontSize);
        faq.setTextSize(savedFontSize);

        SharedPreferencesHelper.initializeDefaultUnavailableSeats(this);

        ImageButton buttonHome = findViewById(R.id.button_home);
        ImageButton buttonSearch = findViewById(R.id.button_search);
        ImageButton buttonFaq = findViewById(R.id.button_faq);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatBotActivity.class);
                startActivity(intent);
            }
        });

        buttonFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpMenu.class);
                startActivity(intent);
            }
        });

        FrameLayout buyTicketsFrame = findViewById(R.id.buyTicketsFrame);
        buyTicketsFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatBotActivity.class);
                startActivity(intent);
            }
        });

        FrameLayout myTicketsFrame = findViewById(R.id.myTicketsFrame);
        myTicketsFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyTicketsActivity.class);
                startActivity(intent);
            }
        });

        FrameLayout faqFrame = findViewById(R.id.faqFrame);
        faqFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FaqActivity.class);
                startActivity(intent);
            }
        });

        FrameLayout callFrame = findViewById(R.id.callRep);
        callFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "tel:1234567890";
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse(phoneNumber));

                if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                } else {
                    requestPermissions(new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                }
            }
        });

        Button accessibilityButton = findViewById(R.id.accessibility_button);
        accessibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AccessibilityMenu.class);
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
        mytick.setTextSize(savedFontSize);
        call.setTextSize(savedFontSize);
        buy.setTextSize(savedFontSize);
        through.setTextSize(savedFontSize);
        voice.setTextSize(savedFontSize);
        faq.setTextSize(savedFontSize);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                String phoneNumber = "tel:1234567890";
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse(phoneNumber));
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                }
            } else {
                //TODO REPLACE WITH TOAST
            }
        }
    }
}
