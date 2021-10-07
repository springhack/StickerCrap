package org.springhack.stickercrap;

public class Constants {
    public static String FILE_PROVIDER = BuildConfig.APPLICATION_ID + ".fileprovider";
    public static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36";
    public static class Emojis {
        public static String NO = "\uD83D\uDD05";
        public static String LIKE = "\uD83D\uDC96";
        public static String NORMAL = "\uD83D\uDCAD";
    }
    public static String[] ALLOW_PACKAGES = {
            "com.fluidtouch.noteshelf2",
            "com.samsung.android.app.notes",
            "com.samsung.android.spdfnote"
    };
    public static class FetchConfig {
        public static String STICKER_LIST_URL = "https://sticker.weixin.qq.com/cgi-bin/mmemoticon-bin/emoticonview?oper=billboard&t=rank";
        public static String STICKER_LIST_SELECTOR = ".table_container .tbody tr .table_cell.detail";
        public static class NameResolver {
            public static String SELECTOR =".detail_content a.title";
            public static String GETTER = "text";
        }
        public static class UrlResolver {
            public static String SELECTOR =".detail_content a.title";
            public static String GETTER = "href";
        }
        public static class AuthorResolver {
            public static String SELECTOR =".detail_content .desc";
            public static String GETTER = "text";
        }
        public static class ImageResolver {
            public static String SELECTOR ="img";
            public static String GETTER = "src";
        }
        public static class StickerResolver {
            public static String SELECTOR =".stiker_content_ele";
            public static String GETTER = "src";
        }
    }
}
