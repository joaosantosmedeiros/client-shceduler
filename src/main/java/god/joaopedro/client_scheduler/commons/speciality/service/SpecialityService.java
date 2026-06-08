package god.joaopedro.client_scheduler.commons.speciality.service;

import god.joaopedro.client_scheduler.commons.speciality.model.Speciality;
import god.joaopedro.client_scheduler.commons.speciality.model.dto.SpecialityDTO;
import god.joaopedro.client_scheduler.commons.speciality.repository.SpecialityRepository;
import god.joaopedro.client_scheduler.exceptions.InvalidFieldException;
import god.joaopedro.client_scheduler.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialityService {

    private final SpecialityRepository repository;

    public Speciality createSpeciality(SpecialityDTO dto) {
        if(dto == null)
            throw new IllegalArgumentException("especialidade não deve ser nula");

        if(repository.findByName(dto.name()).isPresent())
            throw new InvalidFieldException(Constants.ID, Constants.IN_USE);

        return repository.save(new Speciality(dto));
    }
}
