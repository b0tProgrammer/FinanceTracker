package com.mohith.financedatatracker.dto;

import com.mohith.financedatatracker.model.Record;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FinancialRecordRequest {
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be strictly greater than zero")
    @Digits(integer = 13, fraction = 2, message = "Amount must have up to 2 decimal places")
    private BigDecimal amount;

    @NotNull(message = "Record type is required (INCOME or EXPENSE)")
    private Record.RecordType type;

    @NotBlank(message = "Category cannot be blank")
    @Size(max = 50, message = "Category name cannot exceed 50 characters")
    private String category;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Date cannot be in the future")
    private LocalDate date;

    @Size(max = 255, message = "Notes cannot exceed 255 characters")
    private String notes;
}
