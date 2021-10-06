package org.springhack.stickercrap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class StickerAdapter extends ArrayAdapter<Sticker> {

    private int resourceId;

    public StickerAdapter(Context context, int textViewResourceId,
                          List<Sticker> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Sticker sticker = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView index = (TextView) view.findViewById(R.id.sticker_index);
        TextView name = (TextView) view.findViewById(R.id.sticker_name);
        TextView author = (TextView) view.findViewById(R.id.sticker_author);
        ImageView image = (ImageView) view.findViewById(R.id.sticker_image);

        index.setText(Integer.toString(position + 1));
        name.setText(sticker.name);
        author.setText(sticker.author);
        Picasso.get().load(sticker.image).into(image);
        return view;
    }

}
