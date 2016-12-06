package com.taca.app.auth13;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import static android.R.attr.key;
import static android.R.id.list;
import static com.google.android.gms.internal.zzwy.fb;

/*
임의의 고객센터와 매칭하는 테스트
채팅 리스트와 채팅 채널을 만드는 실습
1.파이어베이스에 cscenter@fb.com / 123456 계정 생성
생성된 아이디로 매칭을 한다.
2.채팅리스트 구조
    루트 > list > 본인아이디 > cscenter@fb.com ? key > true or key에 정보를 넣고자 하는 경우 클래스를 만들면 됨.
 */

public class MatchActivity extends AppCompatActivity {

    //1.인증 후 db  현재는 다 true니까 바로 db
    //2.db

    FirebaseDatabase mDb;
    DatabaseReference mRef;
    String uid = "guest";
    String cs = "cscenter"; // 메일보다는 넥네임으로 한는게

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        mDb = FirebaseDatabase.getInstance();
        mRef = mDb.getReference();
        U.getInstance().log(mRef.toString());

    }

    public void onMatch(View view) {
        U.getInstance().log("고객센터와 채팅(매치)");

        // 루트 > list > 본인아이디 > cscenter(고객센터 아이디) 여기까지의 경로가 존재하는지 check
        final Query query = mRef.child("list").child(uid).child(cs);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //리스너는 직접 해제하지 않으면 액티비티가 백그라운드로 가도
                //계속해서 작동하므로 1회성인지 지속성인지를 구분하여
                //리스너를 유지하는 정책을 결정해야 한다.
                query.removeEventListener(this);  // this -> ValueEventListener

                if (dataSnapshot.getValue() == null) {
                    //처음으로 채팅을 한다.

                    //mRef.child("channel").push() 자체적으는 생성이 안되고 최종적으로 값이 존재햐야 한다.
                    //키는 생성됨, 채팅방에는 고유키가 있어야 됨!!!
                    //채팅 채널 생성 root > channel > 고유키
                    DatabaseReference ref = mRef.child("channel").push();
                    U.getInstance().log("고유키 -> " + ref.getKey());

                    ExChatModel msg = new ExChatModel("관리자", "채팅방이 생성됐습니다", 0, U.getInstance().curTmEx(), 0);


                    //1. 채팅방 리스트 : 채널에 한개의 줄기를 생성해 둔다.
                    mRef.child("channel").child(ref.getKey()).child(U.getInstance().curTm()).setValue(msg);

                    //2. 나의 채팅리스트를 구성한다. (채팅 채널 정보를 가지고)
                    //실습 : ChatListModel를 구성하여 세팅하기
                    ChatListModel clm = new ChatListModel(ref.getKey(), cs, "", 0, 0, 1);

                    //쌍방향!!! 내 입장에서 가지 생성하면
                    mRef.child("list").child(uid).child(cs).push().setValue(clm);
                    //상대방 입장에서도 가지 생성, 프로필만 바꿔서
                    clm.setProfile(uid);
                    mRef.child("list").child(cs).child(uid).push().setValue(clm);
                    U.getInstance().log("채팅 채널 생성 후 채팅방 이동");

                    //채팅방이동
                    goChat(uid, cs, ref.getKey());
                } else {
                    //이미 채팅을 했었다.
                    //채팅방으로 이동

                    //해보기!!!!
                    //uid와 cs 사이에 채팅 채널값 획득
                    //dataSnapshot에서 키를 구해서 전체 노드를 완성 후 데이터 획득
                    //아래는 채널정보를 구하는 방식, 채널정보를 로컬에 저장
                    Iterator<DataSnapshot> ds = dataSnapshot.getChildren().iterator();
                    while(ds.hasNext()) {
//                        U.getInstance().log("사용자와 id 밑에 있는 노드 값:" + ds.next().getKey());

                        mRef.child("list").child(uid).child(cs).child(ds.next().getKey())
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        ChatListModel cm = dataSnapshot.getValue(ChatListModel.class);
                                        goChat(uid,cs,cm.getChannel());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
//                        break;
                    }
                    U.getInstance().log("채팅방이동");
                    //채팅방이동 여러번 뜸, 리스너가 계속 작동해서 그럼, 리스너 이벤트를 죽이거나
                    //리스너이벤트가 속한 액티비티를 죽이는데 이번은 버튼을 눌러사 가기때문에 일회성 리스너이벤트 죽임 -- 55번줄
                    //55번째 줄 query.removeEventListener(this);  // this -> ValueEventListener
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//mRef.child("chat").push().setValue(msg); // 줄기1 chat, 줄기2 push(랜덤값), 값 setValue(msg)
//위와 비슷하게 가지만들기  mRef는 루트

// 루트 > list > 본인아이디 > cscenter@fb.com ? key > true or key에 정보를 넣고자 하는 경우 클래스를 만들면 됨.

//본인아디이 밑에가 채팅목록
//I/FB: Invalid Firebase Database path: cscenter@fb.com. Firebase Database paths must not contain '.', '#', '$', '[', or ']'

        //채팅하기
        //1.기존 채팅내역이 있으면 뿌려주고 시작
        //2.기존 채팅내역이 없으면 db 루트 만들고 시작...

//        try {
//            mRef.child("list").child(uid).child(cs).push().setValue(true);
//        } catch (Exception e) {
//            U.getInstance().log(e.getMessage());
//        }
    }

    //채팅방으로 이동
    public void goChat(String myId, String youId, String channel) {
        Intent intent = new Intent(this, ExtChatActivity.class);
        intent.putExtra("myId", myId);
        intent.putExtra("youId", youId);
        intent.putExtra("channel", channel);
        startActivity(intent);
    }
}
