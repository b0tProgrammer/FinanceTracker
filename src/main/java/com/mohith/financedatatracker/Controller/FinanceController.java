package com.mohith.financedatatracker.Controller;

import com.mohith.financedatatracker.dto.DashboardSummaryDTO;
import com.mohith.financedatatracker.dto.FinancialRecordRequest;
import com.mohith.financedatatracker.model.Record;
import com.mohith.financedatatracker.service.FinancialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinancialService financeService;

    // ADMIN only: Create records
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Record createRecord(@Valid @RequestBody FinancialRecordRequest request) {
        return financeService.createRecord(request);
    }

    // ADMIN only: Soft Delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecord(@PathVariable Long id) {
        financeService.deleteRecord(id);
    }

    // ANALYST and ADMIN: View Summaries
    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public DashboardSummaryDTO getSummary() {
        return financeService.getDashboardSummary();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('VIEWER', 'ANALYST', 'ADMIN')")
    public List<Record> getAllRecords() {
        return financeService.getAllRecords();
    }
}
