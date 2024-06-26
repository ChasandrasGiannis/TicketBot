package com.example.ticketbot;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class Auditorium extends BaseActivity {

    TextView cont,canc,starting,auditTitle,stage,textAv,textBooked,textSel;
    TextView textselectedPlay,textTime,textSeat,textPrice;
    private LinearLayout toggleLinearLayout;

    private GridLayout seatGrid;
    private static final int ROWS = 8;
    private static final int COLUMNS = 10;
    private ArrayList<String> selectedSeats = new ArrayList<>();
    private ArrayList<String> seats = new ArrayList<>();
    private ArrayList<String> rows = new ArrayList<>();
    private ArrayList<String> ticketTypes = new ArrayList<>();
    private int ticketCount;
    private String selectedPlay;
    private ImageButton accessibility;

    private String selectedTime;
    String[] discounts={"student","unemployed","3 and above children"};
    AutoCompleteTextView autocomplete;
    ArrayAdapter<String> arrayAdapter;
    private Map<Integer, Character> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auditorium);
        seatGrid = findViewById(R.id.seat_grid);

        // Receive ticket count and selected play from the previous activity
        Intent intent = getIntent();
        selectedPlay = intent.getStringExtra("play");
        selectedTime = intent.getStringExtra("time");
        ticketCount = intent.getIntExtra("ticketCount", 1);

        textselectedPlay=(TextView) findViewById(R.id.textselectedPlay);
        textTime=(TextView) findViewById(R.id.textTime);
        textSeat=(TextView) findViewById(R.id.textSeat);
        textPrice=(TextView) findViewById(R.id.textPrice);

        textselectedPlay.setText(selectedPlay);
        String datetime=DateTimeFormatter(selectedTime);
        textTime.setText(" ‧ "+"5/7/2024" + " " + getHoursAndMinutes(selectedTime));
        toggleLinearLayout=findViewById(R.id.TicketLayout);

        // Initialize seats
        initializeSeats();

        starting=(TextView) findViewById(R.id.textView);
        auditTitle=(TextView) findViewById(R.id.auditTitle);
        stage=(TextView) findViewById(R.id.textView3);
        textAv=(TextView) findViewById(R.id.textAv);
        textBooked=(TextView) findViewById(R.id.textBooked);
        textSel=(TextView) findViewById(R.id.textSel);

        if(selectedPlay.equals("Romeo and Juliet")){
            auditTitle.setText("Auditorium 1");
        }else{
            auditTitle.setText("Auditorium 2");
        }


        cont=(TextView) findViewById(R.id.buttonPurchase);
        canc=(TextView) findViewById(R.id.buttonCancel);
        accessibility=(ImageButton) findViewById(R.id.imageAc);

        //change font size
        float savedFontSize = FontSizeUtil.loadFontSize(this);
        starting.setTextSize(savedFontSize);
        auditTitle.setTextSize(savedFontSize);
        stage.setTextSize(savedFontSize);
        textAv.setTextSize(savedFontSize);
        textBooked.setTextSize(savedFontSize);
        textSel.setTextSize(savedFontSize);
        cont.setTextSize(savedFontSize);
        canc.setTextSize(savedFontSize);

        accessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Auditorium.this,AccessibilityMenu.class);
                startActivity(i);
            }
        });

        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedSeats.isEmpty()){
                    Intent i=new Intent(getApplicationContext(),SeatErrorPop.class);
                    startActivity(i);
                } else if (selectedSeats.size()<ticketCount) {
                    int difference=ticketCount-selectedSeats.size();
                    Toast.makeText(Auditorium.this, "You must select " + difference + " seats more as you previously stated.", Toast.LENGTH_SHORT).show();
                } else{
                    returnSeatSelectionToChatbot();
                }

            }
        });

        this.map = new HashMap<>();
        char value = 'a';
        for (int key = 0; key <= 7; key++) {
            this.map.put(key, value);
            value++;
        }

    }


    private void initializeSeats() {
        Set<String> seatsSelectedByOthers = SharedPreferencesHelper.loadUnavailableSeats(this, selectedPlay, selectedTime);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                final ImageView seat = new ImageView(this);
                final String seatLabel = "Row " + row + " Seat " + col;

                if (seatsSelectedByOthers.contains(seatLabel)) {
                    seat.setImageResource(R.drawable.ic_seat_selected);
                } else {
                    seat.setImageResource(R.drawable.ic_seat);
                }

                seat.setPadding(8, 8, 8, 8);
                seat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (seatsSelectedByOthers.contains(seatLabel)) {
                            Toast.makeText(Auditorium.this, "This seat is selected by someone else.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (selectedSeats.contains(seatLabel)) {
                            int index = rows.indexOf(convertRowToLetter(seatLabel.charAt(4)));
                            rows.remove(convertRowToLetter(seatLabel.charAt(4)));
                            seats.remove(String.valueOf(seatLabel.charAt(11)));
                            ticketTypes.remove(index);                                      //TODO CHECK IF THIS IS PERFORMED CORRECTLY
                            selectedSeats.remove(seatLabel);
                            toggleLinearLayout.setVisibility(View.GONE);
                            seat.setImageResource(R.drawable.ic_seat);
                        } else {
                            if (selectedSeats.size() < ticketCount) {
                                rows.add(convertRowToLetter(seatLabel.charAt(4)));
                                seats.add(String.valueOf(seatLabel.charAt(11)));
                                ticketTypes.add("regular");                                 //TODO CHANGE HERE WITH TICKET TYPE SELECTION
                                selectedSeats.add(seatLabel);
                                toggleLinearLayout.setVisibility(View.VISIBLE);
                                textSeat.setText(rows.get(rows.size()-1).toUpperCase()+seats.get(seats.size()-1));
                                seat.setImageResource(R.drawable.seat_selected);
                            } else {
                                Toast.makeText(Auditorium.this, "You can only select " + ticketCount + " seats.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col);
                seatGrid.addView(seat, params);
            }
        }
    }

    private String DateTimeFormatter(String time){
        // Define the pattern to match the datetime string
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

        // Parse the string to a ZonedDateTime object
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(time, inputFormatter);

        // Define the Greek format pattern
        DateTimeFormatter greekFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Format the ZonedDateTime to the Greek format
        String formattedDate = zonedDateTime.format(greekFormatter);
        return formattedDate;
    }

    private String convertRowToLetter(char character) {
        int number = Character.getNumericValue(character);
        if (map.containsKey(number)) {
            return String.valueOf(map.get(number));
        } else {
            throw new IllegalArgumentException("Number must be between 0 and 7");
        }
    }

    public static String getHoursAndMinutes(String isoTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return zonedDateTime.format(formatter);
    }
    private void returnSeatSelectionToChatbot() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra("seats", seats);
        intent.putStringArrayListExtra("rows", rows);
        intent.putStringArrayListExtra("ticket_types", ticketTypes);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");

        //change font size
        float savedFontSize = FontSizeUtil.loadFontSize(this);
        starting.setTextSize(savedFontSize);
        auditTitle.setTextSize(savedFontSize);
        stage.setTextSize(savedFontSize);
        textAv.setTextSize(savedFontSize);
        textBooked.setTextSize(savedFontSize);
        textSel.setTextSize(savedFontSize);
        cont.setTextSize(savedFontSize);
        canc.setTextSize(savedFontSize);

    }

}

