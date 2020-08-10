package com.dsm.pick.domains.repository;

import com.dsm.pick.domains.domain.Teacher;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class UserRepository {
    private EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void save(Teacher user) {
        entityManager.persist(user);
    }

    public Optional<Teacher> findById(String id) {
        return Optional.ofNullable(entityManager.find(Teacher.class, id));
    }

    public Optional<Teacher> findByRefreshToken(String refreshToken) {
        return entityManager.createQuery("SELECT t FROM Teacher t WHERE t.refreshToken = :token", Teacher.class)
                .setParameter("token", refreshToken)
                .getResultList()
                .stream()
                .findAny();
    }
}
