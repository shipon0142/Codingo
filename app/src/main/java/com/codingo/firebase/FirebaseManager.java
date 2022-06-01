package com.codingo.firebase;

import static com.codingo.MainActivity.LOGGED_IN_USER;

import android.content.Context;
import android.util.Log;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.codingo.MainActivity;
import com.codingo.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseManager {


    FirebaseDatabase database;
    DatabaseReference user_ref;
    private FirebaseAuth mAuth;
    Context context;
    public FirebaseManager(Context context){
        super();
        this.context=context;
        database = FirebaseDatabase.getInstance();
        user_ref = database.getReference("app_user");
    }
    public interface SuccessListener{
        public void getsuccess();
        public void getFail();
    }

    public void addDatabase(User user,SuccessListener successListener) {
        String key = user_ref.push().getKey();
        user_ref.child(key).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                successListener.getsuccess();
            }
        });

    }

    SuccessListener successListener;
    public void addEnrolledCourse(String userid,String course,SuccessListener successListener) {
        user_ref.child(userid).child("enrolled").setValue(course, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                new FirebaseManager(context).getMyUserInfo(LOGGED_IN_USER.getEmail(), new FirebaseManager.RetriveUserListener() {
                    @Override
                    public void getUser(User user) {
                        LOGGED_IN_USER=user;

                    }
                });
                successListener.getsuccess();
            }
        });



    }
    public void signUp(String email,String password,String name, SuccessListener successListener) {

        hasUser(email, new HasUserListener() {
            @Override
            public void hasUser(boolean flag) {


                if(!flag){
                    User user=new User();
                    user.setName(name);
                    user.setEmail(email);
                    user.setPassword(password);
                    addDatabase(user, new SuccessListener() {
                        @Override
                        public void getsuccess() {
                            successListener.getsuccess();

                        }

                        @Override
                        public void getFail() {
                            successListener.getFail();

                        }
                    });
                }else {
                    successListener.getFail();
                }


            }
        });

    }
    public void getUserDetails(String email,String password, RetriveUserListener retriveUserListener) {
        final User[] user = {new User()};
        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable = snapshot.getChildren();
                Log.d("myrequest",snapshot.toString());
                boolean flag=false;
                for (DataSnapshot aSnapshotIterable : snapshotIterable) {

                    try {
                        User value = aSnapshotIterable.getValue(User.class);
                        if(value.getEmail().equals(email) && value.getPassword().equals(password)){
                            user[0] =value;
                            flag=true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                retriveUserListener.getUser(user[0]);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                retriveUserListener.getUser(new User());
            }
        });


    }


    public interface HasUserListener{
        public void hasUser(boolean flag);
    }
    public void hasUser(String email, HasUserListener hasUserListener) {

        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable = snapshot.getChildren();
                Log.d("myrequest",snapshot.toString());
                boolean flag=false;
                for (DataSnapshot aSnapshotIterable : snapshotIterable) {

                    try {
                        User value = aSnapshotIterable.getValue(User.class);
                        if(value.getEmail().equals(email)){
                            flag=true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                hasUserListener.hasUser(flag);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hasUserListener.hasUser(false);
            }
        });


    }
    public interface RetriveAllUserListener{
        public void getAllUser(ArrayList <User> users);
    }
    public void getAlUser(RetriveAllUserListener retriveUserListener) {
        ArrayList<User>arrayList=new ArrayList<>();
        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable = snapshot.getChildren();
                 Log.d("myrequest",snapshot.toString());
                for (DataSnapshot aSnapshotIterable : snapshotIterable) {

                    try {
                        User value = aSnapshotIterable.getValue(User.class);
                        value.setId(aSnapshotIterable.getKey());
                        arrayList.add(value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                retriveUserListener.getAllUser(arrayList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                retriveUserListener.getAllUser(new ArrayList<>());
            }
        });


    }
    public interface RetriveUserListener{
        public void getUser(User user);
    }
    public void getMyUserInfo(String userEmail,RetriveUserListener retriveUserListener) {

        user_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Iterable<DataSnapshot> snapshotIterable = snapshot.getChildren();
                 Log.d("myrequest",snapshot.toString());
                for (DataSnapshot aSnapshotIterable : snapshotIterable) {

                    try {
                        User value = aSnapshotIterable.getValue(User.class);
                        value.setId(aSnapshotIterable.getKey());
                      if(value.getEmail().equals(userEmail)){
                          retriveUserListener.getUser(value);
                          break;
                      }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                retriveUserListener.getUser(null);
            }
        });


    }

    public FirebaseUser getCurrentUser() {
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) return null;
        else return mAuth.getCurrentUser();

    }
}
