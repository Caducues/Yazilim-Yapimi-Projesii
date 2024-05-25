package com.example.yyproje;

public class UserSession {
    private static UserSession instance;
    private int userId;
    private int kelimeSayisi;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getKelimeSayisi() {
        return kelimeSayisi;
    }

    public void setKelimeSayisi(int kelimeSayisi) {
        this.kelimeSayisi = kelimeSayisi;
    }
}
