package com.example.effort;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class Diary extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DiaryAdapter adapter;
    private RecyclerView recyclerView;
    private  RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);

        Log.e("테스트","oncreate");
        findViewById(R.id.addDiary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myActivivty(WriteDiary.class);
            }
        });

       final ArrayList<DiaryData> arrayList =new ArrayList<>();

       final ArrayList<String> ef=new ArrayList<>();
       final Date dd=new Date();



        FirebaseFirestore db=FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("user").document(user.getUid());
        db.collection("diary")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("f", document.getId() + " => " + document.getData());
                                arrayList.add(new DiaryData(
                                        document.getData().get("title").toString(),
                                        (ArrayList<String>)document.getData().get("contents"),
                                        document.getData().get("publisher").toString(),
                                        new Date(document.getDate("createdAt").getTime())));
                                Log.e("데이터","제발 되주세요 "+task.getException());
                                Log.e("저장된 데이터",document.getData().get("title").toString());
                            }
                        } else {
                            Log.d("f", "Error getting documents: ", task.getException());
                        }
                    }
                });





        recyclerView=findViewById(R.id.recycleviewDairy);
        recyclerView.setHasFixedSize(true);//리사이클러뷰 가존성능강화
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter=new DiaryAdapter(arrayList,this);
        recyclerView.setAdapter(adapter);



    }

    private  void myActivivty(Class c){
        Intent intent = new Intent(this,c);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
       Log.e("테스트","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
       Log.e("테스트","resume")  ;  }
}
