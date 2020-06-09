package com.example.papap;

public class Payment {
    private String id, state;
    private int amount;

    public Payment(String id, String state, int amount) {
        this.id = id;
        this.state = state;;
        this.amount = amount;
    }

    public Payment() {
    }

    public String getid() {
        return id;
    }

    public String getState() {
        return state;
    }

    public int getAmount() {
        return amount;
    }

}
