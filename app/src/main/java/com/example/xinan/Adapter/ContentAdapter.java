package com.example.xinan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.xinan.R;
import com.example.xinan.db.Content;


import java.util.List;

public class ContentAdapter extends ArrayAdapter<Content> {
    static class ViewHolder {
        TextView name;
        TextView title;
        TextView tag;
        TextView description;
        TextView time;
    }

    private int resourceId;

    public ContentAdapter(Context context, int resource, List<Content> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Content con = getItem(position);
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
        viewHolder.name = (TextView) view.findViewById(R.id.name);
        viewHolder.description = (TextView) view.findViewById(R.id.description);
        viewHolder.tag = (TextView) view.findViewById(R.id.tag);
        viewHolder.title = (TextView) view.findViewById(R.id.title);
        viewHolder.time = (TextView) view.findViewById(R.id.time);
        viewHolder.name.setText(con.getNick());
        viewHolder.tag.setText(con.getTag());
        viewHolder.description.setText(con.getDescription());
        viewHolder.title.setText(con.getTag());
        viewHolder.time.setText(con.getDateBt());

        return view;
    }
}
