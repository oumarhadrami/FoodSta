package com.hadrami.oumar.foodsta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {


    ImageButton messageSend;
    EditText messageText;
    DatabaseReference messagesDatabase;
    DatabaseReference newMessage;

    String currentCustomerUID;
    String itemKey;
    String fullName;
    String isMe;

    LinearLayoutManager mLayoutManager;


    RecyclerView MessageList;


    FirebaseAuth mAuth;


    @Override
    protected void onStart() {
        super.onStart();



        final FirebaseRecyclerAdapter<Chat, MessageViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Chat, MessageViewHolder>(
                Chat.class,
                R.layout.message_card_right,
                MessageViewHolder.class,
                messagesDatabase
        ) {


            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, Chat model, final int position) {
                viewHolder.setTextt(model.getText());
                isMe = model.getUid();
                if (isMe != null && !isMe.equals(currentCustomerUID)) {
                    viewHolder.messageLinearLayout.setGravity(Gravity.START);
                    viewHolder.messageText.setTextColor(getResources().getColor(android.R.color.black));
                    viewHolder.messageCardView.setBackgroundResource(R.drawable.bubble_outt);

                } else {
                    viewHolder.messageLinearLayout.setGravity(Gravity.END);
                    viewHolder.messageText.setTextColor(getResources().getColor(android.R.color.white));
                    viewHolder.messageCardView.setBackgroundResource(R.drawable.bubble_in);
                }

            }


        };
        MessageList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition =
                        mLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    MessageList.scrollToPosition(positionStart);
                }
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mAuth = FirebaseAuth.getInstance();


        messageSend = (ImageButton) findViewById(R.id.message_send);
        messageText = (EditText) findViewById(R.id.message_text);

        MessageList = (RecyclerView) findViewById(R.id.chat_list);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(false);
        MessageList.setHasFixedSize(true);
        MessageList.setLayoutManager(mLayoutManager);

        currentCustomerUID = mAuth.getCurrentUser().getUid();

        //change the database so it's like cooksta !! It's still incomplete
        messagesDatabase = FirebaseDatabase.getInstance().getReference("Orders").child("nv2c6vHosoLDKnnc").child("messages");


        messageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessage(currentCustomerUID);


            }
        });


    }

    private void sendMessage(String currentCustomerUID) {
        final String MessageText = messageText.getText().toString();

        if (!TextUtils.isEmpty(MessageText)) {
            messageSend.setEnabled(true);
            newMessage = messagesDatabase.push();
            newMessage.child("name").setValue(fullName);
            newMessage.child("text").setValue(MessageText);
            newMessage.child("uid").setValue(currentCustomerUID);
            messageText.getText().clear();

        } else {
            messageSend.setEnabled(false);
        }

    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        View mView;
        LinearLayout messageLinearLayout;
        CardView messageCardView;
        TextView messageText;


        public MessageViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            messageLinearLayout = (LinearLayout) mView.findViewById(R.id.chattttt_linearlayout);
            messageText = (TextView) mView.findViewById(R.id.sender_text_right);
            messageCardView = (CardView) mView.findViewById(R.id.carddddd);

        }


        public void setTextt(String Text) {

            messageText.setText(Text);
        }
    }

}

