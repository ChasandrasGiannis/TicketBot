package com.example.ticketbot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Set;

public class PaymentActivity extends AppCompatActivity {
    private EditText cardNumberEditText, cardholderNameEditText, expiryMonthEditText, expiryYearEditText, cvcEditText;
    private TextView totalPriceTextView;
    private ArrayList<String> ticketTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cardNumberEditText = findViewById(R.id.card_number);
        cardholderNameEditText = findViewById(R.id.cardholder_name);
        expiryMonthEditText = findViewById(R.id.expiry_month);
        expiryYearEditText = findViewById(R.id.expiry_year);
        cvcEditText = findViewById(R.id.cvc);
        totalPriceTextView = findViewById(R.id.total_price);

        Intent intent = getIntent();
        ticketTypes = intent.getStringArrayListExtra("ticket_types");

        displayTotalPrice();

        Button payButton = findViewById(R.id.pay_button);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnPaymentNotificationToChatbot();
            }
        });

        Button cancelButtonPayment = findViewById(R.id.cancel_button_payment);
        cancelButtonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnPaymentNotificationToChatbot();
            }
        });
    }
    private boolean validateForm() {
        if (cardNumberEditText.getText().toString().isEmpty() ||
                cardholderNameEditText.getText().toString().isEmpty() ||
                expiryMonthEditText.getText().toString().isEmpty() ||
                expiryYearEditText.getText().toString().isEmpty() ||
                cvcEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void displayTotalPrice() {
        int totalPrice = 0;
        for (String type : ticketTypes) {
            if (type.equals("regular")) {
                totalPrice += 20;
            } else if (type.equals("student")) {
                totalPrice += 10;
            } else if (type.equals("educator")) {
                totalPrice += 10;
            } else if (type.equals("elderly")) {
                totalPrice += 15;
            }else if (type.equals("family")){
                totalPrice += 12;
            } else if (type.equals("unemployed")) {
                totalPrice += 10;
            } else {
                totalPrice += 20;
            }
        }
        totalPriceTextView.setText("Total Price: " + totalPrice + " €");
    }

    private void returnPaymentNotificationToChatbot() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
