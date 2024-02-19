package com.straumann.patient_management.service;

import com.straumann.patient_management.exception.PatientNotFoundException;
import com.straumann.patient_management.model.Patient;
import com.straumann.patient_management.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    public void testGetAllPatients() {
        Patient patient1 = new Patient();
        patient1.setId(1L);
        patient1.setName("John");
        patient1.setAge(30);

        Patient patient2 = new Patient();
        patient2.setId(2L);
        patient2.setName("Alice");
        patient2.setAge(25);

        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient1, patient2));

        List<Patient> patients = patientService.getAllPatients();

        assertEquals(2, patients.size());
        assertEquals("John", patients.get(0).getName());
        assertEquals("Alice", patients.get(1).getName());
    }

    @Test
    public void testGetPatientById() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John");
        patient.setAge(30);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Optional<Patient> optionalPatient = patientService.getPatientById(1L);

        assertEquals(patient, optionalPatient.orElse(null));
    }

    @Test
    public void testCreatePatient() {
        Patient patient = new Patient();
        patient.setName("John");
        patient.setAge(30);

        when(patientRepository.save(patient)).thenReturn(patient);

        Patient createdPatient = patientService.createPatient(patient);

        assertEquals("John", createdPatient.getName());
        assertEquals(30, createdPatient.getAge());
    }

    @Test
    public void testUpdatePatient() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("John");
        patient.setAge(30);

        Patient updatedPatient = new Patient();
        updatedPatient.setId(1L);
        updatedPatient.setName("Alice");
        updatedPatient.setAge(25);

        when(patientRepository.existsById(1L)).thenReturn(true);
        when(patientRepository.save(updatedPatient)).thenReturn(updatedPatient);

        Patient result = patientService.updatePatient(1L, updatedPatient);

        assertEquals("Alice", result.getName());
        assertEquals(25, result.getAge());
    }

    @Test
    public void testDeletePatient() {
        long id = 1L;

        when(patientRepository.existsById(id)).thenReturn(true);

        patientService.deletePatient(id);

        verify(patientRepository, times(1)).deleteById(id);
    }

    @Test
    public void testDeletePatient_NotFound() {
        long id = 1L;

        when(patientRepository.existsById(id)).thenReturn(false);

        assertThrows(PatientNotFoundException.class, () -> patientService.deletePatient(id));
    }
}
