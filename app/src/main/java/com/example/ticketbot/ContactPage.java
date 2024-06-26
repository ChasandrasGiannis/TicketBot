package com.example.ticketbot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;


public class ContactPage extends BaseActivity {

    ImageButton accmenu,backtohelp;
    private TextView description;
    private TextView email,phone1,theater,phone2,address,wh;
    private ConstraintLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactpage);

        description=(TextView) findViewById(R.id.textContact);
        email=(TextView) findViewById(R.id.textEmail1);
        phone1=(TextView) findViewById(R.id.textPhone1);
        theater=(TextView) findViewById(R.id.textTheaterTitle);
        phone2=(TextView) findViewById(R.id.textPhone2);
        address=(TextView) findViewById(R.id.textAddress);
        wh=(TextView) findViewById(R.id.textWorkingHours);

        parentLayout = findViewById(R.id.parentlayout);

        float savedFontSize = FontSizeUtil.loadFontSize(this);
        description.setTextSize(savedFontSize);
        email.setTextSize(savedFontSize);
        phone1.setTextSize(savedFontSize);
        theater.setTextSize(savedFontSize);
        phone2.setTextSize(savedFontSize);
        address.setTextSize(savedFontSize);
        wh.setTextSize(savedFontSize);

 //       adjustParentLayoutSize(description);
//        adjustParentLayoutSize(email);
//        adjustParentLayoutSize(phone1);
//        adjustParentLayoutSize(theater);
//        adjustParentLayoutSize(phone2);
//        adjustParentLayoutSize(address);
        

        accmenu=(ImageButton) findViewById(R.id.imageAc);
        accmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent i=new Intent(getApplicationContext(),AccessibilityMenu.class);
               startActivity(i);
            }
        });

        backtohelp=(ImageButton)findViewById(R.id.leftContactArrow) ;
        backtohelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ContactPage.this,HelpMenu.class);
                startActivity(i);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");

        float savedFontSize = FontSizeUtil.loadFontSize(this);
        description.setTextSize(savedFontSize);
        email.setTextSize(savedFontSize);
        phone1.setTextSize(savedFontSize);
        theater.setTextSize(savedFontSize);
        phone2.setTextSize(savedFontSize);
        address.setTextSize(savedFontSize);
        wh.setTextSize(savedFontSize);

 //       adjustParentLayoutSize(description);
//        adjustParentLayoutSize(email);
//        adjustParentLayoutSize(phone1);
//        adjustParentLayoutSize(theater);
//        adjustParentLayoutSize(phone2);
//        adjustParentLayoutSize(address);
    }


    private void adjustParentLayoutSize(TextView textView) {
        int[] textSize = ViewUtil.measureTextView(textView);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) parentLayout.getLayoutParams();
        params.width = textSize[0] *10;
        params.height = textSize[1] *5;
        parentLayout.setLayoutParams(params);
    }

}
