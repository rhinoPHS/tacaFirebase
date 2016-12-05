package com.taca.app.auth13;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //인증 관련 객체
    FirebaseAuth mAuth;
    //인증 관련 리스너
    FirebaseAuth.AuthStateListener mAuthListener;
    //진행 프로그레스
    ProgressDialog mPd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //fb 인증 객체 생성
        mAuth = FirebaseAuth.getInstance();
        //fb 인증관련 리스너 객체 생성
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //인증에 관련된 상태가 변경되면 호출
                //1. 인증 상태가 변경된 유저 객체 리턴
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user == null){
                    U.getInstance().log("로그 아웃 완료");
                }
                else{
                    U.getInstance().log("["+user.getUid()+"] 님 로그인 완료");
                }
                //상태값 변경
            }
        };
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

    //프로그레스 온
    public void onProgress(String msg){
        if(mPd == null)
        {
            mPd = new ProgressDialog(this);
            mPd.setMessage(msg);
            mPd.setIndeterminate(true);
        }
        mPd.show();
    }

    //프로그레스 오프
    public void offProgress(){
        if(mPd != null && mPd.isShowing())
        {
            mPd.dismiss();
        }
    }


    //익명계정로그인
    public void onLogin(View view) {
        onProgress("익명 계정 요청");
        U.getInstance().log("익명 로그인 요청");
        //->익명 계정을 요청!!
        //->요청이 완료되면 이벤트를 받아서 후속 처리
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                offProgress();
                if(task.isSuccessful()){
                    U.getInstance().log("익명 게정 생성 성공");
                }else{
                    U.getInstance().log("익명 게정 생성 실패");
                }
            }
        });
    }

    //익명계정로그아웃
    public void onLogout(View view) {
        U.getInstance().log("익명 로그아웃 요청");
    }

    //익명계정에서 이메일계정으로 전환
    public void onAuthChage(View view) {
        U.getInstance().log("익명 계정 전환 요청");
    }
}
