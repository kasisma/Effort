package com.example.effort;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.effort.adapters.PostAdepter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProfileSet extends AppCompatActivity implements View.OnClickListener {
    FirebaseUser firebaseUser;
    public static final String TAG = "프로파일 결과";

    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    private RecyclerView mpostRecyler;
    private PostAdepter mAdapter;
    private List<Post> mDatas;
    FirebaseDatabase database;
    private RecyclerView.LayoutManager mLayoutManager;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set);
        Log.e(TAG,"onCreate");

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myActivivty(Profile.class);
            }
        });

        findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                    myActivivty(LoginActivity.class);
            }
        });





    }

    public void setprofile(){
        final ImageView imageView=findViewById(R.id.imageView2);
        final TextView textView=findViewById(R.id.textView1);
        final TextView textView2=findViewById(R.id.textView2);
        final TextView textView3=findViewById(R.id.textView3);
        final TextView textView1=findViewById(R.id.tvTitle);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("user").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            if(document.getData().get("photoUri") != null){

                                Glide.with(ProfileSet.this).load(document.getData().get("photoUri")).centerCrop().override(500,500).into(imageView);

                            }
                            Log.d("hj", "DocumentSnapshot data: " + document.getData());
                            Toast.makeText(ProfileSet.this, "실행", Toast.LENGTH_SHORT).show();
                            textView.setText("이름:"+document.getData().get("name").toString());
                            textView2.setText("나이:"+document.getData().get("phone").toString());
                            textView3.setText("목표:"+document.getData().get("birthDay").toString());
                            textView1.setText("다짐:"+document.getData().get("addres").toString());

                        } else {
                            Log.d("gfdf", "No such document");
                        }
                    }
                } else {
                    Log.d("tr", "get failed with ", task.getException());
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
            startActivity(new Intent(this,AddEffort.class));
    }

    private  void myActivivty(Class c){
        Intent intent = new Intent(this,c);
        startActivityForResult(intent,12);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        setprofile();


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
    }

}
