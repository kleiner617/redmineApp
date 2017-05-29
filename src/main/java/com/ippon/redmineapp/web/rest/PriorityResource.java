package com.ippon.redmineapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.redmineapp.domain.Priority;

import com.ippon.redmineapp.repository.PriorityRepository;
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
 * REST controller for managing Priority.
 */
@RestController
@RequestMapping("/api")
public class PriorityResource {

    private final Logger log = LoggerFactory.getLogger(PriorityResource.class);
        
    @Inject
    private PriorityRepository priorityRepository;

    /**
     * POST  /priorities : Create a new priority.
     *
     * @param priority the priority to create
     * @return the ResponseEntity with status 201 (Created) and with body the new priority, or with status 400 (Bad Request) if the priority has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/priorities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Priority> createPriority(@RequestBody Priority priority) throws URISyntaxException {
        log.debug("REST request to save Priority : {}", priority);
        if (priority.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("priority", "idexists", "A new priority cannot already have an ID")).body(null);
        }
        Priority result = priorityRepository.save(priority);
        return ResponseEntity.created(new URI("/api/priorities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("priority", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /priorities : Updates an existing priority.
     *
     * @param priority the priority to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated priority,
     * or with status 400 (Bad Request) if the priority is not valid,
     * or with status 500 (Internal Server Error) if the priority couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/priorities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Priority> updatePriority(@RequestBody Priority priority) throws URISyntaxException {
        log.debug("REST request to update Priority : {}", priority);
        if (priority.getId() == null) {
            return createPriority(priority);
        }
        Priority result = priorityRepository.save(priority);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("priority", priority.getId().toString()))
            .body(result);
    }

    /**
     * GET  /priorities : get all the priorities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of priorities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/priorities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Priority>> getAllPriorities(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Priorities");
        Page<Priority> page = priorityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/priorities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /priorities/:id : get the "id" priority.
     *
     * @param id the id of the priority to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the priority, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/priorities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Priority> getPriority(@PathVariable Long id) {
        log.debug("REST request to get Priority : {}", id);
        Priority priority = priorityRepository.findOne(id);
        return Optional.ofNullable(priority)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /priorities/:id : delete the "id" priority.
     *
     * @param id the id of the priority to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/priorities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePriority(@PathVariable Long id) {
        log.debug("REST request to delete Priority : {}", id);
        priorityRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("priority", id.toString())).build();
    }

}
