package org.springhack.stickercrap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static org.springhack.stickercrap.Utils.AttrGetter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Sticker> list = new ArrayList();
                    Set<String> likes = Utils.GetLikeStickers(getApplicationContext());
                    for (String like : likes) {
                        list.add(Utils.GetSticker(getApplicationContext(), like));
                    }
                    Document document =
                            Jsoup
                                    .connect(Constants.FetchConfig.STICKER_LIST_URL)
                                    .userAgent(Constants.USER_AGENT)
                                    .referrer(Constants.FetchConfig.STICKER_LIST_URL)
                                    .get();
                    Elements elements = document.select(Constants.FetchConfig.STICKER_LIST_SELECTOR);
                    for (Element node : elements) {
                        Sticker sticker = new Sticker(
                                AttrGetter(node.select(Constants.FetchConfig.NameResolver.SELECTOR).first(),Constants.FetchConfig.NameResolver.GETTER),
                                AttrGetter(node.select(Constants.FetchConfig.UrlResolver.SELECTOR).first(),Constants.FetchConfig.UrlResolver.GETTER),
                                AttrGetter(node.select(Constants.FetchConfig.AuthorResolver.SELECTOR).first(),Constants.FetchConfig.AuthorResolver.GETTER),
                                AttrGetter(node.select(Constants.FetchConfig.ImageResolver.SELECTOR).first(),Constants.FetchConfig.ImageResolver.GETTER));
                        String key = Utils.GetIDFromURL(sticker.url);
                        if (!likes.contains(key)) {
                            list.add(sticker);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ListView list_view = findViewById(R.id.stickers_list);
                            StickerAdapter adapter = new StickerAdapter(
                                    MainActivity.this,
                                    R.layout.stickers_list_item,
                                    list
                            );
                            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Sticker sticker = adapter.getItem(position);
                                    Intent intent = new Intent(MainActivity.this, StickerViewer.class);
                                    intent.putExtra("url", sticker.url);
                                    intent.putExtra("name", sticker.name);
                                    intent.putExtra("image", sticker.image);
                                    intent.putExtra("author", sticker.author);
                                    startActivity(intent);
                                }
                            });
                            list_view.setAdapter(adapter);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
