package com.fasheonic.fasheonic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    EditText username,fullname,email,password;
//    Button register;
    TextView logintext;
    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username=findViewById(R.id.username);
        fullname=findViewById(R.id.fullname);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        logintext=findViewById(R.id.logintext);
        register=findViewById(R.id.register);

        auth=FirebaseAuth.getInstance();

        logintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd=new ProgressDialog(SignUpActivity.this);
                pd.setMessage("Please Wait...");
                pd.show();

                String str_username=username.getText().toString();
                String str_fullname=fullname.getText().toString();
                String str_email=email.getText().toString();
                String str_password=password.getText().toString();

                if(TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname) ||
                TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)){
                    Toast.makeText(SignUpActivity.this,"All filled are required",Toast.LENGTH_SHORT).show();

                } else if(str_password.length()<6){
                    Toast.makeText(SignUpActivity.this,"Password must have 6 characters",Toast.LENGTH_SHORT).show();

                } else{
                    Register(str_username,str_fullname,str_email,str_email);
                }
            }
        });


       register.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent SignupAct = new Intent(SignUpActivity.this,LoginActivity.class);
              startActivity(SignupAct);
              finish();

           }
        });

   }

   private void Register(final String username, final String fullname, String email, String password){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                   if(task.isSuccessful()){
                                FirebaseUser firebaseUser=auth.getCurrentUser();
                                firebaseUser.sendEmailVerification();
                                String userid=firebaseUser.getUid();

                            reference= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username.toLowerCase());
                            hashMap.put("fullname",fullname);
                            hashMap.put("bio","");
                            hashMap.put("imageurl","https://firebasestorage.googleapis.com/v0/b/fasheonic-895ca.appspot.com/o/logo.png?alt=media&token=1d892c1a-b417-4e36-b30c-de94c76b7f45");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        pd.dismiss();

                                        Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else{
                            pd.dismiss();
                            Toast.makeText(SignUpActivity.this,"You cant Register with this email or password",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

   }
}
