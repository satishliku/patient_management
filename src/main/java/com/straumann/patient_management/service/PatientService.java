package com.straumann.patient_management.service;

import com.straumann.patient_management.exception.PatientNotFoundException;
import com.straumann.patient_management.model.Patient;
import com.straumann.patient_management.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "patients")
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Cacheable
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Cacheable
    public Optional<Patient> getPatientById(Long id) {
        return patientRepository.findById(id);
    }

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long id, Patient updatedPatient) {
        if (patientRepository.existsById(id)) {
            updatedPatient.setId(id);
            return patientRepository.save(updatedPatient);
        } else {
            throw new PatientNotFoundException("Patient not found with id: " + id);
        }
    }

    public void deletePatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
        } else {
            throw new PatientNotFoundException("Patient not found with id: " + id);
        }
    }
}
