package com.fasheonic.fasheonic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    Button login;
    TextView signuptext;

    FirebaseAuth auth;
    ProgressDialog pd;
    TextView user_full_name,user_email;
//    DatabaseReference reference;
//    ProgressDialog pd;



    //    private TextView Signup;
    private TextView pass;
    //    private Button loginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_full_name=findViewById(R.id.user_full_name);
        user_email=findViewById(R.id.user_email);
        auth=FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);
//        Signup = (TextView) findViewById(R.id.signuptext);
        pass=(TextView)findViewById(R.id.forgotpass);
//        loginbtn=(Button)findViewById(R.id.LoginBtn);
//
//        loginbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent homeintent = new Intent(LoginActivity.this,HomeActivity.class);
//                startActivity(homeintent);
//            }
//        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pass = new Intent(LoginActivity.this,ForgetPass.class);
                startActivity(pass);
            }
        });
//
//        Signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent SignupAct = new Intent(LoginActivity.this,SignUpActivity.class);
//                startActivity(SignupAct);
//
//
//            }
//        });

        email = findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        //signuptext=findViewById(R.id.signuptext);

        auth=FirebaseAuth.getInstance();

//        signuptext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
//            }
//        });


//        login.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String str_email=email.getText().toString();
//                String str_password=password.getText().toString();
//
//                if(TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)){
//                    Toast.makeText(LoginActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
//                }
//                else{
//
//                    auth.signInWithEmailAndPassword(str_email,str_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if(task.isSuccessful()){
//                                FirebaseUser user=auth.getCurrentUser();
//                                if(user.getDisplayName()!=null){
//                                    user_full_name.setText(user.getDisplayName());
//
//                                }
//                                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
//                                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(intent);
//
//                            }
//                            else{
//                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    });
//
//
//
//
//
//
//
//                }
//
//
//
//
//
//            }
//        });
//
//
//
//














































        ////VIDEO WALA

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd=new ProgressDialog(LoginActivity.this);
                pd.setMessage("Please Wait...");
                pd.show();

                String str_email=email.getText().toString();
                String str_password=password.getText().toString();

                if(TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)){
                    Toast.makeText(LoginActivity.this,"All fields are required",Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.signInWithEmailAndPassword(str_email,str_password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        ////registered login
                                        FirebaseUser user = auth.getCurrentUser();
                                        Toast.makeText(LoginActivity.this,"Welcome "+user.toString(),Toast.LENGTH_LONG).show();
                                        //Intent intentA=new Intent(LoginActivity.this,HomeActivity.class);
                                        //startActivity(intentA);
                                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                pd.dismiss();
                                                Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                                intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK | intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                pd.dismiss();

                                            }
                                        });
                                    } else{
                                        pd.dismiss();
                                        Toast.makeText(LoginActivity.this,"Authentication failed",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });


    }
}
