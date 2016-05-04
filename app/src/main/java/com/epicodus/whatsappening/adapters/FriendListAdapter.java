package com.epicodus.whatsappening.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epicodus.whatsappening.R;
import com.epicodus.whatsappening.models.Friend;

import java.util.ArrayList;

/**
 * Created by Guest on 5/4/16.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendViewHolder> {
    private ArrayList<Friend> mFriends = new ArrayList<>();
    private Context mContext;

    public FriendListAdapter(Context context, ArrayList<Friend> friends) {
        mContext = context;
        mFriends = friends;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_list_item, parent, false);
        FriendViewHolder viewHolder = new FriendViewHolder(view, mFriends);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        holder.bindUser(mFriends.get(position));
    }

    @Override
    public int getItemCount() {
        return mFriends.size();
    }

}
