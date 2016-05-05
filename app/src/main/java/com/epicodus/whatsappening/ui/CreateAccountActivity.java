package com.epicodus.whatsappening.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.epicodus.whatsappening.Constants;
import com.epicodus.whatsappening.R;
import com.epicodus.whatsappening.models.Friend;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = CreateAccountActivity.class.getSimpleName();
    @Bind(R.id.signAppButton)
    Button mSignAppButton;
    @Bind(R.id.nameEditText) EditText mNameEditText;
    @Bind(R.id.signAppEmailEditText) EditText mEmailEditText;
    @Bind(R.id.signAppPasswordEditText) EditText mPasswordEditText;
    @Bind(R.id.confirmPasswordEditText)
    EditText mConfirmPasswordEditText;
    @Bind(R.id.imageView)
    ImageView mImageView;
    private Firebase mFirebaseRef;
    private String uid;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferencesEditor;
    Uri file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);

        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);
        mSignAppButton.setOnClickListener(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(CreateAccountActivity.this);
        mSharedPreferencesEditor = mSharedPreferences.edit();

        mConfirmPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            createNewUser();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == (R.id.signAppButton)) {
            createNewUser();
        }
    }

    public void createNewUser() {
        final String name = mNameEditText.getText().toString();
        final String email = mEmailEditText.getText().toString();
        final String password = mPasswordEditText.getText().toString();
        final String confirmPassword = mConfirmPasswordEditText.getText().toString();

        boolean validEmail = isValidEmail(email);
        boolean validName = isValidName(name);
        boolean validPassword = isValidPassword(password, confirmPassword);
        if (!validEmail || !validName || !validPassword) return;

        mFirebaseRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                loginWithPassword();
                uid = result.get("uid").toString();
                createUserInFirebaseHelper(name, email, uid);
                Intent intent = new Intent(CreateAccountActivity.this, FriendsActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Log.d(TAG, "error occurred " +
                        firebaseError);
            }
        });
    }

    private void createUserInFirebaseHelper(final String name, final String email, final String uid) {
        final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_FRIENDS).child(uid);
        Friend newFriend = new Friend(name, email, uid);
        userLocation.setValue(newFriend);
    }

    private boolean isValidEmail(String email) {
        boolean isGoodEmail =
                (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEmailEditText.setError("Please enter a valid email address");
            return false;
        }
        return isGoodEmail;
    }

    private boolean isValidName(String name) {
        if (name.equals("")) {
            mNameEditText.setError("Please enter your name");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            mPasswordEditText.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            mPasswordEditText.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    public void loginWithPassword() {
        final String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        if (email.equals("")) {
            mEmailEditText.setError("Please enter your email");
        }
        if (password.equals("")) {
            mPasswordEditText.setError("Password cannot be blank");
        }
        mSharedPreferencesEditor.putString(Constants.KEY_USER_EMAIL, email).apply();

        mSharedPreferencesEditor.putString(Constants.KEY_UID, uid).apply();
        Intent intent = new Intent(CreateAccountActivity.this, FriendsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
