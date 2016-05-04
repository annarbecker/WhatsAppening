package com.epicodus.whatsappening.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.epicodus.whatsappening.Constants;
import com.epicodus.whatsappening.R;
import com.epicodus.whatsappening.adapters.FirebaseFriendListAdapter;
import com.epicodus.whatsappening.models.Friend;
import com.firebase.client.Firebase;
import com.firebase.client.Query;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FriendsActivity extends AppCompatActivity {
    private Query mQuery;
    private Firebase mFirebaseFriendsRef;
    private FirebaseFriendListAdapter mAdapter;

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ButterKnife.bind(this);

        mFirebaseFriendsRef = new Firebase(Constants.FIREBASE_URL_FRIENDS);

        setUpFirebaseQuery();
        setUpRecyclerView();
    }

    private void setUpFirebaseQuery() {
        String friend = mFirebaseFriendsRef.toString();
        mQuery = new Firebase(friend);
    }

    private void setUpRecyclerView() {
        mAdapter = new FirebaseFriendListAdapter(mQuery, Friend.class);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }
}
