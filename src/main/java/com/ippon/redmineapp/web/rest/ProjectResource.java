package com.ippon.redmineapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.ippon.redmineapp.domain.Project;

import com.ippon.redmineapp.domain.util.ParseJSON;
import com.ippon.redmineapp.repository.ProjectRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Project.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    @Inject
    private ProjectRepository projectRepository;

    /**
     * POST  /projects : Create a new project.
     *
     * @param project the project to create
     * @return the ResponseEntity with status 201 (Created) and with body the new project, or with status 400 (Bad Request) if the project has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/projects",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Project> createProject(@RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to save Project : {}", project);
        if (project.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("project", "idexists", "A new project cannot already have an ID")).body(null);
        }
        Project result = projectRepository.save(project);
        return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("project", result.getId().toString()))
            .body(result);
    }


    /**
     * POST  /getProjects : Gets all projects from redmine.
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/getProjects",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void getAllProjects() throws URISyntaxException {

        log.debug("I've made it to the REST!");

        String projJSON = "" +

            "{" +
            "\"projects\": [" +
            "{" +
            "\"id\": \"2\"," +
            "\"name\": \"Direction Technique\"," +
            "\"identifier\": \"direction-technique\"," +
            "\"description\": \"\"," +
            "\"status\": \"1\"," +
            "\"created_on\": \"2014-03-26T09:20:29Z\"," +
            "\"updated_on\": \"2014-03-26T09:20:29Z\"" +
            "}," +
            "{" +
            "\"id\": \"43\"," +
            "\"name\": \"FeedBack\"," +
            "\"identifier\": \"feedback\"," +
            "\"description\": \"\"," +
            "\"status\": \"1\"," +
            "\"created_on\": \"2016-02-02T11:22:50Z\"," +
            "\"updated_on\": \"2016-02-02T11:22:50Z\"" +
            "}" +
            "]," +
            "\"total_count\": \"10\"," +
            "\"offset\": \"0\"," +
            "\"limit\": \"25\"" +
            "}";

        ParseJSON parseList = new ParseJSON();
        ArrayList <Project> projectList = new ArrayList<Project>();
        projectList = parseList.parseString(projJSON);

        log.debug("The list has been parsed");


        projectList.forEach(proj->{
            //TODO: Need to check to see if project already exists.  If it does, update it here, don't save it.
            projectRepository.save(proj);
        });

        log.debug("Should have saved to DB!");

        return;
    }




    /**
     * PUT  /projects : Updates an existing project.
     *
     * @param project the project to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated project,
     * or with status 400 (Bad Request) if the project is not valid,
     * or with status 500 (Internal Server Error) if the project couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/projects",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Project> updateProject(@RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to update Project : {}", project);
        if (project.getId() == null) {
            return createProject(project);
        }
        Project result = projectRepository.save(project);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("project", project.getId().toString()))
            .body(result);
    }

    /**
     * GET  /projects : get all the projects.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projects in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/projects",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Project>> getAllProjects(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Projects");
        Page<Project> page = projectRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /projects/:id : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the project, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/projects/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        log.debug("REST request to get Project : {}", id);
        Project project = projectRepository.findOne(id);
        return Optional.ofNullable(project)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /projects/:id : delete the "id" project.
     *
     * @param id the id of the project to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/projects/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.debug("REST request to delete Project : {}", id);
        projectRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("project", id.toString())).build();
    }

}
