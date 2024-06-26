package com.example.ticketbot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class SelectTicketsActivity extends AppCompatActivity implements TicketSelectorAdapter.TicketCountListener {

    private static List<Ticket> tickets;
    public static TicketSelectorAdapter adapter;
    private TextView ticketCountTextView;
    private int totalTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_tickets);

        tickets = SharedPreferencesHelper.loadTickets(this);
        if (tickets == null) {
            tickets = new ArrayList<>();
        }

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        ticketCountTextView = findViewById(R.id.ticket_count);
        androidx.appcompat.widget.AppCompatButton doneButton = findViewById(R.id.button_done);

        totalTickets = tickets.size();
        updateTicketCount(0);

        adapter = new TicketSelectorAdapter(tickets, this, this);
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
                returnTicketsToChatbot(new ArrayList<>());
            }
        });

        doneButton.setOnClickListener(v -> {
            List<Ticket> selectedTickets = adapter.getSelectedTickets();
            returnTicketsToChatbot(selectedTickets);
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

    public static void saveTickets(Context context) {
        if (context != null) {
            SharedPreferencesHelper.saveTickets(context, tickets);
        }
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

    private void returnTicketsToChatbot(List<Ticket> selectedTickets) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("selected_tickets", new ArrayList<>(selectedTickets));
        setResult(RESULT_OK, intent);
        finish();
    }
}
