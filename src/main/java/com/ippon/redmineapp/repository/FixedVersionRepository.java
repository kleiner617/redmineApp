package com.ippon.redmineapp.repository;

import com.ippon.redmineapp.domain.FixedVersion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the FixedVersion entity.
 */
@SuppressWarnings("unused")
public interface FixedVersionRepository extends JpaRepository<FixedVersion,Long> {

}
