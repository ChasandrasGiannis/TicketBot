package com.example.ticketbot;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Rating extends BaseActivity{

    TextView start,last,thanks;
    RatingBar ratingBar;
    ImageButton backtohelp,mic;
    Button submitStars,submitFeedback;
    EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        start=(TextView) findViewById(R.id.startingText);
        last=(TextView) findViewById(R.id.lastText);
        thanks=(TextView) findViewById(R.id.thanksText);

        //change font size
        float savedFontSize = FontSizeUtil.loadFontSize(this);
        start.setTextSize(savedFontSize);
        last.setTextSize(savedFontSize);
        thanks.setTextSize(savedFontSize);

        ratingBar=(RatingBar) findViewById(R.id.ratingBar);
        submitStars=(Button) findViewById(R.id.submitStars);

        submitStars.setTextSize(savedFontSize);
        submitStars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=String.valueOf(ratingBar.getRating());
                float ra=ratingBar.getRating();
                if(ra!=0){
                    ratingBar.setRating(0);
                    Toast.makeText(getApplicationContext(),"You successfully rated us with "+s +" Stars",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Please select a number of stars first ",Toast.LENGTH_SHORT).show();
                }

            }
        });

        inputText=(EditText) findViewById(R.id.editFeedback);
        submitFeedback=(Button) findViewById(R.id.submitFeedback);

        inputText.setTextSize(savedFontSize);
        submitFeedback.setTextSize(savedFontSize);

        FeedbackManager feedbackManager = new FeedbackManager(this);

        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s=inputText.getText().toString();
                if(!s.isEmpty())
                {
                    feedbackManager.saveFeedback(s);
                    inputText.setText(""); //erases what the user has written
                    Toast.makeText(getApplicationContext(),"Thanks for your feedback",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Please enter your feedback",Toast.LENGTH_SHORT).show();
                }

            }
            });

        //show list of feedbacks
        String allFeedbacks = feedbackManager.getAllFeedbacks();
        Log.d("FeedbackLogger", allFeedbacks);

        backtohelp=(ImageButton)findViewById(R.id.leftRateArrow) ;
        backtohelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Rating.this,HelpMenu.class);
                startActivity(i);
            }
        });

        mic=(ImageButton) findViewById(R.id.microphone);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak(v);
            }
        });


    }

    public void speak(View view){
        Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Start Speaking");
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            inputText.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");
        float savedFontSize = FontSizeUtil.loadFontSize(this);
        start.setTextSize(savedFontSize);
        last.setTextSize(savedFontSize);
        thanks.setTextSize(savedFontSize);
        submitStars.setTextSize(savedFontSize);
        inputText.setTextSize(savedFontSize);
        submitFeedback.setTextSize(savedFontSize);
    }
}
