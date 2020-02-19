package com.example.effort;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Sign extends AppCompatActivity {

    public static final String TAG = "회원가입";

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore mstore=FirebaseFirestore.getInstance();
    private EditText emailText,mPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        Log.e(TAG, "onCreate");
        emailText=findViewById(R.id.Email);
        mPasswordText=findViewById(R.id.password);

        findViewById(R.id.btjoin).setOnClickListener(onClickListener);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.e(TAG, "onStart");
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.btjoin:
                            signup();
                    break;
            }
        }
    };

    private  void signup(){
     final    String email=((EditText) findViewById(R.id.Email)).getText().toString();
        final  String password=((EditText) findViewById(R.id.password)).getText().toString();
        final  String passwordcheck=((EditText) findViewById(R.id.passwordcheck)).getText().toString();

        if(email.length()>0&&password.length()>0){
            if(password.equals(passwordcheck)){
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startloginAcitivity();
                                    starToast("회원가입을 성공했습니다 ");

                                    Intent intent =new Intent();
                                    intent.putExtra("id",email);
                                    intent.putExtra("pa",password);
                                    setResult(RESULT_OK);

                                } else {
                                    if(task.getException()!=null){
                                        starToast(task.getException().toString());
                                    }
                                }

                                // ...
                            }
                        });
            }else {
                starToast("비밀번호가 일치하지 않습니다");
            }
        }else {
            starToast("이메일 또는 비밀번호를 입력해주세요");
        }

    }

    private void starToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private  void startloginAcitivity(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart");
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
