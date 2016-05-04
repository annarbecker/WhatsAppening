package com.epicodus.whatsappening.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.epicodus.whatsappening.Constants;
import com.epicodus.whatsappening.R;
import com.epicodus.whatsappening.adapters.FirebaseMessageListAdapter;
import com.epicodus.whatsappening.adapters.MessageListAdapter;
import com.epicodus.whatsappening.models.Friend;
import com.epicodus.whatsappening.models.Message;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity{
    @Bind(R.id.messageRecyclerView) RecyclerView mMessageRecyclerView;
    @Bind(R.id.messageEditText)
    EditText mMessageEditText;
    private SharedPreferences mSharedPreferences;
    private Friend mSender;
    private Firebase mMessagesRef;
    private String getterId;
    private Query currentUserRef;
    private Query friendRef;
    private MessageListAdapter mAdapter;
    private ArrayList<Map> gotList = new ArrayList<>();
    private ArrayList<Map> sentList = new ArrayList<>();
    private ArrayList<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mMessagesRef = new Firebase(Constants.FIREBASE_URL_MESSAGES);
        Intent intent = getIntent();
        mSender = Parcels.unwrap(intent.getParcelableExtra("friend"));
        getterId = mSharedPreferences.getString(Constants.KEY_UID, null);
        mMessageEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String message = mMessageEditText.getText().toString();
                            Message newMessage = new Message(message, getterId, mSender.getUid());
                            Firebase messageFirebaseRef = new Firebase(Constants.FIREBASE_URL_MESSAGES).push();
                            String pushId = messageFirebaseRef.getKey();
                            newMessage.setId(pushId);
                            messageFirebaseRef.setValue(newMessage);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        setUpFirebaseQuery();
        setUpRecyclerView();
    }

    private void setUpFirebaseQuery() {
        currentUserRef = mMessagesRef.orderByChild("getter").equalTo(getterId);
        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();

                Map got = (Map) dataSnapshot.getValue();
                if(got != null) {

                    List gotObjectList = new ArrayList<>(got.values());
                    for(int i = 0; i < gotObjectList.size(); i++) {
                        Log.d("it", "works");
                        Map thisMap = (Map) gotObjectList.get(i);
                        if(thisMap.get("sender").equals(mSender.getUid())) {
                            String body = thisMap.get("body").toString();
                            String sender = thisMap.get("sender").toString();
                            String getter = thisMap.get("getter").toString();
                            Log.d("sender getter", sender + " " + getter);
                            Message newMessage = new Message(body, sender, getter);
                            messages.add(newMessage);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        friendRef = mMessagesRef.orderByChild("sender").equalTo(getterId);
        friendRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map got = (Map) dataSnapshot.getValue();
                if(got != null) {

                    List gotObjectList = new ArrayList<>(got.values());
                    for(int i = 0; i < gotObjectList.size(); i++) {
                        Log.d("it", "works");
                        Map thisMap = (Map) gotObjectList.get(i);
                        if(thisMap.get("getter").equals(mSender.getUid())) {
                            String body = thisMap.get("body").toString();
                            String sender = thisMap.get("sender").toString();
                            String getter = thisMap.get("getter").toString();
                            Log.d("sender getter", sender + " " + getter);
                            Message newMessage = new Message(body, sender, getter);
                            messages.add(newMessage);
                        }
                    }
                    Collections.sort(messages, new Comparator<Message>() {
                        public int compare(Message m1, Message m2) {
                            return m1.getDate().compareTo(m2.getDate());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void setUpRecyclerView() {
        mAdapter = new MessageListAdapter(this, messages);
        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMessageRecyclerView.setAdapter(mAdapter);
    }
}
