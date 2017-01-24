package com.hadrami.oumar.foodsta;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {


    DatabaseReference mCooksDatabase;
    RecyclerView cooksRecyclerView;
    FirebaseAuth mAuth;
    String ccokId;

    AutoScrollViewPager viewPager;
    OfferPager offerPager;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = (AutoScrollViewPager)rootView.findViewById(R.id.offers_viewpager);
        TabLayout tabLayout = (TabLayout)rootView.findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);
        offerPager = new OfferPager(getActivity());
        viewPager.setAdapter(offerPager);

        mCooksDatabase= FirebaseDatabase.getInstance().getReference().child("Cooks");
        mAuth= FirebaseAuth.getInstance();
        ccokId = mAuth.getCurrentUser().getUid();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        cooksRecyclerView = (RecyclerView)rootView.findViewById(R.id.cook_recyclerview);
        cooksRecyclerView.setHasFixedSize(false);
        cooksRecyclerView.setLayoutManager(mLayoutManager);



        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        viewPager.stopAutoScroll();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewPager.startAutoScroll();
        viewPager.setInterval(2500);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("Bro","3");
        FirebaseRecyclerAdapter<Cook,ItemViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Cook, ItemViewHolder>(
                Cook.class,
                R.layout.cook_card,
                ItemViewHolder.class,
                mCooksDatabase

        ) {
            @Override
            protected void populateViewHolder(ItemViewHolder viewHolder, Cook model, int position) {
                final String cookKey = getRef(position).getKey();
                Log.i("Bro","4");
                viewHolder.setNamee(model.getFullNameee());
                viewHolder.setDescriptionn(model.getCuisineee());
                viewHolder.setPricee(model.getRatingee());
                if(model.getImageee()!=null)
                    viewHolder.setImage(getActivity(),model.getImageee());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent cookIntent = new Intent(getActivity(),CookPageActivity.class);
                        cookIntent.putExtra("cookKey",cookKey);
                        startActivity(cookIntent);

                    }
                });
            }
        };
        cooksRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder
    {
        ;
        View mView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mView =itemView;
        }



        public void setNamee(String Name){
            TextView itemName = (TextView)mView.findViewById(R.id.cook_nameee);
            itemName.setText(Name);
        }

        public void setDescriptionn(String Description)
        {
            TextView itemDescription =(TextView)mView.findViewById(R.id.cook_addressee);
            itemDescription.setText(Description);
        }

        public void setPricee(String Price)
        {
            TextView itemPrice =(TextView)mView.findViewById(R.id.cook_ratingee);
            itemPrice.setText(Price);
        }

        public void setImage(Context context , String Image)
        {
            ImageView itemImage =(ImageView)mView.findViewById(R.id.cook_imageee);
            Picasso.with(context).load(Image).into(itemImage);


        }
    }




}
