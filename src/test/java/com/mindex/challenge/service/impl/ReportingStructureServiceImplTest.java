package com.mindex.challenge.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String reportingStructureIdUrl;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        reportingStructureIdUrl = "http://localhost:" + port + "/reportingStructure/employee/{id}";
    }

    @Test
    public void test() {

        Employee employeeWithCount4 = employeeService.read("16a596ae-edd3-4847-99fe-c4518e82c86f");
        Employee employeeWithCount0 = employeeService.read("62c1084e-6e34-4630-93fd-9153afb65309");

        // validating reporting structure for directReportsNumber = 4
        ReportingStructure reportingStructureWithCount4 = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, employeeWithCount4.getEmployeeId()).getBody();
        ReportingStructure expectedReportingStructureWithCount4 = new ReportingStructure(employeeWithCount4, 4);

        assertNotNull(reportingStructureWithCount4);
        assertReportingStructureEquivalence(expectedReportingStructureWithCount4, reportingStructureWithCount4);


        // validating reporting structure for directReportsNumber = 0
        ReportingStructure reportingStructureWithCount0 = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, employeeWithCount0.getEmployeeId()).getBody();
        ReportingStructure expectedReportingStructureWithCount0 = new ReportingStructure(employeeWithCount0, 0);

        assertNotNull(reportingStructureWithCount0);
        assertReportingStructureEquivalence(expectedReportingStructureWithCount0, reportingStructureWithCount0);

    }

    private static void assertReportingStructureEquivalence(ReportingStructure expected, ReportingStructure actual) {
        assertEquals(expected.getNumberOfReports(), actual.getNumberOfReports());
        assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
        // check direct reports of each employee
        if (expected.getDirectReports() != null){
            for(int i = 0; i < expected.getDirectReports().size(); i++){
                assertEmployeeEquivalence(expected.getDirectReports().get(i), actual.getDirectReports().get(i));
            }
        } else {
            assertNull(actual.getDirectReports());
        }
    }


}