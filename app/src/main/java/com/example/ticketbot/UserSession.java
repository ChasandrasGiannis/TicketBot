package com.example.ticketbot;

public class UserSession {
    private State state;
    private Play play;
    private String time;
    private String ticket_type = "regular";
    private String email = null;
    private String phone = null;
    private String name = null;
    private String personalInfo;
    private String contactInfo;
    private String paymentInfo;
    private int ticket_number;
    private String ticketsString;

    public UserSession() {
        this.state = State.INITIAL;
    }

    public String getTicketsString() {
        return ticketsString;
    }
    public void setTicketsString(String ticketsString) {
        this.ticketsString = ticketsString;
    }
    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Play getPlay() {
        return play;
    }

    public void setPlay(Play play) {
        this.play = play;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTicket_type() {
        return ticket_type;
    }

    public void setTicket_type(String ticket_type) {
        this.ticket_type = ticket_type;
    }

    public String getTicketPrice() {
        if (ticket_type.equals("elderly")) {
            return "5€";
        } else if (ticket_type.equals("family")) {
            return "free for children below 5, 5€ for children below 18 and 10€ for adults";
        } else if (ticket_type.equals("student")) {
            return "free";
        } else if (ticket_type.equals("educator")) {
            return "free";
        } else if (ticket_type.equals("unemployed")) {
            return "5€";
        }

        return "10€"; // price of a regular ticket
    }
    public String getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(String personalInfo) {
        this.personalInfo = personalInfo;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(String paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public int getTicket_number() {
        return ticket_number;
    }

    public void setTicket_number(int ticket_number) {
        this.ticket_number = ticket_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

enum State {
    INITIAL,

    // ticket reservation states
    ASK_PLAY_AND_TIME,
    ASK_TICKET_NUMBER,
    SEAT_SELECTION_PERMISSION,
    ASK_SEAT_SELECTION,
    CONFIRM_SEAT_SELECTION,
    PERSONAL_INFO_PERMISSION,
    ASK_PERSONAL_INFO,
    CONFIRM_RESERVATION,
    RESERVATION_COMPLETE,

    // ticket cancellation states
    CONFIRM_CANCELLATION_INTENT,
    SELECT_TICKET,
    CONFIRM_CANCELLATION,


    // ticket editing states
    ASK_EDITING_TYPE,
    CONFIRM_SWITCH_TO_CANCELLATION,
    CONFIRM_EDITING_INTENT,
    SELECT_TICKET_ED,
    SELECTION_CONFIRMATION_ED,
    CHANGE_SEAT,
    CONFIRM_CHANGE
}
