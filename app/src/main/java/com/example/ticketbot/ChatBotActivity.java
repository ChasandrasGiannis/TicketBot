package com.example.ticketbot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ChatBotActivity extends BaseActivity implements GetReplyTask.AsyncResponse, BotInterpreter.BotInterpreterCallback {

    private static final int REQUEST_CODE_BUY_TICKETS = 1;
    private static final int REQUEST_CODE_SELECT_TICKETS = 2;
    private static final int REQUEST_CODE_SELECT_SEATS = 3;

    private static final int REQUEST_CODE_PAY = 4;

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private EditText editTextMessage;
    private Button buttonSend;
    private AppCompatImageButton buttonBack;
    private BotInterpreter botInterpreter;

    private LinearLayout linearLayoutBook;
    private LinearLayout linearLayoutCancel;
    private LinearLayout linearLayoutInfo;

    private TextView buttonBookText,buttonCancelText,buttonDetailsText,textViewLeftMessage,textViewRightMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        recyclerView = findViewById(R.id.recyclerView);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        buttonBack = findViewById(R.id.buttonBack);
        linearLayoutBook = findViewById(R.id.linearBook);
        linearLayoutCancel = findViewById(R.id.linearCancel);
        linearLayoutInfo = findViewById(R.id.linearInfo);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        String starterText = "Hello there! I am the TicketBot.You can choose the service you would like me to help you with or type in your desired one.";
        Message starterMessage = new Message(starterText, false);
        messageList.add(starterMessage);
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);

        botInterpreter = new BotInterpreter(this, this);

        //messages from FAQ
        Intent intent = getIntent();
        String messageFaqDisc = intent.getStringExtra("messageFaqDisc" );
        String messageFaqLoc = intent.getStringExtra("messageFaqLoc" );
        String messageFaqChange = intent.getStringExtra("messageFaqChange");
        String messageFaqCancel = intent.getStringExtra("messageFaqCancel");
        if(messageFaqDisc!=null){
            sendMessage(messageFaqDisc);
            linearLayoutBook.setVisibility(View.GONE);
            linearLayoutCancel.setVisibility(View.GONE);
            linearLayoutInfo.setVisibility(View.GONE);
        } else if (messageFaqLoc!=null) {
            sendMessage(messageFaqLoc);
            linearLayoutBook.setVisibility(View.GONE);
            linearLayoutCancel.setVisibility(View.GONE);
            linearLayoutInfo.setVisibility(View.GONE);
        } else if (messageFaqChange!=null) {
            sendMessage(messageFaqChange);
            linearLayoutBook.setVisibility(View.GONE);
            linearLayoutCancel.setVisibility(View.GONE);
            linearLayoutInfo.setVisibility(View.GONE);
        }else if (messageFaqCancel!=null) {
            sendMessage(messageFaqCancel);
            linearLayoutBook.setVisibility(View.GONE);
            linearLayoutCancel.setVisibility(View.GONE);
            linearLayoutInfo.setVisibility(View.GONE);
        }

        buttonBookText=(TextView) findViewById(R.id.buttonBookText);
        buttonCancelText=(TextView) findViewById(R.id.buttonCancelText);
        buttonDetailsText=(TextView) findViewById(R.id.buttonDetailsText);
        textViewLeftMessage=(TextView) findViewById(R.id.textViewLeftMessage);
        textViewRightMessage=(TextView) findViewById(R.id.textViewRightMessage);

        //change font size
        float savedFontSize = FontSizeUtil.loadFontSize(this);
        buttonBookText.setTextSize(savedFontSize);
        buttonCancelText.setTextSize(savedFontSize);
        buttonDetailsText.setTextSize(savedFontSize);
        if(textViewRightMessage!=null){
            textViewRightMessage.setTextSize(savedFontSize);
        }
        if(textViewLeftMessage!=null){
            textViewLeftMessage.setTextSize(savedFontSize);
        }

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = editTextMessage.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    sendMessage(messageText);
                }
                linearLayoutBook.setVisibility(View.GONE);
                linearLayoutCancel.setVisibility(View.GONE);
                linearLayoutInfo.setVisibility(View.GONE);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        linearLayoutBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutBook.setVisibility(View.GONE);
                linearLayoutCancel.setVisibility(View.GONE);
                linearLayoutInfo.setVisibility(View.GONE);

                String messageBook = "Hello! I would like to make a reservation";

                sendMessage(messageBook);
            }
        });
        linearLayoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutBook.setVisibility(View.GONE);
                linearLayoutCancel.setVisibility(View.GONE);
                linearLayoutInfo.setVisibility(View.GONE);

                String messageCancel = "Hello, I would like to cancel a ticket";

                sendMessage(messageCancel);
            }
        });
        linearLayoutInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutBook.setVisibility(View.GONE);
                linearLayoutCancel.setVisibility(View.GONE);
                linearLayoutInfo.setVisibility(View.GONE);

                String messageInfo = "Hello! I would like to through some of my booking details";

                sendMessage(messageInfo);
            }
        });
    }

    public void sendMessage(String messageText) {
        // send message
        Message sentMessage = new Message(messageText, true);
        messageList.add(sentMessage);
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
        editTextMessage.setText("");

        // execute GetReplyTask to get the response from the bot
        new GetReplyTask(ChatBotActivity.this).execute(messageText);

        Message typingMessage = new Message("typing...", false);
        messageList.add(typingMessage);
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }

    @Override
    public void processFinish(String output) {
        // get reply
        if (output != null) {

            int lastIndex = messageList.size() - 1;
            messageList.remove(lastIndex);
            messageAdapter.notifyItemRemoved(lastIndex);
            if (lastIndex > 0) {
                recyclerView.scrollToPosition(lastIndex - 1);
            } else {
                recyclerView.scrollToPosition(0);
            }

            String botReply = botInterpreter.getAnswer(output);
            Message receivedMessage = new Message(botReply, false);
            messageList.add(receivedMessage);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
        } else {
            Log.e("ChatBotActivity", "Failed to get a reply from the bot.");
        }
    }

    public void launchPersonalInfoActivity(String play, String time) {
        Intent intent = new Intent(ChatBotActivity.this, PersonalInfoActivity.class);
        intent.putExtra("play", play);
        intent.putExtra("time", time);
        startActivityForResult(intent, REQUEST_CODE_BUY_TICKETS);
    }

    @Override
    public void launchSelectTicketsActivity() {
        Intent intent = new Intent(ChatBotActivity.this, SelectTicketsActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SELECT_TICKETS);
    }

    @Override
    public void launchAuditoriumActivity(String play, String time, int ticketNo) {
        Intent intent = new Intent(ChatBotActivity.this, Auditorium.class);
        intent.putExtra("play", play);
        intent.putExtra("time", time);
        intent.putExtra("ticketCount", ticketNo);
        startActivityForResult(intent, REQUEST_CODE_SELECT_SEATS);
    }
    @Override
    public void launchPaymentActivity(ArrayList<String> ticketTypes) {
        Intent intent = new Intent(ChatBotActivity.this, PaymentActivity.class);
        intent.putStringArrayListExtra("ticket_types", ticketTypes);
        startActivityForResult(intent, REQUEST_CODE_PAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BUY_TICKETS && resultCode == RESULT_OK && data != null) {
            String firstName = data.getStringExtra("firstName");
            String lastName = data.getStringExtra("lastName");
            String email = data.getStringExtra("email");
            String phoneNumber = data.getStringExtra("phoneNumber");

            botInterpreter.setName(firstName + " " + lastName);
            botInterpreter.setEmail(email);
            botInterpreter.setPhone(phoneNumber);
            String personalInfoMessage = String.format("Personal Info: \nFirst Name: %s \nLast Name: %s \nEmail: %s \nPhone Number: %s",
                    firstName, lastName, email, phoneNumber);

            new GetReplyTask(ChatBotActivity.this).execute(personalInfoMessage);

            Message typingMessage = new Message("typing...", false);
            messageList.add(typingMessage);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
        }

        if (requestCode == REQUEST_CODE_SELECT_TICKETS && resultCode == RESULT_OK && data != null) {
            ArrayList<Ticket> selectedTickets = data.getParcelableArrayListExtra("selected_tickets");

            // following this unconventional implementation due to wit.ai limitations in recognising long complicated patterns
            String botReply;
            if (!selectedTickets.isEmpty()) {
                botInterpreter.setTickets(selectedTickets);
                botReply = botInterpreter.handleConversationState("uniqueUserId", "cancellation_tickets_provided", null, null, null, null, 0, null, null);
            } else {
                botReply = botInterpreter.handleConversationState("uniqueUserId", "no_cancellation_tickets", null, null, null, null, 0, null, null);
            }

            Message receivedMessage = new Message(botReply, false);
            messageList.add(receivedMessage);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
        }

        if (requestCode == REQUEST_CODE_SELECT_SEATS && resultCode == RESULT_OK && data != null) {
            ArrayList<String> rows = data.getStringArrayListExtra("rows");
            ArrayList<String> seats = data.getStringArrayListExtra("seats");
            ArrayList<String> ticketTypes = data.getStringArrayListExtra("ticket_types");


            String seatSelectionMessage = "";
            for (int i = 0; i < rows.size(); i++) {
                Log.e("row:", rows.get(i));
                Log.e("seat:", seats.get(i));
                seatSelectionMessage += String.format("row: %s seat: %s ", rows.get(i), seats.get(i));
            }

            botInterpreter.setTicketTypes(ticketTypes); //wit.ai limitations

            new GetReplyTask(ChatBotActivity.this).execute(seatSelectionMessage);

            Message typingMessage = new Message("typing...", false);
            messageList.add(typingMessage);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
        }

        if (requestCode == REQUEST_CODE_PAY && resultCode == RESULT_OK ) {
            String botReply = botInterpreter.handleConversationState("uniqueUserId", "payment_received", null, null, null, null, 0, null, null);
            Message receivedMessage = new Message(botReply, false);
            messageList.add(receivedMessage);
            messageAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerView.scrollToPosition(messageList.size() - 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked");

        float savedFontSize = FontSizeUtil.loadFontSize(this);
        buttonBookText.setTextSize(savedFontSize);
        buttonCancelText.setTextSize(savedFontSize);
        buttonDetailsText.setTextSize(savedFontSize);
        if(textViewRightMessage!=null){
            textViewRightMessage.setTextSize(savedFontSize);
        }
        if(textViewLeftMessage!=null){
            textViewLeftMessage.setTextSize(savedFontSize);
        }

    }
}
