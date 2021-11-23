package com.sun.myexamples;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sun.rxjava2examples.R;

import io.reactivex.ObservableEmitter;

public class BFragment extends Fragment implements FragmentCommunicateUtils.SenderAndReceiver {

    private final static String TAG = BFragment.class.getSimpleName();
    private FragmentCommunicateUtils.Emitter mEmitter;
    private Bean b;
    EditText mName;

    public BFragment() {
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mRoot = inflater.inflate(R.layout.communicate, container, false);
        mRoot.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = mRoot.findViewById(R.id.et);
                b.name = et.getText().toString();
                mEmitter.sendMessage(new FragmentCommunicateUtils.Message(112, b));

            }
        });
        mName = mRoot.findViewById(R.id.et);
        return mRoot;
    }


    @Override
    public void setEmitter(FragmentCommunicateUtils.Emitter emitter) {
        //作为 发送者
        Log.d(TAG, "setEmitter: ");
        mEmitter = emitter;
    }

    @Override
    public void setObj(Object obj) {
        //作为 发送者
        Log.d(TAG, "setObj: ");
        b = (Bean) obj;
    }

    @Override
    public void onCreate() {
        //作为 接收者
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onReceive(FragmentCommunicateUtils.Message msg) {
        //作为接收者
        Log.d(TAG, "onReceive: ");
        if(msg.what == 112) {
            mName.setText(((Bean) msg.obj).name);
        }
    }

    @Override
    public void onFinish() {
        //作为接收者
        Log.d(TAG, "onFinish: ");
    }
}
