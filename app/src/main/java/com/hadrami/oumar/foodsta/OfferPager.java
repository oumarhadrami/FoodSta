package com.hadrami.oumar.foodsta;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by oumar on 08/01/2017.
 */

public class OfferPager extends PagerAdapter {

    private int[] image_resources = {R.drawable.offer_6,R.drawable.offer_2,R.drawable.offer_1,R.drawable.offer_3};
    Context context;
    private LayoutInflater layoutInflater;
    public OfferPager(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return image_resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(FrameLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View OfferView = layoutInflater.inflate(R.layout.offers_pager,null);
        ImageView offerImage = (ImageView)OfferView.findViewById(R.id.offer_image);
        offerImage.setImageResource(image_resources[position]);
        container.addView(OfferView);


        return OfferView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((FrameLayout)object);
    }
}
