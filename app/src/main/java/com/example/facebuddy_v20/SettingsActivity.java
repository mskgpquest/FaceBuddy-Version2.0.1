package com.example.facebuddy_v20;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {
  private static int GalleryPick = 1;
  
  private Uri ImageUri;
  
  private String downloadUrl;
  
  private ImageView profileImageView;
  
  private ProgressDialog progressDialog;
  
  private Button saveBtn;
  
  private EditText userBioEt;
  
  private EditText userNameEt;
  
  private StorageReference userProfileImgRef;
  
  private DatabaseReference userRef;
  
  private void retrieveUserInfo() {
    this.userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
          public void onCancelled(DatabaseError param1DatabaseError) {}
          
          public void onDataChange(DataSnapshot param1DataSnapshot) {
            if (param1DataSnapshot.exists()) {
              String str2 = param1DataSnapshot.child("image").getValue().toString();
              String str3 = param1DataSnapshot.child("name").getValue().toString();
              String str1 = param1DataSnapshot.child("status").getValue().toString();
              SettingsActivity.this.userNameEt.setText(str3);
              SettingsActivity.this.userBioEt.setText(str1);
              Picasso.get().load(str2).placeholder(R.drawable.profile_image).into(SettingsActivity.this.profileImageView);
            } 
          }
        });
  }
  
  private void saveInfoOnlyWithoutImage() {
    String str1 = this.userNameEt.getText().toString();
    String str2 = this.userBioEt.getText().toString();
    if (str1.equals("")) {
      Toast.makeText((Context)this, "Username is required!!!", Toast.LENGTH_LONG).show();
      return;
    } 
    if (str2.equals("")) {
      Toast.makeText((Context)this, "Bio is required!!!", Toast.LENGTH_LONG).show();
      return;
    } 
    this.progressDialog.setTitle("Account Settings");
    this.progressDialog.setMessage("Please wait while we are updating the settings ...");
    this.progressDialog.show();
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
    hashMap.put("name", str1);
    hashMap.put("status", str2);
    this.userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
          public void onComplete(Task<Void> param1Task) {
            if (param1Task.isSuccessful()) {
              Intent intent = new Intent((Context)SettingsActivity.this, ContactsActivity.class);
              SettingsActivity.this.startActivity(intent);
              SettingsActivity.this.finish();
              SettingsActivity.this.progressDialog.dismiss();
              Toast.makeText((Context)SettingsActivity.this, "Congratulation! profile Settings have been updated!!!", 1).show();
              return;
            }
            Toast.makeText((Context)SettingsActivity.this, "Sorry! Some error occured!!!", 1).show();
          }
        });
  }
  
  private void saveUserData() {
    final String getUserName = this.userNameEt.getText().toString();
    final String getUserStatus = this.userBioEt.getText().toString();
    if (this.ImageUri == null) {
      this.userRef.addValueEventListener(new ValueEventListener() {
            public void onCancelled(DatabaseError param1DatabaseError) {}
            
            public void onDataChange(DataSnapshot param1DataSnapshot) {
              if (param1DataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("image")) {
                SettingsActivity.this.saveInfoOnlyWithoutImage();
                return;
              } 
              Toast.makeText((Context)SettingsActivity.this, "Please select image first!", Toast.LENGTH_LONG).show();
            }
          });
      return;
    } 
    if (str1.equals("")) {
      Toast.makeText((Context)this, "Username is required!!!", 1).show();
      return;
    } 
    if (str2.equals("")) {
      Toast.makeText((Context)this, "Bio is required!!!", Toast.LENGTH_LONG).show();
      return;
    } 
    this.progressDialog.setTitle("Account Settings");
    this.progressDialog.setMessage("Please wait while we are updating the settings ...");
    this.progressDialog.show();
    final StorageReference filePath = this.userProfileImgRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
    storageReference.putFile(this.ImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
          public Task<Uri> then(Task<UploadTask.TaskSnapshot> param1Task) throws Exception {
            if (param1Task.isSuccessful()) {
              SettingsActivity.access$302(SettingsActivity.this, filePath.getDownloadUrl().toString());
              return filePath.getDownloadUrl();
            } 
            throw param1Task.getException();
          }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
          public void onComplete(Task<Uri> param1Task) {
            if (param1Task.isSuccessful()) {
              SettingsActivity.access$302(SettingsActivity.this, ((Uri)param1Task.getResult()).toString());
              HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
              hashMap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
              hashMap.put("name", getUserName);
              hashMap.put("status", getUserStatus);
              hashMap.put("image", SettingsActivity.this.downloadUrl);
              SettingsActivity.this.userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(Task<Void> param2Task) {
                      if (param2Task.isSuccessful()) {
                        Intent intent = new Intent((Context)SettingsActivity.this, ContactsActivity.class);
                        SettingsActivity.this.startActivity(intent);
                        SettingsActivity.this.finish();
                        SettingsActivity.this.progressDialog.dismiss();
                        Toast.makeText((Context)SettingsActivity.this, "Congratulation! profile Settings have been updated!!!", Toast.LENGTH_LONG).show();
                        return;
                      } 
                      Toast.makeText((Context)SettingsActivity.this, "Sorry! Some error occured!!!", Toast.LENGTH_LONG).show();
                    }
                  });
            } 
          }
        });
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == GalleryPick && paramInt2 == -1 && paramIntent != null) {
      Uri uri = paramIntent.getData();
      this.ImageUri = uri;
      this.profileImageView.setImageURI(uri);
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_settings);
    this.userProfileImgRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
    this.userRef = FirebaseDatabase.getInstance().getReference().child("User");
    this.saveBtn = (Button)findViewById(R.id.save_settings_btn);
    this.userNameEt = (EditText)findViewById(R.id.username_settings);
    this.userBioEt = (EditText)findViewById(R.id.bio_settings);
    this.profileImageView = (ImageView)findViewById(R.id.settings_profile_image);
    this.progressDialog = new ProgressDialog((Context)this);
    this.profileImageView.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.GET_CONTENT");
            intent.setType("image/*");
            SettingsActivity.this.startActivityForResult(intent, SettingsActivity.GalleryPick);
          }
        });
    this.saveBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            SettingsActivity.this.saveUserData();
          }
        });
    retrieveUserInfo();
  }
}


/* Location:              C:\Users\dell\Desktop\Decompilation\dex2jar-2.0\classes-dex2jar.jar!\com\example\facebudduy\SettingsActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */