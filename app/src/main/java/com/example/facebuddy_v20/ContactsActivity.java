package com.example.facebuddy_v20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ContactsActivity extends AppCompatActivity {
  private String calledBy = "";
  
  private DatabaseReference contactsRef;
  
  private String currentUserID;
  
  ImageView findPeopleBtn;
  
  private FirebaseAuth mAuth;
  
  RecyclerView myContactsList;
  
  BottomNavigationView navView;
  
  private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
      public boolean onNavigationItemSelected(MenuItem param1MenuItem) {
        Intent intent;
        switch (param1MenuItem.getItemId()) {
          default:
            return false;
          case R.id.navigation_settings:
            intent = new Intent((Context)ContactsActivity.this, SettingsActivity.class);
            ContactsActivity.this.startActivity(intent);
            return false;
          case R.id.navigation_notifications:
            intent = new Intent((Context)ContactsActivity.this, NotificationActivity.class);
            ContactsActivity.this.startActivity(intent);
            return false;
          case R.id.navigation_logout:
            Toast.makeText((Context)ContactsActivity.this, "Aware!Don't logout too freuently, without reason!!!\nAfter your session qouta ends, you may face trouble to login again.For more info contact developer!", 1).show();
            FirebaseAuth.getInstance().signOut();
            intent = new Intent((Context)ContactsActivity.this, RegistrationActivity.class);
            ContactsActivity.this.startActivity(intent);
            ContactsActivity.this.finish();
            return false;
          case R.id.navigation_home:
            break;
        } 
        Toast.makeText((Context)ContactsActivity.this, "Already at Home!", 0).show();
        return false;
      }
    };
  
  private String profileImage = "";
  
  private String userName = "";
  
  private DatabaseReference userRef;
  
  private void checkForReceivingCall() {
    this.userRef.child(this.currentUserID).child("Ringing").addValueEventListener(new ValueEventListener() {
          public void onCancelled(DatabaseError param1DatabaseError) {}
          
          public void onDataChange(DataSnapshot param1DataSnapshot) {
            if (param1DataSnapshot.hasChild("ringing")) {
              ContactsActivity.access$302(ContactsActivity.this, param1DataSnapshot.child("ringing").getValue().toString());
              Intent intent = new Intent((Context)ContactsActivity.this, CallingActivity.class);
              intent.putExtra("visit_user_id", ContactsActivity.this.calledBy);
              ContactsActivity.this.startActivity(intent);
            } 
          }
        });
  }
  
  private void validateUser() {
    FirebaseDatabase.getInstance().getReference().child("User").child(this.currentUserID).addValueEventListener(new ValueEventListener() {
          public void onCancelled(DatabaseError param1DatabaseError) {}
          
          public void onDataChange(DataSnapshot param1DataSnapshot) {
            if (!param1DataSnapshot.exists()) {
              Intent intent = new Intent((Context)ContactsActivity.this, SettingsActivity.class);
              ContactsActivity.this.startActivity(intent);
              ContactsActivity.this.finish();
            } 
          }
        });
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_contacts);
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    this.mAuth = firebaseAuth;
    this.currentUserID = firebaseAuth.getCurrentUser().getUid();
    this.contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
    this.userRef = FirebaseDatabase.getInstance().getReference().child("User");
    BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.nav_view);
    this.navView = bottomNavigationView;
    bottomNavigationView.setOnNavigationItemSelectedListener(this.navigationItemSelectedListener);
    this.findPeopleBtn = (ImageView)findViewById(R.id.find_people_btn);
    RecyclerView recyclerView = (RecyclerView)findViewById(R.id.contacts_list);
    this.myContactsList = recyclerView;
    recyclerView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(getApplicationContext()));
    this.findPeopleBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent((Context)ContactsActivity.this, FindPeopleActivity.class);
            ContactsActivity.this.startActivity(intent);
          }
        });
  }
  
  protected void onStart() {
    super.onStart();
    checkForReceivingCall();
    validateUser();
    FirebaseRecyclerAdapter<Contacts, ContactsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>((new FirebaseRecyclerOptions.Builder()).setQuery((Query)this.contactsRef.child(this.currentUserID), Contacts.class).build()) {
        protected void onBindViewHolder(final ContactsViewHolder holder, int param1Int, Contacts param1Contacts) {
          final String listUserId = getRef(param1Int).getKey();
          ContactsActivity.this.userRef.child(str).addValueEventListener(new ValueEventListener() {
                public void onCancelled(DatabaseError param2DatabaseError) {}
                
                public void onDataChange(DataSnapshot param2DataSnapshot) {
                  if (param2DataSnapshot.exists()) {
                    ContactsActivity.access$002(ContactsActivity.this, param2DataSnapshot.child("name").getValue().toString());
                    ContactsActivity.access$102(ContactsActivity.this, param2DataSnapshot.child("image").getValue().toString());
                    holder.userNameTxt.setText(ContactsActivity.this.userName);
                    Picasso.get().load(ContactsActivity.this.profileImage).into(holder.profileImageView);
                  } 
                  holder.callBtn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View param3View) {
                          Intent intent = new Intent((Context)ContactsActivity.this, CallingActivity.class);
                          intent.putExtra("visit_user_id", listUserId);
                          ContactsActivity.this.startActivity(intent);
                        }
                      });
                }
              });
        }
        
        public ContactsViewHolder onCreateViewHolder(ViewGroup param1ViewGroup, int param1Int) {
          return new ContactsViewHolder(LayoutInflater.from(param1ViewGroup.getContext()).inflate(R.layout.contact_design, param1ViewGroup, false));
        }
      };
    this.myContactsList.setAdapter((RecyclerView.Adapter)firebaseRecyclerAdapter);
    firebaseRecyclerAdapter.startListening();
  }
  
  public static class ContactsViewHolder extends RecyclerView.ViewHolder {
    Button callBtn;
    
    ImageView profileImageView;
    
    TextView userNameTxt;
    
    public ContactsViewHolder(View param1View) {
      super(param1View);
      this.userNameTxt = (TextView)param1View.findViewById(R.id.name_contact);
      this.callBtn = (Button)param1View.findViewById(R.id.call_btn);
      this.profileImageView = (ImageView)param1View.findViewById(R.id.image_contact);
    }
  }
}


/* Location:              C:\Users\dell\Desktop\Decompilation\dex2jar-2.0\classes-dex2jar.jar!\com\example\facebudduy\ContactsActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */