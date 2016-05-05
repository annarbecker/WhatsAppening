package com.epicodus.whatsappening.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.epicodus.whatsappening.Constants;
import com.epicodus.whatsappening.R;
import com.epicodus.whatsappening.models.Friend;
import com.epicodus.whatsappening.ui.ChatActivity;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Guest on 5/4/16.
 */
public class FriendViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.friendNameTextView) TextView mFriendNameTextView;
    private Context mContext;
    private ArrayList<Friend> mFriends = new ArrayList<>();
    private String currentUserId;

    public FriendViewHolder(View itemView, ArrayList<Friend> friends) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();
        mFriends = friends;
        itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int itemPosition = getLayoutPosition();
                Intent intent = new Intent(mContext, ChatActivity.class);
                Friend friend = mFriends.get(itemPosition);
                intent.putExtra("friend", Parcels.wrap(friend));
                mContext.startActivity(intent);
            }
        });
    }

    public void bindUser(Friend friend) {
        mFriendNameTextView.setText(friend.getName());
    }
}
