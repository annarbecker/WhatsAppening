package com.epicodus.whatsappening.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.epicodus.whatsappening.Constants;
import com.epicodus.whatsappening.R;
import com.epicodus.whatsappening.models.Friend;
import com.epicodus.whatsappening.models.Message;
import com.firebase.client.Firebase;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity{
    @Bind(R.id.messageRecyclerView) RecyclerView mMessageRecyclerView;
    @Bind(R.id.messageEditText)
    EditText mMessageEditText;
    private SharedPreferences mSharedPreferences;
    private Friend mCurrentUser;
    private Friend mGetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent = getIntent();
        mGetter = Parcels.unwrap(intent.getParcelableExtra("friend"));
        Log.d("tag", "");
        mMessageEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            String message = mMessageEditText.getText().toString();
                            String uid = mSharedPreferences.getString(Constants.KEY_UID, null);

                            Message newMessage = new Message(message, uid, mGetter.getUid());
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
    }
}
