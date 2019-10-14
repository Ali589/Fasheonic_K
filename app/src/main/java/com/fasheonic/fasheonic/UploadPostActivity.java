package com.fasheonic.fasheonic;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class UploadPostActivity extends AppCompatActivity {

    Uri imageUrl;
    String myUrl="";
    StorageTask uploadtask;
    StorageReference storagereference;

    ImageView close,image_added;
    TextView post;
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);

        close=findViewById(R.id.close);
        image_added=findViewById(R.id.image_added);
        post=findViewById(R.id.post);
        description=findViewById(R.id.description);

        storagereference= FirebaseStorage.getInstance().getReference("posts");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UploadPostActivity.this,HomePg.class));
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        CropImage.activity()
                .setAspectRatio(1,1)
                .start(UploadPostActivity.this);
    }

    private String getFileExtensions(Uri uri)
    {
        ContentResolver contentresolver=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentresolver.getType(uri));
    }

    public void uploadImage(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Posting");
        progressDialog.show();

        if(imageUrl!=null)
        {
            final StorageReference filereference = storagereference.child(System.currentTimeMillis()
                    + "."+  getFileExtensions(imageUrl));

            uploadtask=filereference.putFile(imageUrl);
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful())
                    {
                        throw  task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUrl=downloadUri.toString();
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("posts");
                        String postid=reference.push().getKey();


                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("postid",postid);
                        hashMap.put("postImage",myUrl);
                        hashMap.put("description",description.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//
//                          //Testing//
//                        for (int i=0; i<1;i++){
                            reference.child(postid).setValue(hashMap);
                            //reference.child(postid).setValue(myUrl);
                            //reference.child(postid).setValue(description.getText().toString());
                           // reference.child(postid).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                        }

                        //reference.child(postid).setValue(description.getText().toString());
//
//                        reference.child(postid).setValue(hashMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(UploadPostActivity.this,HomeActivity.class));
                        finish();

                    }
                    else{
                        Toast.makeText(UploadPostActivity.this,"Failed",Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadPostActivity.this,""+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        } else{
            Toast.makeText(UploadPostActivity.this,"No image Selected",Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && requestCode == RESULT_OK)
//        {
//            CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            imageUrl = result.getUri();
//            image_added.setImageURI(imageUrl);
//        }
//        else{
//            Toast.makeText(this,"Something Gone Wrong",Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(UploadPostActivity.this,HomeActivity.class));
//            finish();
//        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUrl = result.getUri();
                image_added.setImageURI(imageUrl);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this,""+error,Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UploadPostActivity.this,HomeActivity.class));
                finish();


        }
    }
    }
}
