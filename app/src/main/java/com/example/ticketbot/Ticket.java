package com.example.ticketbot;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Ticket implements Parcelable {

    private String title;
    private String date;
    private String time;
    private String location;
    private String row;
    private String seat;
    private String type;
    private String name;
    private String email;
    private String phone;
    private int price;
    private String code;
    private int imageResId;

    public Ticket(String title, String date, String time, String location, String row, String seat, String type, String name, String email, String phone, int price, String code, int imageResId) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
        this.row = row;
        this.seat = seat;
        this.type = type;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.price = price;
        this.code = code;
        this.imageResId = imageResId;
    }

    public Ticket(String title, String date, String time, String location, String row, String seat, String type, String name, int price, String code, int imageResId) {
        this.title = title;
        this.date = date;
        this.time = time;
        this.location = location;
        this.row = row;
        this.seat = seat;
        this.type = type;
        this.name = name;
        this.price = price;
        this.code = code;
        this.imageResId = imageResId;
    }

    public Ticket(String row, String seat, String type) {
        this.title = null;
        this.date = null;
        this.time = null;
        this.location = null;
        this.row = row;
        this.seat = seat;
        this.type = type;
        this.name = null;
        this.price = 0;
        this.code = null;
        this.imageResId = 0;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
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

    public String getTitle() { return title; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getLocation() { return location; }
    public String getRow() { return row; }
    public String getSeat() { return seat; }
    public String getType() { return type; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getCode() { return code; }
    public int getImageResId() { return imageResId; }
    public String getFormattedSeat() {
        return row + seat;
    }

    // Parcelable implementation
    protected Ticket(Parcel in) {
        title = in.readString();
        date = in.readString();
        time = in.readString();
        location = in.readString();
        row = in.readString();
        seat = in.readString();
        type = in.readString();
        name = in.readString();
        price = in.readInt();
        code = in.readString();
        imageResId = in.readInt();
    }

    public static final Creator<Ticket> CREATOR = new Creator<Ticket>() {
        @Override
        public Ticket createFromParcel(Parcel in) {
            return new Ticket(in);
        }

        @Override
        public Ticket[] newArray(int size) {
            return new Ticket[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(location);
        dest.writeString(row);
        dest.writeString(seat);
        dest.writeString(type);
        dest.writeString(name);
        dest.writeInt(price);
        dest.writeString(code);
        dest.writeInt(imageResId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return price == ticket.price &&
                imageResId == ticket.imageResId &&
                Objects.equals(title, ticket.title) &&
                Objects.equals(date, ticket.date) &&
                Objects.equals(time, ticket.time) &&
                Objects.equals(location, ticket.location) &&
                Objects.equals(row, ticket.row) &&
                Objects.equals(seat, ticket.seat) &&
                Objects.equals(type, ticket.type) &&
                Objects.equals(name, ticket.name) &&
                Objects.equals(code, ticket.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, date, time, location, row, seat, type, name, price, code, imageResId);
    }
}
