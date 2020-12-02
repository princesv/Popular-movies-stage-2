package com.prince.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.RecyclerViewItemViewHolder> {
    List<String> name;
    List<String> userName;
    List<String> avatar;
    List<Double> rating;
    List<String> review;
    Context context;
    ReviewListAdapter(Context context,Reviews reviews){
        this.context = context;
        this.name = reviews.getName();
        this.userName = reviews.getUserName();
        this.avatar = reviews.getAvatar();
        this.rating = reviews.getRating();
        this.review = reviews.getReviews();
    }

    @NonNull
    @Override
    public RecyclerViewItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.review_list_item,parent,false);
        return new RecyclerViewItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewItemViewHolder holder, int position) {
        holder.nameTv.setText(name.get(position));
        holder.userName.setText(userName.get(position));
        holder.rating.setText(""+rating.get(position));
        holder.content.setText(review.get(position));
        //  holder.animalImage.setImageResource(animalImage[position]);
        Picasso.get().load("http://image.tmdb.org/t/p/w185//"+avatar.get(position)).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public class RecyclerViewItemViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTv;
        public TextView userName;
        public TextView rating;
        public TextView content;
        public ImageView avatar;

        public RecyclerViewItemViewHolder(@NonNull View itemView) {

            super(itemView);
            nameTv = itemView.findViewById(R.id.nameTv);
            userName = itemView.findViewById(R.id.userNameTv);
            rating = itemView.findViewById(R.id.ratingTv);
            content = itemView.findViewById(R.id.reviewTv);
            avatar = itemView.findViewById(R.id.avatarIv);
        }
    }
}
