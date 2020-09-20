package com.supernover.agasobanuyestreamingappclient.Adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.supernover.agasobanuyestreamingappclient.Model.GetVideoDetails;
import com.supernover.agasobanuyestreamingappclient.MovieDetailsActivity;
import com.supernover.agasobanuyestreamingappclient.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;


public class SearchAdapter extends FirebaseRecyclerAdapter<GetVideoDetails, SearchAdapter.SearchViewHolder> {










    public SearchAdapter(@NonNull FirebaseRecyclerOptions<GetVideoDetails> options) { super(options); }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_movie_details,parent,false);
        return new SearchViewHolder(view);





    }

    @Override
    protected void onBindViewHolder(@NonNull SearchViewHolder holder, int position, @NonNull GetVideoDetails model) {


        Picasso.get().load(model.getVideo_thumb()).into(holder.poster);

        holder.contentname.setText(model.getVideo_name());

        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent detail = new Intent(v.getContext(), MovieDetailsActivity.class);

                detail.putExtra("detailposter",model.getVideo_thumb());
                detail.putExtra("detailtitle",model.getVideo_name());
                //detail.putExtra("detailyear",model.getYear());
                //detail.putExtra("detailquality",model.getQuality());
                detail.putExtra("detaildescription",model.getVideo_description());
                detail.putExtra("detailcategory",model.getVideo_category());


                v.getContext().startActivity(detail);

            }
        });










    }

    class SearchViewHolder extends RecyclerView.ViewHolder{


        ImageView poster;
        TextView contentname;


        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);


            poster = itemView.findViewById(R.id.detail_movies_cover);
            contentname = itemView.findViewById(R.id.detail_movie_title);



        }
    }










}
