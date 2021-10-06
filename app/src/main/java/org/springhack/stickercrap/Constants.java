package org.springhack.stickercrap;

public class Constants {
    public static String FILE_PROVIDER = BuildConfig.APPLICATION_ID + ".fileprovider";
    public static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36";
    public static String STICKER_LIST_URL = "https://sticker.weixin.qq.com/cgi-bin/mmemoticon-bin/emoticonview?oper=billboard&t=rank";
    public static String[] ALLOW_PACKAGES = {
            "com.fluidtouch.noteshelf2",
            "com.samsung.android.app.notes",
            "com.samsung.android.spdfnote"
    };
}
