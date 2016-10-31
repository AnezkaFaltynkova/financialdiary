package com.faltynka.financialdiary.sqlite.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Record {
    private int id;
    private String firebaseId;
    private int fromId;
    private int toId;
    private int amount;
    private long date;
    private String note;
    private long edited;
    private int deleted;

    public Record() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getDate() {
        return date;
    }

    public void setDate(String dateString) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date result = null;
        try {
            result = df.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.date = result.getTime();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getEdited() {
        return edited;
    }

    public void setEdited() {
        Date date = new Date();
        this.edited = date.getTime();
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }
}
