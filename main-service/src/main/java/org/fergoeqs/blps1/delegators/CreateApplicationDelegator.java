package org.fergoeqs.blps1.delegators;

import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.fergoeqs.blps1.dto.ApplicationRequest;
import org.fergoeqs.blps1.dto.ApplicationResponse;
import org.fergoeqs.blps1.services.ApplicationService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

@Component("CreateApplicationDelegator")
@RequiredArgsConstructor
public class CreateApplicationDelegator implements JavaDelegate {

    private final ApplicationService applicationService;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void execute(DelegateExecution execution) {
        transactionTemplate.execute(status -> {
            Long vacancyId = getLongVariable(execution, "vacancyId");
            Long applicantId = getLongVariable(execution, "applicantId");
            Long resumeId = getNullableLongVariable(execution, "resumeId");
            String coverLetter = getNullableStringVariable(execution, "coverLetter");

            ApplicationRequest request = new ApplicationRequest(
                    vacancyId,
                    applicantId,
                    resumeId,
                    coverLetter
            );

            try {
                ApplicationResponse response = applicationService.createApplication(request);

                execution.setVariable("applicationId", response.id());
                execution.setVariable("applicationStatus", response.status());
                execution.setVariable("warningMessage", response.warningMessage());

                return response;
            } catch (Exception e) {
                throw new BpmnError("ApplicationValidationError");
            }
        });
    }

    private Long getLongVariable(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            return Long.parseLong((String) value);
        }
        throw new IllegalArgumentException("Invalid type for variable: " + name);
    }

    private Long getNullableLongVariable(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        if (value == null) return null;
        return getLongVariable(execution, name);
    }

    private String getNullableStringVariable(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        return value != null ? value.toString() : null;
    }
}