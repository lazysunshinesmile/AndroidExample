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

import io.reactivex.Observable;


public class AFragment extends Fragment implements FragmentCommunicateUtils.SenderAndReceiver {
    private final static String TAG = AFragment.class.getSimpleName() + "sunxiang";
    Bean bean;
    Observable observable;
    EditText mName;
    private FragmentCommunicateUtils.Emitter mEmitter;

    public AFragment() {
        bean = new Bean();
        bean.id = 12;
        bean.name = "sunxiang;";

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mRoot = inflater.inflate(R.layout.communicate, container, false);
        mRoot.findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = mRoot.findViewById(R.id.et);
                bean.name = et.getText().toString();
                mEmitter.sendMessage(new FragmentCommunicateUtils.Message(112, bean));
            }
        });
        mName = mRoot.findViewById(R.id.et);
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        AFragment a = (AFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.a_fragment);
        BFragment b = (BFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.b_fragment);
        FragmentCommunicateUtils.interconnect(bean, a, b);
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
        bean = (Bean) obj;
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
