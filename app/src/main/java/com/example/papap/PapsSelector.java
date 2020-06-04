package com.example.papap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class PapsSelector {
    private String gender;
    private int age;
    private ArrayList<String> preferences;
    private int calories;

    public PapsSelector(String gender, int age, ArrayList<String> preferences){
        this.gender = gender;
        this.age = age;
        this.preferences = preferences;

        if(this.gender.equals("Nino")){
            switch (age){
                case 6:
                case 7:
                    this.calories = 635;
                    break;
                case 8:
                case 9:
                case 10:
                    this.calories = 714;
                    break;
                case 11:
                case 12:
                    this.calories = 819;
                    break;
            }

        } if(this.gender.equals("Nina")) {
            switch (age){
                case 6:
                case 7:
                    this.calories = 725;
                    break;
                case 8:
                case 9:
                case 10:
                    this.calories = 659;
                    break;
                case 11:
                case 12:
                    this.calories = 759;
                    break;
            }
        }
    }

    public boolean validPaps(int calories, ArrayList<String> ingredients){
        if(this.calories-calories>=-100){
            this.calories -= calories;

            for(int i =0;ingredients.size()<i;i++){
                if(this.preferences.contains(ingredients.get(i))){
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
