package com.example.ticketbot;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BotInterpreter {
    Context context;
    private Map<String, List<String>> intentResponses;
    private Map<String, String> responseInfo;
    private SecureRandom randomizer = new SecureRandom();
    private Map<String, UserSession> userSessions = new HashMap<>();
    private BotInterpreterCallback callback;
    private Play romeoAndJuliet;
    private Play hamlet;
    private ArrayList<Ticket> tickets = null;
    private ArrayList<String> ticketTypes = null;
    private String name;
    private String email = null;
    private String phone = null;

    private Map<Character, Integer> letterMap;


    public interface BotInterpreterCallback {
        void launchPersonalInfoActivity(String name, String time);
        void launchSelectTicketsActivity();
        void launchAuditoriumActivity(String play, String time, int ticketNo);
        void launchPaymentActivity(ArrayList<String> ticketTypes);
    }

    public BotInterpreter(BotInterpreterCallback callback, Context context) {
        this.callback = callback;
        this.context = context;
        intentResponses = new HashMap<>();
        initializeResponses();
        initializePlays();
        initializeResponseInfo();

        letterMap = new HashMap<>();
        char key = 'A';
        for (int value = 0; value <= 7; value++) {
            letterMap.put(key, value);
            key++;
        }
    }

    public BotInterpreter() {
        intentResponses = new HashMap<>();
        initializePlays();
        initializeResponses();
        initializeResponseInfo();
    }

    private void initializeResponses() {
        intentResponses.put("book_ticket", Arrays.asList(
                "Which performance are you interested in, and what time suits you?",
                "Which show would you like to watch, and when?",
                "What play would you prefer to see, and at what time?",
                "Which theatrical performance do you want to attend, and at what time?",
                "Which play are you interested in, and when would you like to see it?",
                "Which drama would you like to see, and what time works for you?",
                "Which stage production would you like to attend, and at what time?",
                "Which show would you like tickets for, and at what time?",
                "Which performance do you want to see, and at what time?",
                "Which play are you looking to watch, and at what time?"
        ));
        intentResponses.put("cancel_ticket", Arrays.asList(
                "Certainly! I can help you with canceling your ticket. First, I will help you choose which tickets you want to cancel. Shall we begin?",
                "Of course! I can assist you in canceling your ticket. To start, I'll help you select the tickets you wish to cancel. Shall we start?",
                "Absolutely! I can help with your ticket cancellation. First, let's choose which tickets you want to cancel. Ready to proceed?",
                "Sure! I can assist you with canceling your ticket. First, I'll guide you in selecting the tickets to cancel. Shall we get started?",
                "Definitely! I'm here to help you cancel your ticket. First, we'll decide which tickets to cancel. Shall we begin?",
                "Sure! I can help you cancel your ticket. First, let's choose the tickets you wish to cancel. Shall we start the process?",
                "Certainly! Let's get started on canceling your ticket. First, we'll select which tickets to cancel. Ready?",
                "Of course! I'll help you with canceling your ticket. First, let's choose the tickets you want to cancel. Shall we begin?",
                "Absolutely! I'm here to help you with canceling your ticket. First, we need to choose which tickets to cancel. Shall we proceed?",
                "Sure! Let's begin the process of canceling your ticket. First, I'll help you decide which tickets to cancel. Shall we?"
        ));
        intentResponses.put("edit_ticket", Arrays.asList(
                "Of course! I can assist you with changing your ticket. What would you like to change?",
                "Certainly! I can help you with changing your ticket. What would you like to modify?",
                "Absolutely! I'm here to assist you with changing your ticket. What do you want to change?",
                "Sure! I can help you with changing your ticket. What would you like to edit?",
                "Definitely! I'm ready to assist you with changing your ticket. What would you like to change?",
                "Yes, I can assist you with changing your ticket. What would you like to modify?",
                "Certainly! I can help you change your ticket. What would you like to change?",
                "Of course! I can help you edit your ticket. What do you want to change?",
                "Absolutely! I can assist you with modifying your ticket. What would you like to change?",
                "Sure! I'm here to help you change your ticket. What would you like to edit?"
        ));
        intentResponses.put("call_a_representative", Arrays.asList(
                "Please contact our theater's customer service representative with the following details: ",
                "You can get in touch with our theater's customer service representative at the following information: ",
                "Contact our theater's customer service representative at these details: ",
                "Reach out to our theater's customer service representative using the following contact information: ",
                "Here are the contact details to reach our theater's customer service representative: ",
                "Use the following contact details to reach our theater's customer service representative: ",
                "To reach our theater's representative, use the contact details below: ",
                "Our theater's representative can be reached at these contact details: ",
                "You can get in touch with our theater's representative using the following information: ",
                "Reach out to our theater's representative at these details: "
        ));
        intentResponses.put("get_auditorium", Arrays.asList(
                "You can watch ",
                "You can see ",
                "You can view ",
                "You can enjoy ",
                "You can catch ",
                "You can attend ",
                "You can experience "
        ));
        intentResponses.put("get_contact_info", Arrays.asList(
                "You can contact the theater through the following channels:",
                "The theater can be reached through the following means:",
                "You have the option to contact the theater through the following methods:",
                "The theater can be contacted through the following:",
                "For communication with the theater, consider the following options:",
                "To get in touch with the theater, utilize the following means:",
                "Contacting the theater is possible through the following:",
                "If you need to reach the theater, these options are available:",
                "For contacting the theater, here are the available options:",
                "To reach the theater, use the following means:"
        ));
        intentResponses.put("get_discounts", Arrays.asList(
                "Yes, we do offer discounts! Here are the discounts available at our theater:",
                "Certainly, we provide discounts! Here's a list of the available discounts at our theater:",
                "Indeed, discounts are available! Here's what we offer at our theater:",
                "Absolutely, we have discounts! Check out the available discounts at our theater:",
                "Of course, we offer discounts! Here's a rundown of what's available at our theater:",
                "Definitely, discounts are part of our offerings! Here's what you can get at our theater:",
                "Yes, discounts are available! Here's the list of discounts at our theater:",
                "Sure, we provide discounts! Take a look at what we offer at our theater:",
                "Yes, we have discounts! Here's what's available at our theater:",
                "Certainly, discounts are available! Here's the rundown of discounts at our theater:"
        ));
        intentResponses.put("get_opening_hours", Arrays.asList("Our theater's opening hours vary " +
                "depending on the schedule of performances. Generally, we are open during the following times:\n" +
                "\n" +
                "    Monday to Friday: 10:00 AM to 6:00 PM\n" +
                "    Weekends: Closed\n" +
                "\n" +
                "However, please note that these hours might change during holidays or special events. " +
                "It's always a good idea to check our website or contact our customer service for the most up-to-date information. " +
                "If there's anything else you'd like to know, feel free to ask!"));
        intentResponses.put("get_play_time", Arrays.asList(
                "Performance available at ",
                "Show scheduled at ",
                "Performance taking place at ",
                "Show available at ",
                "Performance at ",
                "Show occurring at:",
                "Performance happening at ",
                "Show held at ",
                "Performance set for ",
                "Show at "
        ));
        intentResponses.put("get_plays", Arrays.asList(
                "We have two plays showing at the moment:",
                "Currently, there are two plays being performed:",
                "Right now, we have two plays on stage:",
                "At present, two plays are being performed:",
                "There are two plays currently running:",
                "We have two plays available for viewing:",
                "Two plays are being performed at the moment:",
                "We are showing two plays right now:",
                "Two plays are on stage currently:",
                "There are two plays being staged at present:"
        ));
        intentResponses.put("get_theater_location", Arrays.asList(
                "Our theater is located at: ",
                "You can find our theater at: ",
                "The location of our theater is at: ",
                "Our theater is situated at: ",
                "You'll find our theater at the following address: ",
                "Our theater's location is: ",
                "Here is our theater's location: ",
                "Our theater can be found at: ",
                "You can locate our theater at the following address: ",
                "Here is where you can find our theater: "
        ));
        intentResponses.put("get_ticket_price", Arrays.asList("The price for one "));
        intentResponses.put("get_duration", Arrays.asList(
                "The performance of ",
                "The show of ",
                "The production of ",
                "The staging of ",
                "The act of "
        ));
        intentResponses.put("get_description", Arrays.asList(""));
        intentResponses.put("get_cast", Arrays.asList(
                "Here is the cast for our current production of ",
                "Presenting the cast for our current production of ",
                "Here are the actors in our current production of ",
                "Introducing the cast for our current production of ",
                "The following is the cast for our current production of ",
                "Check out the cast for our current production of ",
                "Here is the lineup for our current production of ",
                "Listed below is the cast for our current production of "
        ));
        intentResponses.put("seat_confirmation_provided", Arrays.asList(
                "Could you please provide your personal information?",
                "May I have your personal information, please?",
                "Can you please provide your personal information?",
                "Could you share your personal information with us?"
        ));
        intentResponses.put("play_and_time_provided", Arrays.asList(
                "And how many tickets would you like?",
                "How many tickets would you like to purchase?",
                "How many tickets do you need?",
                "Could you please specify how many tickets you want?",
                "How many tickets are you interested in?",
                "Please let me know how many tickets you would like.",
                "How many tickets should I reserve for you?",
                "Can you tell me the number of tickets you'd like?",
                "What is the number of tickets you wish to buy?",
                "How many tickets would you like to have?"
        ));
        intentResponses.put("personal_info_provided", Arrays.asList(
                "Thank you for providing your details. Here is a summary of your booking:",
                "Thank you for sharing your information. Here is a summary of your booking:",
                "Thank you for your details. Here is the summary of your booking:",
                "Thanks for providing your details. Here is a summary of your booking:",
                "Thank you for the information. Here is your booking summary:",
                "Thank you for your details. Here is an overview of your booking:",
                "Thanks for sharing your details. Here is a summary of your booking:",
                "Thank you for providing your details. Here is an overview of your booking:",
                "Thank you for your information. Here is a summary of your booking:",
                "Thanks for the details. Here is a summary of your booking:"
        ));
        intentResponses.put("cancellation_tickets_provided", Arrays.asList(
                "Here are the tickets you selected:",
                "These are the tickets you selected:",
                "The following tickets have been selected by you:",
                "You have selected the following tickets:",
                "These tickets are the ones you chose:",
                "Here is the list of tickets you selected:",
                "Below are the tickets you have selected:",
                "The tickets you chose are as follows:",
                "Here are the tickets you picked:",
                "You selected these tickets:"
        ));
        intentResponses.put("cancel_procedure", Arrays.asList(
                "No problem at all! If there's anything else you need assistance with or any other questions you have, feel free to let me know.",
                "Certainly! If you need help with anything else or have any other questions, just let me know.",
                "Absolutely! If there's anything else I can assist you with or if you have more questions, don't hesitate to ask.",
                "Of course! If you have any other questions or need further assistance, please let me know.",
                "No worries! If you need any additional help or have more questions, feel free to reach out.",
                "Gladly! If there's anything else you need or any other questions you have, just let me know.",
                "Sure thing! If you need further assistance or have other questions, don't hesitate to let me know.",
                "Certainly! If there's anything more you need help with or any additional questions, please let me know.",
                "Absolutely! If you require further assistance or have other questions, feel free to ask.",
                "No issue at all! If there's anything else you need or any other inquiries, feel free to let me know."
        ));
        intentResponses.put("no_cancellation_tickets", Arrays.asList(
                "It seems like you didn't choose any tickets. If you're unsure which ticket you want to cancel or if you need more time to decide, that's perfectly fine." +
                        " If you have any questions or need assistance with anything else, feel free to let me know! I'm here to help.",
                "It appears that you didn't select any tickets. If you're uncertain which ticket to cancel or need more time, that's completely fine." +
                        " If you have any questions or need further assistance, please let me know! I'm here to help.",
                "You didn't choose any tickets. If you're unsure about which ticket to cancel or need more time to decide, that's okay. " +
                        "If you have any questions or require assistance with anything else, feel free to let me know! I'm here to help.",
                "No tickets were selected. If you're not sure which ticket to cancel or need more time to decide, that's perfectly okay. " +
                        "If you have any questions or need help with anything else, please let me know! I'm here to assist.",
                "It looks like no tickets were chosen. If you're unsure which ticket to cancel or need additional time, that's absolutely fine." +
                        "If you have any questions or need assistance with anything else, feel free to let me know! I'm here to help.",
                "It seems no tickets were selected. If you need more time to decide or are unsure which ticket to cancel, that's perfectly fine." +
                        " If you have any questions or need assistance with anything else, please let me know! I'm here to help.",
                "You didn't select any tickets. If you're unsure which ticket to cancel or need more time to decide, that's okay." +
                        " If you have any questions or need help with anything else, feel free to let me know! I'm here to assist.",
                "No tickets have been chosen. If you're uncertain about which ticket to cancel or need more time to decide, that's perfectly fine." +
                        " If you have any questions or need help with anything else, please let me know! I'm here to help.",
                "It appears no tickets were chosen. If you're unsure which ticket to cancel or need more time, that's completely fine." +
                        "If you have any questions or need further assistance, please let me know! I'm here to help.",
                "It seems no tickets were selected. If you need more time to decide or are unsure which ticket to cancel, that's perfectly fine." +
                        " If you have any questions or need assistance with anything else, please let me know! I'm here to help."
        ));

        intentResponses.put("view_booking_details", Arrays.asList(
                "If you want to go through your booking details, you can access them directly via MyTickets located on the top left corner of the home screen." +
                        " This section allows you to view all your booking information conveniently.",
                "To review your booking details, simply access MyTickets located on the top left corner of the home screen." +
                        " This section provides convenient access to all your booking information.",
                "You can view your booking details by accessing MyTickets, found on the top left corner of the home screen." +
                        " This section lets you conveniently see all your booking information.",
                "For easy access to your booking details, go to MyTickets on the top left corner of the home screen. Here, you can view all your booking information.",
                "If you need to check your booking details, you can find them in MyTickets on the top left corner of the home screen." +
                        " This section provides a convenient overview of all your booking information.",
                "To see your booking details, access MyTickets located at the top left corner of the home screen. " +
                        "This section allows you to conveniently view all your booking information.",
                "You can review your booking details by accessing MyTickets on the top left corner of the home screen." +
                        " This section allows you to conveniently view all your booking information.",
                "For access to your booking details, go to MyTickets on the top left corner of the home screen. This section provides an easy way to view all your booking information.",
                "If you wish to view your booking details, you can access them via MyTickets on the top left corner of the home screen." +
                        " This section allows you to conveniently see all your booking information.",
                "To check your booking details, go to MyTickets located on the top left corner of the home screen." +
                        " This section offers a convenient way to view all your booking information."
        ));
        intentResponses.put("ticket_number_provided", Arrays.asList(""));
        intentResponses.put("valid_editing", Arrays.asList(
                "First, I will help you choose which tickets you want to change. Shall we begin?",
                "Initially, I'll assist you in selecting the tickets you want to change. Shall we start?",
                "To start, I will help you choose which tickets you want to modify. Ready to begin?",
                "First, let's choose the tickets you want to change. Shall we get started?",
                "Initially, I'll help you select the tickets you want to edit. Shall we proceed?",
                "To begin, I will guide you in choosing the tickets you want to change. Shall we start?",
                "First, I'll assist you in choosing which tickets you want to edit. Shall we get started?",
                "Initially, I'll help you decide which tickets you want to modify. Shall we begin?",
                "First, let's decide on the tickets you want to change. Ready to proceed?",
                "To start, I will assist you in selecting the tickets you want to change. Shall we begin?"
        ));
        intentResponses.put("change_play", Arrays.asList(
                "You will need to first cancel your current tickets and then make a new reservation for the desired play. " +
                        "Would you like to proceed with canceling your tickets?",
                "First, you'll need to cancel your current tickets and then book the desired play." +
                        " Would you like to proceed with canceling your tickets?",
                "You must cancel your existing tickets before making a new reservation for the play you want. " +
                        "Would you like to proceed with canceling your tickets?",
                "To change the play, you'll need to cancel your current tickets and then reserve the new play." +
                        " Would you like to proceed with canceling your tickets?",
                "You'll need to cancel your current tickets first, then make a new booking for the desired play. " +
                        "Would you like to proceed with canceling your tickets?",
                "First, you must cancel your current tickets and then make a new reservation for the play you want." +
                        " Would you like to proceed with canceling your tickets?",
                "You have to cancel your existing tickets before booking the desired play." +
                        " Would you like to proceed with canceling your tickets?",
                "To change the play, you need to cancel your current tickets and make a new reservation." +
                        " Would you like to proceed with canceling your tickets?",
                "You will need to cancel your current tickets first and then reserve the play you prefer." +
                        " Would you like to proceed with canceling your tickets?",
                "To book a different play, you must first cancel your current tickets. " +
                        "Would you like to proceed with canceling your tickets?"
        ));
        intentResponses.put("switch_to_cancellation", Arrays.asList(
                "First, I will help you choose which tickets you want to cancel. Shall we begin?",
                "To start, I will assist you in selecting the tickets you want to cancel. Shall we begin?",
                "Initially, I will help you choose which tickets to cancel. Ready to begin?",
                "First, let's decide which tickets you want to cancel. Shall we start?",
                "To begin, I will help you select the tickets you wish to cancel. Shall we proceed?",
                "First, I will guide you in choosing the tickets you want to cancel. Shall we start?",
                "Initially, let's choose which tickets you want to cancel. Shall we get started?",
                "First, I'll assist you in selecting the tickets you wish to cancel. Ready to begin?",
                "To start, I'll help you decide which tickets to cancel. Shall we begin?",
                "First, let's pick the tickets you want to cancel. Shall we proceed?"
        ));
        intentResponses.put("invalid_editing", Arrays.asList(
                "I am afraid there are no available seats to perform this change." +
                        " If you have any questions or need assistance with anything else, please let me know! I'm here to help.",
                "Unfortunately, there are no available seats for this change." +
                        " If you have any questions or need assistance with anything else, please let me know! I'm here to help.",
                "I'm sorry, but there are no available seats to make this change." +
                        " If you have any questions or need help with anything else, please let me know! I'm here to assist.",
                "I apologize, but there are no available seats to perform this change. " +
                        "If you have any questions or need assistance with anything else, please let me know! I'm here to help.",
                "Unfortunately, no seats are available to make this change." +
                        " If you have any questions or need further assistance, please let me know! I'm here to help.",
                "I'm afraid there are no seats available for this change. " +
                        "If you need assistance with anything else or have any questions, please let me know! I'm here to help.",
                "I apologize, but we have no available seats to make this change." +
                        " If you need assistance with anything else or have any questions, please let me know! I'm here to help.",
                "Unfortunately, we don't have any available seats for this change. " +
                        "If you have any questions or need further assistance, please let me know! I'm here to help."
        ));
        intentResponses.put("nice", Arrays.asList(
                "Nice!", "Great!", "Awesome!", "Cool!", "Fantastic!", "Wonderful!", "Excellent!"
        ));
        intentResponses.put("seat_selection_provided", Arrays.asList("You have chosen the following seats:"));
        intentResponses.put("reservation_confirmation", Arrays.asList(
                "Thank you for your patience! Your tickets have been successfully reserved." +
                        " You can always view your booking details by accessing MyTickets, found on the top left corner of the home screen." +
                        " If there is anything else I can help you with, please let me know.",
                "Thank you for your patience! Your tickets are now successfully reserved." +
                        " To view your booking details, go to MyTickets at the top left corner of the home screen. If you need any further assistance, please let me know.",
                "Thanks for your patience! Your tickets have been successfully reserved. You can check your booking details by accessing MyTickets in the top left corner of the home screen." +
                        " Let me know if there's anything else I can assist you with.",
                "We appreciate your patience! Your tickets are successfully reserved. To view your booking details, access MyTickets at the top left corner of the home screen." +
                        " If you need any more help, please let me know.", "Thank you for waiting! Your tickets have been successfully reserved." +
                        " You can find your booking details in MyTickets, located on the top left corner of the home screen. If you need further assistance, please let me know.",
                "Thanks for your patience! Your tickets are now reserved. You can always view your booking details by clicking on MyTickets at the top left corner of the home screen." +
                        " If you need any other help, please let me know.", "We appreciate your patience! Your tickets have been successfully reserved." +
                        " To see your booking details, go to MyTickets in the top left corner of the home screen. If you need anything else, please let me know.",
                "Thank you for your patience! Your tickets are now reserved. To view your booking details, access MyTickets located at the top left corner of the home screen. " +
                        "If there's anything else you need, please let me know.",
                "Thanks for your patience! Your tickets are successfully reserved. You can view your booking details by accessing MyTickets in the top left corner of the home screen." +
                        " If you need further assistance, please let me know.",
                "We appreciate your patience! Your tickets have been successfully reserved. " +
                        "You can always check your booking details by going to MyTickets at the top left corner of the home screen. If you need any additional help, please let me know."
        ));
        intentResponses.put("sold-out", Arrays.asList(
                "I am sorry but this performance is sold out. If you have any questions or need assistance with anything else, please let me know! I'm here to help.",
                "Unfortunately, this performance is sold out. If you have any questions or need help with anything else, please let me know! I'm here to assist.",
                "I'm sorry, but this performance is sold out. If you need any assistance or have other questions, please let me know! I'm here to help.",
                "Regrettably, this performance is sold out. If you have any questions or need help with anything else, please let me know! I'm here to help.",
                "I apologize, but this performance is sold out. If you have any questions or need assistance with anything else, please let me know! I'm here to assist.",
                "Unfortunately, this performance has sold out. If you have any questions or need further assistance, please let me know! I'm here to help.",
                "I'm sorry, but there are no tickets left for this performance. If you need assistance with anything else or have any questions, please let me know! I'm here to help.",
                "Regrettably, this performance is fully booked. If you have any questions or need assistance with anything else, please let me know! I'm here to help.",
                "I apologize, but all tickets for this performance are sold out. If you have any questions or need further assistance, please let me know! I'm here to help.",
                "Unfortunately, this performance is fully booked. If you need any other assistance or have questions, please let me know! I'm here to assist."
        ));
        intentResponses.put("gratitude", Arrays.asList(
                "You're welcome! If you have any other questions or need further assistance, feel free to ask. Enjoy your day!",
                "You're welcome! If you have any other inquiries or need more help, feel free to ask. Enjoy your day!",
                "You're welcome! If you need any other assistance or have additional questions, feel free to ask. Enjoy your day!",
                "You're welcome! Should you have any more questions or need further assistance, feel free to ask. Enjoy your day!",
                "You're welcome! If you have any other questions or require further help, feel free to ask. Enjoy your day!",
                "You're welcome! If there's anything else you need or any other questions you have, feel free to ask. Enjoy your day!",
                "You're welcome! If you need any further assistance or have any other questions, feel free to ask. Enjoy your day!",
                "You're welcome! If there's anything else I can help you with or if you have more questions, feel free to ask. Enjoy your day!",
                "You're welcome! Should you need any other assistance or have more questions, feel free to ask. Enjoy your day!",
                "You're welcome! If you have any additional questions or need further help, feel free to ask. Enjoy your day!"
        ));
    }

    private void initializeResponseInfo() {
        responseInfo = new HashMap<>();
        responseInfo.put("book_ticket", "");
        responseInfo.put("call_a_representative", "\nPhone Number +30 211 7700 000\nEmail: support@theater.com");
        responseInfo.put("cancel_ticket", "");
        responseInfo.put("edit_ticket", "");
        responseInfo.put("get_auditorium", "");
        responseInfo.put("get_contact_info", "\nPhone Number: +30 211 7801 000\nEmail: info@theater.com");
        responseInfo.put("get_discounts",
                "\nElderly Discount: Available for patrons aged 65 and above.\n" +
                        "Student Discount: Available for students with valid student IDs.\n" +
                        "Educator Discount: Available for educators with valid ID.\n" +
                        "Family Discount: Available for families purchasing multiple tickets.\n" +
                        "Unemployed Discount: Available for individuals who are currently unemployed.");
        responseInfo.put("get_opening_hours", "");
        responseInfo.put("get_play_time", "");
        responseInfo.put("get_plays", String.format("\n%s\n%s\n\nboth at %s and at %s.",
                romeoAndJuliet.getName(), hamlet.getName(), hamlet.getTimetable()[0], hamlet.getTimetable()[1] ));
        responseInfo.put("get_theater_location", "\nBas. Sofias 85, Athens 11521");
        responseInfo.put("get_ticket_price", "");
        responseInfo.put("get_duration", "");
        responseInfo.put("get_description", "");
        responseInfo.put("get_cast", "");
        responseInfo.put("seat_selection_provided", "");
        responseInfo.put("cancellation_tickets_provided", "");
        responseInfo.put("cancel_procedure", "");
        responseInfo.put("no_cancellation_tickets", "");
        responseInfo.put("view_booking_details", " Is there anything else I can assist you with?");
        responseInfo.put("ticket_number_provided", "");
        responseInfo.put("play_and_time_provided", "");
        responseInfo.put("personal_info_provided", "");
        responseInfo.put("change_play", "");
        responseInfo.put("semi_valid_editing", "");
        responseInfo.put("seat_confirmation_provided", "");
        responseInfo.put("gratitude", "");
    }

    private void updateResponseInfo(UserSession session) {
        if (session.getPlay() != null) {
            responseInfo.replace("get_auditorium",
                    String.format("%s at our theater with performances available at %s and %s in %s.",
                            session.getPlay().getName(), session.getPlay().getTimetable()[0],
                            session.getPlay().getTimetable()[1], session.getPlay().getAuditorium()));
            responseInfo.replace("get_play_time", String.format("%s and %s in %s.",
                    session.getPlay().getTimetable()[0], session.getPlay().getTimetable()[1], session.getPlay().getAuditorium()));
            responseInfo.replace("get_duration", String.format("%s lasts about %s, including a 15-minute intermission.",
                    session.getPlay().getName(), session.getPlay().getDuration()));
            responseInfo.replace("get_description", session.getPlay().getDescription());
            responseInfo.replace("get_cast", String.format("%s:\n%s", session.getPlay().getName(), session.getPlay().getCast()));
        }
        responseInfo.replace("get_ticket_price", String.format("%s ticket is %s", session.getTicket_type(), session.getTicketPrice()));

        if (tickets != null) {

            String ticketsToBeCanceled = "";
            for (int i = 0; i < tickets.size(); i++) {
                Ticket ticket = tickets.get(i);
                ticketsToBeCanceled += String.format("\nPlay: %s Date: %s Time: %s Row: %s Seat: %s\n",
                        ticket.getTitle(), ticket.getDate(), ticket.getTime(), ticket.getRow(), ticket.getSeat());
            }
            ticketsToBeCanceled += "\n Are you sure you want to cancel them?";

            responseInfo.replace("cancellation_tickets_provided", ticketsToBeCanceled);
        }

        if (session.getTicket_number() != 0) {
            responseInfo.replace("ticket_number_provided", String.format("You have selected %s tickets. Now, please choose exactly %s seats from the seating selection screen that will open next." +
                    "\nAre you ready to proceed with seat selection?", String.valueOf(session.getTicket_number()), String.valueOf(session.getTicket_number())));
        }

        if (tickets != null) {
            String ticketsString = "\n";
            for (Ticket ticket : tickets) {
                ticketsString += ticket.getRow() + ticket.getSeat() + " ";
            }
            ticketsString += "\nIs this correct?";
            responseInfo.replace("seat_selection_provided", ticketsString);
        }

        if (session.getEmail() != null && session.getPhone() != null && this.name != null) {
            String ticketsString = "";
            for (Ticket ticket : tickets) {
                ticketsString += ticket.getRow() + ticket.getSeat() + " ";
            }
            responseInfo.replace("personal_info_provided", String.format("\n\nPlay: %s\nDate: 5/7/2024\nTime: %s\nSeats: %s\nName: %s\nEmail: %s\nPhone: %s\n\n",
                    session.getPlay().getName(), getHoursAndMinutes(session.getTime()), ticketsString, this.name, session.getEmail(), session.getPhone())
                    + "Please confirm that all the information is correct. Do you want to proceed to check-out?");
        }
    }

    public void initializePlays() {
        romeoAndJuliet = new Play(
                "Romeo and Juliet",
                "Romeo and Juliet is about two young lovers from feuding families, the Montagues and Capulets." +
                        " Despite the conflict, they secretly marry, leading to a series of tragic events and their untimely deaths." +
                        " The play explores themes of love and family conflict.",
                "Leonardo DiCaprio, Claire Danes, Samuel Thompson.",
                "Auditorium 1",
                "2 hours and 30 minutes"
        );
        hamlet = new Play(
                "Hamlet",
                "Hamlet is about Prince Hamlet of Denmark, who seeks revenge against his uncle Claudius." +
                        " Claudius has murdered Hamlet's father, taken the throne, and married Hamlet's mother. " +
                        "The play explores themes of revenge, madness, and the consequences of corruption.",
                "Benedict Cumberbatch, David Johnson, Mary Williams.",
                "Auditorium 2",
                "3 hours"
        );
        String[] hours = {"19:00", "22:00"};
        romeoAndJuliet.setTimetable(hours);
        hamlet.setTimetable(hours);
    }

    public String getAnswer(String witResponse) {
        try {
            JSONObject responseJson = new JSONObject(witResponse);
            JSONArray intents = responseJson.getJSONArray("intents");
            JSONObject entities = responseJson.getJSONObject("entities");

            if (intents.length() > 0) {
                String intent = intents.getJSONObject(0).getString("name");

                Log.e("INTENT : ", intent);

                String playName = extractEntity(entities, "play:wit_play");
                Play play = null;
                if (playName != null) {
                    play = playName.equalsIgnoreCase("Romeo and Juliet") ? romeoAndJuliet : hamlet;
                }
                String time = extractEntity(entities, "wit$datetime:datetime");
                String ticketType = extractEntity(entities, "ticket_type:ticket_type");
                ArrayList<Ticket> seatSelections = extractSeatSelections(entities);
                String email = extractEntity(entities, "wit$email:email");
                String phone = extractEntity(entities, "wit$phone_number:phone_number");
                int ticket_number = 0;
                if ("ticket_number_provided".equals(intent)) {
                    ticket_number = Integer.valueOf(extractEntity(entities, "wit$number:number"));
                }

                return handleConversationState("uniqueUserId", intent, play, time, ticketType, seatSelections, ticket_number, email, phone);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("getAnswer : ", "NO INTENT FOUND");
        return "I'm not sure how to respond to that. Maybe you would like to speak to a representative or ask me in another way.";
    }

    private String extractEntity(JSONObject entities, String entityName) {
        try {
            Log.e("extractEntity:", "Checking for entity: " + entityName);
            if (entities.has(entityName)) {
                Log.e("extractEntity:", "Found entity: " + entityName);
                JSONArray entityArray = entities.getJSONArray(entityName);
                if (entityArray.length() > 0) {
                    String value = entityArray.getJSONObject(0).getString("value");
                    Log.e("extractEntity:", entityName + " value extracted: " + value);
                    return value;
                } else {
                    Log.e("extractEntity:", entityName + " array is empty.");
                }
            } else {
                Log.e("extractEntity:", "No entity found for: " + entityName);
            }
        } catch (JSONException e) {
            Log.e("extractEntity:", "JSONException occurred: " + e.getMessage());
        }
        return null;
    }

    private ArrayList<Ticket> extractSeatSelections(JSONObject entities) {
        if (this.tickets == null) {
            this.tickets = new ArrayList<>();
        }

        try {
            if (entities.has("row:row") && entities.has("seat:seat") && entities.has("wit$number:number")) {
                JSONArray rows = entities.getJSONArray("row:row");
                JSONArray seats = entities.getJSONArray("seat:seat");
                JSONArray numbers = entities.getJSONArray("wit$number:number");

                int minLength = Math.min(rows.length(), Math.min(seats.length(), numbers.length()));
                for (int i = 0; i < minLength; i++) {
                    String rowValue = rows.getJSONObject(i).getString("value");
                    Log.e("extractEntity:",  "row value extracted: " + rowValue);
                    String seatValue = seats.getJSONObject(i).getString("value");
                    Log.e("extractEntity:",  "seat value extracted: " + seatValue);
                    int numberValue = numbers.getJSONObject(i).getInt("value");
                    Log.e("extractEntity:",  "number value extracted: " + numberValue);

                    Ticket ticket = new Ticket(String.valueOf(rowValue.charAt(5)), String.valueOf(numberValue), ticketTypes.get(i)); //TODO supposing they are parallel arrays add this to the parameter list:
                    this.tickets.add(ticket);
                }
            } else {
                Log.e("extractSeatSelections:", "Entities for row, seat, or number are missing.");
            }
        } catch (JSONException e) {
            Log.e("extractSeatSelections:", "JSONException occurred: " + e.getMessage());
        }

        return this.tickets;
    }


    public String handleConversationState(String userId, String intent, Play play, String time, String ticketType, ArrayList<Ticket> seatSelections,
                                          int ticket_number, String email, String phone) {
        UserSession session = userSessions.getOrDefault(userId, new UserSession());

        if (play != null) { session.setPlay(play); }
        if (time != null) { session.setTime(time); }
        if (ticketType != null) { session.setTicket_type(ticketType); }
        if (ticket_number != 0) { session.setTicket_number(ticket_number); }
        boolean flag = true;
        if (email != null) {
            if (email.equals(this.email)) {
                session.setEmail(email);
                flag = false;
            }
        }
        if (flag) {
            session.setEmail(this.email);
        }
        flag = true;
        if (phone != null) {
            if (email.equals(this.phone)){
                session.setPhone(phone);
                flag = false;
            }
        }
        if (flag) {
            session.setPhone(this.phone);
        }
        updateResponseInfo(session);

        if (! ("book_ticket".equals(intent) || "seat_selection_provided".equals(intent) || "personal_info_provided".equals(intent)
                || "cancel_ticket".equals(intent) || "wit$confirmation".equals(intent) || "cancellation_tickets_provided".equals(intent)
                || "no_cancellation_tickets".equals(intent) || "wit$negation".equals(intent) || "ticket_number_provided".equals(intent)
                || "edit_ticket".equals(intent) || "seat_confirmation_provided".equals(intent) || "payment_received".equals(intent))) {
            if (intentResponses.containsKey(intent)) {
                return getRandomResponse(intentResponses.get(intent)) + responseInfo.get(intent);
            }
        }

        switch (session.getState()) {
            case INITIAL:
                if ("book_ticket".equals(intent)) {
                    if (play != null && time != null) {
                        session.setPlay(play);
                        session.setTime(time);
                        session.setState(State.ASK_SEAT_SELECTION);
                        userSessions.put(userId, session);
                        return getRandomResponse(intentResponses.get("play_and_time_provided"))+responseInfo.get("play_and_time_provided");
                    }
                    session.setState(State.ASK_PLAY_AND_TIME);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get("get_plays"))+responseInfo.get("get_plays") + "\n\n" + getRandomResponse(intentResponses.get("book_ticket"))+responseInfo.get("book_ticket");

                } else if ("cancel_ticket".equals(intent)) {
                    session.setState(State.CONFIRM_CANCELLATION_INTENT);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get("cancel_ticket"))+responseInfo.get("cancel_ticket");

                } else if ("edit_ticket".equals(intent)) {
                    session.setState(State.CONFIRM_SWITCH_TO_CANCELLATION);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get("change_play"));
                }
                break;


            // TICKET RESERVATION STATES

            case ASK_PLAY_AND_TIME:
                if (play != null && time != null) {
                    session.setPlay(play);
                    session.setTime(time);
                    session.setState(State.ASK_TICKET_NUMBER);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get("play_and_time_provided"))+responseInfo.get("play_and_time_provided");
                }
                break;
            case ASK_TICKET_NUMBER:
                Set<String> seatsSelectedByOthers = SharedPreferencesHelper.loadUnavailableSeats(context, session.getPlay().getName(), session.getTime());
                if ("ticket_number_provided".equals(intent)) {
                    Log.e("Size:", String.valueOf(seatsSelectedByOthers.size()));
                    if (seatsSelectedByOthers.size() < 80) {
                        session.setState(State.SEAT_SELECTION_PERMISSION);
                        userSessions.put(userId, session);
                        return getRandomResponse(intentResponses.get(intent)) + responseInfo.get(intent);
                    } else {
                        session.setState(State.INITIAL);
                        userSessions.put(userId, session);
                        return getRandomResponse(intentResponses.get("sold-out"));
                    }

                }
            case SEAT_SELECTION_PERMISSION:
                if ("wit$confirmation".equals(intent)) {
                    session.setState(State.ASK_SEAT_SELECTION);
                    if (callback != null) {
                        callback.launchAuditoriumActivity(session.getPlay().getName(), session.getTime(), session.getTicket_number());
                    }
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get("nice"));
                } else if ("wit$negation".equals(intent)) {
                    session.setState(State.INITIAL);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get("cancel_procedure"));
                }
                break;
            case ASK_SEAT_SELECTION:
                if ("seat_selection_provided".equals(intent)) {
                    session.setState(State.CONFIRM_SEAT_SELECTION);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get(intent)) + responseInfo.get(intent);
                }
                break;
            case CONFIRM_SEAT_SELECTION:
                if ("wit$confirmation".equals(intent)) {
                    session.setState(State.PERSONAL_INFO_PERMISSION);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get("nice")) + " " + getRandomResponse(intentResponses.get("seat_confirmation_provided")) + responseInfo.get("seat_confirmation_provided");
                } else if ("wit$negation".equals(intent)) {
                    session.setState(State.INITIAL);
                    userSessions.put(userId, session);
                    tickets = null;
                    return getRandomResponse(intentResponses.get("cancel_procedure"));
                }
                break;
            case PERSONAL_INFO_PERMISSION:
                if ("wit$confirmation".equals(intent)) {
                    session.setState(State.ASK_PERSONAL_INFO);
                    userSessions.put(userId, session);
                    if (callback != null) {
                        callback.launchPersonalInfoActivity(session.getPlay().getName(), session.getTime());
                    }
                    return getRandomResponse(intentResponses.get("nice"));
                } else if ("wit$negation".equals(intent)) {
                    session.setState(State.INITIAL);
                    userSessions.put(userId, session);
                    tickets = null;
                    return getRandomResponse(intentResponses.get("cancel_procedure"));
                }
                break;
            case ASK_PERSONAL_INFO:
                if ("personal_info_provided".equals(intent)) {
                    for (Ticket ticket : tickets) {
                        ticket.setTitle(session.getPlay().getName());
                        ticket.setDate("5/7/2024");
                        ticket.setTime(getHoursAndMinutes(session.getTime()));
                        ticket.setLocation("Megaro Mousikis");
                        ticket.setName(name);
                        ticket.setEmail(email);
                        ticket.setPhone(phone);
                        ticket.setPrice(getCharge(ticket.getType()));
                        int image_id = session.getPlay().getName().equals("Romeo and Juliet") ? R.drawable.ic_ticket_image : R.drawable.hamlet;
                        ticket.setImageResId(image_id);
                    }
                    session.setState(State.CONFIRM_RESERVATION);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get(intent)) + responseInfo.get(intent);
                }
                break;
            case CONFIRM_RESERVATION:
                if ("wit$confirmation".equals(intent)) {
                    if (callback != null) {
                        callback.launchPaymentActivity(ticketTypes);
                    }
                    session.setState(State.RESERVATION_COMPLETE);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get("nice"));
                } else if ("wit$negation".equals(intent)) {
                    session.setState(State.INITIAL);
                    userSessions.put(userId, session);
                    tickets = null;
                    return getRandomResponse(intentResponses.get("cancel_procedure"));
                }
                break;
            case RESERVATION_COMPLETE:
                if ("payment_received".equals(intent)) {
                    for (Ticket ticket : tickets) {
                        MyTicketsActivity.addTicket(ticket, context);

                        Set<String> unavailableSeats = SharedPreferencesHelper.loadUnavailableSeats(context, session.getPlay().getName(), session.getTime());
                        Log.e("TESTING ROW:", "Row " + getNumber(ticket.getRow().charAt(0)) + " Seat " + ticket.getSeat() );
                        unavailableSeats.add("Row " + getNumber(ticket.getRow().charAt(0)) + " Seat " + ticket.getSeat());
                        SharedPreferencesHelper.saveUnavailableSeats(context, session.getPlay().getName(), session.getTime(), unavailableSeats);
                    }
                    session.setState(State.INITIAL);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get("reservation_confirmation"));
                }
                break;


            // TICKET CANCELLATION STATES

            case CONFIRM_CANCELLATION_INTENT:
                if ("wit$confirmation".equals(intent)) {
                    if (callback != null) {
                        callback.launchSelectTicketsActivity();
                    }
                    session.setState(State.SELECT_TICKET);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get("nice"));
                } else if ("wit$negation".equals(intent)) {
                    session.setState(State.INITIAL);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get("cancel_procedure"));
                }
                break;
            case SELECT_TICKET:
                if ("cancellation_tickets_provided".equals(intent)) {
                    session.setState(State.CONFIRM_CANCELLATION);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get(intent)) + responseInfo.get(intent);
                }  else if ("no_cancellation_tickets".equals(intent)) {
                    session.setState(State.INITIAL);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get(intent)) + responseInfo.get(intent);
                }
                break;
            case CONFIRM_CANCELLATION:
                if ("wit$confirmation".equals(intent)) {
                    for (int i = 0; i < tickets.size(); i++) {
                        SelectTicketsActivity.removeTicket(tickets.get(i), context);
                    }
                    String form = tickets.size()>1 ? "tickets" : "ticket";
                    String form2 = tickets.size()>1 ? "have" : "has";
                    tickets = null;
                    session.setState(State.INITIAL);
                    userSessions.put(userId, session);
                    return "Your " + form + " " + form2 + " been successfully cancelled! You will be refunded within three days. If you need anything else please let me know.";
                } else if ("wit$negation".equals(intent)) {
                    session.setState(State.INITIAL);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get("cancel_procedure"));
                }
                break;


            // TICKET EDITING STATES

            case CONFIRM_SWITCH_TO_CANCELLATION:
                if ("wit$confirmation".equals(intent)) {
                    session.setState(State.CONFIRM_CANCELLATION_INTENT);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get("switch_to_cancellation"));
                } else if ("wit$negation".equals(intent)) {
                    session.setState(State.INITIAL);
                    userSessions.put(userId, session);
                    return getRandomResponse(intentResponses.get("cancel_procedure"));
                }
                break;


            // DEFAULT CASE, STATELESS
            default:
                if (intentResponses.containsKey(intent)) {
                    return getRandomResponse(intentResponses.get(intent)) + responseInfo.get(intent);
                }
                break;
        }
        return "I'm not sure how to respond to that. Maybe you would like to speak to a representative or ask me in another way.";
    }

    private String getRandomResponse(List<String> responses) {
        return responses.get(randomizer.nextInt(responses.size()));
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    public void setTicketTypes(ArrayList<String> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getNumber(char letter) {
        if (letterMap.containsKey(letter)) {
            return letterMap.get(letter);
        } else {
            throw new IllegalArgumentException("Character must be between 'a' and 'h'");
        }
    }

    public int getCharge(String type) {
        if (type.equals("regular")) {
            return 20;
        } else if (type.equals("student")) {
            return 10;
        } else if (type.equals("educator")) {
            return 10;
        } else if (type.equals("elderly")) {
            return 15;
        }else if (type.equals("family")){
            return 12;
        } else if (type.equals("unemployed")) {
            return 10;
        } else {
            return 20;
        }
    }

    public static String getHoursAndMinutes(String isoTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return zonedDateTime.format(formatter);
    }
}
