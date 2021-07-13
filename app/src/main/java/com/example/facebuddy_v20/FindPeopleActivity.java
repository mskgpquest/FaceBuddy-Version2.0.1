package com.example.facebuddy_v20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class FindPeopleActivity extends AppCompatActivity {
  private RecyclerView findFriendsList;
  
  private EditText searchEt;
  
  private String str = "";
  
  private DatabaseReference userRef;
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_find_people);
    this.userRef = FirebaseDatabase.getInstance().getReference().child("User");
    this.searchEt = (EditText)findViewById(R.id.search_user_text);
    RecyclerView recyclerView = (RecyclerView)findViewById(R.id.find_friend_list);
    this.findFriendsList = recyclerView;
    recyclerView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager(getApplicationContext()));
    this.searchEt.addTextChangedListener(new TextWatcher() {
          public void afterTextChanged(Editable param1Editable) {}
          
          public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
          
          public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {
            if (FindPeopleActivity.this.searchEt.getText().toString().equals("")) {
              Toast.makeText((Context)FindPeopleActivity.this, "Please write a name to search!!!", 0).show();
              return;
            } 
            FindPeopleActivity.access$102(FindPeopleActivity.this, param1CharSequence.toString());
            FindPeopleActivity.this.onStart();
          }
        });
  }
  
  protected void onStart() {
    FirebaseRecyclerOptions firebaseRecyclerOptions;
    super.onStart();
    if (this.str.equals("")) {
      firebaseRecyclerOptions = (new FirebaseRecyclerOptions.Builder()).setQuery((Query)this.userRef, Contacts.class).build();
    } else {
      FirebaseRecyclerOptions.Builder builder = new FirebaseRecyclerOptions.Builder();
      Query query = this.userRef.orderByChild("name").startAt(this.str);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.str);
      stringBuilder.append("");
      firebaseRecyclerOptions = builder.setQuery(query.endAt(stringBuilder.toString()), Contacts.class).build();
    } 
    FirebaseRecyclerAdapter<Contacts, FindFriendsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contacts, FindFriendsViewHolder>(firebaseRecyclerOptions) {
        protected void onBindViewHolder(FindFriendsViewHolder param1FindFriendsViewHolder, final int position, final Contacts model) {
          param1FindFriendsViewHolder.userNameTxt.setText(model.getName());
          Picasso.get().load(model.getImage()).into(param1FindFriendsViewHolder.profileImageView);
          param1FindFriendsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param2View) {
                  String str = FindPeopleActivity.null.this.getRef(position).getKey();
                  Intent intent = new Intent((Context)FindPeopleActivity.this, ProfileActivity.class);
                  intent.putExtra("visit_user_id", str);
                  intent.putExtra("profile_image", model.getImage());
                  intent.putExtra("profile_name", model.getName());
                  FindPeopleActivity.this.startActivity(intent);
                }
              });
        }
        
        public FindFriendsViewHolder onCreateViewHolder(ViewGroup param1ViewGroup, int param1Int) {
          return new FindFriendsViewHolder(LayoutInflater.from(param1ViewGroup.getContext()).inflate(R.layout.contact_design, param1ViewGroup, false));
        }
      };
    this.findFriendsList.setAdapter((RecyclerView.Adapter)firebaseRecyclerAdapter);
    firebaseRecyclerAdapter.startListening();
  }
  
  public static class FindFriendsViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout cardView;
    
    ImageView profileImageView;
    
    TextView userNameTxt;
    
    Button videoCallBtn;
    
    public FindFriendsViewHolder(View param1View) {
      super(param1View);
      this.userNameTxt = (TextView)param1View.findViewById(R.id.name_contact);
      this.videoCallBtn = (Button)param1View.findViewById(R.id.call_btn);
      this.profileImageView = (ImageView)param1View.findViewById(R.id.image_contact);
      this.cardView = (RelativeLayout)param1View.findViewById(R.id.card_view1);
      this.videoCallBtn.setVisibility(8);
    }
  }
}


/* Location:              C:\Users\dell\Desktop\Decompilation\dex2jar-2.0\classes-dex2jar.jar!\com\example\facebudduy\FindPeopleActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */