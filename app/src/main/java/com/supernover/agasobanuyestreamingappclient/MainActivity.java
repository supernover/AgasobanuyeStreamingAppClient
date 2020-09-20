package com.supernover.agasobanuyestreamingappclient;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.supernover.agasobanuyestreamingappclient.Adapter.MovieShowAdapter;
import com.supernover.agasobanuyestreamingappclient.Adapter.SliderPagerAdapterNew;
import com.supernover.agasobanuyestreamingappclient.Model.GetVideoDetails;
import com.supernover.agasobanuyestreamingappclient.Model.MovieItemClickListenerNew;
import com.supernover.agasobanuyestreamingappclient.Model.SliderSide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements MovieItemClickListenerNew {


    MovieShowAdapter movieShowAdapter;
    DatabaseReference mDatabaseReference;
    private List<GetVideoDetails> uploads, uploadslistLatests, uploadsListPopular;
    private List<GetVideoDetails> actionsmovies, sportsmovies,
            comedymovies, romanticmovies ,advanturemovies;

    private ViewPager sliderPager;
    private List<SliderSide> uploadsSlider;
    private TabLayout indicator, tabmoviesactions;
    private RecyclerView MoviesRv, moviesRvWeek,tab;
    ProgressDialog progressDialog;




    private static final String TAG = "MainActivity";

    private AdView mAdView;
    InterstitialAd interstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Didier , Database birahura!");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        sliderPager = findViewById(R.id.slider_pager);
        indicator = findViewById(R.id.indicator);



        interstitialAd = new InterstitialAd(MainActivity.this);

        interstitialAd.setAdUnitId("ca-app-pub-3063877521249388~8852848151");

        interstitialAd.loadAd(new AdRequest.Builder().build());




        progressDialog = new ProgressDialog(this);


        MobileAds.initialize(this, "ca-app-pub-3063877521249388~8852848151");
        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        mAdView.loadAd(request);


        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });




        inViews();
        addAllMovies();
        iniPopularMovies();
        iniWeekMovies();
        moviesViewTab();
        SliderPagerAdapterNew adapterNew = new SliderPagerAdapterNew(this,uploadsSlider);
        sliderPager.setAdapter(adapterNew);

        indicator.setupWithViewPager(sliderPager,true);


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
               // Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
               // Log.w(TAG, "Failed to read value.", error.toException());
            }




        });







    }

    private void addAllMovies(){

        uploads = new ArrayList<>();
        uploadslistLatests = new ArrayList<>();
        uploadsListPopular = new ArrayList<>();
        actionsmovies = new ArrayList<>();
        advanturemovies = new ArrayList<>();
        comedymovies = new ArrayList<>();
        sportsmovies = new ArrayList<>();
        romanticmovies = new ArrayList<>();
        uploadsSlider = new ArrayList<>();

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("videos");
        progressDialog.setMessage("loading...");
        progressDialog.show();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){

                    GetVideoDetails upload = postSnapshot.getValue(GetVideoDetails.class);
                    SliderSide slide = postSnapshot.getValue(SliderSide.class);
                    if (upload.getVideo_type().equals("Latest Movies")){
                        uploadslistLatests.add(upload);
                    }
                    if (upload.getVideo_type().equals("Best popular movies")){
                        uploadsListPopular.add(upload);
                    }
                    if (upload.getVideo_category().equals("Action")){
                        actionsmovies.add(upload);
                    }
                    if (upload.getVideo_category().equals("Adventure")){
                        advanturemovies.add(upload);
                    }
                    if (upload.getVideo_category().equals("Comedy")){
                        comedymovies.add(upload);
                    }
                    if (upload.getVideo_category().equals("Romantics")){
                        romanticmovies.add(upload);
                    }
                    if (upload.getVideo_category().equals("Sports")){
                        sportsmovies.add(upload);
                    }
                    if (upload.getVideo_slide().equals("Slide movies")){
                        uploadsSlider.add(slide);
                    }
                    uploads.add(upload);
                }
                iniSlider();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void iniSlider() {
        SliderPagerAdapterNew adapterNew = new SliderPagerAdapterNew(this, uploadsSlider);
        sliderPager.setAdapter(adapterNew);
        adapterNew.notifyDataSetChanged();
        //setup timer

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(),4000,  6000);
        indicator.setupWithViewPager(sliderPager,true);




    }

    private void iniWeekMovies(){

        movieShowAdapter = new MovieShowAdapter(this, uploadslistLatests,this);
        moviesRvWeek.setAdapter(movieShowAdapter);
        moviesRvWeek.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false));
        movieShowAdapter.notifyDataSetChanged();

    }


    private void iniPopularMovies(){

        movieShowAdapter = new MovieShowAdapter(this, uploadsListPopular,this);
        MoviesRv.setAdapter(movieShowAdapter);
        MoviesRv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false));
        movieShowAdapter.notifyDataSetChanged();

    }



    private void moviesViewTab(){

        getActionsMovies();
        tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Action"));
        tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Adventure"));
        tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Comedy"));
        tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Romantics"));
        tabmoviesactions.addTab(tabmoviesactions.newTab().setText("Series"));
        tabmoviesactions.setTabTextColors(ColorStateList.valueOf(Color.WHITE));
        tabmoviesactions.setTabGravity(TabLayout.GRAVITY_FILL);

        tabmoviesactions.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()){
                    case 0:
                        getActionsMovies();
                        break;
                    case 1:
                        getAdvantureMovies();
                        break;
                    case 2:
                        getComedyMovies();
                        break;
                    case 3:
                        getRomanticMovies();
                        break;
                    case 4:
                        getSportsMovies();
                        break;
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    FirebaseRecyclerOptions<GetVideoDetails> movieoptions =
            new FirebaseRecyclerOptions.Builder<GetVideoDetails>()
                    .setQuery(FirebaseDatabase.getInstance()
                            .getReference("videos"), GetVideoDetails.class)
                    .build();

    private void inViews(){
        tabmoviesactions = findViewById(R.id.tabActionMovies);
        sliderPager = findViewById(R.id.slider_pager);
        indicator = findViewById(R.id.indicator);
        moviesRvWeek = findViewById(R.id.rv_movies_week);
        MoviesRv = findViewById(R.id.Rv_movies);
        tab = findViewById(R.id.tabrecycler);

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMovieClick(GetVideoDetails movie, ImageView imageView) {

        Intent intent = new Intent(this,MovieDetailsActivity.class);
        intent.putExtra("title",movie.getVideo_name());
        intent.putExtra("imgURL",movie.getVideo_thumb());
        intent.putExtra("imgCover",movie.getVideo_thumb());
        intent.putExtra("movieUrl",movie.getVideo_url());
        intent.putExtra("movieCategory",movie.getVideo_category());
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                imageView,"sharedName");
        startActivity(intent,options.toBundle());

        interstitialAd.setAdListener(new AdListener(){

        @Override
        public void onAdClosed() {
        super.onAdClosed();
        startActivity(intent);
        interstitialAd.loadAd(new AdRequest.Builder().build());

        }
        });



         if (interstitialAd.isLoaded()){

         interstitialAd.show();
         }

         else{
         startActivity(intent);
         }









    }

    public class SliderTimer extends TimerTask {
        public void run(){
           MainActivity.this.runOnUiThread(new Runnable() {
               @Override
               public void run() {

                   if (sliderPager.getCurrentItem()<uploadsSlider.size()-1){
                       sliderPager.setCurrentItem(sliderPager.getCurrentItem()+1);

                   }else {
                       sliderPager.setCurrentItem(0);
                   }



               }
           });
        }
    }

    FirebaseRecyclerOptions<GetVideoDetails> options =
            new FirebaseRecyclerOptions.Builder<GetVideoDetails>()
                    .setQuery(FirebaseDatabase.getInstance()
                            .getReference("videos"), GetVideoDetails.class)
                    .build();









    private void getActionsMovies(){

        movieShowAdapter = new MovieShowAdapter(this, actionsmovies,this);
        tab.setAdapter(movieShowAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false));
        movieShowAdapter.notifyDataSetChanged();

    }
    private void getSportsMovies(){

        movieShowAdapter = new MovieShowAdapter(this, sportsmovies,this);
        tab.setAdapter(movieShowAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false));
        movieShowAdapter.notifyDataSetChanged();

    }
    private void getRomanticMovies(){

        movieShowAdapter = new MovieShowAdapter(this, romanticmovies,this);
        tab.setAdapter(movieShowAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false));
        movieShowAdapter.notifyDataSetChanged();

    }

    private void getComedyMovies(){

        movieShowAdapter = new MovieShowAdapter(this, comedymovies,this);
        tab.setAdapter(movieShowAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false));
        movieShowAdapter.notifyDataSetChanged();

    }

    private void getAdvantureMovies(){

        movieShowAdapter = new MovieShowAdapter(this, advanturemovies,this);
        tab.setAdapter(movieShowAdapter);
        tab.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false));
        movieShowAdapter.notifyDataSetChanged();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.searchmenu:
                startActivity(new Intent(MainActivity.this,Search.class));
                break;


            case R.id.requestmenu:

                startActivity(new Intent(MainActivity.this,RequestContent.class));
                break;

        }

        return true;
    }

}