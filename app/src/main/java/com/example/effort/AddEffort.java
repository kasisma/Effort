package com.example.effort;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AddEffort extends AppCompatActivity {

    public static final String TAG = "포스터 어뎁터";
    private static final int REQUEST_CODE = 0;
    private String profilePat=" ";
    FirebaseUser user;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance(); //유저 정보를 가져옴
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
  static  int j;
    private ArrayList<String> pathList = new ArrayList<>();//이미지 경로
    private LinearLayout parent;
    private int pathCount, successCount;
    int sy=0, sm=0, sd=0, h=0, mi=0;
    int ey=0, em=0,ed=0;
    Button stardate,dendate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_effort);

        parent=findViewById(R.id.contextlayout);
        stardate=findViewById(R.id.button14);
        dendate=findViewById(R.id.button16);
        findViewById(R.id.button14).setOnClickListener(onClickListener);
        findViewById(R.id.button12).setOnClickListener(onClickListener);
        findViewById(R.id.button10).setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.button10:
                postUploader();
                    break;
                case R.id.button12:
                    goToAlbum();
                    break;
                case R.id.button14:
                    showDate();
                    break;
                case R.id.button16:
                    showDate();
                    break;
            }
        }
    };

    void showDate() {
        Calendar calendar=new GregorianCalendar(Locale.KOREA);
        int toYear=calendar.get(Calendar.YEAR);
        int toMonth=calendar.get(Calendar.MONTH);
        int toDay=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                sy = year;
                sm = month+1;
                sd = dayOfMonth;
                Log.e("day",""+sy+sm+sd);
                stardate.setText(sy+":"+sm+":"+sd);
            }
        },toYear, toMonth, toDay);

        datePickerDialog.setMessage("시작날짜");
        datePickerDialog.show();
    }
    private void postUploader() {//파일 업로드 메소드
        final String title = ((EditText) findViewById(R.id.post_title)).getText().toString();

        if (title.length() > 0 ) {// 제한 제목과 내용이 있어야 한다
            final ArrayList<String> contentsList=new ArrayList<>();
            int pathCount=0;
             user=FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            for(int i = 0; i < parent.getChildCount(); i++){//리니어 레이아웃의 숫자만큼
                    View view=parent.getChildAt(i);
                    if(view instanceof EditText){//형변환이 가능하다면~~ 이라는 뜻
                        String text=((EditText)view).getText().toString();//뷰에 있는 에딧테스트를 스트령 형식으로 저장
                        if(text.length()>0){
                            contentsList.add(text);    //콘테인 리스트에 더해진다
                        }
                    }else{
                        contentsList.add(pathList.get(pathCount));//페스 리스트르 담는다
                        final StorageReference mountainImagesRef = storageRef.child("users/" + user.getUid() + "/"+pathCount+".jpg");

                        try {
                            //InputStream stream = new FileInputStream(profilePath);
                            InputStream stream = new FileInputStream(profilePat);
                            StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index",""+pathCount).build();
                            UploadTask uploadTask = mountainImagesRef.putStream(stream);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                        @Override
                                        public void onSuccess(Uri uri) {

                                            Log.e("로그","uri:"+uri);
                                        }
                                    });


                                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                    // ...
                                }
                            });
                        } catch (FileNotFoundException e) {
                            Log.e("로그", "에러: " + e.toString());
                        }
                        pathCount++;
                    }


            }

            WriteInfo writeInfo = new WriteInfo(title,"f",user.getUid());
            Uploader(writeInfo);
        } else {

        }
    }


    private void Uploader(WriteInfo writeInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("post").add(writeInfo)
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
                        Log.w("dsf", "Error adding document", e);
                    }
                });
    }

    private void goToAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                try{
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    pathList.add(profilePat);
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    String r= Integer.toString(j);
                    profilePat=SaveBitmapToFileCache(img);
                    ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);//dl부분 공부하시오

                    ImageView imageView=new ImageView(AddEffort.this);
                    imageView.setLayoutParams(layoutParams);
                    Glide.with(this).load(profilePat).override(500,500).into(imageView);
                    parent.addView(imageView);

                    EditText editText=new EditText(AddEffort.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE|InputType.TYPE_CLASS_TEXT);//이부분 공부
                    parent.addView(editText);

//                    bitmap=img;
//                    imageView.setImageBitmap(img);
//                    SaveBitmapToFileCache(img);//비트맵을 파일 형식으로 저장

                }catch(Exception e)
                {

                }
            }
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String SaveBitmapToFileCache(Bitmap bitmap) {
        String im="";
        File mFile = new File(getExternalFilesDir(null), "profileImage.jpg");
        OutputStream out = null;


        try
        {
            im=mFile.toString();
            mFile.createNewFile();
            out = new FileOutputStream(mFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }


        return im;
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
