package com.taca.app.auth13;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExtChatActivity extends AppCompatActivity
{
    // firebase database
    FirebaseDatabase  mDb;
    DatabaseReference mRef;

    ListView listView;
    EditText chatInput;
    ArrayList<ExChatModel> msgs;
    ChatApdater cAdapter;

    String myId, youId, channel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ext_chat);
        // 채팅에 필요한 메타 정보
        myId        = getIntent().getStringExtra("myId");
        youId       = getIntent().getStringExtra("youId");
        channel     = getIntent().getStringExtra("channel");

        msgs        = new ArrayList<ExChatModel>();
        listView    = (ListView)findViewById(R.id.listview);
        chatInput   = (EditText)findViewById(R.id.chatInput);
        cAdapter    = new ChatApdater();
        listView.setAdapter(cAdapter);

        mDb         = FirebaseDatabase.getInstance();
        mRef        = mDb.getReference();
        U.getInstance().log( mRef.toString() );

        // chat이라는 줄기에 데이터가 변화하는것을 감지하여 이벤트를 처리한다.
        mRef.child("channel").child(channel)
                .addChildEventListener(new ChildEventListener() {
            // 자식이 추가되면 호출
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                U.getInstance().log("==== 메시지가 추가되었다 =====");
                U.getInstance().log(dataSnapshot.toString());
                // 데이터 파싱
                ExChatModel bcm
                        = dataSnapshot.getValue(ExChatModel.class);
                msgs.add(bcm);
                //U.getInstance().log(bcm.getUserName()+":"+bcm.getMsg());
                // 리스트뷰ㅜ 갱신
                cAdapter.notifyDataSetChanged();
                listView.setSelection(msgs.size()-1);
            }
            // 자식자체에 변화가 있으면 호출
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            // 자식이 삭제되었으면 호출
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            // 순서가 존재할 경우 자식의 순서가 바귀면 호출
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mRef.child("chat").addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                U.getInstance().log("==== addValueEventListener =====");
                U.getInstance().log(dataSnapshot.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mRef.child("chat").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                U.getInstance().log("==== addListenerForSingleValueEvent =====");
                U.getInstance().log(dataSnapshot.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    // 채팅 메시지를 FB RDB로 전송한다.
    public void onSend(View view)
    {
        //chatInput.setText("msg-" + new Random().nextInt(100));
        String chatMsg = chatInput.getText().toString();
        if( chatMsg.length() == 0 ){
            U.getInstance().log("메시지를 입력하세요");
            return;
        }
        long time = U.getInstance().curTmEx();
        ExChatModel msg = new ExChatModel(
                myId,
                chatMsg,
                1,
                time,
                1
        );//"guest", chatMsg);
        mRef.child("channel").child(channel).child(time+"").setValue(msg);
    }
    // 채팅 리스트 화면 구성
    class ViewHolder
    {
        TextView userName, msg;
    }
    class ChatApdater extends BaseAdapter
    {
        ViewHolder holder;
        @Override
        public int getCount() {
            return msgs.size();
        }
        @Override
        public ExChatModel getItem(int position) {
            return msgs.get(position);
        }
        @Override
        public long getItemId(int position) {
            return 0;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if( convertView == null ){
                convertView = ExtChatActivity.this.getLayoutInflater()
                        .inflate(R.layout.chat_cell_layout, parent, false);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.userName = (TextView)convertView.findViewById(R.id.username);
                holder.msg = (TextView)convertView.findViewById(R.id.msg);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            ExChatModel model = getItem(position);
            holder.userName.setText( model.getUid() );
            holder.msg.setText( model.getMsg() );

            return convertView;
        }
    }
}






