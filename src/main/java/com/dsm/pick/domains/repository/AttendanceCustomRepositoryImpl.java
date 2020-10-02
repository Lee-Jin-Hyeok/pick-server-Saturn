package com.dsm.pick.domains.repository;

import com.dsm.pick.domains.domain.Attendance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class AttendanceCustomRepositoryImpl implements AttendanceCustomRepository {

    @Autowired
    EntityManager entityManager;

    @Override
    public List<Attendance> findByDateAndFloorAndPriority(LocalDate date, int floor, int priority) {
        return entityManager.createQuery("SELECT a FROM Attendance a INNER JOIN a.student s " +
                "WHERE s.club.location.floor = :floor " +
                "AND s.club.location.priority = :priority " +
                "AND a.activity.date = :date", Attendance.class)
                .setParameter("floor", floor)
                .setParameter("priority", priority)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public Attendance findByDateAndFloorAndPriorityAndNumberAndPeriod(LocalDate date, int floor, int priority, String number, String period) {
        return entityManager.createQuery("SELECT a FROM Attendance a INNER JOIN a.student s " +
                "WHERE s.club.location.floor = :floor " +
                "AND s.club.location.priority = :priority " +
                "AND a.activity.date = :date " +
                "AND s.num = :number " +
                "AND a.period = :period", Attendance.class)
                .setParameter("floor", floor)
                .setParameter("priority", priority)
                .setParameter("date", date)
                .setParameter("number", number)
                .setParameter("period", period)
                .getResultList()
                .stream()
                .findAny()
                .get();
    }
}
