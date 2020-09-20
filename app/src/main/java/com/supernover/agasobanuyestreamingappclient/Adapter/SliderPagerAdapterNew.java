package com.supernover.agasobanuyestreamingappclient.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.supernover.agasobanuyestreamingappclient.Model.SliderSide;
import com.supernover.agasobanuyestreamingappclient.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class SliderPagerAdapterNew extends PagerAdapter {


    private Context mContex;
    List <SliderSide> mList;
    FloatingActionButton play_fab;


    public SliderPagerAdapterNew(Context mContex, List<SliderSide> mList) {
        this.mContex = mContex;
        this.mList = mList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContex.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideLayout = inflater.inflate(R.layout.slide_item, null);
        ImageView slideimage = slideLayout.findViewById(R.id.slider_img);
        TextView slidetitle = slideLayout.findViewById(R.id.slider_title);


        FloatingActionButton floatingActionButton = slideLayout.findViewById(R.id.floatingActionButton);
        Glide.with(mContex).load(mList.get(position).getVideo_thumb()).into(slideimage);

        slidetitle.setText(mList.get(position).getVideo_name()+"\n"+mList.get(position).getVideo_description());


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // we will do something here
                Snackbar.make(view, "Yihitemo Hasi", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();




            }
        });
        container.addView(slideLayout);
        return slideLayout;

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
