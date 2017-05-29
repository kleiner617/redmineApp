package com.ippon.redmineapp.web.rest;

import com.ippon.redmineapp.RedmineappApp;

import com.ippon.redmineapp.domain.FixedVersion;
import com.ippon.redmineapp.repository.FixedVersionRepository;

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
 * Test class for the FixedVersionResource REST controller.
 *
 * @see FixedVersionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RedmineappApp.class)
public class FixedVersionResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private FixedVersionRepository fixedVersionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFixedVersionMockMvc;

    private FixedVersion fixedVersion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FixedVersionResource fixedVersionResource = new FixedVersionResource();
        ReflectionTestUtils.setField(fixedVersionResource, "fixedVersionRepository", fixedVersionRepository);
        this.restFixedVersionMockMvc = MockMvcBuilders.standaloneSetup(fixedVersionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FixedVersion createEntity(EntityManager em) {
        FixedVersion fixedVersion = new FixedVersion()
                .name(DEFAULT_NAME);
        return fixedVersion;
    }

    @Before
    public void initTest() {
        fixedVersion = createEntity(em);
    }

    @Test
    @Transactional
    public void createFixedVersion() throws Exception {
        int databaseSizeBeforeCreate = fixedVersionRepository.findAll().size();

        // Create the FixedVersion

        restFixedVersionMockMvc.perform(post("/api/fixed-versions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(fixedVersion)))
                .andExpect(status().isCreated());

        // Validate the FixedVersion in the database
        List<FixedVersion> fixedVersions = fixedVersionRepository.findAll();
        assertThat(fixedVersions).hasSize(databaseSizeBeforeCreate + 1);
        FixedVersion testFixedVersion = fixedVersions.get(fixedVersions.size() - 1);
        assertThat(testFixedVersion.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllFixedVersions() throws Exception {
        // Initialize the database
        fixedVersionRepository.saveAndFlush(fixedVersion);

        // Get all the fixedVersions
        restFixedVersionMockMvc.perform(get("/api/fixed-versions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(fixedVersion.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getFixedVersion() throws Exception {
        // Initialize the database
        fixedVersionRepository.saveAndFlush(fixedVersion);

        // Get the fixedVersion
        restFixedVersionMockMvc.perform(get("/api/fixed-versions/{id}", fixedVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(fixedVersion.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFixedVersion() throws Exception {
        // Get the fixedVersion
        restFixedVersionMockMvc.perform(get("/api/fixed-versions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFixedVersion() throws Exception {
        // Initialize the database
        fixedVersionRepository.saveAndFlush(fixedVersion);
        int databaseSizeBeforeUpdate = fixedVersionRepository.findAll().size();

        // Update the fixedVersion
        FixedVersion updatedFixedVersion = fixedVersionRepository.findOne(fixedVersion.getId());
        updatedFixedVersion
                .name(UPDATED_NAME);

        restFixedVersionMockMvc.perform(put("/api/fixed-versions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFixedVersion)))
                .andExpect(status().isOk());

        // Validate the FixedVersion in the database
        List<FixedVersion> fixedVersions = fixedVersionRepository.findAll();
        assertThat(fixedVersions).hasSize(databaseSizeBeforeUpdate);
        FixedVersion testFixedVersion = fixedVersions.get(fixedVersions.size() - 1);
        assertThat(testFixedVersion.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteFixedVersion() throws Exception {
        // Initialize the database
        fixedVersionRepository.saveAndFlush(fixedVersion);
        int databaseSizeBeforeDelete = fixedVersionRepository.findAll().size();

        // Get the fixedVersion
        restFixedVersionMockMvc.perform(delete("/api/fixed-versions/{id}", fixedVersion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FixedVersion> fixedVersions = fixedVersionRepository.findAll();
        assertThat(fixedVersions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
