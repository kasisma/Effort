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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PasswordReset extends AppCompatActivity {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore mstore=FirebaseFirestore.getInstance();
    EditText editText,editText2;
    public static final String TAG = "비밀번호변경 ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        Log.e(TAG,"onCreate");

        editText =findViewById(R.id.textView);
        editText2=findViewById(R.id.textView2);

        findViewById(R.id.button2).setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.button:

                    break;
                case  R.id.button2:
                    send();
                    break;
            }
        }
    };

    private  void send(){
        String email=((EditText) findViewById(R.id.textView)).getText().toString();

        if(email.length()>0){

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                starToast("이메일을 보냈습니다 ");
                                startloginAcitivity();

                            }
                            else {
                                starToast("없는 이메일 입니다");
                            }
                        }
                    });
        }else {
            starToast("이메일을입력해주세요");
        }

    }

    private void starToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private  void startMainAcitivity(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
