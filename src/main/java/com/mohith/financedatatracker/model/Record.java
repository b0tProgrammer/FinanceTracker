package com.mohith.financedatatracker.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("is_deleted = false") // Automatically ignores soft-deleted records
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordType type;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private LocalDate date;
    private String notes;
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
    public enum RecordType {
        INCOME, EXPENSE
    }
}
