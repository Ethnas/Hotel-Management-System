package no.ntnu.hmsproject.domain;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    String userid;
    @SerializedName("email")
    String email;
    String password;
    @SerializedName("firstName")
    String firstname;
    @SerializedName("lastName")
    String lastname;
    String jwt;
    @SerializedName("phone")
    String phone;

    public User(String eml, String uid, String firstName, String lastName, int phoneNumber, String pwd) {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
