package com.taca.app.auth13;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    //인증 관련 객체
    FirebaseAuth mAuth;
    //인증 관련 리스너
    FirebaseAuth.AuthStateListener mAuthListener;
    //진행 프로그레스
    ProgressDialog mPd;

    EditText uid,upw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uid = (EditText)findViewById(R.id.uid);
        upw = (EditText)findViewById(R.id.upw);

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
        // Out하면 그 전의 아이디는 가비지가 된다. 따라서 로그아웃은 따로 장치를 둬서 막아놓거나 해야 한다.
        // 로그 아웃을 하고, 새로 로그인시는 익명을 요청해서 가야한다.
        mAuth.signOut();
    }

    //익명계정에서 이메일계정으로 전환
    //이메일로 메일보낼 수 있고 그 링크 타고 오면 가입 완료할 수 있게 함.
    public void onAuthChage(View view) {
        U.getInstance().log("익명 계정 전환 요청");

        String uid = this.uid.getText().toString();
        String upw = this.upw.getText().toString();

        if(uid.length() == 0 || upw.length() == 0){
            U.getInstance().log("정확하게 입력하세요");
            return;
        }
        onProgress("계정 전환중");
        //이메일 비번으로 계정 전환 진행
        AuthCredential credential = EmailAuthProvider.getCredential(uid,upw);
//        EmailAuthCredential

        mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                offProgress();
                if(task.isSuccessful()){
                    U.getInstance().log("이메일 계정전환 성공");
                }else{
                    U.getInstance().log("이메일 계정전환 실패");
                }
            }
        });
    }
    //계정 삭제
    public void deleteUser(View view){
        //계정 삭제
        //비동기 끝나는 타임을 알아야 하니까 addOncompleteListener
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    offProgress();
                    if(task.isSuccessful()){
                        U.getInstance().log("프로필 삭제 완료->회원탈퇴");
                    }
                    else{
                        U.getInstance().log("프로필 삭제 실패");
                    }
                }
            });
        }

    }

    public void onProfileUpdate(View view) {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null){
            U.getInstance().log("프로필 정보 확인");
            U.getInstance().log(""+user.getDisplayName());
            U.getInstance().log(""+user.getUid());
            U.getInstance().log(""+user.getEmail());
            U.getInstance().log(""+user.getProviderId());
//            U.getInstance().log(""+user.getPhotoUrl().toString());
        }
        //비동기할때는 프로그레스로 하는게 편하다
        onProgress("익명 프로필 변경 요청");
        //프로필생성
        //프로필 업데이트 UI
        //수업때는 수동으로 실제는 ui만들어서 사용자가 입력및 카메라로 아니면 사진
        UserProfileChangeRequest profile =
        new UserProfileChangeRequest.Builder()
                .setDisplayName("taCA phs")
                //주소 2가지 1. 파이어베이스 저장소 / 2.웹에 있느 주소
                .setPhotoUri(Uri.parse("https://www.google.co.kr/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png"))
                .build();
        //프로필 업데이트
        //addOnCompleteListener 비동기니까 작성한다고 하는데 뭔 말인지...
        user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                offProgress();
                if(task.isSuccessful())
                    U.getInstance().log("프로필 업데이트 완료");
                else
                    U.getInstance().log("프로필 업데이트 실패");
            }
        });
    }
}
