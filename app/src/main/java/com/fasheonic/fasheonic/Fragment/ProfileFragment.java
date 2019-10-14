package com.fasheonic.fasheonic.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fasheonic.fasheonic.Adapter.MyPhotoAdapter;
import com.fasheonic.fasheonic.EditProfileActivity;
import com.fasheonic.fasheonic.Model.Post;
import com.fasheonic.fasheonic.Model.User;
import com.fasheonic.fasheonic.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

//import com.fasheonic.fasheonic.Adapter.MyPhotoAdapter;

public class ProfileFragment extends Fragment {

ImageView image_profile,options;
TextView posts,following,username,fullname,bio;
Button edit_profile;

RecyclerView recyclerView;
MyPhotoAdapter myPhotoAdapter;
List<Post> postList;

FirebaseUser firebaseUser;
String profileId;

ImageButton my_Photos;//savePhotos;

//userInfo();
//getFollowers();
//getNrPosts();
//myPhotos();


/*if(profileId.equals(firebaseUser.getUid())){
edit_profile.setText("Edit Profile");
}
else{
checkFollow();
//saved_photos.setVisibility(View.GONE);
}

 */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);

        //*******************FIREBASE**********//
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS",Context.MODE_PRIVATE);

        profileId = prefs.getString("profileid","none");

       image_profile = view.findViewById(R.id.image_profile);
       posts = view.findViewById(R.id.posts);
       following = view.findViewById(R.id.following);
       fullname = view.findViewById(R.id.fullname);
       username = view.findViewById(R.id.username);
       bio = view.findViewById(R.id.bio);
       edit_profile = view.findViewById(R.id.edit_profile);
        my_Photos = view.findViewById(R.id.my_photos);
       // savePhotos = view.findViewById(R.id.saved_photos);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        myPhotoAdapter = new MyPhotoAdapter(getContext(),postList);
        recyclerView.setAdapter(myPhotoAdapter);

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn = edit_profile.getText().toString();

                if(btn.equals(edit_profile)){
                    //go to Edit Profile
                   startActivity(new Intent(getContext(), EditProfileActivity.class));

                }
                else if(btn.equals(following)){
                    //**********************FIREBASE************//
                   FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following").child(profileId).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).child("followers").child(firebaseUser.getUid()).removeValue();


                }
            }
        });


        return view;
    }

    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileId);
        reference.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getContext() == null) {
                    return;
                }

                User user = dataSnapshot.getValue(User.class);
                Glide.with(getContext()).load(user.getImageurl()).into(image_profile);
                username.setText(user.getUsername());
                fullname.setText(user.getFullname());
                bio.setText(user.getBio());

            }

            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


        private void checkFollow() {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following");
            reference.addValueEventListener(new ValueEventListener() {

                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(profileId).exists()) {
                        edit_profile.setText("following");
                    } else {
                        edit_profile.setText("follow");
                    }

                }

                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }



           private void getFollowers() {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId).child("followers");
            reference.addValueEventListener(new ValueEventListener() {

                public void onDataChange(DataSnapshot dataSnapshot) {
                  following.setText("" + dataSnapshot.getChildrenCount());
//followers.setText
                }

                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }


   /*     private void getNrPosts(){
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
            reference.addValueEventListener(new ValueEventListener() {

                public void onDataChange(DataSnapshot dataSnapshot) {
                    int i=0;
                    if (DataSnapshot snapshot : dataSnapshot.getChildren()){
                      Post post = snapshot.getValue(Post.class);
                      if(post.getPublisher().equals(profileId)){
                          i++;
                      }
                    }
                    posts.setText(""+i);
                }

                public void onCancelled(DatabaseError databaseError) {

                }


            });


    }

    */



  private void myPhotos(){
      DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
      reference.addValueEventListener(new ValueEventListener() {

          public void onDataChange(DataSnapshot dataSnapshot) {
              postList.clear();

              for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                  Post post = snapshot.getValue(Post.class);
                  if(post.getPublisher().equals(profileId)) {
                      postList.add(post);
                  }
              }

              Collections.reverse(postList);
              myPhotoAdapter.notifyDataSetChanged();


          }

          public void onCancelled(DatabaseError databaseError) {

          }

      });

  }


//  }


}
