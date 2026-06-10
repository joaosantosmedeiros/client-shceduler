package god.joaopedro.client_scheduler.commons.doctor.service;

import god.joaopedro.client_scheduler.commons.doctor.model.Doctor;
import god.joaopedro.client_scheduler.commons.doctor.model.dto.DoctorDTO;
import god.joaopedro.client_scheduler.commons.doctor.repository.DoctorRepository;
import god.joaopedro.client_scheduler.exceptions.InvalidFieldException;
import god.joaopedro.client_scheduler.utils.Constants;
import god.joaopedro.client_scheduler.utils.CpfValidator;
import god.joaopedro.client_scheduler.utils.CrmValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository repository;

    public Doctor getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    public List<Doctor> list() {
        return repository.findAll();
    }

    public Doctor create(DoctorDTO dto) {
        if(dto == null)
            throw new IllegalArgumentException("médico não deve ser nulo");

        if(!CpfValidator.validate(dto.cpf()))
            throw new InvalidFieldException(Constants.CPF, Constants.INVALID_OBJECT);
        if(!CrmValidator.validate(dto.crm()))
            throw new InvalidFieldException(Constants.CRM, Constants.INVALID_OBJECT);

        if(repository.findByCpf(dto.cpf()).isPresent())
            throw new InvalidFieldException(Constants.CPF, Constants.IN_USE);
        if(repository.findByCrm(dto.crm()).isPresent())
            throw new InvalidFieldException(Constants.CRM, Constants.IN_USE);

        return repository.save(new Doctor(dto));
    }
}
