package org.fergoeqs.blps1.delegators;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.fergoeqs.blps1.dto.ApplicationResponse;
import org.fergoeqs.blps1.services.ApplicationService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component("AddCoverLetterDelegator")
@RequiredArgsConstructor
public class AddCoverLetterDelegator implements JavaDelegate {

    private final ApplicationService applicationService;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void execute(DelegateExecution execution) {
        transactionTemplate.execute(status -> {
            Long applicationId = getLongVariable(execution, "applicationId");
            String coverLetter = getStringVariable(execution, "content");

            logBeforeUpdate(applicationId, coverLetter);
            try {
                ApplicationResponse response = applicationService.addCoverLetter(applicationId, coverLetter);
                saveResultsToExecution(execution, response);
                logAfterUpdate(response);
                return response;
            } catch (Exception e) {
                throw new BpmnError("LetterValidationError");
            }
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

    private String getStringVariable(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        if (value == null) {
            throw new IllegalArgumentException("Variable '" + name + "' is required");
        }
        return value.toString();
    }

    private void logBeforeUpdate(Long applicationId, String coverLetter) {
        System.out.println("=== Добавление сопроводительного письма ===");
        System.out.println("Application ID: " + applicationId);
        System.out.println("Cover letter length: " + (coverLetter != null ? coverLetter.length() : 0));
    }

    private void saveResultsToExecution(DelegateExecution execution, ApplicationResponse response) {
        execution.setVariable("updatedApplicationId", response.id());
        execution.setVariable("newApplicationStatus", response.status());
        execution.setVariable("remainingSlots", response.remainingSlots());

        if (response.warningMessage() != null) {
            execution.setVariable("warningMessage", response.warningMessage());
        }
    }

    private void logAfterUpdate(ApplicationResponse response) {
        System.out.println("=== Сопроводительное письмо успешно добавлено ===");
        System.out.println("Updated application ID: " + response.id());
        System.out.println("New status: " + response.status());
        System.out.println("Remaining slots: " + response.remainingSlots());

        if (response.warningMessage() != null) {
            System.out.println("Warning: " + response.warningMessage());
        }
    }
}
