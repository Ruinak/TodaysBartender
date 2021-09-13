package com.cos.todaysbartender.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.cos.todaysbartender.R;
import com.cos.todaysbartender.activity.user.JoinActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity2";

    // FirebaseUser
    private FirebaseUser user;

    private ScrollView svMain;
    private AppCompatButton btnGoRecommend, btnGoList, btnGoPopularity, btnGoBoard, btnGoDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        background();
        userCheck();
    }

    // id 는 init 에서 호출
    public void init(){
        svMain = findViewById(R.id.svMain);

        user = FirebaseAuth.getInstance().getCurrentUser();

        btnGoRecommend = findViewById(R.id.btnGoRecommend);
        btnGoList = findViewById(R.id.btnGoList);
        btnGoPopularity = findViewById(R.id.btnGoPopularity);
        btnGoBoard = findViewById(R.id.btnGoBoard);
        btnGoDetail = findViewById(R.id.btnGoDetail);
    }
    public void initLr(){

    }

    // 사용자 확인
    public void userCheck(){
        // 현재 로그인된 사용자가 있는지 확인, 없으면 회원가입 화면으로 보냄
        if(user == null){
            startMyActivity(JoinActivity.class);
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document != null){
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                                // startMyActivity(UserInitActivity.class);
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    // 배경화면에 AnimationDrawable 을 이용해서 색상이 변하는 배경화면 구현
    public void background(){
        AnimationDrawable animationDrawable = (AnimationDrawable) svMain.getBackground();
        animationDrawable.setEnterFadeDuration(1);
        animationDrawable.setExitFadeDuration(1500);
        animationDrawable.start();
    }

    // startMyActivity 에 넣어주는 클래스의 화면으로 이동함
    private void startMyActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // Toast 생성
    private void Toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}