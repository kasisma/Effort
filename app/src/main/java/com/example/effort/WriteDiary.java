package com.example.effort;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;

public class WriteDiary extends AppCompatActivity {

    private FirebaseUser user;
    private ArrayList<String> pathlist=new ArrayList<>();
    private LinearLayout parent;// 더해지는 레이아웃의 부모
    private int pattCount, successCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);


        findViewById(R.id.check).setOnClickListener(onClickListener);
        findViewById(R.id.btImage).setOnClickListener(onClickListener);
        String s;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.check:
                    Diaryupdate();
                    break;
                case R.id.btImage:
                    goToAlbum();
                    break;
            }
        }
    };

    public void Diaryupdate() {
        final String title = ((EditText) findViewById(R.id.tvTitle)).getText().toString();
        final String content = ((EditText) findViewById(R.id.tvcoentent)).getText().toString();

        if (title.length() > 0 && content.length() > 0) {

            final ArrayList<String> contentsList=new ArrayList<>();
            user = FirebaseAuth.getInstance().getCurrentUser();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();

            for(int i=0;i<parent.getChildCount();i++){
                View view=parent.getChildAt(i);
                if(view instanceof EditText){
                    String text=((EditText)view).getText().toString();
                    if(text.length()>0){
                        contentsList.add(text);
                    }
                }else{
                    contentsList.add(pathlist.get(pattCount));  //이미지뷰만 있을경우  pathlist 와 successCount 같으면 완료
                    final StorageReference mountainImagesRef = storageRef.child("users/"+user.getUid()+"/"+pattCount+".jpg");
                    try{
                        InputStream stream = new FileInputStream(new File(pathlist.get(pattCount)));
                        StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + (contentsList.size() - 1)).build(); // 메타 데이터 추가 해주었음
                        UploadTask uploadTask = mountainImagesRef.putStream(stream,metadata);
                        uploadTask.addOnFailureListener(new OnFailureListener() {//메타데이터 가져오기
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index")); //int로 변영해서 인덱스 값을 얻음
                                mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {// Uri를 가져오기 위한 작업
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        contentsList.set(index,uri.toString());
                                        successCount++;
                                        if(pathlist.size()==successCount){ //리스트와 카운터가 같으면 성공
                                            DiaryData writeDiary = new DiaryData(title, contentsList, user.getUid(),new Date());
                                            upload(writeDiary);
                                            for(int a=0;a<contentsList.size();a++){
                                                Log.e("콘텐츠 목록",""+contentsList.get(a)); //콘텐츠 목록을 알수있다
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }catch (Exception e) {
                        //에러시 수행
                        Log.e("로그","에러:이유가 뭐죠?"+e.toString());
                        Toast.makeText(this, "에러2", Toast.LENGTH_SHORT).show();
                    }

                    pattCount++;
                }
            }        if(pathlist.size()==0){
                DiaryData writeDiary = new DiaryData(title, contentsList, user.getUid(),new Date());
                upload(writeDiary);
            }
        }

        else {

        }
    }

    private void upload(DiaryData diaryData) {//document 자동으로 document 아이디가 차곡차곡 들어간다
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("diary").add(diaryData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }


    private void goToAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 02);
    }

    private void myActivivty(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    Bitmap bitmap;

   private String profilePath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 02: {
                if (resultCode == Activity.RESULT_OK) {

                    try {
                        InputStream in = getContentResolver().openInputStream(data.getData());
                        Bitmap img = BitmapFactory.decodeStream(in);
                        in.close();
                        bitmap=img;
                        SaveBitmapToFileCache(img);

                    } catch (Exception e) {

                    }


                    parent = findViewById(R.id.contexts);

                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    Log.e("부모그룹","부모그룹생성");
                    ImageView imageView = new ImageView(WriteDiary.this);
                    imageView.setLayoutParams(layoutParams);
                    Glide.with(this).load(profilePath).override(500,500).into(imageView);
                    parent.addView(imageView);


                    EditText editText = new EditText(WriteDiary.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
                    parent.addView(editText);


                }
                break;
            }
        }
    }

    private void SaveBitmapToFileCache(Bitmap bitmap) {
        Log.e("change","변환 실행");
        File mFile = new File(getExternalFilesDir(null), bitmap.toString()+".jpg");//patent 객체 폴더에 child라는 객체생성
        mFile.delete();
        Log.e("f","파일객체 없음"+mFile.toString());
        Log.e("vkdlf","mfile객체 생성"+mFile);
        OutputStream out = null;
            try {

                mFile.createNewFile();
                out = new FileOutputStream(mFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                Log.e("파일","파일객체생성"+mFile.toString());
                profilePath=mFile.toString();
                pathlist.add(profilePath);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


    }
}