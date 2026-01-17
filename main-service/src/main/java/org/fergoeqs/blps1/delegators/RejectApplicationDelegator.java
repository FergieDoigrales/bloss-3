package org.fergoeqs.blps1.delegators;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.fergoeqs.blps1.dto.ApplicationResponse;
import org.fergoeqs.blps1.services.ApplicationService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component("RejectApplicationDelegator")
@RequiredArgsConstructor
public class RejectApplicationDelegator implements JavaDelegate {

    private final ApplicationService applicationService;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void execute(DelegateExecution execution) {
        transactionTemplate.execute(status -> {
            Long applicationId = getLongVariable(execution, "applicationId");

            ApplicationResponse response = applicationService.rejectApplication(applicationId, -1L);
            saveResultsToExecution(execution, response);
            logAfterReject(response);

            return response;
        });
    }

    private Long getLongVariable(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        if (value == null) {
            throw new IllegalArgumentException("Variable '" + name + "' is required");
        }
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid format for variable '" + name + "': " + value);
            }
        }
        throw new IllegalArgumentException("Unsupported type for variable '" + name + "': " + value.getClass());
    }

    private void logBeforeReject(Long applicationId, Long userId) {
        System.out.println("=== Отклонение заявки ===");
        System.out.println("Application ID: " + applicationId);
        System.out.println("User ID: " + userId);
    }

    private void saveResultsToExecution(DelegateExecution execution, ApplicationResponse response) {
        execution.setVariable("rejectedApplicationId", response.id());
        execution.setVariable("newStatus", response.status());
        execution.setVariable("applicantName", response.applicantName());
        execution.setVariable("vacancyTitle", response.vacancyTitle());
    }

    private void logAfterReject(ApplicationResponse response) {
        System.out.println("=== Заявка успешно отклонена ===");
        System.out.println("Application ID: " + response.id());
        System.out.println("New status: " + response.status());
        System.out.println("Applicant: " + response.applicantName());
        System.out.println("Vacancy: " + response.vacancyTitle());
    }
}