package com.ippon.redmineapp.repository;

import com.ippon.redmineapp.domain.Priority;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Priority entity.
 */
@SuppressWarnings("unused")
public interface PriorityRepository extends JpaRepository<Priority,Long> {

}
