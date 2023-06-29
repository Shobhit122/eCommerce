package molu.example.ecommerce.domain;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String userid = null;
    private String username = null;
    private String email= null;
    private String phoneno = null;
    private List<String> address;
    private List<String> cart;
    private List<String> favorite;

    public User() {
    }

    public User(String userid, String username, String email, String phoneno, List<String> address, List<String> cart, List<String> favorite) {
        this.userid = userid;
        this.username = username;
        this.email = email;
        this.phoneno = phoneno;
        this.address = address;
        this.cart = cart;
        this.favorite = favorite;
    }

    public List<String> getFavorite() {
        return favorite;
    }

    public void setFavorite(List<String> favorite) {
        this.favorite = favorite;
    }

    public List<String> getCart() {
        return cart;
    }

    public void setCart(List<String> cart) {
        this.cart = cart;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }
}
