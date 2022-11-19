package com.sun.study.life_cycle;

import com.sun.study.life_cycle.entity.Student;

import java.util.ArrayList;
import java.util.List;

public class DBModel {



    public List<Student> getStudents(String classId) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Student> ll = new ArrayList<>();
        for(int i=0; i<50; i++) {
            Student s = new Student();
            s.id= i;
            s.name = "name: " + classId;
            s.type = 0;
            if(i == 25) {
                s.type = 1;
            }

            ll.add(s);
        }
        return ll;
    }

}
