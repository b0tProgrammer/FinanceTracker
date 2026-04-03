package com.mohith.financedatatracker.service;

import com.mohith.financedatatracker.dto.DashboardSummaryDTO;
import com.mohith.financedatatracker.dto.FinancialRecordRequest;
import com.mohith.financedatatracker.model.Record;
import com.mohith.financedatatracker.repository.FinancialRepo;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancialService {
    private final FinancialRepo repository;
    public Record createRecord(FinancialRecordRequest request) {
        Record record = Record.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .date(request.getDate())
                .notes(request.getNotes())
                .build();
        return repository.save(record);
    }

    public void deleteRecord(Long id) {
        Record record = repository.findById(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Record not found with id: " + id));
        record.setDeleted(true); // Softly delete
        repository.save(record);
    }

    public DashboardSummaryDTO getDashboardSummary() {
        BigDecimal totalIncome = repository.sumAmountByType(Record.RecordType.INCOME);
        BigDecimal totalExpense = repository.sumAmountByType(Record.RecordType.EXPENSE);
        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        // Convert List<Object[]> to a Map for easy JSON serialization
        Map<String, BigDecimal> categoryTotals = repository.getExpenseCategoryTotals().stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (BigDecimal) row[1]
                ));

        return new DashboardSummaryDTO(totalIncome, totalExpense, netBalance, categoryTotals);
    }

    public List<Record> getAllRecords() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "date"));
    }
}
