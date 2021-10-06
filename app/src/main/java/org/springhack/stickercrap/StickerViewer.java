package org.springhack.stickercrap;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.AlignSelf;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StickerViewer extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker_viewer);

        final int DPS = getResources().getDimensionPixelSize(R.dimen.dps);
        File shared_files = new File(getCacheDir(), "shared_files");
        shared_files.mkdirs();

        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.viewer);
        View flex_view = LayoutInflater.from(this).inflate(R.layout.flexbox, null);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        flex_view.setLayoutParams(params);
        layout.addView(flex_view);
        FlexboxLayout flex = (FlexboxLayout) flex_view.findViewById(R.id.flexbox_layout);

        ImageView logo = (ImageView)findViewById(R.id.logo);
        String logo_url = getIntent().getExtras().getString("image");
        Picasso.get().load(logo_url).into(logo);

        TextView title = (TextView)findViewById(R.id.title);
        String name = getIntent().getExtras().getString("name");
        title.setText(name);
        setTitle(name);

        TextView desc = (TextView)findViewById(R.id.desc);
        String author = getIntent().getExtras().getString("author");
        desc.setText("By " + author);

        flex.setPadding(0, (int) (desc.getTextSize() * 2 + desc.getHeight() * 2 + desc.getPaddingTop()), 0, 0);

        String url = getIntent().getExtras().getString("url");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup
                            .connect(url)
                            .userAgent(Constants.USER_AGENT)
                            .referrer(Constants.STICKER_LIST_URL)
                            .get();
                    Elements elements = document.select(".stiker_content_ele");
                    List<String> stickers = new ArrayList<String>();
                    for (Element node : elements) {
                        stickers.add(node.absUrl("src"));
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (String url : stickers) {
                                ImageView sticker = new ImageView(StickerViewer.this);
                                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(DPS, DPS);
                                params.setMargins((int)(0.1 * DPS), (int)(0.1 * DPS), (int)(0.1 * DPS), (int)(0.1 * DPS));
                                params.setAlignSelf(AlignItems.CENTER);
                                sticker.setLayoutParams(params);
                                Picasso.get().load(url).into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        sticker.setImageBitmap(bitmap);
                                        File file = new File(shared_files, md5(url).concat(".png"));
                                        if (!file.exists()) {
                                            try {
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                                return;
                                            }
                                        }
                                        sticker.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {
                                                Uri uri = FileProvider.getUriForFile(StickerViewer.this, Constants.FILE_PROVIDER, file);
                                                for (String pkg : Constants.ALLOW_PACKAGES) {
                                                    getApplicationContext().grantUriPermission(pkg, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                }
                                                ClipData data = ClipData.newUri(getContentResolver(), "galleryUri", uri);
                                                flex_view.startDragAndDrop(data, new View.DragShadowBuilder(v),null, View.DRAG_FLAG_GLOBAL);
                                                return true;
                                            }
                                        });
                                    }
                                    @Override
                                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                    }
                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                                    }
                                });
                                flex.addView(sticker);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
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