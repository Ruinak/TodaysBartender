package com.cos.todaysbartender.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.cos.todaysbartender.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {
    private static final String TAG = "PasswordResetActivity";

    // FirebaseAuth 객체 생성
    private FirebaseAuth firebaseAuth;

    // 전역변수로 Button, TextInputEditText 선언
    private TextInputEditText etEmail;
    private Button btnSend, btnGoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        init();
        initLr();
    }

    // id 는 init 에서 호출
    public void init() {
        firebaseAuth = FirebaseAuth.getInstance();

        btnSend = findViewById(R.id.btnSend);
        btnGoLogin = findViewById(R.id.btnGoLogin);
        etEmail = findViewById(R.id.etEmail);
    }

    // Listener 는 initLr 에서 호출
    private void initLr() {
        // 전송 버튼시 이메일 발송
        btnSend.setOnClickListener(v -> {
            Send();
        });
        // 돌아가기 버튼 클릭시 LoginActivity 로 이동
        btnGoLogin.setOnClickListener(v -> {
            startMyActivity(LoginActivity.class);
        });
    }

    // 전송 클릭시 입력된 이메일로 메일 발송하기
    private void Send() {
        String email = etEmail.getText().toString();

        // 이메일 입력여부 확인
        if(email.length() > 0 ) {
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast("이메일을 보냈습니다.");
                    }
                }
            });
        }else {
            Toast("이메일을 입력해주세요.");
        }
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