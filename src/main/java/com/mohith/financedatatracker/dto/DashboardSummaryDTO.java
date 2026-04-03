package com.mohith.financedatatracker.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryDTO {

    // Total income across all records
    private BigDecimal totalIncome;

    // Total expenses across all records
    private BigDecimal totalExpenses;

    // Net balance (totalIncome - totalExpenses)
    private BigDecimal netBalance;

    // A map of category names to their total expense amounts (e.g., {"Rent": 1500.00, "Groceries": 300.00})
    private Map<String, BigDecimal> categoryTotals;
}