package com.example.engine.batch;


import com.example.engine.domain.entity.LoanApplication;
import com.example.engine.domain.enums.LoanApplicationStatus;
import com.example.engine.repository.LoanApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanStatusScheduler {

    private final LoanApplicationRepository loanRepository;

    @Scheduled(fixedDelay = 60_000)
    public void moveSubmittedLoansToUnderReview() {


        LocalDateTime cutoffTime =
                LocalDateTime.now().minusMinutes(2);

        List<LoanApplication> loans =
                loanRepository.findByStatusAndCreatedAtBefore(
                        LoanApplicationStatus.SUBMITTED,
                        cutoffTime
                );

        if (loans.isEmpty()) {
            return;
        }

        for (LoanApplication loan : loans) {
            loan.setStatus(LoanApplicationStatus.UNDER_REVIEW);

            log.info(
                    "Loan {} moved from SUBMITTED → UNDER_REVIEW",
                    loan.getId()
            );
        }

        loanRepository.saveAll(loans);
    }
}
