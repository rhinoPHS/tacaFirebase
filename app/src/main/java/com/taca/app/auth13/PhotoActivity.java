package com.taca.app.auth13;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.miguelbcr.ui.rx_paparazzo.RxPaparazzo;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class PhotoActivity extends AppCompatActivity {

    FirebaseStorage storage;
    String BRENCH = "gs://fbtestauth-c36e4.appspot.com";
    StorageReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        //스토리지 객체생성
        storage =FirebaseStorage.getInstance();
        storage.getReferenceFromUrl(BRENCH);
        U.getInstance().log(ref.getBucket());
        U.getInstance().log(ref.getName());
        U.getInstance().log(ref.getPath());
    }
    public void onCamera(View view)
    {
        RxPaparazzo.takeImage(this)
                .usingCamera()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    // See response.resultCode() doc
                    if (response.resultCode() != RESULT_OK) {
//                        response.targetUI().showUserCanceled();
                        return;
                    }

//                    response.targetUI().loadImage(response.data());
                });
    }
    public void onPhoto(View view){
        RxPaparazzo.takeImage(this)
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    // See response.resultCode() doc
                    if (response.resultCode() != RESULT_OK) {
//                        response.targetUI().showUserCanceled();
                        return;
                    }

//                    response.targetUI().loadImage(response.data());
                });
    }
}
