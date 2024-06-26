package com.example.ticketbot;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TicketSelectorAdapter extends RecyclerView.Adapter<TicketSelectorAdapter.TicketViewHolder> {
    private List<Ticket> tickets;
    private TicketCountListener ticketCountListener;
    private Context context;
    private List<Ticket> selectedTickets = new ArrayList<>();

    public TicketSelectorAdapter(List<Ticket> tickets, TicketCountListener ticketCountListener, Context context) {
        this.tickets = tickets;
        this.ticketCountListener = ticketCountListener;
        this.context = context;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_page, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.bind(ticket);

        holder.selectButton.setVisibility(View.VISIBLE);

        holder.selectButton.setOnClickListener(v -> {
            if (selectedTickets.contains(ticket)) {
                selectedTickets.remove(ticket);
                holder.selectButton.setText("Select");
            } else {
                selectedTickets.add(ticket);
                holder.selectButton.setText("Selected!");
            }
        });
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public List<Ticket> getSelectedTickets() {
        return selectedTickets;
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        private TextView ticketTitle;
        private TextView ticketDate;
        private TextView ticketTime;
        private TextView ticketLocation;
        private TextView ticketAuditorium;
        private TextView ticketType;
        private TextView ticketName;
        private TextView ticketPrice;
        private TextView ticketCode;
        private ImageView ticketQr;
        private ImageView ticketImage;
        private ImageView binIcon;
        private androidx.appcompat.widget.AppCompatButton selectButton;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketTitle = itemView.findViewById(R.id.ticket_title);
            ticketDate = itemView.findViewById(R.id.ticket_date);
            ticketTime = itemView.findViewById(R.id.ticket_time);
            ticketLocation = itemView.findViewById(R.id.ticket_location);
            ticketAuditorium = itemView.findViewById(R.id.ticket_auditorium);
            ticketType = itemView.findViewById(R.id.ticket_type);
            ticketName = itemView.findViewById(R.id.ticket_name);
            ticketPrice = itemView.findViewById(R.id.ticket_price);
            ticketCode = itemView.findViewById(R.id.ticket_code);
            ticketQr = itemView.findViewById(R.id.ticket_qr);
            ticketImage = itemView.findViewById(R.id.ticket_image);
            binIcon = itemView.findViewById(R.id.bin_icon);
            selectButton = itemView.findViewById(R.id.button_select);
            binIcon.setVisibility(View.INVISIBLE);
        }

        public void bind(Ticket ticket) {
            ticketTitle.setText(ticket.getTitle());
            ticketDate.setText(ticket.getDate());
            ticketTime.setText(ticket.getTime());
            ticketLocation.setText(ticket.getLocation());
            ticketAuditorium.setText("Row: " + ticket.getRow() + " Seat: " + ticket.getSeat());
            ticketType.setText("Ticket Type: " + ticket.getType());
            ticketName.setText("Name: " + ticket.getName());
            ticketPrice.setText("Price: " + ticket.getPrice() + "$");
            ticketCode.setText(ticket.getCode());
            ticketImage.setImageResource(ticket.getImageResId());
        }
    }

    public interface TicketCountListener {
        void onTicketCountChanged(int newCount);
    }

    public Context getContext() {
        return context;
    }
}
