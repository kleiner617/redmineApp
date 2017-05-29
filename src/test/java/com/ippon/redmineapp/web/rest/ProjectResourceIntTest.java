package com.ippon.redmineapp.web.rest;

import com.ippon.redmineapp.RedmineappApp;

import com.ippon.redmineapp.domain.Project;
import com.ippon.redmineapp.repository.ProjectRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProjectResource REST controller.
 *
 * @see ProjectResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedmineappApp.class)
public class ProjectResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_IDENTIFIER = "AAAAA";
    private static final String UPDATED_IDENTIFIER = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_ON_STR = dateTimeFormatter.format(DEFAULT_CREATED_ON);

    private static final ZonedDateTime DEFAULT_UPDATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATED_ON_STR = dateTimeFormatter.format(DEFAULT_UPDATED_ON);

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restProjectMockMvc;

    private Project project;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjectResource projectResource = new ProjectResource();
        ReflectionTestUtils.setField(projectResource, "projectRepository", projectRepository);
        this.restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
                .name(DEFAULT_NAME)
                .identifier(DEFAULT_IDENTIFIER)
                .description(DEFAULT_DESCRIPTION)
                .status(DEFAULT_STATUS)
                .createdOn(DEFAULT_CREATED_ON)
                .updatedOn(DEFAULT_UPDATED_ON);
        return project;
    }

    @Before
    public void initTest() {
        project = createEntity(em);
    }

    @Test
    @Transactional
    public void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project

        restProjectMockMvc.perform(post("/api/projects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(project)))
                .andExpect(status().isCreated());


        // Validate the Project in the database
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projects.get(projects.size() - 1);

        assertThat(testProject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProject.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
        assertThat(testProject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProject.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProject.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testProject.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    public void getProjects() throws Exception {

        restProjectMockMvc.perform(post("/api/getProjects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)));


        // Validate the Project in the database
        List<Project> projects = projectRepository.findAll();
//        assertThat(projects).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projects.get(projects.size() - 1);

        assertThat(testProject.getName()).isEqualTo("FeedBack");

//        assertThat(testProject.getName()).isEqualTo(DEFAULT_NAME);
//        assertThat(testProject.getIdentifier()).isEqualTo(DEFAULT_IDENTIFIER);
//        assertThat(testProject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
//        assertThat(testProject.getStatus()).isEqualTo(DEFAULT_STATUS);
//        assertThat(testProject.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
//        assertThat(testProject.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
    }

    @Test
    @Transactional
    public void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projects
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].identifier").value(hasItem(DEFAULT_IDENTIFIER.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON_STR)))
                .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON_STR)));
    }

    @Test
    @Transactional
    public void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.identifier").value(DEFAULT_IDENTIFIER.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON_STR))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON_STR));
    }

    @Test
    @Transactional
    public void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findOne(project.getId());
        updatedProject
                .name(UPDATED_NAME)
                .identifier(UPDATED_IDENTIFIER)
                .description(UPDATED_DESCRIPTION)
                .status(UPDATED_STATUS)
                .createdOn(UPDATED_CREATED_ON)
                .updatedOn(UPDATED_UPDATED_ON);

        restProjectMockMvc.perform(put("/api/projects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedProject)))
                .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projects.get(projects.size() - 1);
        assertThat(testProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProject.getIdentifier()).isEqualTo(UPDATED_IDENTIFIER);
        assertThat(testProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProject.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProject.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testProject.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
    }

    @Test
    @Transactional
    public void deleteProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Get the project
        restProjectMockMvc.perform(delete("/api/projects/{id}", project.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Project> projects = projectRepository.findAll();
        assertThat(projects).hasSize(databaseSizeBeforeDelete - 1);
    }
}
