package com.ippon.redmineapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.redmineapp.domain.Issue;

import com.ippon.redmineapp.repository.IssueRepository;
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
 * REST controller for managing Issue.
 */
@RestController
@RequestMapping("/api")
public class IssueResource {

    private final Logger log = LoggerFactory.getLogger(IssueResource.class);

    @Inject
    private IssueRepository issueRepository;

    /**
     * POST  /issues : Create a new issue.
     *
     * @param issue the issue to create
     * @return the ResponseEntity with status 201 (Created) and with body the new issue, or with status 400 (Bad Request) if the issue has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/issues",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Issue> createIssue(@RequestBody Issue issue) throws URISyntaxException {
        log.debug("REST request to save Issue : {}", issue);
        if (issue.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("issue", "idexists", "A new issue cannot already have an ID")).body(null);
        }
        Issue result = issueRepository.save(issue);
        return ResponseEntity.created(new URI("/api/issues/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("issue", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /issues : Updates an existing issue.
     *
     * @param issue the issue to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated issue,
     * or with status 400 (Bad Request) if the issue is not valid,
     * or with status 500 (Internal Server Error) if the issue couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/issues",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Issue> updateIssue(@RequestBody Issue issue) throws URISyntaxException {
        log.debug("REST request to update Issue : {}", issue);
        if (issue.getId() == null) {
            return createIssue(issue);
        }
        Issue result = issueRepository.save(issue);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("issue", issue.getId().toString()))
            .body(result);
    }




//    String issueList = "" +
//        "{" +
//        "\"issues\": [" +
//        "{" +
//        "\"id\": 11940," +
//        "\"project\": {" +
//        "\"id\": 104," +
//        "\"name\": \"Laptop-v2\"" +
//        "}," +
//        "\"tracker\": {" +
//        "\"id\": 4," +
//        "\"name\": \"Task\"" +
//        "}," +
//        "\"status\": {" +
//        "\"id\": 3," +
//        "\"name\": \"Resolved\"" +
//        "}," +
//        "\"priority\": {" +
//        "\"id\": 2," +
//        "\"name\": \"P3 (Normal)\"" +
//        "}," +
//        "\"author\": {" +
//        "\"id\": 485," +
//        "\"name\": \"Guillaume DERBOUX\"" +
//        "}," +
//        "\"assigned_to\": {" +
//        "\"id\": 485," +
//        "\"name\": \"Guillaume DERBOUX\"" +
//        "}," +
//        "\"fixed_version\": {" +
//        "\"id\": 350," +
//        "\"name\": \"Sprint 1 Version\"" +
//        "}," +
//        "\"parent\": {" +
//        "\"id\": 11393" +
//        "}," +
//        "\"subject\": \"Modélisation des données\"," +
//        "\"description\": \"\"," +
//        "\"start_date\": \"2017-05-24\"," +
//        "\"done_ratio\": 100," +
//        "\"estimated_hours\": 4," +
//        "\"created_on\": \"2017-05-24T10:11:41Z\"," +
//        "\"updated_on\": \"2017-05-26T12:43:00Z\"" +
//        "}," +
//        "{" +
//        "\"id\": 11938," +
//        "\"project\": {" +
//        "\"id\": 104," +
//        "\"name\": \"Laptop-v2\"" +
//        "}," +
//        "\"tracker\": {" +
//        "\"id\": 4," +
//        "\"name\": \"Task\"" +
//        "}," +
//        "\"status\": {" +
//        "\"id\": 1," +
//        "\"name\": \"New\"" +
//        "}," +
//        "\"priority\": {" +
//        "\"id\": 2," +
//        "\"name\": \"P3 (Normal)\"" +
//        "}," +
//        "\"author\": {" +
//        "\"id\": 133," +
//        "\"name\": \"Yohann LONG HIM NAM\"" +
//        "}," +
//        "\"fixed_version\": {" +
//        "\"id\": 350," +
//        "\"name": \"Sprint 1 Version\"" +
//        "}," +
//        "\"parent\": {" +
//        "\"id\": 11396" +
//        "}," +
//        "\"subject\": \"Suppression d'un SLT\"," +
//        "\"description\": \"\"," +
//        "\"start_date\": \"2017-05-24\"," +
//        "\"done_ratio\": 0," +
//        "\"estimated_hours\": 3," +
//        "\"created_on\": \"2017-05-24T09:52:01Z\"," +
//        "\"updated_on\": \"2017-05-26T12:43:03Z\"" +
//        "}" +
//        "]," +
//        "\"total_count\": 122," +
//        "\"offset\": 0," +
//        "\"limit\": 2" +
//        "}";





    /**
     * GET  /issues : get all the issues.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of issues in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/issues",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Issue>> getAllIssues(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Issues");
        Page<Issue> page = issueRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/issues");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /issues/:id : get the "id" issue.
     *
     * @param id the id of the issue to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the issue, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/issues/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Issue> getIssue(@PathVariable Long id) {
        log.debug("REST request to get Issue : {}", id);
        Issue issue = issueRepository.findOne(id);
        return Optional.ofNullable(issue)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /issues/:id : delete the "id" issue.
     *
     * @param id the id of the issue to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/issues/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        log.debug("REST request to delete Issue : {}", id);
        issueRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("issue", id.toString())).build();
    }

}
