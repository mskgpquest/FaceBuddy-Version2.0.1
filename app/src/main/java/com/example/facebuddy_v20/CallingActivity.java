package com.example.facebuddy_v20;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.HashMap;

public class CallingActivity extends AppCompatActivity {
  private ImageView acceptCallBtn;
  
  private String callingId = "";
  
  private ImageView cancelCallBtn;
  
  private String checker = "";
  
  private MediaPlayer mediaPlayer;
  
  private TextView nameContact;
  
  private ImageView profileImage;
  
  private String receiverUserId = "";
  
  private String receiverUserImage = "";
  
  private String receiverUserName = "";
  
  private String ringingId = "";
  
  private String senderUserId = "";
  
  private String senderUserImage = "";
  
  private String senderUserName = "";
  
  private DatabaseReference userRef;
  
  private void cancelCallingUser() {
    this.userRef.child(this.senderUserId).child("Calling").addListenerForSingleValueEvent(new ValueEventListener() {
          public void onCancelled(DatabaseError param1DatabaseError) {}
          
          public void onDataChange(DataSnapshot param1DataSnapshot) {
            if (param1DataSnapshot.exists() && param1DataSnapshot.hasChild("calling")) {
              CallingActivity.access$1302(CallingActivity.this, param1DataSnapshot.child("calling").getValue().toString());
              CallingActivity.this.userRef.child(CallingActivity.this.callingId).child("Ringing").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> param2Task) {
                      if (param2Task.isSuccessful())
                        CallingActivity.this.userRef.child(CallingActivity.this.senderUserId).child("Calling").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                              public void onComplete(Task<Void> param3Task) {
                                CallingActivity.this.startActivity(new Intent((Context)CallingActivity.this, RegistrationActivity.class));
                                CallingActivity.this.finish();
                              }
                            }); 
                    }
                  });
              return;
            } 
            CallingActivity.this.startActivity(new Intent((Context)CallingActivity.this, RegistrationActivity.class));
            CallingActivity.this.finish();
          }
        });
    this.userRef.child(this.senderUserId).child("Ringing").addListenerForSingleValueEvent(new ValueEventListener() {
          public void onCancelled(DatabaseError param1DatabaseError) {}
          
          public void onDataChange(DataSnapshot param1DataSnapshot) {
            if (param1DataSnapshot.exists() && param1DataSnapshot.hasChild("ringing")) {
              CallingActivity.access$1402(CallingActivity.this, param1DataSnapshot.child("ringing").getValue().toString());
              CallingActivity.this.userRef.child(CallingActivity.this.ringingId).child("Calling").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> param2Task) {
                      if (param2Task.isSuccessful())
                        CallingActivity.this.userRef.child(CallingActivity.this.senderUserId).child("Ringing").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                              public void onComplete(Task<Void> param3Task) {
                                CallingActivity.this.startActivity(new Intent((Context)CallingActivity.this, RegistrationActivity.class));
                                CallingActivity.this.finish();
                              }
                            }); 
                    }
                  });
              return;
            } 
            CallingActivity.this.startActivity(new Intent((Context)CallingActivity.this, RegistrationActivity.class));
            CallingActivity.this.finish();
          }
        });
  }
  
  private void getAndSetUserProfileInfo() {
    this.userRef.addValueEventListener(new ValueEventListener() {
          public void onCancelled(DatabaseError param1DatabaseError) {}
          
          public void onDataChange(DataSnapshot param1DataSnapshot) {
            if (param1DataSnapshot.child(CallingActivity.this.receiverUserId).exists()) {
              CallingActivity callingActivity = CallingActivity.this;
              CallingActivity.access$602(callingActivity, param1DataSnapshot.child(callingActivity.receiverUserId).child("image").getValue().toString());
              callingActivity = CallingActivity.this;
              CallingActivity.access$702(callingActivity, param1DataSnapshot.child(callingActivity.receiverUserId).child("name").getValue().toString());
              CallingActivity.this.nameContact.setText(CallingActivity.this.receiverUserName);
              Picasso.get().load(CallingActivity.this.receiverUserImage).placeholder(R.drawable.profile_image).into(CallingActivity.this.profileImage);
            } 
            if (param1DataSnapshot.child(CallingActivity.this.senderUserId).exists()) {
              CallingActivity callingActivity = CallingActivity.this;
              CallingActivity.access$1002(callingActivity, param1DataSnapshot.child(callingActivity.senderUserId).child("image").getValue().toString());
              callingActivity = CallingActivity.this;
              CallingActivity.access$1102(callingActivity, param1DataSnapshot.child(callingActivity.senderUserId).child("name").getValue().toString());
            } 
          }
        });
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_calling);
    this.senderUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    this.receiverUserId = getIntent().getExtras().get("visit_user_id").toString();
    this.userRef = FirebaseDatabase.getInstance().getReference().child("User");
    this.mediaPlayer = MediaPlayer.create((Context)this, R.raw.ringing);
    this.nameContact = (TextView)findViewById(R.id.name_calling);
    this.profileImage = (ImageView)findViewById(R.id.profile_image_calling);
    this.cancelCallBtn = (ImageView)findViewById(R.id.cancel_call);
    this.acceptCallBtn = (ImageView)findViewById(R.id.make_call);
    this.cancelCallBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            CallingActivity.this.mediaPlayer.stop();
            CallingActivity.access$102(CallingActivity.this, "clicked");
            CallingActivity.this.cancelCallingUser();
          }
        });
    this.acceptCallBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            CallingActivity.this.mediaPlayer.stop();
            HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
            hashMap.put("picked", "picked");
            CallingActivity.this.userRef.child(CallingActivity.this.senderUserId).child("Ringing").updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                  public void onComplete(Task<Void> param2Task) {
                    if (param2Task.isSuccessful()) {
                      Intent intent = new Intent((Context)CallingActivity.this, VideoChatActivity.class);
                      CallingActivity.this.startActivity(intent);
                    } 
                  }
                });
          }
        });
    getAndSetUserProfileInfo();
  }
  
  protected void onStart() {
    super.onStart();
    this.mediaPlayer.start();
    this.userRef.child(this.receiverUserId).addListenerForSingleValueEvent(new ValueEventListener() {
          public void onCancelled(DatabaseError param1DatabaseError) {}
          
          public void onDataChange(DataSnapshot param1DataSnapshot) {
            if (!CallingActivity.this.checker.equals("clicked") && !param1DataSnapshot.hasChild("Calling") && !param1DataSnapshot.hasChild("Ringing")) {
              HashMap<String, Object> hashMap = new HashMap<String, Object>();
              hashMap.put("calling", CallingActivity.this.receiverUserId);
              CallingActivity.this.userRef.child(CallingActivity.this.senderUserId).child("Calling").updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> param2Task) {
                      if (param2Task.isSuccessful()) {
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("ringing", CallingActivity.this.senderUserId);
                        CallingActivity.this.userRef.child(CallingActivity.this.receiverUserId).child("Ringing").updateChildren(hashMap);
                      } 
                    }
                  });
            } 
          }
        });
    this.userRef.addValueEventListener(new ValueEventListener() {
          public void onCancelled(DatabaseError param1DatabaseError) {}
          
          public void onDataChange(DataSnapshot param1DataSnapshot) {
            if (param1DataSnapshot.child(CallingActivity.this.senderUserId).hasChild("Ringing") && !param1DataSnapshot.child(CallingActivity.this.senderUserId).hasChild("Calling"))
              CallingActivity.this.acceptCallBtn.setVisibility(0); 
            if (param1DataSnapshot.child(CallingActivity.this.receiverUserId).child("Ringing").hasChild("picked")) {
              CallingActivity.this.mediaPlayer.stop();
              Intent intent = new Intent((Context)CallingActivity.this, VideoChatActivity.class);
              CallingActivity.this.startActivity(intent);
            } 
          }
        });
  }
}


/* Location:              C:\Users\dell\Desktop\Decompilation\dex2jar-2.0\classes-dex2jar.jar!\com\example\facebudduy\CallingActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */