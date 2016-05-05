package com.epicodus.whatsappening.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epicodus.whatsappening.Constants;
import com.epicodus.whatsappening.R;
import com.epicodus.whatsappening.models.Friend;
import com.epicodus.whatsappening.models.Message;

import java.util.ArrayList;

/**
 * Created by Guest on 5/4/16.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageViewHolder> {
    private ArrayList<Message> mMessages = new ArrayList<>();
    private Context mContext;
    private String mCurrentFriend;

    public MessageListAdapter(Context context, ArrayList<Message> messages, String currentFriend) {
        mContext = context;
        mMessages = messages;
        mCurrentFriend = currentFriend;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_item, parent, false);
        MessageViewHolder viewHolder = new MessageViewHolder(view, mMessages, mCurrentFriend);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.bindMessage(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

}