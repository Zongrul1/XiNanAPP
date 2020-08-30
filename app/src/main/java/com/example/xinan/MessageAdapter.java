package com.example.xinan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xinan.db.Message;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {
    static class ViewHolder {
        TextView title;
        TextView note;
    }

    private int resourceId;

    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title = (TextView)view.findViewById(R.id.list_example_text1);
        viewHolder.note = (TextView)view.findViewById(R.id.list_example_text2);
        viewHolder.title.setText(message.getTitle());
        viewHolder.note.setText(message.getNote());

        return view;
    }
}
