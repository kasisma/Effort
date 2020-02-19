package com.example.effort;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "메인 화면";
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"onCreate");
        setContentView(R.layout.activity_main);

        init();

        findViewById(R.id.layouotpro).setOnClickListener(onClickListener);
        findViewById(R.id.layoutplan).setOnClickListener(onClickListener);
        findViewById(R.id.layouttime).setOnClickListener(onClickListener);
        findViewById(R.id.layoutDairy).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.layouotpro:
                    myActivivty(ProfileSet.class);
                    break;
                case  R.id.layoutplan:
                    myActivivty(Plan.class);
                    break;
                case  R.id.layouttime:
                        myActivivty(Study.class);
                    break;
                case R.id.layoutDairy:
                    myActivivty(Diary.class);
                    break;
            }
        }
    };



    private  void myActivivty(Class c){
        Intent intent = new Intent(this,c);
        startActivity(intent);
    }



    public void init(){
        if(user==null){
            myActivivty(LoginActivity.class);
        }else {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("user").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document!=null){
                            if (document.exists()){

                                //
                                Toast.makeText(MainActivity.this, "회원정보 있음", Toast.LENGTH_SHORT).show();
                            } else {
                                myActivivty(Profile.class);
                                Toast.makeText(MainActivity.this, "회원정보가 없습니다", Toast.LENGTH_SHORT).show();
                                myActivivty(Profile.class);//회원 정보가 없을시 프로파일일력창으로 이동
                            }
                        }
                    } else {

                    }
                }
            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                init();
                break;
        }
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
