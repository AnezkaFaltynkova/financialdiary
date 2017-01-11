package com.faltynka.financialdiary.sqlite.model;

import java.io.Serializable;

public class Category implements Serializable{
    private int id;
    private String firebaseId;
    private String name;
    private int type;
    private long edited;
    private int deleted;

    public Category() {
    }

    public Category(String name, int type, long edited, int deleted) {
        this.name = name;
        this.type = type;
        this.edited = edited;
        this.deleted = deleted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public long getEdited() {
        return edited;
    }

    public void setEdited(long edited) {
        this.edited = edited;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof Category) {
            Category other = (Category) o;
            return this.id == other.getId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
