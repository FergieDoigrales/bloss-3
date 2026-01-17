package org.fergoeqs.blps1.services;

import org.fergoeqs.blps1.dto.ApplicantRequest;
import org.fergoeqs.blps1.dto.ApplicantResponse;
import org.fergoeqs.blps1.dto.ResumeResponse;
import org.fergoeqs.blps1.exceptions.ResourceNotFoundException;
import org.fergoeqs.blps1.models.applicantdb.Applicant;
import org.fergoeqs.blps1.models.applicantdb.Resume;
import org.fergoeqs.blps1.repositories.applicantdb.ApplicantRepository;
import org.fergoeqs.blps1.repositories.applicantdb.ResumeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final ResumeRepository resumeRepository;


    public ApplicantService(ApplicantRepository applicantRepository, ResumeRepository resumeRepository) {
        this.applicantRepository = applicantRepository;
        this.resumeRepository = resumeRepository;
    }

    public Optional<Applicant> getApplicantById(Long id) {
        return applicantRepository.findById(id);
    }

    @Transactional
    public ApplicantResponse createApplicant(ApplicantRequest request) {
                    Applicant applicant = new Applicant();
                    applicant.setName(request.name());
                    applicant.setContactInfo(request.contactInfo());
                    applicant.setMail(request.email());

                    Applicant saved = applicantRepository.save(applicant);
                    return new ApplicantResponse(
                            saved.getId(),
                            saved.getName(),
                            saved.getContactInfo(),
                            saved.getMail()
                    );
                }


    @Transactional
    public void deleteApplicant(Long id) {
                    applicantRepository.deleteById(id);
    }

    @Transactional
    public Resume addResume(Long applicantId, Resume resume) {

                    Applicant applicant = applicantRepository.findById(applicantId)
                            .orElseThrow(() -> new IllegalArgumentException("Applicant not found with id: " + applicantId));
                    if (resume == null) {
                        throw new IllegalArgumentException("Resume cannot be null");
                    }
                    resume.setApplicant(applicant);
                    return resumeRepository.save(resume);
                }


    public Page<ResumeResponse> getResumesByApplicantId(Long applicantId, Pageable pageable) {
        if (!applicantRepository.existsById(applicantId)) {
            throw new ResourceNotFoundException("Applicant not found");
        }
        return resumeRepository.findByApplicantId(applicantId, pageable)
                .map(resume -> new ResumeResponse(resume.getId(), resume.getContent()));
    }
}