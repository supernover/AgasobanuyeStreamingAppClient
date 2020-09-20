package com.supernover.agasobanuyestreamingappclient;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.supernover.agasobanuyestreamingappclient.Adapter.MovieShowAdapter;
import com.supernover.agasobanuyestreamingappclient.Model.GetVideoDetails;
import com.supernover.agasobanuyestreamingappclient.Model.MovieItemClickListenerNew;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements MovieItemClickListenerNew, RewardedVideoAdListener {


    private ImageView MoviesThumbNail, MoviesCoverImg;
    TextView tv_title, tv_description;
    FloatingActionButton play_fab;
    RecyclerView RvCast, recyclerView_similarMovies;
    MovieShowAdapter movieShowAdapter;
    DatabaseReference mDatabasereference;
    List<GetVideoDetails> uploads, actionsmovies, sportMovies, comedymovies,
            romanticmovies, advanturemovies;
    String current_video_url;
    String current_video_category;
    private RewardedVideoAd mRewardedVideoAd;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        inView();
        similarmoviesRecycler();
        similarMovies();
        MobileAds.initialize(this, "ca-app-pub-3063877521249388~8852848151");

        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        play_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }

                Intent intent = new Intent(MovieDetailsActivity.this, MoviePlayerActivity.class);
                intent.putExtra("videoUri", current_video_url);
                startActivity(intent);

            }
        });


    }

    private void similarMovies() {

        if (current_video_category.equals("Action")) {
            movieShowAdapter = new MovieShowAdapter(this, actionsmovies, this);


            recyclerView_similarMovies.setAdapter(movieShowAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL, false));
            movieShowAdapter.notifyDataSetChanged();
        }
        if (current_video_category.equals("Sports")) {
            movieShowAdapter = new MovieShowAdapter(this, sportMovies, this);


            recyclerView_similarMovies.setAdapter(movieShowAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL, false));
            movieShowAdapter.notifyDataSetChanged();
        }
        if (current_video_category.equals("Adventure")) {
            movieShowAdapter = new MovieShowAdapter(this, advanturemovies, this);


            recyclerView_similarMovies.setAdapter(movieShowAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL, false));
            movieShowAdapter.notifyDataSetChanged();
        }
        if (current_video_category.equals("Comedy")) {
            movieShowAdapter = new MovieShowAdapter(this, comedymovies, this);


            recyclerView_similarMovies.setAdapter(movieShowAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL, false));
            movieShowAdapter.notifyDataSetChanged();
        }
        if (current_video_category.equals("Romantics")) {
            movieShowAdapter = new MovieShowAdapter(this, romanticmovies, this);


            recyclerView_similarMovies.setAdapter(movieShowAdapter);
            recyclerView_similarMovies.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.HORIZONTAL, false));
            movieShowAdapter.notifyDataSetChanged();
        }

    }

    private void similarmoviesRecycler() {

        uploads = new ArrayList<>();
        sportMovies = new ArrayList<>();
        comedymovies = new ArrayList<>();
        romanticmovies = new ArrayList<>();
        advanturemovies = new ArrayList<>();
        actionsmovies = new ArrayList<>();
        mDatabasereference = FirebaseDatabase.getInstance().getReference("videos");

        mDatabasereference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }

                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                    GetVideoDetails upload = postsnapshot.getValue(GetVideoDetails.class);
                    if (upload.getVideo_category().equals("Action")) {
                        actionsmovies.add(upload);
                    }
                    if (upload.getVideo_category().equals("Sports")) {
                        sportMovies.add(upload);
                    }
                    if (upload.getVideo_category().equals("Adventure")) {
                        advanturemovies.add(upload);
                    }
                    if (upload.getVideo_category().equals("Comedy")) {
                        comedymovies.add(upload);
                    }
                    if (upload.getVideo_category().equals("Romantics")) {
                        romanticmovies.add(upload);
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }

    private void inView() {
        play_fab = findViewById(R.id.play_fab);
        tv_title = findViewById(R.id.detail_movie_title);
        tv_description = findViewById(R.id.detail_movie_desc);
        MoviesThumbNail = findViewById(R.id.details_movies_img);
        MoviesCoverImg = findViewById(R.id.detail_movies_cover);
        recyclerView_similarMovies = findViewById(R.id.recyler_similar_movies);
        String moviesTitle = getIntent().getExtras().getString("title");
        String imgRecoresId = getIntent().getExtras().getString("imgURL");
        String imageCover = getIntent().getExtras().getString("imgCover");
        String movieDetailstext = getIntent().getExtras().getString("movieDetails");
        String moviesUrl = getIntent().getExtras().getString("movieUrl");
        String moviesCategory = getIntent().getExtras().getString("movieCategory");


        current_video_url = moviesUrl;
        current_video_category = moviesCategory;
        Glide.with(this).load(imgRecoresId).into(MoviesThumbNail);
        Glide.with(this).load(imageCover).into(MoviesCoverImg);
        tv_title.setText(moviesTitle);
        tv_description.setText(movieDetailstext);
        getSupportActionBar().setTitle(moviesTitle);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMovieClick(GetVideoDetails movie, ImageView imageView) {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
        tv_title.setText(movie.getVideo_name());
        getSupportActionBar().setTitle(movie.getVideo_name());
        Glide.with(this).load(movie.getVideo_thumb()).into(MoviesThumbNail);
        Glide.with(this).load(movie.getVideo_thumb()).into(MoviesCoverImg);
        tv_description.setText(movie.getVideo_description());
        current_video_url = movie.getVideo_url();
        current_video_category = movie.getVideo_category();
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MovieDetailsActivity.this,
                imageView, "sharedName");
        options.toBundle();




    }
    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onRewardedVideoAdClosed() {

        // Load the next rewarded video ad.
        loadRewardedVideoAd();

    }

    @Override
    public void onRewarded(RewardItem reward) {
        Toast.makeText(this, "onRewarded! currency: " + reward.getType() + "  amount: " +
                reward.getAmount(), Toast.LENGTH_SHORT).show();
        // Reward the user.

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onRewardedVideoCompleted() {
        Toast.makeText(this, "onRewardedVideoCompleted", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onResume() {
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }
}