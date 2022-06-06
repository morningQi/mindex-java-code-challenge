package com.mindex.challenge.data;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Compensation {
  private String employeeId;
  private double salary;
  private LocalDate effectiveDate;

}
