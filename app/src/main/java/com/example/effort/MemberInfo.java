package com.example.effort;

public class MemberInfo {
    private  String name;
    private  String phone;
    private  String birthDay;
    private  String addres;
    private  String photoUri;

    public MemberInfo(String name, String phone, String birthDay, String addres, String photoUri) {
        this.name = name;
        this.phone = phone;
        this.birthDay = birthDay;
        this.addres = addres;
       this.photoUri=photoUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public String getphotoUri() {
        return photoUri;
    }

    public void setphotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
