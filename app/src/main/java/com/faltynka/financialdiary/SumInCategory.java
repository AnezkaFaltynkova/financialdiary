package com.faltynka.financialdiary;

import java.io.Serializable;

public class SumInCategory implements Serializable {
    private String category;
    private int sum;

    public SumInCategory() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
