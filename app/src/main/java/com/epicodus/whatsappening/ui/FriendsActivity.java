package com.epicodus.whatsappening.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
    private Firebase mFirebaseRef;

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ButterKnife.bind(this);

        mFirebaseFriendsRef = new Firebase(Constants.FIREBASE_URL_FRIENDS);
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

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
        Intent intent = new Intent(FriendsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
