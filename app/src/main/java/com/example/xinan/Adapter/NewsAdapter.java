package com.example.xinan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.xinan.R;
import com.example.xinan.db.News;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    static class ViewHolder {
        TextView title;
        TextView note;
    }

    private int resourceId;

    public NewsAdapter(Context context, int resource, List<News> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = getItem(position);
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
        viewHolder.title.setText(news.getTitle());
        viewHolder.note.setText(news.getNote());

        return view;
    }
}
