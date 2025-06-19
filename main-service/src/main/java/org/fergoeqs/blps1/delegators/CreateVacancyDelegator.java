package org.fergoeqs.blps1.delegators;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.fergoeqs.blps1.dto.VacancyRequest;
import org.fergoeqs.blps1.dto.VacancyResponse;
import org.fergoeqs.blps1.services.VacancyService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Component("createVacancyDelegator")
@RequiredArgsConstructor

public class CreateVacancyDelegator implements JavaDelegate {

    private final VacancyService vacancyService;
    private final TransactionTemplate transactionTemplate;


    @Override
    public void execute(DelegateExecution execution) {
        transactionTemplate.execute(status -> {
            Object userId = execution.getVariable("userId");
            Object pendingLimit = execution.getVariable("pendingLimit");
            long userIdValue = (userId instanceof Number)
                    ? ((Number) userId).longValue()
                    : Long.parseLong(userId.toString());
            int pendingLimitValue = (pendingLimit instanceof Number)
                    ? ((Number) pendingLimit).intValue()
                    : Integer.parseInt(pendingLimit.toString());
            System.out.println("ТУТ ЧЕТО ПРОИСХОДИТ?");

            VacancyRequest request = new VacancyRequest(execution.getVariable("title").toString(),
                    execution.getVariable("description").toString(),
                    (Boolean) execution.getVariable("resumeRequired"),
                    (Boolean) execution.getVariable("coverLetterRequired"),
                    execution.getVariable("keywords").toString(),
                    Long.parseLong(execution.getVariable("employerId").toString()),
                    pendingLimitValue);
            System.out.println("вахуи пон");
            VacancyResponse response = vacancyService.createVacancy(request, userIdValue);

            return response;
        });


        System.out.println("Вакансия должна была добавиться...");
    }
}
