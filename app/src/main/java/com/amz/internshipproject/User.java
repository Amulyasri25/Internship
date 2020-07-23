package com.amz.internshipproject;

public class User {

    private  String name,email,mobileNumber,userType,address,state;

    public User() {
    }


    public User(String name, String email, String mobileNumber,String userType,String address,String state) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.userType = userType;
        this.address=address;
        this.state=state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public  String getState(){ return state;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

