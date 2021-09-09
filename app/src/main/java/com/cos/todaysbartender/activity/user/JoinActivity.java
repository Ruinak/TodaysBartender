package com.cos.todaysbartender.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cos.todaysbartender.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class JoinActivity extends AppCompatActivity {
    private static final String TAG = "JoinActivity";

    // FirebaseAuth 객체 생성
    private FirebaseAuth firebaseAuth;

    // 전역변수로 Button, TextInputEditText 선언
    private Button btnJoin, btnGoLogin;
    private TextInputEditText etEmail, etPassword, etPasswordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        init();
        initLr();
    }

    // id 는 init 에서 호출
    public void init(){
        firebaseAuth = FirebaseAuth.getInstance();

        btnJoin = findViewById(R.id.btnJoin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordCheck = findViewById(R.id.etPasswordCheck);
        btnGoLogin = findViewById(R.id.btnGoLogin);
    }

    // Listener 는 initLr 에서 호출
    public void initLr(){
        // 회원가입 버튼 클릭시 회원가입 진행
        btnJoin.setOnClickListener(v -> {
            join();
        });

        // 로그인 버튼 클릭시 LoginActivity 로 이동
        btnGoLogin.setOnClickListener(v -> {
            startMyActivity(LoginActivity.class);
        });
    }

    // 회원가입 진행
    private void join() {
        String email = etEmail.getText().toString();
        String password =  etPassword.getText().toString();
        String passwordCheck =  etPasswordCheck.getText().toString();

        // 이메일, 비밀번호, 비밀번호 체크 입력 여부 확인
        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            // 비밀번호와 비밀번호 체크의 일치 여부 확인
            if (password.equals(passwordCheck)) {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // 비밀번호가 일치하는 경우
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast("성공적으로 회원가입이 되었습니다.");
                            startMyActivity(LoginActivity.class);
                        } else {
                            // 일치하지 않는 경우 회원가입 실패
                            if (task.getException() != null) {
                                Toast("다시 확인해주세요");
                            }
                        }
                    }
                });
            } else {
                // 비밀번호가 일치하지 않는 경우
                Toast("비밀번호가 일치하지 않습니다.");
            }
        }else {
            // 이메일이나 비밀번호를 입력하지 않은 경우
            Toast("이메일 또는 비밀번호를 입력해주세요.");
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