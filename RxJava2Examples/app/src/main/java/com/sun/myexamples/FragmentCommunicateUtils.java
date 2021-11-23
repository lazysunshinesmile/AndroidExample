package com.sun.myexamples;


import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class FragmentCommunicateUtils {
    private final static String TAG = FragmentCommunicateUtils.class.getSimpleName() + "sunxiang";

    public static void connect(final Object b, final Receiver receiver, final Sender sender) {
        MyObserver observer = new MyObserver(receiver);
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Message> emitter) throws Exception {
                Emitter e = new Emitter(emitter);
                sender.setEmitter(e);
                sender.setObj(b);
                Log.d(TAG, "subscribe: bbb");
            }
        }).subscribe(observer);
    }


    public static void interconnect(final Object b, final SenderAndReceiver sar1, final SenderAndReceiver sar2) {
        MyObserver observer = new MyObserver(sar1);
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Message> emitter) throws Exception {
                Emitter e = new Emitter(emitter);
                sar2.setEmitter(e);
                sar2.setObj(b);
                Log.d(TAG, "subscribe: ");
            }
        }).subscribe(observer);

        MyObserver observer2 = new MyObserver(sar2);
        Observable.create(new ObservableOnSubscribe<Message>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Message> emitter) throws Exception {
                Emitter e = new Emitter(emitter);
                sar1.setEmitter(e);
                sar1.setObj(b);
                Log.d(TAG, "subscribe: ");
            }
        }).subscribe(observer2);
    }

    public interface SenderAndReceiver extends Sender, Receiver{
    }

    public interface Sender {
        void setEmitter(Emitter emitter);
        void setObj(Object obj);
    }

    public interface Receiver {
        void onCreate();
        void onReceive(Message msg);
        void onFinish();
    }


    public static class Emitter {
        private ObservableEmitter<Message> mEmitter;

        protected Emitter(ObservableEmitter<Message> mEmitter) {
            this.mEmitter = mEmitter;
        }

        public void sendMessage(Message message) {
            mEmitter.onNext(message);
        }
    }

    private static class MyObserver implements Observer<Message> {
        private Receiver mReceiver;

        public MyObserver(Receiver receiver) {
            this.mReceiver = receiver;
        }

        @Override
        public void onSubscribe(@NonNull Disposable d) {
            Log.d(TAG, "onSubscribe: d:" + d);
            mReceiver.onCreate();
        }

        @Override
        public void onNext(@NonNull Message message) {
            Log.d(TAG, "onNext: o:" + message);
            mReceiver.onReceive(message);
        }

        @Override
        public void onError(@NonNull Throwable e) {
            Log.d(TAG, "onError: msg:" + e.getMessage() + ", cause:" + e.getCause());
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "onComplete: ");
            mReceiver.onFinish();

        }
    }





    public static class Message {
        int what;
        Object obj;

        public Message() {
        }

        public Message(int what, Object obj) {
            this.what = what;
            this.obj = obj;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "what=" + what +
                    ", obj=" + obj +
                    '}';
        }
    }

}
