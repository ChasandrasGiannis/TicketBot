package com.example.ticketbot;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyFontSizeToViews(getWindow().getDecorView());
    }

    void applyFontSizeToViews(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) root;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                applyFontSizeToViews(child);
            }
        } else if (root instanceof TextView) {
            FontSizeUtil.applyFontSize(this, (TextView) root);
        }
    }
}

