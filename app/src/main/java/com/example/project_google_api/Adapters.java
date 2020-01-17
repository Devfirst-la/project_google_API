package com.example.project_google_api;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.project_google_api.model.*;

import java.util.List;

public class Adapters  extends RecyclerView.Adapter<Adapters.ViewHolder> {

    private List<Articles> articles;
    private Context context;

    public Adapters(List<Articles> articles, Context context) {
        this.articles = articles;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapters.ViewHolder holder, int position) {
       Adapters.ViewHolder holers = holder;
        final Articles articles1 = articles.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());///
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(articles1.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.processBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.processBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);
        holder.title.setText(articles1.getTitle());
        holder.desc.setText(articles1.getDescription());
        holder.source.setText(articles1.getSource().getName());
        holder.time.setText(" \u2022 "+Utils.DateToTimeFormat(articles1.getPublishedAt()));///
        holder.publishe_AT.setText(Utils.DateFormat(articles1.getPublishedAt()));
        holder.author.setText( articles1.getAuthor());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,articles1.getTitle(),Toast.LENGTH_LONG).show();

               Intent  intent = new Intent(context,Detail.class);
                intent.putExtra("url",articles1.getUrl());
                intent.putExtra("title",articles1.getTitle());
                intent.putExtra("img",articles1.getUrlToImage());
                intent.putExtra("date",articles1.getPublishedAt());
                intent.putExtra("source",articles1.getSource().getName());
                intent.putExtra("author",articles1.getContent());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        TextView title,desc,author,publishe_AT,source,time;
        ImageView imageView;
        ProgressBar processBar;

        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.txt_title);
            desc = itemView.findViewById(R.id.txt_desc);
            author = itemView.findViewById(R.id.txt_author);
            publishe_AT=itemView.findViewById(R.id.txt_publishedAT);
            source = itemView.findViewById(R.id.txt_source);
            time = itemView.findViewById(R.id.txt_time);
            imageView = itemView.findViewById(R.id.t_img);
            processBar = itemView.findViewById(R.id.p_load_photo);
            relativeLayout = itemView.findViewById(R.id.load_relativelaout);

        }

    }

}
