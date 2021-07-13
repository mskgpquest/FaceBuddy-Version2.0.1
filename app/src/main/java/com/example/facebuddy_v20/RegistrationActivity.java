package com.example.facebuddy_v20;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {
  private CountryCodePicker ccp;
  
  private String checker = "";
  
  private EditText codeText;
  
  private Button continueAndNextBtn;
  
  private ProgressDialog loadingBar;
  
  private Button loginbtn;
  
  private FirebaseAuth mAuth;
  
  private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
  
  private PhoneAuthProvider.ForceResendingToken mResendToken;
  
  private String mVerificationId;
  
  private String phoneNumber = "";
  
  private EditText phoneText;
  
  private RelativeLayout relativeLayout;
  
  private void sendUserToMainActivity() {
    startActivity(new Intent((Context)this, ContactsActivity.class));
    finish();
  }
  
  private void signInWithPhoneAuthCredential(PhoneAuthCredential paramPhoneAuthCredential) {
    this.mAuth.signInWithCredential((AuthCredential)paramPhoneAuthCredential).addOnCompleteListener((Activity)this, new OnCompleteListener<AuthResult>() {
          public void onComplete(Task<AuthResult> param1Task) {
            if (param1Task.isSuccessful()) {
              RegistrationActivity.this.loadingBar.dismiss();
              Toast.makeText((Context)RegistrationActivity.this, "Congratulations! You are logged in succesfully !", Toast.LENGTH_LONG).show();
              RegistrationActivity.this.sendUserToMainActivity();
              return;
            } 
            RegistrationActivity.this.loadingBar.dismiss();
            String str = param1Task.getException().toString();
            RegistrationActivity registrationActivity = RegistrationActivity.this;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error : ");
            stringBuilder.append(str);
            Toast.makeText((Context)registrationActivity, stringBuilder.toString(), 1).show();
          }
        });
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_registration);
    this.mAuth = FirebaseAuth.getInstance();
    this.loadingBar = new ProgressDialog((Context)this);
    this.phoneText = (EditText)findViewById(R.id.phoneText);
    this.codeText = (EditText)findViewById(R.id.codeText);
    this.continueAndNextBtn = (Button)findViewById(R.id.continueNextButton);
    this.loginbtn = (Button)findViewById(R.id.loginbtn);
    this.relativeLayout = (RelativeLayout)findViewById(R.id.phoneAuth);
    CountryCodePicker countryCodePicker = (CountryCodePicker)findViewById(R.id.ccp);
    this.ccp = countryCodePicker;
    countryCodePicker.registerCarrierNumberEditText(this.phoneText);
    this.continueAndNextBtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (RegistrationActivity.this.continueAndNextBtn.getText().equals("Submit") || RegistrationActivity.this.checker.equals("Code Sent")) {
              String str = RegistrationActivity.this.codeText.getText().toString();
              if (str.equals("")) {
                Toast.makeText((Context)RegistrationActivity.this, "Please write Verification Code", Toast.LENGTH_LONG).show();
              } else {
                RegistrationActivity.this.loadingBar.setTitle("Code Verfication");
                RegistrationActivity.this.loadingBar.setMessage("Please wait, while we are verifying your code");
                RegistrationActivity.this.loadingBar.setCanceledOnTouchOutside(false);
                RegistrationActivity.this.loadingBar.show();
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(RegistrationActivity.this.mVerificationId, str);
                RegistrationActivity.this.signInWithPhoneAuthCredential(phoneAuthCredential);
              } 
              return;
            } 
            RegistrationActivity registrationActivity = RegistrationActivity.this;
            RegistrationActivity.access$602(registrationActivity, registrationActivity.ccp.getFullNumberWithPlus());
            if (!RegistrationActivity.this.phoneNumber.equals("")) {
              RegistrationActivity.this.loadingBar.setTitle("Phone Number Verfication");
              RegistrationActivity.this.loadingBar.setMessage("Please wait, while we are verifying your phone number");
              RegistrationActivity.this.loadingBar.setCanceledOnTouchOutside(false);
              RegistrationActivity.this.loadingBar.show();
              PhoneAuthProvider phoneAuthProvider = PhoneAuthProvider.getInstance();
              String str = RegistrationActivity.this.phoneNumber;
              TimeUnit timeUnit = TimeUnit.SECONDS;
              RegistrationActivity registrationActivity1 = RegistrationActivity.this;
              phoneAuthProvider.verifyPhoneNumber(str, 60L, timeUnit, (Activity)registrationActivity1, registrationActivity1.mCallbacks);
              return;
            } 
            Toast.makeText((Context)RegistrationActivity.this, "Please give a Valid Phone Number", 1).show();
          }
        });
    this.mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        public void onCodeSent(String param1String, PhoneAuthProvider.ForceResendingToken param1ForceResendingToken) {
          super.onCodeSent(param1String, param1ForceResendingToken);
          RegistrationActivity.access$402(RegistrationActivity.this, param1String);
          RegistrationActivity.access$1002(RegistrationActivity.this, param1ForceResendingToken);
          RegistrationActivity.this.relativeLayout.setVisibility(8);
          RegistrationActivity.access$102(RegistrationActivity.this, "Code Sent");
          RegistrationActivity.this.continueAndNextBtn.setText("Submit");
          RegistrationActivity.this.codeText.setVisibility(0);
          RegistrationActivity.this.loadingBar.dismiss();
          Toast.makeText((Context)RegistrationActivity.this, "Code has been sent, Please check !", 1).show();
        }
        
        public void onVerificationCompleted(PhoneAuthCredential param1PhoneAuthCredential) {
          RegistrationActivity.this.signInWithPhoneAuthCredential(param1PhoneAuthCredential);
        }
        
        public void onVerificationFailed(FirebaseException param1FirebaseException) {
          if (param1FirebaseException instanceof com.google.firebase.auth.FirebaseAuthInvalidCredentialsException) {
            Toast.makeText((Context)RegistrationActivity.this, "Invalid Phone Number !!!", 1).show();
          } else if (param1FirebaseException instanceof com.google.firebase.FirebaseTooManyRequestsException) {
            Toast.makeText((Context)RegistrationActivity.this, "SMS quota with this phone number has exceeded, Please try again later !!!", 1).show();
          } 
          RegistrationActivity.this.loadingBar.dismiss();
          RegistrationActivity.this.relativeLayout.setVisibility(0);
          RegistrationActivity.this.continueAndNextBtn.setText("Continue");
          RegistrationActivity.this.codeText.setVisibility(8);
        }
      };
    this.loginbtn.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent((Context)RegistrationActivity.this, LoginActivity.class);
            RegistrationActivity.this.startActivity(intent);
            RegistrationActivity.this.finish();
          }
        });
  }
  
  protected void onStart() {
    super.onStart();
    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
      startActivity(new Intent((Context)this, ContactsActivity.class));
      finish();
    } 
  }
}


/* Location:              C:\Users\dell\Desktop\Decompilation\dex2jar-2.0\classes-dex2jar.jar!\com\example\facebudduy\RegistrationActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */