package com.taca.app.auth13;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Random;

public class BasicChatActivity extends AppCompatActivity {

    //firebase database
    FirebaseDatabase mDb;
    DatabaseReference mRef;

    ListView listView;
    EditText chatInput;

    ArrayList<BasicChatModel> msgs;
    ChatAdpater chatAdpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_chat);

        msgs = new ArrayList<BasicChatModel>();

        listView = (ListView) findViewById(R.id.listview);
        chatInput = (EditText) findViewById(R.id.chatInput);

        chatAdpater = new ChatAdpater();

        listView.setAdapter(chatAdpater);

        mDb = FirebaseDatabase.getInstance();
        mRef = mDb.getReference();
        U.getInstance().log(mRef.toString());

        //chat이라는 줄기에 데이터가 변화하는 것을 감지하여 이벤트를 처리한다.
        //액티비티를 파괴 하지 않는 이상 리스너는 물려있음.
        mRef.child("chat").addChildEventListener(new ChildEventListener() {
            //채팅 추가
            //앱이 시작하자 마자 자식이 있으면 구동, 전송 해도 구동
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                U.getInstance().log("=====메시지가 추가 됨=====");
                U.getInstance().log(dataSnapshot.toString());
                U.getInstance().log(s);

                //데이터 파싱
                BasicChatModel bcm = dataSnapshot.getValue(BasicChatModel.class);

                msgs.add(bcm);

                U.getInstance().log(bcm.getUserName() + ":" + bcm.getMsg());

                //리스트뷰 갱신
                chatAdpater.notifyDataSetChanged();

                listView.setSelection(msgs.size()-1);
            }

            //자식 자체에 변화가 있으면 호출
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            //유저나감
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onSend(View view) {
        chatInput.setText("msg-" + new Random().nextInt(100));
        String chatMsg = chatInput.getText().toString();
        if (chatMsg.length() == 0) {
            U.getInstance().log("메시지를 입력하세요");
            return;
        }
        BasicChatModel msg = new BasicChatModel("guest", chatMsg);
        mRef.child("chat").push().setValue(msg);
    }

    class ViewHolder{
        TextView username,msg;
    }


    //채팅 리스트 화면 구성
    class ChatAdpater extends BaseAdapter{

        ViewHolder holder;

        @Override
        public int getCount() {
            return msgs.size();
        }

        @Override
        public BasicChatModel getItem(int i) {
            return msgs.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = BasicChatActivity.this.getLayoutInflater()
                        .inflate(R.layout.chat_cell_layout,parent,false);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.username = (TextView)convertView.findViewById(R.id.username);
                holder.msg = (TextView)convertView.findViewById(R.id.msg);
            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            BasicChatModel model = getItem(position);
            holder.username.setText(model.getUserName());
            holder.msg.setText(model.getMsg());
            return convertView;
        }
    }
}
