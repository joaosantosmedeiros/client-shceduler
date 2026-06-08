package god.joaopedro.client_scheduler.commons.speciality.service;

import god.joaopedro.client_scheduler.commons.speciality.model.Speciality;
import god.joaopedro.client_scheduler.commons.speciality.model.dto.SpecialityDTO;
import god.joaopedro.client_scheduler.commons.speciality.repository.SpecialityRepository;
import god.joaopedro.client_scheduler.exceptions.InvalidFieldException;
import god.joaopedro.client_scheduler.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SpecialityService {

    private final SpecialityRepository repository;

    public List<Speciality> findAll() {
        return repository.findAll();
    }

    public Speciality getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    public Speciality createSpeciality(SpecialityDTO dto) {
        if(dto == null)
            throw new IllegalArgumentException("especialidade não deve ser nula");

        if(repository.findByName(dto.name()).isPresent())
            throw new InvalidFieldException(Constants.NAME, Constants.IN_USE);

        return repository.save(new Speciality(dto));
    }

    public Speciality updateSpeciality(UUID id, SpecialityDTO dto) {
        if(dto == null)
            throw new IllegalArgumentException("especialidade não deve ser nula");

        Speciality speciality = repository.findById(id).orElseThrow(() ->
                new InvalidFieldException(Constants.ID, Constants.INVALID_REFERENCE));

        if(!speciality.getName().equalsIgnoreCase(dto.name())){
            if(repository.findByName(dto.name()).isPresent())
                throw new InvalidFieldException(Constants.NAME, Constants.IN_USE);

            speciality.setName(dto.name());
            speciality.setUpdatedAt(LocalDateTime.now());
            speciality = repository.save(speciality);
        }

        return speciality;
    }

    public void deleteSpeciality(UUID id) {
        Speciality speciality = repository.findById(id).orElseThrow(() ->
            new InvalidFieldException(Constants.ID, Constants.INVALID_REFERENCE));

        if(speciality.getIsActive()) {
            speciality.setIsActive(Boolean.FALSE);
            speciality.setUpdatedAt(LocalDateTime.now());
            repository.save(speciality);
        }
    }
}
