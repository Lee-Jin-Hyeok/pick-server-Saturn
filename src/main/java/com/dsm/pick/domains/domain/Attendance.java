package com.dsm.pick.domains.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "date", referencedColumnName = "date")
    private Activity activity;

    @ManyToOne
    @JoinColumn(name = "student_num", referencedColumnName = "num")
    private Student student;

    @Column(name = "period")
    private String period;

    @ManyToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    @Column(name = "state")
    private String state;

    public Attendance() {}
    public Attendance(int id, Activity activity, Student student, String period, Teacher teacher, String state) {
        this.id = id;
        this.activity = activity;
        this.student = student;
        this.period = period;
        this.teacher = teacher;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
