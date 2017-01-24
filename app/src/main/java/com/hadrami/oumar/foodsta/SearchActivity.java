package com.hadrami.oumar.foodsta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SearchActivity extends AppCompatActivity {

    EditText searchText;
    ImageButton clearTextImageButton;
    private DatabaseReference mItemsDatabse;
    private DatabaseReference mSuggestionsDatabase;
    private RecyclerView mMenu;
    ProgressBar progressBar;
    CardView NoResultsCardView;
    int flag = 0;
    InputMethodManager inputManager;

    @Override
    protected void onStart() {
        super.onStart();

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);



        mItemsDatabse= FirebaseDatabase.getInstance().getReference().child("Items");
        mSuggestionsDatabase = FirebaseDatabase.getInstance().getReference().child("Suggesstions").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mMenu = (RecyclerView)findViewById(R.id.menu_list);
        mMenu.setHasFixedSize(true);
        mMenu.setLayoutManager(mLayoutManager);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        searchText = (EditText) findViewById(R.id.search_text);
        clearTextImageButton = (ImageButton) findViewById(R.id.clear_text);
        NoResultsCardView = (CardView)findViewById(R.id.no_results) ;
        clearTextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
            }
        });

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    //code comes here

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);





                }
                return false;
            }
        });

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                showSuggestions(s);

            }
        });


    }


    private void showSuggestions(Editable s) {
        Log.i("Bro", "EVERYTHING NEW");
        final String textEntered = s.toString();

        mSuggestionsDatabase.setValue(null);

        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        new CountDownTimer(2000, 1000) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                progressBar.setVisibility(View.GONE);
            }

        }.start();


        mItemsDatabse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("Bro", "1");

                for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                    String Name = (String) suggestionSnapshot.child("Name").getValue();
                    String CookName = (String) suggestionSnapshot.child("CookName").getValue();
                    String Cuisine = (String) suggestionSnapshot.child("Cuisine").getValue();
                    String UID = (String) suggestionSnapshot.child("UID").getValue();
                    String Rating = (String) suggestionSnapshot.child("Rating").getValue();

                    if (!textEntered.toLowerCase().equals("")) {
                        if (Name.toLowerCase().contains(textEntered.toLowerCase()) || CookName.toLowerCase().contains(textEntered.toLowerCase())
                                || Cuisine.toLowerCase().contains(textEntered.toLowerCase())) {

                            mSuggestionsDatabase.child(UID).child("FullName").setValue(CookName);
                            mSuggestionsDatabase.child(UID).child("Cuisine").setValue(Cuisine);
                            mSuggestionsDatabase.child(UID).child("Rating").setValue(Rating);
                        }



                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerAdapter<Cooks,ItemViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Cooks, ItemViewHolder>(
                Cooks.class,
                R.layout.suggestion_card,
                ItemViewHolder.class,
                mSuggestionsDatabase
        ) {
            @Override
            protected void populateViewHolder(final ItemViewHolder viewHolder, Cooks model, final int position) {

                final String cookKey = getRef(position).getKey();
                Log.i("Bro","4");
                viewHolder.setNamee(model.getFullNameee());
                viewHolder.setDescriptionn(model.getCuisineee());
                viewHolder.setPricee(model.getRatingee());
                if(model.getImageee()!=null)
                viewHolder.setImage(getApplicationContext(),model.getImageee());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent cookIntent = new Intent(SearchActivity.this,CookPageActivity.class);
                        cookIntent.putExtra("cookKey",cookKey);
                        startActivity(cookIntent);

                    }
                });






            }
        };

        mMenu.setAdapter(firebaseRecyclerAdapter);
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
            TextView itemName = (TextView)mView.findViewById(R.id.cook_name);
            itemName.setText(Name);
        }

        public void setDescriptionn(String Description)
        {
            TextView itemDescription =(TextView)mView.findViewById(R.id.cook_address);
            itemDescription.setText(Description);
        }

        public void setPricee(String Price)
        {
            TextView itemPrice =(TextView)mView.findViewById(R.id.cook_rating);
            itemPrice.setText(Price);
        }

        public void setImage(Context context ,String Image)
        {
            ImageView itemImage =(ImageView)mView.findViewById(R.id.cook_image);
            Picasso.with(context).load(Image).into(itemImage);


        }
    }


}
