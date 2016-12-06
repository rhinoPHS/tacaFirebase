package com.taca.app.auth13;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class CommanderActivity extends AppCompatActivity {
    FirebaseRemoteConfig mRemoteConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        U.getInstance().log(U.getInstance().getString(this, "APP_MAIN_DOMAIN"));

        mRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings =
        new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mRemoteConfig.setConfigSettings(configSettings);

        long cacheException = 3600;
        if(mRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled())
        {
            cacheException = 0;
        }
        //매개변수 가져오기!!
        //블랙리스트 관리, 푸쉬줘서 죽이고, 매개변수 죽이고...
        //앱 시작하면 finish()해버리기 블랙리스트들에게...
        //특정인물만 버튼을 추가하고 말고...
        mRemoteConfig.fetch(cacheException)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if( !task.isSuccessful() ){
                            U.getInstance().log("매개변수 획득 실패");
                            return;
                        }else{
                            // 캐싱된 데이터를 앱에 반영!!
                            mRemoteConfig.activateFetched();
                            String domain =
                                    mRemoteConfig.getString("APP_MAIN_DOMAIN");
                            int APP_LEVEL =
                                    (int)mRemoteConfig.getLong("APP_LEVEL");
                            U.getInstance().log("domain : " + domain);
                            U.getInstance().log("APP_LEVEL : " + APP_LEVEL);
                           //원격저장소 저장
                            U.getInstance().setString(CommanderActivity.this,
                                    "APP_MAIN_DOMAIN", domain);

                        }
                    }
                });



        //Intent intent = new Intent(this, BasicChatActivity.class);
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivity(intent);
        finish();
    }
}
