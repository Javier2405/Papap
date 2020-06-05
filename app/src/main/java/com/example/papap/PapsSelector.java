package com.example.papap;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Se asigna un pap selector a cada bebe y con este vemos si la papilla es valida
public class PapsSelector {
    private String gender;
    private String age;
    private String preferences;
    private int calories;

    public PapsSelector(String gender, String age, String preferences){
        this.gender = gender;
        this.age = age;
        this.preferences = preferences;

        if(this.gender.equals("Masculino")){
            switch (age){
                case "6 meses":
                case "7 meses":
                    this.calories = 635;
                    break;
                case "8 meses":
                case "9 meses":
                case "10 meses":
                    this.calories = 714;
                    break;
                case "11 meses":
                case "12 meses":
                    this.calories = 819;
                    break;
            }

        } if(this.gender.equals("Femenino")) {
            switch (age){
                case "6 meses":
                case "7 meses":
                    this.calories = 725;
                    break;
                case "8 meses":
                case "9 meses":
                case "10 meses":
                    this.calories = 659;
                    break;
                case "11 meses":
                case "12 meses":
                    this.calories = 759;
                    break;
            }
        }
    }

    public boolean validPaps(List<String> ingredients){


            for(int i =0;ingredients.size()<i;i++){
                if(this.preferences.equals(ingredients.get(i))){
                    return false;
                }
            }

            return true;

    }

    public boolean validCalories(int calories){
        if(this.calories-calories>-100){
            this.calories -= calories;
            return true;
        } else {
            return false;
        }
    }

    public void resetCalories(){
        if(this.gender.equals("Masculino")){
            switch (age){
                case "6 meses":
                case "7 meses":
                    this.calories = 635;
                    break;
                case "8 meses":
                case "9 meses":
                case "10 meses":
                    this.calories = 714;
                    break;
                case "11 meses":
                case "12 meses":
                    this.calories = 819;
                    break;
            }

        } if(this.gender.equals("Femenino")) {
            switch (age){
                case "6 meses":
                case "7 meses":
                    this.calories = 725;
                    break;
                case "8 meses":
                case "9 meses":
                case "10 meses":
                    this.calories = 659;
                    break;
                case "11 meses":
                case "12 meses":
                    this.calories = 759;
                    break;
            }
        }
    }
}
