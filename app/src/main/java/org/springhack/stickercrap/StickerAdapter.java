package org.springhack.stickercrap;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        int like_count = Utils.GetLikeStickers(getContext()).size();
        int real_position = position - like_count + 1;

        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView index = (TextView) view.findViewById(R.id.sticker_index);
        TextView name = (TextView) view.findViewById(R.id.sticker_name);
        TextView author = (TextView) view.findViewById(R.id.sticker_author);
        ImageView image = (ImageView) view.findViewById(R.id.sticker_image);
        Button like = (Button) view.findViewById(R.id.sticker_like);

        name.setText(sticker.name);
        author.setText(sticker.author);
        index.setText(real_position > 0 ? Integer.toString(real_position) : Constants.Emojis.NO);
        like.setText(sticker.like ? Constants.Emojis.LIKE : Constants.Emojis.NORMAL);
        Picasso.get().load(sticker.image).into(image);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sticker.like = !sticker.like;
                Utils.ChangeStickerStatus(getContext(), sticker);
                Toast.makeText(getContext(), (sticker.like ? "Like " : "Unlike ") + sticker.name, 1000).show();
                like.setText(sticker.like ? Constants.Emojis.LIKE : Constants.Emojis.NORMAL);
            }
        });
        return view;
    }

}
