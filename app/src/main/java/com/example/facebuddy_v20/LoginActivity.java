package com.example.facebuddy_v20;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
  private EditText InputUserPhoneNumber;
  
  private EditText InputUserVerificationCode;
  
  private Button SendVerificationCodeButton;
  
  private Button VerifyButton;
  
  private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
  
  private ProgressDialog loadingBar;
  
  private FirebaseAuth mAuth;
  
  private PhoneAuthProvider.ForceResendingToken mResendToken;
  
  private String mVerificationId;
  
  private void SendUserToMainActivity() {
    startActivity(new Intent((Context)this, ContactsActivity.class));
    finish();
  }
  
  private void signInWithPhoneAuthCredential(PhoneAuthCredential paramPhoneAuthCredential) {
    this.mAuth.signInWithCredential((AuthCredential)paramPhoneAuthCredential).addOnCompleteListener((Activity)this, new OnCompleteListener<AuthResult>() {
          public void onComplete(Task<AuthResult> param1Task) {
            if (param1Task.isSuccessful()) {
              LoginActivity.this.loadingBar.dismiss();
              Toast.makeText((Context)LoginActivity.this, "Congratulations, you're logged in Successfully.", 0).show();
              LoginActivity.this.SendUserToMainActivity();
              return;
            } 
            String str = param1Task.getException().toString();
            LoginActivity loginActivity = LoginActivity.this;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error: ");
            stringBuilder.append(str);
            Toast.makeText((Context)loginActivity, stringBuilder.toString(), 0).show();
            LoginActivity.this.loadingBar.dismiss();
          }
        });
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_login);
    this.mAuth = FirebaseAuth.getInstance();
    this.InputUserPhoneNumber = (EditText)findViewById(R.id.phone_number_input);
    this.InputUserVerificationCode = (EditText)findViewById(R.id.verification_code_input);
    this.SendVerificationCodeButton = (Button)findViewById(R.id.send_ver_code_button);
    this.VerifyButton = (Button)findViewById(R.id.verify_button);
    this.loadingBar = new ProgressDialog((Context)this);
    this.SendVerificationCodeButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            String str = LoginActivity.this.InputUserPhoneNumber.getText().toString();
            if (TextUtils.isEmpty(str)) {
              Toast.makeText((Context)LoginActivity.this, "Please enter your phone number first...", 0).show();
              return;
            } 
            LoginActivity.this.loadingBar.setTitle("Phone Verification");
            LoginActivity.this.loadingBar.setMessage("Please wait, while we are authenticating using your phone...");
            LoginActivity.this.loadingBar.setCanceledOnTouchOutside(false);
            LoginActivity.this.loadingBar.show();
            PhoneAuthProvider phoneAuthProvider = PhoneAuthProvider.getInstance();
            TimeUnit timeUnit = TimeUnit.SECONDS;
            LoginActivity loginActivity = LoginActivity.this;
            phoneAuthProvider.verifyPhoneNumber(str, 60L, timeUnit, (Activity)loginActivity, loginActivity.callbacks);
          }
        });
    this.VerifyButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            LoginActivity.this.InputUserPhoneNumber.setVisibility(4);
            LoginActivity.this.SendVerificationCodeButton.setVisibility(4);
            String str = LoginActivity.this.InputUserVerificationCode.getText().toString();
            if (TextUtils.isEmpty(str)) {
              Toast.makeText((Context)LoginActivity.this, "Please write verification code first...", 0).show();
              return;
            } 
            LoginActivity.this.loadingBar.setTitle("Verification Code");
            LoginActivity.this.loadingBar.setMessage("Please wait, while we are verifying verification code...");
            LoginActivity.this.loadingBar.setCanceledOnTouchOutside(false);
            LoginActivity.this.loadingBar.show();
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(LoginActivity.this.mVerificationId, str);
            LoginActivity.this.signInWithPhoneAuthCredential(phoneAuthCredential);
          }
        });
    this.callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        public void onCodeSent(String param1String, PhoneAuthProvider.ForceResendingToken param1ForceResendingToken) {
          LoginActivity.access$502(LoginActivity.this, param1String);
          LoginActivity.access$802(LoginActivity.this, param1ForceResendingToken);
          Toast.makeText((Context)LoginActivity.this, "Code has been sent, please check and verify...", 0).show();
          LoginActivity.this.loadingBar.dismiss();
          LoginActivity.this.InputUserPhoneNumber.setVisibility(4);
          LoginActivity.this.SendVerificationCodeButton.setVisibility(4);
          LoginActivity.this.InputUserVerificationCode.setVisibility(0);
          LoginActivity.this.VerifyButton.setVisibility(0);
        }
        
        public void onVerificationCompleted(PhoneAuthCredential param1PhoneAuthCredential) {
          LoginActivity.this.signInWithPhoneAuthCredential(param1PhoneAuthCredential);
        }
        
        public void onVerificationFailed(FirebaseException param1FirebaseException) {
          Toast.makeText((Context)LoginActivity.this, "Invalid Phone Number, Please enter correct phone number with your country code...", 1).show();
          LoginActivity.this.loadingBar.dismiss();
          LoginActivity.this.InputUserPhoneNumber.setVisibility(0);
          LoginActivity.this.SendVerificationCodeButton.setVisibility(0);
          LoginActivity.this.InputUserVerificationCode.setVisibility(4);
          LoginActivity.this.VerifyButton.setVisibility(4);
        }
      };
  }
}


/* Location:              C:\Users\dell\Desktop\Decompilation\dex2jar-2.0\classes-dex2jar.jar!\com\example\facebudduy\LoginActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */