package com.sun.study.life_cycle;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sun.study.life_cycle.entity.Student;
import com.sun.study.life_cycle.utils.ThreadPool;

import java.util.List;

/**
 * ViewModel 所有的逻辑再这个里面
 */
public class AdapterViewModel extends ViewModel {
    private static String TAG = AdapterViewModel.class.getName();

    private MutableLiveData<List<Student>> mAllStudents;
    private DBModel mModel = new DBModel();


    public LiveData<List<Student>> getAllStudents(String classId) {
        if(mAllStudents == null) {
            mAllStudents = new MutableLiveData<>();
            loadAllStudents(classId);
        }

        return mAllStudents;
    }



    private void loadAllStudents(String classId) {
        ThreadPool.getInstance().executeRunnable(() -> {
            List<Student> students = mModel.getStudents(classId);
            mAllStudents.postValue(students);
        });
    }

}
