package com.faltynka.financialdiary.sqlite.model;

public class Category {
    private int id;
    private String firebaseId;
    private String name;
    private int type;

    public Category() {
    }

    public Category(String firebaseId, int id, String name, int type) {
        this.firebaseId = firebaseId;
        this.id = id;
        this.name = name;
        this.type = type;
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
