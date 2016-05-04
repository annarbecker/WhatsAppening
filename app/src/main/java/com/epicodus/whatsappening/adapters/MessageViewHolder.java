package com.epicodus.whatsappening.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
    private Context mContext;
    private ArrayList<Message> mMessages = new ArrayList<>();

    public MessageViewHolder(View itemView, ArrayList<Message> messages) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mMessages = messages;

    }

    public void bindMessage(Message message) {

        mMessageTextView.setText(message.getBody());
    }
}