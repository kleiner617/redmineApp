package com.ippon.redmineapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.redmineapp.domain.FixedVersion;

import com.ippon.redmineapp.repository.FixedVersionRepository;
import com.ippon.redmineapp.web.rest.util.HeaderUtil;
import com.ippon.redmineapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FixedVersion.
 */
@RestController
@RequestMapping("/api")
public class FixedVersionResource {

    private final Logger log = LoggerFactory.getLogger(FixedVersionResource.class);
        
    @Inject
    private FixedVersionRepository fixedVersionRepository;

    /**
     * POST  /fixed-versions : Create a new fixedVersion.
     *
     * @param fixedVersion the fixedVersion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fixedVersion, or with status 400 (Bad Request) if the fixedVersion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fixed-versions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FixedVersion> createFixedVersion(@RequestBody FixedVersion fixedVersion) throws URISyntaxException {
        log.debug("REST request to save FixedVersion : {}", fixedVersion);
        if (fixedVersion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("fixedVersion", "idexists", "A new fixedVersion cannot already have an ID")).body(null);
        }
        FixedVersion result = fixedVersionRepository.save(fixedVersion);
        return ResponseEntity.created(new URI("/api/fixed-versions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("fixedVersion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /fixed-versions : Updates an existing fixedVersion.
     *
     * @param fixedVersion the fixedVersion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fixedVersion,
     * or with status 400 (Bad Request) if the fixedVersion is not valid,
     * or with status 500 (Internal Server Error) if the fixedVersion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/fixed-versions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FixedVersion> updateFixedVersion(@RequestBody FixedVersion fixedVersion) throws URISyntaxException {
        log.debug("REST request to update FixedVersion : {}", fixedVersion);
        if (fixedVersion.getId() == null) {
            return createFixedVersion(fixedVersion);
        }
        FixedVersion result = fixedVersionRepository.save(fixedVersion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("fixedVersion", fixedVersion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /fixed-versions : get all the fixedVersions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of fixedVersions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/fixed-versions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<FixedVersion>> getAllFixedVersions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of FixedVersions");
        Page<FixedVersion> page = fixedVersionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/fixed-versions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /fixed-versions/:id : get the "id" fixedVersion.
     *
     * @param id the id of the fixedVersion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fixedVersion, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/fixed-versions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FixedVersion> getFixedVersion(@PathVariable Long id) {
        log.debug("REST request to get FixedVersion : {}", id);
        FixedVersion fixedVersion = fixedVersionRepository.findOne(id);
        return Optional.ofNullable(fixedVersion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /fixed-versions/:id : delete the "id" fixedVersion.
     *
     * @param id the id of the fixedVersion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/fixed-versions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFixedVersion(@PathVariable Long id) {
        log.debug("REST request to delete FixedVersion : {}", id);
        fixedVersionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("fixedVersion", id.toString())).build();
    }

}
