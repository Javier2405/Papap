package com.example.papap;

public class Card {
    private String titular, cardnum, vencimiento;
    private boolean selected;

    public Card(String titular, String cardnum, String vencimiento, boolean selected) {
        this.titular = titular;
        this.cardnum = cardnum;
        this.vencimiento = vencimiento;
        this.selected = selected;
    }

    public Card() {
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getCardnum() {
        return cardnum;
    }

    public void setCardnum(String cardnum) {
        this.cardnum = cardnum;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
