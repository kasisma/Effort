package com.example.effort;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddPlan extends AppCompatActivity {

    public static final String TAG = "일정추가";
    FirebaseUser user;
    private FirebaseDatabase database;
    AddPlanData addPlanData;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance(); //유저 정보를 가져옴
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();;

    private Button button;
    private EditText title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plan);
        button = findViewById(R.id.button8);
        title=findViewById(R.id.textView10);
        Log.e(TAG, "onCreate");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AddPlanData memberInfo = new AddPlanData(title.getText().toString(), "", "");

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("plan").add(memberInfo)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("fds", "DocumentSnapshot written with ID: " + documentReference.getId());
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddPlan.this, "실행안됨", Toast.LENGTH_SHORT).show();
                                Log.w("dsf", "Error adding document", e);
                            }
                        });

            }
        });
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
