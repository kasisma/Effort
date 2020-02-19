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

public class LoginActivity extends AppCompatActivity  {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore mstore=FirebaseFirestore.getInstance();
    EditText editText,editText2;
    public static final String TAG = "로그인 화면";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.e(TAG,"onCreate");

        editText =findViewById(R.id.textView);
        editText2=findViewById(R.id.textView2);

        findViewById(R.id.button).setOnClickListener(onClickListener);
        findViewById(R.id.button2).setOnClickListener(onClickListener);
        findViewById(R.id.button3).setOnClickListener(onClickListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.button:
                    startjoinAcitivity();
                    break;
                case  R.id.button2:
                    login();
                    break;
                case  R.id.button3:
                    findAcitivity();
                    break;
            }
        }
    };

    private  void login(){
        String email=((EditText) findViewById(R.id.textView)).getText().toString();
        String password=((EditText) findViewById(R.id.textView2)).getText().toString();

        if(email.length()>0&&password.length()>0){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                startMainAcitivity();
                                starToast("로그인에 성공했습니다 ");
                                FirebaseUser user = mAuth.getCurrentUser();
                                myActivivty(MainActivity.class);
                            } else {
                                // If sign in fails, display a message to the user.
                                if(task.getException()!=null){
                                    starToast(task.getException().toString());
                                }
                            }
                        }
                    });
        }else {
            starToast("이메일 또는 비밀번호를 입력해주세요");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==21){
            if(resultCode==RESULT_OK) {
                Intent intent=getIntent();
                editText =findViewById(R.id.textView);
                editText2=findViewById(R.id.textView2);

                editText.setText(intent.getStringExtra("id"));

                Log.v("MainActivity", "Main2Acitiy result : " + data.getStringExtra("result"));
            }
        }
    }

    private void starToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private  void startMainAcitivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivityForResult(intent,21);
    }

    private  void startjoinAcitivity(){
        Intent intent = new Intent(this,Sign.class);
        startActivity(intent);
    }
    private  void findAcitivity(){
        Intent intent = new Intent(this,PasswordReset.class);
        startActivity(intent);
    }

    private  void myActivivty(Class c){
        Intent intent = new Intent(this,c);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
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
