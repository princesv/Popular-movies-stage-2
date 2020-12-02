package com.prince.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerListViewHolder> {
    List<String> titles;
    List<String> keys;
    Context context;
    private final TrailerListItemClickListener mTrailerItemClickListener;

    public interface TrailerListItemClickListener{
        void onTrailerListItemClick(int listItemIndex);

    }

    TrailerListAdapter(Context context, Trailers trailers,TrailerListItemClickListener listener) {
        this.titles = trailers.getTitle();
        this.keys = trailers.getKey();
        this.context = context;
        mTrailerItemClickListener = listener;
    }

    @NonNull
    @Override
    public TrailerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.trailer_list_view_item,parent,false);

        return new TrailerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerListViewHolder holder, int position) {
       holder.titleTv.setText(titles.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class TrailerListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView titleTv;

        public TrailerListViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.trailerTitleTextView);
            itemView.setOnClickListener(this);         //***********VERY IMPORTANT. DONT FORGET**************
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mTrailerItemClickListener.onTrailerListItemClick(position);
        }
    }
}
