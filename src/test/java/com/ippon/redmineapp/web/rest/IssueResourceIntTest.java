package com.ippon.redmineapp.web.rest;

import com.ippon.redmineapp.RedmineappApp;

import com.ippon.redmineapp.domain.Issue;
import com.ippon.redmineapp.repository.IssueRepository;

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
 * Test class for the IssueResource REST controller.
 *
 * @see IssueResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedmineappApp.class)
public class IssueResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_SUBJECT = "AAAAA";
    private static final String UPDATED_SUBJECT = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_START_DATE_STR = dateTimeFormatter.format(DEFAULT_START_DATE);
    private static final String DEFAULT_DONE_RATIO = "AAAAA";
    private static final String UPDATED_DONE_RATIO = "BBBBB";
    private static final String DEFAULT_ESTIMATED_HOURS = "AAAAA";
    private static final String UPDATED_ESTIMATED_HOURS = "BBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_ON_STR = dateTimeFormatter.format(DEFAULT_CREATED_ON);

    private static final ZonedDateTime DEFAULT_UPDATED_ON = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_UPDATED_ON = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_UPDATED_ON_STR = dateTimeFormatter.format(DEFAULT_UPDATED_ON);
    private static final String DEFAULT_STORY_POINTS = "AAAAA";
    private static final String UPDATED_STORY_POINTS = "BBBBB";

    @Inject
    private IssueRepository issueRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restIssueMockMvc;

    private Issue issue;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IssueResource issueResource = new IssueResource();
        ReflectionTestUtils.setField(issueResource, "issueRepository", issueRepository);
        this.restIssueMockMvc = MockMvcBuilders.standaloneSetup(issueResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Issue createEntity(EntityManager em) {
        Issue issue = new Issue()
                .subject(DEFAULT_SUBJECT)
                .description(DEFAULT_DESCRIPTION)
                .startDate(DEFAULT_START_DATE)
                .doneRatio(DEFAULT_DONE_RATIO)
                .estimatedHours(DEFAULT_ESTIMATED_HOURS)
                .createdOn(DEFAULT_CREATED_ON)
                .updatedOn(DEFAULT_UPDATED_ON)
                .storyPoints(DEFAULT_STORY_POINTS);
        return issue;
    }

    @Before
    public void initTest() {
        issue = createEntity(em);
    }

    @Test
    @Transactional
    public void createIssue() throws Exception {
        int databaseSizeBeforeCreate = issueRepository.findAll().size();

        // Create the Issue

        restIssueMockMvc.perform(post("/api/issues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(issue)))
                .andExpect(status().isCreated());

        // Validate the Issue in the database
        List<Issue> issues = issueRepository.findAll();
        assertThat(issues).hasSize(databaseSizeBeforeCreate + 1);
        Issue testIssue = issues.get(issues.size() - 1);
        assertThat(testIssue.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testIssue.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIssue.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testIssue.getDoneRatio()).isEqualTo(DEFAULT_DONE_RATIO);
        assertThat(testIssue.getEstimatedHours()).isEqualTo(DEFAULT_ESTIMATED_HOURS);
        assertThat(testIssue.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testIssue.getUpdatedOn()).isEqualTo(DEFAULT_UPDATED_ON);
        assertThat(testIssue.getStoryPoints()).isEqualTo(DEFAULT_STORY_POINTS);
    }

    @Test
    @Transactional
    public void getAllIssues() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get all the issues
        restIssueMockMvc.perform(get("/api/issues?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(issue.getId().intValue())))
                .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE_STR)))
                .andExpect(jsonPath("$.[*].doneRatio").value(hasItem(DEFAULT_DONE_RATIO.toString())))
                .andExpect(jsonPath("$.[*].estimatedHours").value(hasItem(DEFAULT_ESTIMATED_HOURS.toString())))
                .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON_STR)))
                .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON_STR)))
                .andExpect(jsonPath("$.[*].storyPoints").value(hasItem(DEFAULT_STORY_POINTS.toString())));
    }

    @Test
    @Transactional
    public void getIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);

        // Get the issue
        restIssueMockMvc.perform(get("/api/issues/{id}", issue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(issue.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE_STR))
            .andExpect(jsonPath("$.doneRatio").value(DEFAULT_DONE_RATIO.toString()))
            .andExpect(jsonPath("$.estimatedHours").value(DEFAULT_ESTIMATED_HOURS.toString()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON_STR))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON_STR))
            .andExpect(jsonPath("$.storyPoints").value(DEFAULT_STORY_POINTS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingIssue() throws Exception {
        // Get the issue
        restIssueMockMvc.perform(get("/api/issues/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);
        int databaseSizeBeforeUpdate = issueRepository.findAll().size();

        // Update the issue
        Issue updatedIssue = issueRepository.findOne(issue.getId());
        updatedIssue
                .subject(UPDATED_SUBJECT)
                .description(UPDATED_DESCRIPTION)
                .startDate(UPDATED_START_DATE)
                .doneRatio(UPDATED_DONE_RATIO)
                .estimatedHours(UPDATED_ESTIMATED_HOURS)
                .createdOn(UPDATED_CREATED_ON)
                .updatedOn(UPDATED_UPDATED_ON)
                .storyPoints(UPDATED_STORY_POINTS);

        restIssueMockMvc.perform(put("/api/issues")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedIssue)))
                .andExpect(status().isOk());

        // Validate the Issue in the database
        List<Issue> issues = issueRepository.findAll();
        assertThat(issues).hasSize(databaseSizeBeforeUpdate);
        Issue testIssue = issues.get(issues.size() - 1);
        assertThat(testIssue.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testIssue.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIssue.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testIssue.getDoneRatio()).isEqualTo(UPDATED_DONE_RATIO);
        assertThat(testIssue.getEstimatedHours()).isEqualTo(UPDATED_ESTIMATED_HOURS);
        assertThat(testIssue.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testIssue.getUpdatedOn()).isEqualTo(UPDATED_UPDATED_ON);
        assertThat(testIssue.getStoryPoints()).isEqualTo(UPDATED_STORY_POINTS);
    }

    @Test
    @Transactional
    public void deleteIssue() throws Exception {
        // Initialize the database
        issueRepository.saveAndFlush(issue);
        int databaseSizeBeforeDelete = issueRepository.findAll().size();

        // Get the issue
        restIssueMockMvc.perform(delete("/api/issues/{id}", issue.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Issue> issues = issueRepository.findAll();
        assertThat(issues).hasSize(databaseSizeBeforeDelete - 1);
    }
}
