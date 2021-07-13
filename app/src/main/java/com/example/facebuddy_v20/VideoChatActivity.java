package com.example.facebuddy_v20;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.opentok.android.OpentokError;
import com.opentok.android.Publisher;
import com.opentok.android.PublisherKit;
import com.opentok.android.Session;
import com.opentok.android.Stream;
import com.opentok.android.Subscriber;
import com.opentok.android.SubscriberKit;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class VideoChatActivity extends AppCompatActivity implements Session.SessionListener, PublisherKit.PublisherListener {
  private static String API_Key = "46887064";
  
  private static final String LOG_TAG;
  
  private static final int RC_VIDEO_APP_PERM = 124;
  
  private static String SESSION_ID = "2_MX40Njg4NzA2NH5-MTU5NzY0OTY0MzcwNX5yU1BJOWRSeXB4SVpIbUR2V3Y4eURmeDB-fg";
  
  private static String TOKEN = "T1==cGFydG5lcl9pZD00Njg4NzA2NCZzaWc9YTAxNWNhMGFiMzM0ZTQ0M2IwYjM2MWE2NTJkNDFhOTY1NzU4YTliZjpzZXNzaW9uX2lkPTJfTVg0ME5qZzROekEyTkg1LU1UVTVOelkwT1RZME16Y3dOWDV5VTFCSk9XUlNlWEI0U1ZwSWJVUjJWM1k0ZVVSbWVEQi1mZyZjcmVhdGVfdGltZT0xNTk3NjQ5NzMyJm5vbmNlPTAuNTc4NDEyNDM5NDE4MDIzMiZyb2xlPXB1Ymxpc2hlciZleHBpcmVfdGltZT0xNjAwMjQxNzI0JmluaXRpYWxfbGF5b3V0X2NsYXNzX2xpc3Q9";
  
  private ImageView closeVideoChatBtn;
  
  private Publisher mPublisher;
  
  private FrameLayout mPublisherViewController;
  
  private Session mSession;
  
  private Subscriber mSubscriber;
  
  private FrameLayout mSubscriberViewController;
  
  private String userID = "";
  
  private DatabaseReference userRef;
  
  static {
    LOG_TAG = VideoChatActivity.class.getSimpleName();
  }
  
  @AfterPermissionGranted(124)
  private void requestPermission() {
    Session session;
    String[] arrayOfString = new String[3];
    arrayOfString[0] = "android.permission.INTERNET";
    arrayOfString[1] = "android.permission.CAMERA";
    arrayOfString[2] = "android.permission.RECORD_AUDIO";
    if (EasyPermissions.hasPermissions((Context)this, arrayOfString)) {
      this.mPublisherViewController = (FrameLayout)findViewById(R.id.publisher_container);
      this.mSubscriberViewController = (FrameLayout)findViewById(R.id.subscriber_container);
      session = (new Session.Builder((Context)this, API_Key, SESSION_ID)).build();
      this.mSession = session;
      session.setSessionListener(this);
      this.mSession.connect(TOKEN);
      return;
    } 
    EasyPermissions.requestPermissions((Activity)this, "Hey! This app needs the Mic and Camera Permission, Please Allow!", 124, (String[])session);
  }
  
  public void onConnected(Session paramSession) {
    Log.i(LOG_TAG, "Session Connected!");
    Publisher publisher = (new Publisher.Builder((Context)this)).build();
    this.mPublisher = publisher;
    publisher.setPublisherListener(this);
    this.mPublisherViewController.addView(this.mPublisher.getView());
    if (this.mPublisher.getView() instanceof GLSurfaceView)
      ((GLSurfaceView)this.mPublisher.getView()).setZOrderOnTop(true); 
    this.mSession.publish((PublisherKit)this.mPublisher);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2131427364);
    this.userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    this.userRef = FirebaseDatabase.getInstance().getReference().child("User");
    ImageView imageView = (ImageView)findViewById(R.id.close_video_chat_btn);
    this.closeVideoChatBtn = imageView;
    imageView.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            VideoChatActivity.this.userRef.addValueEventListener(new ValueEventListener() {
                  public void onCancelled(DatabaseError param2DatabaseError) {}
                  
                  public void onDataChange(DataSnapshot param2DataSnapshot) {
                    if (param2DataSnapshot.child(VideoChatActivity.this.userID).hasChild("Ringing")) {
                      VideoChatActivity.this.userRef.child(VideoChatActivity.this.userID).child("Ringing").removeValue();
                      if (VideoChatActivity.this.mPublisher != null)
                        VideoChatActivity.this.mPublisher.destroy(); 
                      if (VideoChatActivity.this.mSubscriber != null)
                        VideoChatActivity.this.mSubscriber.destroy(); 
                      VideoChatActivity.this.startActivity(new Intent((Context)VideoChatActivity.this, RegistrationActivity.class));
                      VideoChatActivity.this.finish();
                    } 
                    if (param2DataSnapshot.child(VideoChatActivity.this.userID).hasChild("Calling")) {
                      VideoChatActivity.this.userRef.child(VideoChatActivity.this.userID).child("Calling").removeValue();
                      if (VideoChatActivity.this.mPublisher != null)
                        VideoChatActivity.this.mPublisher.destroy(); 
                      if (VideoChatActivity.this.mSubscriber != null)
                        VideoChatActivity.this.mSubscriber.destroy(); 
                      VideoChatActivity.this.startActivity(new Intent((Context)VideoChatActivity.this, RegistrationActivity.class));
                      VideoChatActivity.this.finish();
                      return;
                    } 
                    if (VideoChatActivity.this.mPublisher != null)
                      VideoChatActivity.this.mPublisher.destroy(); 
                    if (VideoChatActivity.this.mSubscriber != null)
                      VideoChatActivity.this.mSubscriber.destroy(); 
                    VideoChatActivity.this.startActivity(new Intent((Context)VideoChatActivity.this, RegistrationActivity.class));
                    VideoChatActivity.this.finish();
                  }
                });
          }
        });
    requestPermission();
  }
  
  public void onDisconnected(Session paramSession) {
    Log.i(LOG_TAG, "Stream Disconnected!");
  }
  
  public void onError(PublisherKit paramPublisherKit, OpentokError paramOpentokError) {}
  
  public void onError(Session paramSession, OpentokError paramOpentokError) {
    Log.i(LOG_TAG, "Stream Error!");
  }
  
  public void onPointerCaptureChanged(boolean paramBoolean) {}
  
  public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfint) {
    super.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfint);
    EasyPermissions.onRequestPermissionsResult(paramInt, paramArrayOfString, paramArrayOfint, new Object[] { this });
  }
  
  public void onStreamCreated(PublisherKit paramPublisherKit, Stream paramStream) {}
  
  public void onStreamDestroyed(PublisherKit paramPublisherKit, Stream paramStream) {}
  
  public void onStreamDropped(Session paramSession, Stream paramStream) {
    Log.i(LOG_TAG, "Stream Dropped!");
    if (this.mSubscriber != null) {
      this.mSubscriber = null;
      this.mSubscriberViewController.removeAllViews();
    } 
  }
  
  public void onStreamReceived(Session paramSession, Stream paramStream) {
    Log.i(LOG_TAG, "Stream Received!");
    if (this.mSubscriber == null) {
      Subscriber subscriber = (new Subscriber.Builder((Context)this, paramStream)).build();
      this.mSubscriber = subscriber;
      this.mSession.subscribe((SubscriberKit)subscriber);
      this.mSubscriberViewController.addView(this.mSubscriber.getView());
    } 
  }
}


/* Location:              C:\Users\dell\Desktop\Decompilation\dex2jar-2.0\classes-dex2jar.jar!\com\example\facebudduy\VideoChatActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */