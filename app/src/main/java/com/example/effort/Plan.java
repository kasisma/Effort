package com.example.effort;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Plan extends AppCompatActivity implements TextWatcher {
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private List<AddPlanData> memoList;
    private RecyclerView recyclerView;
    private PlanAdapter adapter;
    private  RecyclerView.LayoutManager layoutManager;
    private  ArrayList<AddPlanData> planlist;
    private Button button;
    public static final String TAG = "일정창 ";
    private CalendarView calendarView;
    private TextView editText;
    private String setday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        button=findViewById(R.id.button6);
        button.setOnClickListener(onClickListener);
        editText=findViewById(R.id.editText3 );
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);//리사이클러뷰 가존성능강화
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;
        planlist=new ArrayList<>();//plan데이터를 담을 어레이 리스트
        planlist.add(new AddPlanData("오늘","진짜열심히","2020-02-16"));
        planlist.add(new AddPlanData("오늘","진짜열심히","2020-02-16"));

        adapter=new PlanAdapter(planlist,this);
        recyclerView.setAdapter(adapter);//리사이클러뷰에 어댑터 연결

        calendarView=findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String y=Integer.toString(year);
                String m=Integer.toString(month);
                String dm=Integer.toString(dayOfMonth);
                editText.setText(y+"-"+m+"-"+dm);
            }
        });

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
                case  R.id.button6:
                    myActivivty(AddPlan.class);
                    break;

            }
        }
    };

    private  void myActivivty(Class c){
        Intent intent = new Intent(this,c);
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


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
//        adapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
