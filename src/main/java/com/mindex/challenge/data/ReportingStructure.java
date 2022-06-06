package com.mindex.challenge.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportingStructure {
  private Employee employee;
  private int numberOfReports;

}
