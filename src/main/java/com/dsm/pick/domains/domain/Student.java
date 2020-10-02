package com.dsm.pick.domains.domain;

import javax.persistence.*;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @Column(name = "num")
    private String num;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "club_name", referencedColumnName = "name")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "class_name", referencedColumnName = "name")
    private SchoolClass schoolClass;

    public Student() {}
    public Student(String num, String name, Club club, SchoolClass schoolClass) {
        this.num = num;
        this.name = name;
        this.club = club;
        this.schoolClass = schoolClass;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }
}
