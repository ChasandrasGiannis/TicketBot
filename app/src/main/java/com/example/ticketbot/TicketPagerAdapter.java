package com.example.ticketbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Set;

public class TicketPagerAdapter extends RecyclerView.Adapter<TicketPagerAdapter.TicketViewHolder> {
    private List<Ticket> tickets;
    private TicketCountListener ticketCountListener;
    private Context context;

    public TicketPagerAdapter(List<Ticket> tickets, TicketCountListener ticketCountListener, Context context) {
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
        holder.binIcon.setOnClickListener(v -> showCancelDialog(holder.itemView.getContext(), position, ticket));
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    private void showCancelDialog(Context context, int position, Ticket canceledTicket) {
        new AlertDialog.Builder(context)
                .setTitle("Cancel Ticket")
                .setMessage("Are you sure you want to cancel this ticket?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    tickets.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, tickets.size());
                    ticketCountListener.onTicketCountChanged(tickets.size());
                    SharedPreferencesHelper.saveTickets(context, tickets);

                    // Update unavailable seats for the play
                    String play = canceledTicket.getTitle();
                    String time = canceledTicket.getTime();
                    Set<String> unavailableSeats = SharedPreferencesHelper.loadUnavailableSeats(context, play, time);
                    String seat = "Row " + (canceledTicket.getRow().charAt(0) - 'A') + " Seat " + canceledTicket.getSeat();
                    unavailableSeats.remove(seat);
                    SharedPreferencesHelper.saveUnavailableSeats(context, play, time, unavailableSeats);

                    showCancellationMessage(context);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showCancellationMessage(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Ticket Canceled")
                .setMessage("Your ticket has been canceled. If you paid for the ticket, you will receive a refund in the upcoming days.")
                .setPositiveButton("OK", null)
                .show();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        private TextView ticketTitle;
        private TextView ticketDate;
        private TextView ticketTime;
        private TextView ticketLocation;
        private TextView ticketAuditorium;
        private TextView ticketSeat;
        private TextView ticketType;
        private TextView ticketName;
        private TextView ticketPrice;
        private TextView ticketCode;
        private ImageView ticketQr;
        private ImageView ticketImage;
        private ImageView binIcon;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketTitle = itemView.findViewById(R.id.ticket_title);
            ticketDate = itemView.findViewById(R.id.ticket_date);
            ticketTime = itemView.findViewById(R.id.ticket_time);
            ticketLocation = itemView.findViewById(R.id.ticket_location);
            ticketAuditorium = itemView.findViewById(R.id.ticket_auditorium);
            ticketSeat = itemView.findViewById(R.id.ticket_seat);
            ticketType = itemView.findViewById(R.id.ticket_type);
            ticketName = itemView.findViewById(R.id.ticket_name);
            ticketPrice = itemView.findViewById(R.id.ticket_price);
            ticketCode = itemView.findViewById(R.id.ticket_code);
            ticketQr = itemView.findViewById(R.id.ticket_qr);
            ticketImage = itemView.findViewById(R.id.ticket_image);
            binIcon = itemView.findViewById(R.id.bin_icon);
        }

        public void bind(Ticket ticket) {
            ticketTitle.setText(" " + ticket.getTitle());
            ticketDate.setText(" " + ticket.getDate());
            ticketTime.setText(" " + ticket.getTime());
            ticketLocation.setText(" " + ticket.getLocation());
            String auditorium = ticket.getTitle().equals("Romeo and Juliet") ? "1" : "2";
            ticketAuditorium.setText(" " + auditorium);
            ticketSeat.setText(" " + ticket.getRow() + ticket.getSeat());
            ticketType.setText(" " + ticket.getType());
            ticketName.setText( " " + ticket.getName());
            ticketPrice.setText( " " + ticket.getPrice() + "$");
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
