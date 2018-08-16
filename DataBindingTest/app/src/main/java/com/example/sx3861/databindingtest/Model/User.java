package com.example.sx3861.databindingtest.Model;

import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.util.Log;

public class User  {
    private final String TAG = "User";
	private final ObservableField<String> name;
	private final ObservableInt age;

	public User(String name, int age) {
		this.name = new ObservableField<>(name);
		this.age = new ObservableInt(age);
	}

    public ObservableField<String> getName() {
        return name;
    }

    public ObservableInt getAge() {
        return age;
    }

    //	public String getName() {
//		return name.get();
//	}
//
//	public void setName(String name) {
//		if(this.name == null) {
//            Log.d(TAG, "setName: name = null");
//			this.name = new ObservableField<>(name);
//		}else {
//            Log.d(TAG, "setName: name != null");
//			this.name.set(name);
//		}
//	}
//
//	public int getAge() {
//		return age.get();
//	}
//
//	public void setAge(int age) {
//        if(this.age == null) {
//            Log.d(TAG, "setName: name = null");
//            this.age = new ObservableInt(age);
//        }else {
//            Log.d(TAG, "setName: name != null");
//            this.age.set(age);
//        }
//	}
}
