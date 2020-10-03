package com.example.xinan.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.xinan.R;
import com.example.xinan.ShowActivity;
import com.example.xinan.View.URLImageView;
import com.example.xinan.db.Content;
import com.example.xinan.db.News;


import java.util.List;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {
    private List<Content> contents;
    private int resourceId;
    private String HttpsUrl;
    private Context context;

    public ContentAdapter(int resource, List<Content> contents,Context context) {
        this.contents = contents;
        resourceId = resource;
        HttpsUrl = "http://img.xnxz.top/";
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        ContentAdapter.ViewHolder vh = new ContentAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.name.setText(contents.get(position).getNick());
        holder.tag.setText(contents.get(position).getTag());
        holder.description.setText(contents.get(position).getDescription());
        holder.title.setText(contents.get(position).getTitle());
        holder.time.setText(contents.get(position).getDateBt());
        holder.id = contents.get(position).getId();
        if(contents.get(position).getPic() != null && contents.get(position).getPic().length() > 0) {
            Glide.with(context)
                    .load(HttpsUrl + contents.get(position).getPic())
                    .apply(new RequestOptions().placeholder(R.drawable.banner1).error(R.drawable.banner3))//加载前图片，加载失败图片
                    .transition(DrawableTransitionOptions.withCrossFade())//渐变
                    .into(new SimpleTarget<Drawable>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            holder.image.setBackground(resource);
                        }
                    });
        }
        else holder.image.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView title;
        TextView tag;
        TextView description;
        TextView time;
        ImageView image;
        int id;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.description);
            tag = view.findViewById(R.id.tag);
            title = view.findViewById(R.id.title);
            time = view.findViewById(R.id.time);
            image = view.findViewById(R.id.image);
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
//    static class ViewHolder {
//        TextView name;
//        TextView title;
//        TextView tag;
//        TextView description;
//        TextView time;
//    }
//
//    private int resourceId;
//
//    public ContentAdapter(Context context, int resource, List<Content> objects) {
//        super(context, resource, objects);
//        resourceId = resource;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Content con = getItem(position);
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
//        viewHolder.name = (TextView) view.findViewById(R.id.name);
//        viewHolder.description = (TextView) view.findViewById(R.id.description);
//        viewHolder.tag = (TextView) view.findViewById(R.id.tag);
//        viewHolder.title = (TextView) view.findViewById(R.id.title);
//        viewHolder.time = (TextView) view.findViewById(R.id.time);
//        viewHolder.name.setText(con.getNick());
//        viewHolder.tag.setText(con.getTag());
//        viewHolder.description.setText(con.getDescription());
//        viewHolder.title.setText(con.getTitle());
//        viewHolder.time.setText(con.getDateBt());
//
//        return view;
//    }
//}
