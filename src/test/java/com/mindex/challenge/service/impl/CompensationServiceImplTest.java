package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.data.Compensation;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String compensationUrl;
    private String compensationIdUrl;

    @Autowired
    private CompensationService compensationService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void test()
    {
        // Create checks
        Compensation testCompensation = new Compensation("62c1084e-6e34-4630-93fd-9153afb65309", 50.0, LocalDate.now());

        Compensation expectedCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();

        assertNotNull(expectedCompensation);
        assertNotNull(expectedCompensation.getEmployeeId());
        assertCompensationEquivalence(testCompensation, expectedCompensation);

        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, expectedCompensation.getEmployeeId()).getBody();

        assertNotNull(readCompensation);
        assertNotNull(readCompensation.getEmployeeId());
        assertCompensationEquivalence(expectedCompensation, readCompensation);
    }

    private void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        final double FLOAT_MAX_DELTA = 1e-10;
        assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
        assertEquals(expected.getSalary(), actual.getSalary(), FLOAT_MAX_DELTA);
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }

}
