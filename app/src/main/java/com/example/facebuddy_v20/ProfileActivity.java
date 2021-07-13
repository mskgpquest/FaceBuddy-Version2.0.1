package com.example.facebuddy_v20;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
  private Button add_friend;
  
  private ImageView background_profile_view;
  
  private DatabaseReference contactsRef;
  
  private String currentState = "new";
  
  private Button decline_friend_request;
  
  private Button delete_contact;
  
  private DatabaseReference friendRequestRef;
  
  private FirebaseAuth mAuth;
  
  private TextView name_profile;
  
  private String receiverUserID = "";
  
  private String receiverUserImage = "";
  
  private String receiverUserName = "";
  
  private String senderUserID;
  
  private void acceptFriendRequest() {
    this.contactsRef.child(this.senderUserID).child(this.receiverUserID).child("Contact").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
          public void onComplete(Task<Void> param1Task) {
            if (param1Task.isSuccessful())
              ProfileActivity.this.contactsRef.child(ProfileActivity.this.receiverUserID).child(ProfileActivity.this.senderUserID).child("Contact").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> param2Task) {
                      if (param2Task.isSuccessful())
                        ProfileActivity.this.friendRequestRef.child(ProfileActivity.this.senderUserID).child(ProfileActivity.this.receiverUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                              public void onComplete(Task<Void> param3Task) {
                                if (param3Task.isSuccessful())
                                  ProfileActivity.this.friendRequestRef.child(ProfileActivity.this.receiverUserID).child(ProfileActivity.this.senderUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        public void onComplete(Task<Void> param4Task) {
                                          if (param4Task.isSuccessful()) {
                                            ProfileActivity.access$102(ProfileActivity.this, "friends");
                                            ProfileActivity.this.add_friend.setText("Delete Contact");
                                            ProfileActivity.this.decline_friend_request.setVisibility(8);
                                          } 
                                        }
                                      }); 
                              }
                            }); 
                    }
                  }); 
          }
        });
  }
  
  private void cancelFriendRequest() {
    this.friendRequestRef.child(this.senderUserID).child(this.receiverUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
          public void onComplete(Task<Void> param1Task) {
            if (param1Task.isSuccessful())
              ProfileActivity.this.friendRequestRef.child(ProfileActivity.this.receiverUserID).child(ProfileActivity.this.senderUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> param2Task) {
                      if (param2Task.isSuccessful()) {
                        ProfileActivity.access$102(ProfileActivity.this, "new");
                        ProfileActivity.this.add_friend.setText("Add Friend");
                        Toast.makeText((Context)ProfileActivity.this, "Friendship Cancelled!", 0).show();
                      } 
                    }
                  }); 
          }
        });
  }
  
  private void deleteContact() {
    this.contactsRef.child(this.senderUserID).child(this.receiverUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
          public void onComplete(Task<Void> param1Task) {
            if (param1Task.isSuccessful())
              ProfileActivity.this.contactsRef.child(ProfileActivity.this.receiverUserID).child(ProfileActivity.this.senderUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> param2Task) {
                      if (param2Task.isSuccessful()) {
                        ProfileActivity.access$102(ProfileActivity.this, "new");
                        ProfileActivity.this.add_friend.setText("Add Friend");
                        Toast.makeText((Context)ProfileActivity.this, "Friendship Cancelled!", 0).show();
                      } 
                    }
                  }); 
          }
        });
  }
  
  private void manageClickEvents() {
    this.friendRequestRef.child(this.senderUserID).addListenerForSingleValueEvent(new ValueEventListener() {
          public void onCancelled(DatabaseError param1DatabaseError) {}
          
          public void onDataChange(DataSnapshot param1DataSnapshot) {
            if (param1DataSnapshot.hasChild(ProfileActivity.this.receiverUserID)) {
              String str = param1DataSnapshot.child(ProfileActivity.this.receiverUserID).child("request_type").getValue().toString();
              if (str.equals("sent")) {
                ProfileActivity.access$102(ProfileActivity.this, "request_sent");
                ProfileActivity.this.add_friend.setText("Cancel Friend Request");
              } else if (str.equals("received")) {
                ProfileActivity.access$102(ProfileActivity.this, "request_received");
                ProfileActivity.this.add_friend.setText("Accept Friend Request");
                ProfileActivity.this.decline_friend_request.setVisibility(0);
                ProfileActivity.this.decline_friend_request.setOnClickListener(new View.OnClickListener() {
                      public void onClick(View param2View) {
                        ProfileActivity.this.cancelFriendRequest();
                      }
                    });
              } 
              return;
            } 
            ProfileActivity.this.contactsRef.child(ProfileActivity.this.senderUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                  public void onCancelled(DatabaseError param2DatabaseError) {}
                  
                  public void onDataChange(DataSnapshot param2DataSnapshot) {
                    if (param2DataSnapshot.hasChild(ProfileActivity.this.receiverUserID)) {
                      ProfileActivity.access$102(ProfileActivity.this, "friends");
                      ProfileActivity.this.add_friend.setText("Delete Contact");
                      return;
                    } 
                    ProfileActivity.access$102(ProfileActivity.this, "new");
                    ProfileActivity.this.add_friend.setText("Add Friend");
                  }
                });
          }
        });
    if (this.senderUserID.equals(this.receiverUserID)) {
      this.add_friend.setVisibility(8);
      return;
    } 
    this.add_friend.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (ProfileActivity.this.currentState.equals("new"))
              ProfileActivity.this.sendFriendRequest(); 
            if (ProfileActivity.this.currentState.equals("request_sent"))
              ProfileActivity.this.cancelFriendRequest(); 
            if (ProfileActivity.this.currentState.equals("request_received"))
              ProfileActivity.this.acceptFriendRequest(); 
            if (ProfileActivity.this.currentState.equals("friends"))
              ProfileActivity.this.deleteContact(); 
          }
        });
  }
  
  private void sendFriendRequest() {
    this.friendRequestRef.child(this.senderUserID).child(this.receiverUserID).child("request_type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
          public void onComplete(Task<Void> param1Task) {
            if (param1Task.isSuccessful())
              ProfileActivity.this.friendRequestRef.child(ProfileActivity.this.receiverUserID).child(ProfileActivity.this.senderUserID).child("request_type").setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> param2Task) {
                      if (param2Task.isSuccessful()) {
                        ProfileActivity.access$102(ProfileActivity.this, "request_sent");
                        ProfileActivity.this.add_friend.setText("Cancel Friend Request");
                        Toast.makeText((Context)ProfileActivity.this, "Friend Request Sent!", 1).show();
                      } 
                    }
                  }); 
          }
        });
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_profile);
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    this.mAuth = firebaseAuth;
    this.senderUserID = firebaseAuth.getCurrentUser().getUid();
    this.friendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
    this.contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
    this.receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
    this.receiverUserImage = getIntent().getExtras().get("profile_image").toString();
    this.receiverUserName = getIntent().getExtras().get("profile_name").toString();
    this.background_profile_view = (ImageView)findViewById(R.id.background_profile_view);
    this.name_profile = (TextView)findViewById(R.id.name_profile);
    this.add_friend = (Button)findViewById(R.id.add_friend);
    this.decline_friend_request = (Button)findViewById(R.id.decline_friend_request);
    Picasso.get().load(this.receiverUserImage).into(this.background_profile_view);
    this.name_profile.setText(this.receiverUserName);
    manageClickEvents();
  }
}


/* Location:              C:\Users\dell\Desktop\Decompilation\dex2jar-2.0\classes-dex2jar.jar!\com\example\facebudduy\ProfileActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */