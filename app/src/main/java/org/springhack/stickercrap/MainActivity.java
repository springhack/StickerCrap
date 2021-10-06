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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
                    Document document =
                            Jsoup
                                    .connect(Constants.STICKER_LIST_URL)
                                    .userAgent(Constants.USER_AGENT)
                                    .referrer(Constants.STICKER_LIST_URL)
                                    .get();
                    Elements elements = document.select(".table_container .tbody tr .table_cell.detail");
                    for (Element node : elements) {
                        Sticker sticker = new Sticker(
                                node.select(".detail_content a.title").first().text(),
                                node.select(".detail_content a.title").first().absUrl("href"),
                                node.select(".detail_content .desc").first().text(),
                                node.select("img").first().absUrl("src"));
                        list.add(sticker);
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
