package com.example.facebuddy_v20;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class NotificationActivity extends AppCompatActivity {
  private DatabaseReference contactsRef;
  
  private String currentUserID;
  
  private DatabaseReference friendRequestRef;
  
  private FirebaseAuth mAuth;
  
  private RecyclerView notificationList;
  
  private DatabaseReference userRef;
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_notification);
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    this.mAuth = firebaseAuth;
    this.currentUserID = firebaseAuth.getCurrentUser().getUid();
    this.friendRequestRef = FirebaseDatabase.getInstance().getReference().child("Friend Requests");
    this.contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
    this.userRef = FirebaseDatabase.getInstance().getReference().child("User");
    RecyclerView recyclerView = (RecyclerView)findViewById(R.id.notification_list);
    this.notificationList = recyclerView;
    recyclerView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(getApplicationContext()));
  }
  
  protected void onStart() {
    super.onStart();
    FirebaseRecyclerAdapter<Contacts, NotificationsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contacts, NotificationsViewHolder>((new FirebaseRecyclerOptions.Builder()).setQuery((Query)this.friendRequestRef.child(this.currentUserID), Contacts.class).build()) {
        protected void onBindViewHolder(final NotificationsViewHolder holder, int param1Int, Contacts param1Contacts) {
          holder.acceptBtn.setVisibility(0);
          holder.cancelBtn.setVisibility(0);
          final String listUserId = getRef(param1Int).getKey();
          getRef(param1Int).child("request_type").getRef().addValueEventListener(new ValueEventListener() {
                public void onCancelled(DatabaseError param2DatabaseError) {}
                
                public void onDataChange(DataSnapshot param2DataSnapshot) {
                  if (param2DataSnapshot.exists()) {
                    if (param2DataSnapshot.getValue().toString().equals("received")) {
                      holder.cardView.setVisibility(0);
                      NotificationActivity.this.userRef.child(listUserId).addValueEventListener(new ValueEventListener() {
                            public void onCancelled(DatabaseError param3DatabaseError) {}
                            
                            public void onDataChange(DataSnapshot param3DataSnapshot) {
                              if (param3DataSnapshot.hasChild("image")) {
                                String str1 = param3DataSnapshot.child("image").getValue().toString();
                                Picasso.get().load(str1).into(holder.profileImageView);
                              } 
                              String str = param3DataSnapshot.child("name").getValue().toString();
                              holder.userNameTxt.setText(str);
                              holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View param4View) {
                                      NotificationActivity.this.contactsRef.child(NotificationActivity.this.currentUserID).child(listUserId).child("Contact").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                            public void onComplete(Task<Void> param5Task) {
                                              if (param5Task.isSuccessful())
                                                NotificationActivity.this.contactsRef.child(listUserId).child(NotificationActivity.this.currentUserID).child("Contact").setValue("Saved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                      public void onComplete(Task<Void> param6Task) {
                                                        if (param6Task.isSuccessful())
                                                          NotificationActivity.this.friendRequestRef.child(NotificationActivity.this.currentUserID).child(listUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                public void onComplete(Task<Void> param7Task) {
                                                                  if (param7Task.isSuccessful())
                                                                    NotificationActivity.this.friendRequestRef.child(listUserId).child(NotificationActivity.this.currentUserID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                          public void onComplete(Task<Void> param8Task) {
                                                                            if (param8Task.isSuccessful())
                                                                              Toast.makeText((Context)NotificationActivity.this, "New contact saved successfully!", 1).show(); 
                                                                          }
                                                                        }); 
                                                                }
                                                              }); 
                                                      }
                                                    }); 
                                            }
                                          });
                                    }
                                  });
                              holder.cancelBtn.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View param4View) {
                                      NotificationActivity.this.friendRequestRef.child(NotificationActivity.this.currentUserID).child(listUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            public void onComplete(Task<Void> param5Task) {
                                              if (param5Task.isSuccessful())
                                                NotificationActivity.this.friendRequestRef.child(NotificationActivity.this.currentUserID).child(listUserId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                      public void onComplete(Task<Void> param6Task) {
                                                        if (param6Task.isSuccessful())
                                                          Toast.makeText((Context)NotificationActivity.this, "Friend Request Cancelled!", 0).show(); 
                                                      }
                                                    }); 
                                            }
                                          });
                                    }
                                  });
                            }
                          });
                      return;
                    } 
                    holder.cardView.setVisibility(8);
                  } 
                }
              });
        }
        
        public NotificationsViewHolder onCreateViewHolder(ViewGroup param1ViewGroup, int param1Int) {
          return new NotificationsViewHolder(LayoutInflater.from(param1ViewGroup.getContext()).inflate(R.layout.find_friend_design, param1ViewGroup, false));
        }
      };
    this.notificationList.setAdapter((RecyclerView.Adapter)firebaseRecyclerAdapter);
    firebaseRecyclerAdapter.startListening();
  }
  
  public static class NotificationsViewHolder extends RecyclerView.ViewHolder {
    Button acceptBtn;
    
    Button cancelBtn;
    
    RelativeLayout cardView;
    
    ImageView profileImageView;
    
    TextView userNameTxt;
    
    public NotificationsViewHolder(View param1View) {
      super(param1View);
      this.userNameTxt = (TextView)param1View.findViewById(R.id.name_notification);
      this.acceptBtn = (Button)param1View.findViewById(R.id.request_accept_btn);
      this.cancelBtn = (Button)param1View.findViewById(R.id.request_decline_btn);
      this.profileImageView = (ImageView)param1View.findViewById(R.id.image_notification);
      this.cardView = (RelativeLayout)param1View.findViewById(R.id.card_view);
    }
  }
}


/* Location:              C:\Users\dell\Desktop\Decompilation\dex2jar-2.0\classes-dex2jar.jar!\com\example\facebudduy\NotificationActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */