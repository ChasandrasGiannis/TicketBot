package com.example.ticketbot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MyTicketsActivity extends AppCompatActivity implements TicketPagerAdapter.TicketCountListener {

    private static List<Ticket> tickets;
    public static TicketPagerAdapter adapter;
    private TextView ticketCountTextView,textTitle;
    private int totalTickets;
    private AppCompatButton accessibility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        tickets = SharedPreferencesHelper.loadTickets(this);
        if (tickets == null) {
            tickets = new ArrayList<>();
        }

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        ticketCountTextView = findViewById(R.id.ticket_count);

        textTitle=(TextView) findViewById(R.id.textTitle);
        //change font size
        float savedFontSize = FontSizeUtil.loadFontSize(this);
        textTitle.setTextSize(savedFontSize+2.0f);
        ticketCountTextView.setTextSize(savedFontSize);

        accessibility=findViewById(R.id.accessibility_button);
        accessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MyTicketsActivity.this,AccessibilityMenu.class);
                startActivity(i);
            }
        });

        totalTickets = tickets.size();
        updateTicketCount(0);

        adapter = new TicketPagerAdapter(tickets, this, this);
        viewPager.setAdapter(adapter);


        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updateTicketCount(position);
            }
        });

        ImageView backArrow = findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTicketsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void updateTicketCount(int currentPosition) {
        String text;
        if (totalTickets == 0) {
            text = "Ticket 0 of 0";
        } else {
            text = "Ticket " + (currentPosition + 1) + " of " + totalTickets;
        }
        ticketCountTextView.setText(text);
    }

    @Override
    public void onTicketCountChanged(int newCount) {
        totalTickets = newCount;
        updateTicketCount(0);
        saveTickets(this);
    }

    public static void addTicket(Ticket ticket, Context context) {
        if (tickets == null) {
            tickets = new ArrayList<>();
        }
        tickets.add(ticket);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        saveTickets(context);
    }

    public static void removeTicket(Ticket ticket, Context context) {
        if (tickets.contains(ticket)) {
            tickets.remove(ticket);
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        saveTickets(context);
    }

    //George
//    public static void removeTicket(Ticket ticket, Context context) {
//        if (tickets != null && tickets.contains(ticket)) {
//            tickets.remove(ticket);
//            if (adapter != null) {
//                adapter.notifyDataSetChanged();
//            }
//
//            Set<String> unavailableSeats = SharedPreferencesHelper.loadUnavailableSeats(context, ticket.getTitle());
//            String seatLabel = ticket.getRow() + ticket.getSeat();
//            unavailableSeats.remove(seatLabel);
//            SharedPreferencesHelper.saveUnavailableSeats(context, ticket.getTitle(), unavailableSeats);
//
//            saveTickets(context);
//        }
//    }

    public static void saveTickets(Context context) {
        if (context != null) {
            SharedPreferencesHelper.saveTickets(context, tickets);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");

        float savedFontSize = FontSizeUtil.loadFontSize(this);
        textTitle.setTextSize(savedFontSize+2.0f);
        ticketCountTextView.setTextSize(savedFontSize);
    }
}
