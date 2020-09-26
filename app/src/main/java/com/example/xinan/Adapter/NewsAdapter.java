package com.example.xinan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xinan.R;
import com.example.xinan.ShowActivity;
import com.example.xinan.db.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<News> news;
    private int resourceId;

    public NewsAdapter(int resource, List<News> news) {
        this.news = news;
        resourceId = resource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(news.get(position).getTitle());
        holder.note.setText(news.get(position).getNote());
        holder.id = news.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title ;
        TextView note;
        int id;

        ViewHolder(View view) {
            super(view);
            title = (TextView)view.findViewById(R.id.list_example_text1);
            note = (TextView)view.findViewById(R.id.list_example_text2);

            // To show how to add click listener to a item in recyclerView
            // Set onClickListener for the fruit array;
            // When clicking a item from the list, a new Toast shows the name of the clicked fruit;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ShowActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", String.valueOf(id));
                    intent.putExtras(bundle);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}


/*
* ListView 老方法
 */
//    public View getView(int position, View convertView, ViewGroup parent) {
//        News news = getItem(position);
//        View view;
//        ViewHolder viewHolder;
//        if (convertView == null) {
//            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
//            viewHolder = new ViewHolder();
//            view.setTag(viewHolder);
//        } else {
//            view = convertView;
//            viewHolder = (ViewHolder) view.getTag();
//        }
//        viewHolder.title = (TextView)view.findViewById(R.id.list_example_text1);
//        viewHolder.note = (TextView)view.findViewById(R.id.list_example_text2);
//        viewHolder.title.setText(news.getTitle());
//        viewHolder.note.setText(news.getNote());
//
//        return view;
//    }

