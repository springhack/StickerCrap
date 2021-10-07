package org.springhack.stickercrap;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.ArraySet;

import org.jsoup.nodes.Element;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

public class Utils {
    public static String LIKE_STICKERS_KEY = "LIKE_STICKERS_KEY";

    public static String AttrGetter(Element node, String getter) {
        if (getter == "text") {
            return node.text();
        } else {
            return node.absUrl(getter);
        }
    }

    public static String GetIDFromURL(String url) {
        Uri uri = Uri.parse(url);
        return uri.getQueryParameter("productid");
    }

    public static Set<String> GetLikeStickers(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("sticker_like", Context.MODE_PRIVATE);
        return prefs.getStringSet(LIKE_STICKERS_KEY, new ArraySet<>());
    }

    public static Sticker GetSticker(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences("sticker_like", Context.MODE_PRIVATE);
        String json = prefs.getString(key, Sticker.toJSON(new Sticker("", "", "", "")));
        return Sticker.fromJSON(json);
    }

    public static void ChangeStickerStatus(Context context, Sticker sticker) {
        ChangeStickerStatus(context, sticker, sticker.like);
    }

    public static void ChangeStickerStatus(Context context, Sticker sticker, Boolean like) {
        Set<String> likes = GetLikeStickers(context);
        String key = GetIDFromURL(sticker.url);
        SharedPreferences.Editor editor = context.getSharedPreferences("sticker_like", Context.MODE_PRIVATE).edit();
        if (like) {
            editor.putString(key, Sticker.toJSON(sticker));
            likes.add(key);
        } else {
            editor.remove(key);
            likes.remove(key);
        }
        editor.putStringSet(LIKE_STICKERS_KEY, likes);
        editor.apply();
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
