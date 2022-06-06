package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure read(String id) {
        Employee employee = employeeRepository.findByEmployeeId(id);
        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return new ReportingStructure(employee, getNumberOfDirectReports(employee));
    }

    private int getNumberOfDirectReports(Employee employee) {
        LOG.debug("Getting total number of direct reports for employee [{}]", employee.getEmployeeId());
        // if the employee has no direct reports
        if (employee.getDirectReports() == null){
            return 0;
        }
        // Performing a BFS to find all the distinct employees
        Queue<Employee> queue = new LinkedList<>();
        queue.add(employee);
        // to avoid duplicate count
        Set<String> visited = new HashSet<>();
        visited.add(employee.getEmployeeId());
        int directReports = 0;

        // assuming no cyclic reporting
        while (!queue.isEmpty()) {
            Employee manager = queue.poll();
            if (!visited.contains(manager.getEmployeeId())) {
                directReports++;
                visited.add(manager.getEmployeeId());
            }

            List<Employee> reporting = manager.getDirectReports();

            if(reporting != null) {
                for (Employee em : reporting) {
                    queue.add(employeeRepository.findByEmployeeId((em.getEmployeeId())));
                }
            }
        }

        return directReports;
    }
}
