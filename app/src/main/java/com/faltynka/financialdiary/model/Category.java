package com.faltynka.financialdiary.model;

public class Category {
    private Long id;
    private String name;
    private Long type;

    public Category() {
    }

    public Category(Long id, String name, Long type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }
}
