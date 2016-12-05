package com.taca.app.auth13;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BaseActivity {

//    인증 관련 객체
//    FirebaseAuth mAuth;
//    인증 관련 리스너
    FirebaseAuth.AuthStateListener mAuthListener;
//    진행 프로그레스
//    ProgressDialog mPd;
    boolean isLogin;
    EditText uid,upw;
    Button loginBtn, joinBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uid = (EditText)findViewById(R.id.uid);
        upw = (EditText)findViewById(R.id.upw);
        loginBtn = (Button)findViewById(R.id.loginbtn);
        joinBtn = (Button)findViewById(R.id.joinbtn);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //인증에 관련된 상태가 변경되면 호출
                //1. 인증 상태가 변경된 유저 객체 리턴
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    isLogin = false;
                    U.getInstance().log("로그 아웃 완료 or 계정 없음");
                }
                else{
                    isLogin = true;
                    U.getInstance().log("["+user.getUid()+"] 님 로그인 완료");
                }
                //상태값 변경
                UIUpdate(user);
            }
        };

    }
    //자동로그인 팁
    //체크박스하면 로그아웃 하지말고
    //체크박스안하면(자동로그인 안하면) 로그아웃하고
    public void UIUpdate(FirebaseUser user){

        if(user == null){ // 로그 오프 혹은 비계정생성 상태
            loginBtn.setText("로그인");
            uid.setVisibility(View.VISIBLE);
            upw.setVisibility(View.VISIBLE);
            joinBtn.setVisibility(View.VISIBLE);

        }
        else{
            loginBtn.setText("로그아웃");
            uid.setVisibility(View.GONE);
            upw.setVisibility(View.GONE);
            joinBtn.setVisibility(View.GONE);
        }
    }

    //앱이 활성화 되기 직전에 호출
    @Override
    protected void onStart() {
        super.onStart();
        //인증 관련 리스너 등록
        if(mAuth!=null && mAuthListener != null)
        {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    //앱이 비활성화 되기 직전 호출
    @Override
    protected void onStop() {
        super.onStop();
        //에러 화면에서 세션을 유지하면서 진행되다가 로그아웃되면 혹은
        //세션이 종료되는 상황이 비정상적인 상황이 발생되어서 로그 아웃처리를
        //해야 한다면 해제를 따로 하지 않고 유지하여 발생시 처리한다.

        //인증 관련 리스너 해제
        if(mAuth!=null && mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void onLogin(View view){
        if(!isLogin){  //로그오프상태
            U.getInstance().log("로그인진행");
            String uid = this.uid.getText().toString();
            String upw = this.upw.getText().toString();

            if(uid.length() == 0 || upw.length() == 0){
                U.getInstance().log("정확하게 입력하세요");
                return;
            }
            onProgress("로그인 중");
            mAuth.signInWithEmailAndPassword(uid,upw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    offProgress();
                    if(!task.isSuccessful()){
                        U.getInstance().log("로그인실패");
                        return;
                    }
                    else{
                        U.getInstance().log("로그인성공");
                    }
                }
            });

        }else{  //로그인상태
            U.getInstance().log("로그오프진행");
            mAuth.signOut();
        }
    }
    public void onJoin(View view){
        //비밀번호는 6자리 이상
        String uid = this.uid.getText().toString();
        String upw = this.upw.getText().toString();

        if(uid.length() == 0 || upw.length() == 0){
            U.getInstance().log("정확하게 입력하세요");
            return;
        }
        onProgress("회원 가입중");
        mAuth.createUserWithEmailAndPassword(uid,upw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                offProgress();
                if(!task.isSuccessful()){
                    U.getInstance().log("회원가입실패");
                    return;
                }
                else{
                    UIUpdate(mAuth.getCurrentUser());
                    U.getInstance().log("회원가입성공");
                }
            }
        });
    }
}
