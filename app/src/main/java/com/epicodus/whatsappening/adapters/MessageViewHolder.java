package com.epicodus.whatsappening.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.epicodus.whatsappening.Constants;
import com.epicodus.whatsappening.R;
import com.epicodus.whatsappening.models.Message;
import com.epicodus.whatsappening.ui.ChatActivity;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 5/4/16.
 */
public class MessageViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.messageTextView) TextView mMessageTextView;
    @Bind(R.id.messageTimeTextView) TextView mMessageTimeTextView;
    @Bind(R.id.friendNameTextView) TextView mFriendNameTextView;

    private Context mContext;
    private ArrayList<Message> mMessages = new ArrayList<>();
    private SharedPreferences mSharedPreferences;
    private String currentUserId;
    private String mCurrentFriend;
    private String userName;

    public MessageViewHolder(View itemView, ArrayList<Message> messages, String currentFriend) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mMessages = messages;
        mCurrentFriend = currentFriend;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        currentUserId = mSharedPreferences.getString(Constants.KEY_UID, null);
        userName = mSharedPreferences.getString("name", null);
    }

    public void bindMessage(Message message) {

        mMessageTextView.setText(message.getBody());
        mMessageTimeTextView.setText(message.getDate().toString());
        mFriendNameTextView.setText(mCurrentFriend);
        Log.d("UID", currentUserId);
//        if(currentUserId.equals(message.getSender())) {
//            Log.d("fire", message.getSender() + "    " + currentUserId);
//            mMessageTextView.setGravity(Gravity.RIGHT);
//            mMessageTimeTextView.setGravity(Gravity.RIGHT);
//            mFriendNameTextView.setText(userName);
//            mFriendNameTextView.setGravity(Gravity.RIGHT);
//        }
    }

    public void bindMessageSender(Message message) {
        Log.d("SENDER", message.getSender());
        Log.d("GETTER", message.getGetter());

        mMessageTextView.setText(message.getBody());
        mMessageTimeTextView.setText(message.getDate().toString());
        mFriendNameTextView.setText(mCurrentFriend);
        mMessageTextView.setGravity(Gravity.RIGHT);
        mMessageTimeTextView.setGravity(Gravity.RIGHT);
        mFriendNameTextView.setText(userName);
        mFriendNameTextView.setGravity(Gravity.RIGHT);
    }

}