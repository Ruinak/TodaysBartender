package com.cos.todaysbartender.activity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cos.todaysbartender.R;
import com.cos.todaysbartender.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    // FirebaseAuth 객체 생성
    private FirebaseAuth firebaseAuth;

    // 전역변수로 Button, TextInputEditText 선언
    private Button btnLogin, btnGoPasswordReset;
    private TextInputEditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        initLr();
    }

    // id 는 init 에서 호출
    public void init(){
        firebaseAuth = FirebaseAuth.getInstance();

        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnGoPasswordReset = findViewById(R.id.btnGoPasswordReset);
    }

    // Listener 는 initLr 에서 호출
    public void initLr(){
        // 로그인 버튼 클릭시 로그인 진행
        btnLogin.setOnClickListener(v -> {
            Login();
        });

        // 비밀번호 재설정 버튼 클릭시 PasswordResetActivity 로 이동
        btnGoPasswordReset.setOnClickListener(v -> {
            Intent intent = new Intent(
                    LoginActivity.this,
                    PasswordResetActivity.class
            );
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    // 로그인 진행
    private void Login() {
        String email = etEmail.getText().toString();
        String password =  etPassword.getText().toString();

        // 이메일이나 비밀번호 입력 여부 확인
        if(email.length() > 0 && password.length() > 0 ) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // 일치하는 경우 로그인 성공
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast("성공적으로 로그인이 되었습니다.");
                        startMyActivity(MainActivity.class);
                    } else {
                        // 일치하지 않는 경우 로그인 실패
                        Toast(task.getException().toString());
                    }
                }
            });
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