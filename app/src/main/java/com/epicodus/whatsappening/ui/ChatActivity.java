package com.epicodus.whatsappening.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.epicodus.whatsappening.Constants;
import com.epicodus.whatsappening.R;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity{
    @Bind(R.id.messageRecyclerView) RecyclerView mMessageRecyclerView;
    @Bind(R.id.messageEditText)
    EditText mMessageEditText;
    private SharedPreferences mSharedPreferences;
    private Friend currentFriend;

    private Firebase mMessagesRef;
    private String currentUserId;
    private MessageListAdapter mAdapter;
    private ArrayList<Message> messages = new ArrayList<>();
    private Firebase mFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        messages.clear();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mMessagesRef = new Firebase(Constants.FIREBASE_URL_MESSAGES);
        Intent intent = getIntent();
        currentFriend = Parcels.unwrap(intent.getParcelableExtra("friend"));
        currentUserId = mSharedPreferences.getString(Constants.KEY_UID, null);
        Log.d("Current User: ", currentUserId);
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        mMessageEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String message = mMessageEditText.getText().toString();
                            Message newMessage = new Message(message, currentUserId, currentFriend.getUid(), new Date());
                            Firebase messageFirebaseRef = new Firebase(Constants.FIREBASE_URL_MESSAGES).push();
                            String pushId = messageFirebaseRef.getKey();
                            newMessage.setId(pushId);
                            messageFirebaseRef.setValue(newMessage);
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mMessageEditText.getWindowToken(), 0);
                            mMessageEditText.setText("");
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        setUpFirebaseQuery();
    }

    private void setUpFirebaseQuery() {
        mMessagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();

                Map got = (Map) dataSnapshot.getValue();
                if(got != null) {

                    List gotObjectList = new ArrayList<>(got.values());
                    for(int i = 0; i < gotObjectList.size(); i++) {
                        Log.d("it", "works");
                        Map thisMap = (Map) gotObjectList.get(i);
                        if((thisMap.get("sender").equals(currentFriend.getUid()) && thisMap.get("getter").equals(currentUserId)) || (thisMap.get("getter").equals(currentFriend.getUid()) && thisMap.get("sender").equals(currentUserId))) {
                            String body = thisMap.get("body").toString();
                            String sender = thisMap.get("sender").toString();
                            String getter = thisMap.get("getter").toString();
                            Long dateLong = (Long) thisMap.get("dateCreated");
                            Date dateCreated = new Date(dateLong * 1000);
                            Log.d("sender getter", sender + " " + getter);
                            Message newMessage = new Message(body, sender, getter, dateCreated);
                            String messageId = (String) thisMap.get("id");
                            newMessage.setId(messageId);
                            messages.add(newMessage);
                        }
                    }
                    Collections.sort(messages, new Comparator<Message>() {
                        public int compare(Message m1, Message m2) {
                            return m1.getDate().compareTo(m2.getDate());
                        }
                    });
                    setUpRecyclerView();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    private void setUpRecyclerView() {
        mAdapter = new MessageListAdapter(getApplicationContext(), messages, currentFriend.getName());
        mMessageRecyclerView.setAdapter(mAdapter);
        mMessageRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        mMessageRecyclerView.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void logout() {
        mFirebaseRef.unauth();
        takeUserToLoginScreenOnUnAuth();
    }

    private void takeUserToLoginScreenOnUnAuth() {
        Intent intent = new Intent(ChatActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
