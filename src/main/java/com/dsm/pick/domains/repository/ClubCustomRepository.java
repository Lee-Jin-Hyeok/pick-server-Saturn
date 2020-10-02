package com.dsm.pick.domains.repository;

import com.dsm.pick.domains.domain.Club;
import com.dsm.pick.domains.domain.SchoolClass;

import java.util.List;

public interface ClubCustomRepository {
    List<Club> findByFloor(int floor);
    Club findByFloorAndPriority(int floor, int priority);
}
