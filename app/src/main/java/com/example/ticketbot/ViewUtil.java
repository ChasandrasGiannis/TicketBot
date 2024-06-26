package com.example.ticketbot;

import android.graphics.Paint;
import android.widget.TextView;

public class ViewUtil {
    public static int[] measureTextView(TextView textView) {
        Paint paint = textView.getPaint();
        String text = textView.getText().toString();
        float width = paint.measureText(text);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float height = fontMetrics.descent - fontMetrics.ascent;
        return new int[]{Math.round(width), Math.round(height)};
    }
}

