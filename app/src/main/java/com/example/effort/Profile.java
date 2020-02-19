package com.example.effort;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Profile extends AppCompatActivity {

    private static final int REQUEST_CODE = 0;
    private ImageView imageView;
    EditText editText,editText2;
    private File tempFile;
    private final int CAPTURE_IMAGE = 1111;
    private static final int PICK_FROM_CAMERA = 2;
    private static final int PICK_FROM_ALBUM = 1;
    private String profilePath;
    private StorageReference storageRef;
    private FirebaseUser user;
    private Bitmap bitmap;

    public static final String TAG = "프로파일 입력 ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        editText =findViewById(R.id.textView2);

        Intent intent =new Intent(this,ProfileSet.class);
        intent.putExtra("image",profilePath);//프로파일 셋에 스트링형식으로 비트맵보냄

        findViewById(R.id.button4).setOnClickListener(onClick);

        final CharSequence[] oItems = {"갤러리에서 사진가져오기", "사진찍기"};
        final AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

        imageView=(ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oDialog.setTitle("프로필 사진")
                        .setItems(oItems, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(which==0){
                                    goToAlbum();
                                }
                                else if(which==1){
                                    camerado();
                                }

                                Toast.makeText(getApplicationContext(),
                                        oItems[which], Toast.LENGTH_LONG).show();
                            }
                        })
                        .setCancelable(false)
                        .show();

            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void camerado(){
        myActivivty(CameraActivity.class);

    }



    private void goToAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE);
    }

    View.OnClickListener onClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.button4:
                   profileUpdata();
                    break;
            }
        }
    };

    private  void profileUpdata(){


        final  String name=((EditText) findViewById(R.id.textView2)).getText().toString();
        final  String phome=((EditText) findViewById(R.id.textView3)).getText().toString();
        final  String birthday=((EditText) findViewById(R.id.textView4)).getText().toString();
        final   String addres=((EditText) findViewById(R.id.textView5)).getText().toString();

        Log.i("메소드","클릭된다");
        if(name.length()>0&&phome.length()>0&&birthday.length()>0&&addres.length()>0){

            FirebaseStorage storage = FirebaseStorage.getInstance();
            // Create a storage reference from our app
            StorageReference storageRef = storage.getReference();

            final  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference mountainImagesRef = storageRef.child("users/"+user.getUid()+"profileImage.jpg");

            try{
                InputStream stream = new FileInputStream(profilePath);
                UploadTask  uploadTask = mountainImagesRef.putStream(stream);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return mountainImagesRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.e("성공","성공"+downloadUri);
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            MemberInfo memberInfo = new MemberInfo(name, phome, birthday,addres,downloadUri.toString());
                            db.collection("user").document(user.getUid()).set(memberInfo)//문서이름을 유저uid로 저장
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(Profile.this, "성공!!!!", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(Profile.this, "실패?!?!?", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Handle failures
                            Toast.makeText(Profile.this, "에러1", Toast.LENGTH_SHORT).show();
                         Log.e("로그","에러ㅠㅠ:");
                        }
                    }
                });
            }catch (Exception e) {
                //에러시 수행
                Log.e("로그","에러:이유가 뭐죠?"+e.toString());
                Toast.makeText(this, "에러2", Toast.LENGTH_SHORT).show();
            }

        }else {
            starToast("회원정보를 입력해주세요");

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case (12):{
                if(resultCode== Activity.RESULT_OK){
                   profilePath=data.getStringExtra("profilePath");
                   Log.e("로그","된다되노디ㅗㄴㄷ다!!:"+profilePath);//이미지값을 스트링으로 불러옴
                   Bitmap bitmap=BitmapFactory.decodeFile(profilePath);//불러온 이미지 값을 비트맵형식으로 저장
                    imageView.setImageBitmap(bitmap);//이미지에 비트맵으로 저장

                }
            }
        }

        if(requestCode == REQUEST_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                try{
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    bitmap=img;
                    Log.e("ff","배트맵"+img.toString());
                    imageView.setImageBitmap(img);
                    SaveBitmapToFileCache(img);//비트맵을 파일 형식으로 저장
//                    File mFile = new File(getExternalFilesDir(null), "profileImage.jpg");//파일 생성
//                    Intent resultent=new Intent();
//                    resultent.putExtra("profilePath",mFile.toString());
//                    Log.e("f",mFile.toString());
//                    setResult(Activity.RESULT_OK,resultent);

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
    private  void myActivivty(Class c){
        Intent intent = new Intent(this,c);
        startActivityForResult(intent,12);
    }
    private void SaveBitmapToFileCache(Bitmap bitmap) {

        File mFile = new File(getExternalFilesDir(null), "profileImage.jpg");
        OutputStream out = null;

        try
        {
            mFile.createNewFile();
            out = new FileOutputStream(mFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            profilePath=mFile.toString();
            Log.e("이미지",profilePath);
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
