package com.codingo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.codingo.apiintegration.ApiManager;
import com.codingo.apiintegration.Configuration;
import com.codingo.bd.R;
import com.codingo.firebase.FirebaseManager;
import com.codingo.model.User;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private LinearLayout signInWithGmailLL;
    private LinearLayout signInWithGmailPasswordLL;
    private EditText emailET;
    private EditText passwordET;
    private static final int REQ_ONE_TAP=1111;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private TextView donthaveaccountTV;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInWithGmailLL = findViewById(R.id.signInWithGmailLL);
        donthaveaccountTV = findViewById(R.id.donthaveaccountTV);
        signInWithGmailPasswordLL = findViewById(R.id.signInWithGmailPasswordLL);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        openPage2();
        signInWithGmailLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        donthaveaccountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent openstartingpoint = new Intent(LoginActivity.this,SignupActivity.class);
                openstartingpoint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(openstartingpoint);
                finish();
            }
        });
        signInWithGmailPasswordLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailET.getText().toString().equals("")) {
                    emailET.setError("Field can't be empty");
                    return;
                }
                if (passwordET.getText().toString().equals("")) {
                    passwordET.setError("Field can't be empty");
                    return;
                }
                signInPassword();
            }
        });

    }

    private void signInPassword() {
        new FirebaseManager(this).getUserDetails(emailET.getText().toString(), passwordET.getText().toString(), new FirebaseManager.RetriveUserListener() {
            @Override
            public void getUser(User user) {
                if(!user.getId().equals("")){
                    com.codingo.MainActivity.LOGGED_IN_USER=user;
                    MainActivity.myEdit = MainActivity.sharedPreferences.edit();
                    MainActivity.myEdit.putString("user", new Gson().toJson(user,User.class));
                    MainActivity.myEdit.apply();
                    if(MainActivity.PUBLIC_JSON_OBJECT==null) {
                        new ApiManager().getRequestRetrofit(LoginActivity.this, Configuration.JSON, new HashMap<>(), false, Configuration.GET_REQUEST, new ApiManager.JsonObjectCallBack() {
                            @Override
                            public void getJsonObject(int ResponseCode, JSONObject jsonObject) {
                                MainActivity.PUBLIC_JSON_OBJECT = jsonObject;
                                Intent openstartingpoint = new Intent(LoginActivity.this, com.codingo.MainActivity.class);
                                startActivity(openstartingpoint);
                                finish();

                            }
                        });
                    }else {
                        Intent openstartingpoint = new Intent(LoginActivity.this, com.codingo.MainActivity.class);
                        startActivity(openstartingpoint);
                        finish();
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                }

            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            User user=new User();
                            user.setName(firebaseUser.getDisplayName());
                            user.setEmail(firebaseUser.getEmail());
                            String image="https://lh3.googleusercontent.com"+firebaseUser.getPhotoUrl().getPath();
                            user.setPhoto(image);

                            new FirebaseManager(LoginActivity.this).hasUser(mAuth.getCurrentUser().getEmail(), new FirebaseManager.HasUserListener() {
                                        @Override
                                        public void hasUser(boolean flag) {
                                            if(!flag){
                                                try {
                                                    mAuth.signOut();
                                                    mGoogleSignInClient.signOut();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                new FirebaseManager(LoginActivity.this).addDatabase(user, new FirebaseManager.SuccessListener() {
                                                    @Override
                                                    public void getsuccess() {
                                                        com.codingo.MainActivity.LOGGED_IN_USER=user;
                                                        MainActivity.myEdit = MainActivity.sharedPreferences.edit();
                                                        MainActivity.myEdit.putString("user", new Gson().toJson(user,User.class));
                                                        MainActivity.myEdit.apply();
                                                        if(MainActivity.PUBLIC_JSON_OBJECT==null) {
                                                            new ApiManager().getRequestRetrofit(LoginActivity.this, Configuration.JSON, new HashMap<>(), false, Configuration.GET_REQUEST, new ApiManager.JsonObjectCallBack() {
                                                                @Override
                                                                public void getJsonObject(int ResponseCode, JSONObject jsonObject) {
                                                                    MainActivity.PUBLIC_JSON_OBJECT = jsonObject;
                                                                    Intent openstartingpoint = new Intent(LoginActivity.this, com.codingo.MainActivity.class);
                                                                    startActivity(openstartingpoint);
                                                                    finish();

                                                                }
                                                            });
                                                        }else {
                                                            Intent openstartingpoint = new Intent(LoginActivity.this, com.codingo.MainActivity.class);
                                                            startActivity(openstartingpoint);
                                                            finish();
                                                        }

                                                    }

                                                    @Override
                                                    public void getFail() {
                                                        try {
                                                            mAuth.signOut();
                                                            mGoogleSignInClient.signOut();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }else {
                                                try {
                                                    mAuth.signOut();
                                                    mGoogleSignInClient.signOut();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                com.codingo.MainActivity.LOGGED_IN_USER=user;
                                                MainActivity.myEdit = MainActivity.sharedPreferences.edit();
                                                MainActivity.myEdit.putString("user", new Gson().toJson(user,User.class));
                                                MainActivity.myEdit.apply();
                                                if(MainActivity.PUBLIC_JSON_OBJECT==null) {
                                                    new ApiManager().getRequestRetrofit(LoginActivity.this, Configuration.JSON, new HashMap<>(), false, Configuration.GET_REQUEST, new ApiManager.JsonObjectCallBack() {
                                                        @Override
                                                        public void getJsonObject(int ResponseCode, JSONObject jsonObject) {
                                                            MainActivity.PUBLIC_JSON_OBJECT = jsonObject;
                                                            Intent openstartingpoint = new Intent(LoginActivity.this, com.codingo.MainActivity.class);
                                                            startActivity(openstartingpoint);
                                                            finish();

                                                        }
                                                    });
                                                }else {
                                                    Intent openstartingpoint = new Intent(LoginActivity.this, com.codingo.MainActivity.class);
                                                    startActivity(openstartingpoint);
                                                    finish();
                                                }
                                            }

                                        }
                                    });




                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }
    private void openPage() {
        //  oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id2))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void openPage2() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id2))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

    }
}