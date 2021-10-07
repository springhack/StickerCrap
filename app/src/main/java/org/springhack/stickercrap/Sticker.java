package org.springhack.stickercrap;

import com.google.gson.Gson;

public class Sticker {

    public static Gson gson = new Gson();

    public String name;
    public String url;
    public String author;
    public String image;
    public Boolean like = false;

    public static Sticker fromJSON(String json) {
        return gson.fromJson(json, Sticker.class);
    }

    public static String toJSON(Sticker sticker) {
        return gson.toJson(sticker, Sticker.class);
    }

    public Sticker(String n, String u, String a, String i) {
        this.name = n;
        this.url = u;
        this.author = a;
        this.image = i;
    }
}
