package com.example.papap;

import androidx.annotation.NonNull;

//Cada bebe tendra una dieta por dia, en el main fragment habra un array de treinta de estos para cada bebe
public class Diet {
    private Pap morning,
            evening,
            nigth;

    public Diet(){
        this.morning = null;
        this.evening = null;
        this.nigth = null;
    }

    public Diet(Pap morning,Pap evening,Pap nigth){
        this.morning = morning;
        this.evening = evening;
        this.nigth = nigth;
    }

    @NonNull
    @Override
    public String toString() {
        return ("Morning" + this.getMorning().getNombre() + ", Evening" + this.getEvening().getNombre() + ", Night" + this.getNigth().getNombre());
    }

    public Pap getMorning() {
        return morning;
    }

    public void setMorning(Pap morning) {
        this.morning = morning;
    }

    public Pap getEvening() {
        return evening;
    }

    public void setEvening(Pap evening) {
        this.evening = evening;
    }

    public Pap getNigth() {
        return nigth;
    }

    public void setNigth(Pap nigth) {
        this.nigth = nigth;
    }
}
