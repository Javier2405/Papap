package com.example.papap;

public class Baby {
    private String Nombre, Edad, Genero, Disgustos, Alergias, id;

    public Baby(String name, String age, String gender, String dislikes, String allergic, String id) {
        this.Nombre = name;
        this.Edad = age;
        this.Genero = gender;
        this.Disgustos = dislikes;
        this.Alergias = allergic;
        this.id = id;
    }

    public Baby() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Nombre;
    }

    public void setName(String name) {
        this.Nombre = name;
    }

    public String getAge() {
        return Edad;
    }

    public void setAge(String age) {
        this.Edad = age;
    }

    public String getGender() {
        return Genero;
    }

    public void setGender(String gender) {
        this.Genero = gender;
    }

    public String getDislikes() {
        return Disgustos;
    }

    public void setDislikes(String dislikes) {
        this.Disgustos = dislikes;
    }

    public String getAllergic() {
        return Alergias;
    }

    public void setAllergic(String allergic) {
        this.Alergias = allergic;
    }
}
