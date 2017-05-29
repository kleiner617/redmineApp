package com.ippon.redmineapp.web.rest;

import com.ippon.redmineapp.RedmineappApp;

import com.ippon.redmineapp.domain.Tracker;
import com.ippon.redmineapp.repository.TrackerRepository;

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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TrackerResource REST controller.
 *
 * @see TrackerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedmineappApp.class)
public class TrackerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private TrackerRepository trackerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTrackerMockMvc;

    private Tracker tracker;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrackerResource trackerResource = new TrackerResource();
        ReflectionTestUtils.setField(trackerResource, "trackerRepository", trackerRepository);
        this.restTrackerMockMvc = MockMvcBuilders.standaloneSetup(trackerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tracker createEntity(EntityManager em) {
        Tracker tracker = new Tracker()
                .name(DEFAULT_NAME);
        return tracker;
    }

    @Before
    public void initTest() {
        tracker = createEntity(em);
    }

    @Test
    @Transactional
    public void createTracker() throws Exception {
        int databaseSizeBeforeCreate = trackerRepository.findAll().size();

        // Create the Tracker

        restTrackerMockMvc.perform(post("/api/trackers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tracker)))
                .andExpect(status().isCreated());

        // Validate the Tracker in the database
        List<Tracker> trackers = trackerRepository.findAll();
        assertThat(trackers).hasSize(databaseSizeBeforeCreate + 1);
        Tracker testTracker = trackers.get(trackers.size() - 1);
        assertThat(testTracker.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllTrackers() throws Exception {
        // Initialize the database
        trackerRepository.saveAndFlush(tracker);

        // Get all the trackers
        restTrackerMockMvc.perform(get("/api/trackers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tracker.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getTracker() throws Exception {
        // Initialize the database
        trackerRepository.saveAndFlush(tracker);

        // Get the tracker
        restTrackerMockMvc.perform(get("/api/trackers/{id}", tracker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tracker.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTracker() throws Exception {
        // Get the tracker
        restTrackerMockMvc.perform(get("/api/trackers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTracker() throws Exception {
        // Initialize the database
        trackerRepository.saveAndFlush(tracker);
        int databaseSizeBeforeUpdate = trackerRepository.findAll().size();

        // Update the tracker
        Tracker updatedTracker = trackerRepository.findOne(tracker.getId());
        updatedTracker
                .name(UPDATED_NAME);

        restTrackerMockMvc.perform(put("/api/trackers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTracker)))
                .andExpect(status().isOk());

        // Validate the Tracker in the database
        List<Tracker> trackers = trackerRepository.findAll();
        assertThat(trackers).hasSize(databaseSizeBeforeUpdate);
        Tracker testTracker = trackers.get(trackers.size() - 1);
        assertThat(testTracker.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteTracker() throws Exception {
        // Initialize the database
        trackerRepository.saveAndFlush(tracker);
        int databaseSizeBeforeDelete = trackerRepository.findAll().size();

        // Get the tracker
        restTrackerMockMvc.perform(delete("/api/trackers/{id}", tracker.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Tracker> trackers = trackerRepository.findAll();
        assertThat(trackers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
