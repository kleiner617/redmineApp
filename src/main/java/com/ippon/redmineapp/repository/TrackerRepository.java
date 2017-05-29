package com.ippon.redmineapp.repository;

import com.ippon.redmineapp.domain.Tracker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Tracker entity.
 */
@SuppressWarnings("unused")
public interface TrackerRepository extends JpaRepository<Tracker,Long> {

    Optional<Tracker> findOneById(Long userId);

    @Query(value = "select distinct tracker from Tracker tracker left join fetch tracker.authorities",
        countQuery = "select count(tracker) from Tracker tracker")
    Page<Tracker> findAllWithAuthorities(Pageable pageable);

}
