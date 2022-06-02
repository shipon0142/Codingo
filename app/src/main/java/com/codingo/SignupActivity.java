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

public class SignupActivity extends AppCompatActivity {
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private LinearLayout signInWithGmailLL;
    private TextView alReadyaccountLL;
    private LinearLayout signupLL;
    private EditText emailET;
    private EditText passwordET;
    private EditText nameET;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signInWithGmailLL = findViewById(R.id.signInWithGmailLL);
        nameET = findViewById(R.id.nameET);
        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        signupLL = findViewById(R.id.signupLL);
        alReadyaccountLL = findViewById(R.id.alReadyaccountLL);
         openPage2();
        signInWithGmailLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        alReadyaccountLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openstartingpoint = new Intent(SignupActivity.this, com.codingo.LoginActivity.class);
                openstartingpoint.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(openstartingpoint);
                finish();
            }
        });
        signupLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameET.getText().toString().equals("")) {
                    nameET.setError("Field can't be empty");
                    return;
                } if (emailET.getText().toString().equals("")) {
                    emailET.setError("Field can't be empty");
                    return;
                }
                if (passwordET.getText().toString().equals("")) {
                    emailET.setError("Field can't be empty");
                    return;
                }
                signUpForEmailPassword();


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
                         new FirebaseManager(SignupActivity.this).hasUser(mAuth.getCurrentUser().getEmail(), new FirebaseManager.HasUserListener() {
                                @Override
                                public void hasUser(boolean flag) {
                                    if(flag){
                                        try {
                                            mAuth.signOut();
                                            mGoogleSignInClient.signOut();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        Toast.makeText(SignupActivity.this, "User already exist", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        User user=new User();
                                        user.setName(firebaseUser.getDisplayName());
                                        user.setEmail(firebaseUser.getEmail());
                                        String image="https://lh3.googleusercontent.com"+firebaseUser.getPhotoUrl().getPath();
                                        user.setPhoto(image);
                                        try {
                                            mAuth.signOut();
                                            mGoogleSignInClient.signOut();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        new FirebaseManager(SignupActivity.this).addDatabase(user, new FirebaseManager.SuccessListener() {
                                            @Override
                                            public void getsuccess() {
                                                com.codingo.MainActivity.LOGGED_IN_USER=user;
                                                MainActivity.myEdit = MainActivity.sharedPreferences.edit();
                                                MainActivity.myEdit.putString("user", new Gson().toJson(user,User.class));
                                                MainActivity.myEdit.apply();
                                                if(MainActivity.PUBLIC_JSON_OBJECT==null) {
                                                    new ApiManager().getRequestRetrofit(SignupActivity.this, Configuration.JSON, new HashMap<>(), false, Configuration.GET_REQUEST, new ApiManager.JsonObjectCallBack() {
                                                        @Override
                                                        public void getJsonObject(int ResponseCode, JSONObject jsonObject) {
                                                            MainActivity.PUBLIC_JSON_OBJECT = jsonObject;
                                                            Intent openstartingpoint = new Intent(SignupActivity.this, com.codingo.MainActivity.class);
                                                            startActivity(openstartingpoint);
                                                            finish();

                                                        }
                                                    });
                                                }else {
                                                    Intent openstartingpoint = new Intent(SignupActivity.this, com.codingo.MainActivity.class);
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
                                    }

                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            try {
                                mAuth.signOut();
                                mGoogleSignInClient.signOut();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

    private void signUpForEmailPassword() {
        new FirebaseManager(this).signUp(emailET.getText().toString(), passwordET.getText().toString(),nameET.getText().toString(), new FirebaseManager.SuccessListener() {
            @Override
            public void getsuccess() {

                new FirebaseManager(SignupActivity.this).getMyUserInfo(emailET.getText().toString(),new FirebaseManager.RetriveUserListener() {
                    @Override
                    public void getUser(User user) {
                        Toast.makeText(SignupActivity.this, "Signup Successfully", Toast.LENGTH_SHORT).show();
                        com.codingo.MainActivity.LOGGED_IN_USER=user;

                        MainActivity.myEdit = MainActivity.sharedPreferences.edit();
                        MainActivity.myEdit.putString("user", new Gson().toJson(user,User.class));
                        MainActivity.myEdit.apply();
                        if(MainActivity.PUBLIC_JSON_OBJECT==null) {
                            new ApiManager().getRequestRetrofit(SignupActivity.this, Configuration.JSON, new HashMap<>(), false, Configuration.GET_REQUEST, new ApiManager.JsonObjectCallBack() {
                                @Override
                                public void getJsonObject(int ResponseCode, JSONObject jsonObject) {
                                    MainActivity.PUBLIC_JSON_OBJECT = jsonObject;
                                    Intent openstartingpoint = new Intent(SignupActivity.this, com.codingo.MainActivity.class);
                                    startActivity(openstartingpoint);
                                    finish();

                                }
                            });
                        }else {
                            Intent openstartingpoint = new Intent(SignupActivity.this, com.codingo.MainActivity.class);
                            startActivity(openstartingpoint);
                            finish();
                        }
                    }
                });
            }

            @Override
            public void getFail() {
                Toast.makeText(SignupActivity.this, "User Already exist.", Toast.LENGTH_SHORT).show();
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
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id2))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

    }
}