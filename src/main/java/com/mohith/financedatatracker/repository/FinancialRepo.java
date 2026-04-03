package com.mohith.financedatatracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.mohith.financedatatracker.model.Record;
import java.math.BigDecimal;
import java.util.List;

public interface FinancialRepo extends JpaRepository<Record, Long> {
    List<Record> findByTypeAndCategory(Record.RecordType type, String category);

    // Summary APIs: Calculate Total Income/Expense
    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM Record f WHERE f.type = :type")
    BigDecimal sumAmountByType(Record.RecordType type);

    // Summary APIs: Category-wise breakdown
    @Query("SELECT f.category, SUM(f.amount) FROM Record f WHERE f.type = 'EXPENSE' GROUP BY f.category")
    List<Object[]> getExpenseCategoryTotals();
}
