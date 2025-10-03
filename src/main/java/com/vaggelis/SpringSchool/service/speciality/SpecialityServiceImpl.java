package com.vaggelis.SpringSchool.service.speciality;

import com.vaggelis.SpringSchool.dto.SpecialityInsertDTO;
import com.vaggelis.SpringSchool.exception.speciality.SpecialityAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.speciality.SpecialityNotFoundException;
import com.vaggelis.SpringSchool.mapper.Mapper;
import com.vaggelis.SpringSchool.models.Speciality;
import com.vaggelis.SpringSchool.repository.ISpecialityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SpecialityServiceImpl implements ISpecialityService{

    private final ISpecialityRepository specialityRepository;

    @Override
    public Speciality addSpeciality(SpecialityInsertDTO dto) throws SpecialityAlreadyExistsException {
        Speciality speciality;

        try {
            speciality = Mapper.mapSpecialityToInsertDto(dto);

            Optional<Speciality> returnedSpeciality = specialityRepository.findByName(dto.getName());

            if (returnedSpeciality.isPresent())throw new SpecialityAlreadyExistsException(Speciality.class, speciality.getName());

            specialityRepository.save(speciality);
        }catch (SpecialityAlreadyExistsException e){
            throw e;
        }return speciality;
    }

    @Override
    public Speciality deleteSpeciality(Long id) throws SpecialityNotFoundException {
        Speciality speciality;

        try {
            speciality = specialityRepository.findSpecialityById(id);
            if (speciality == null) throw new SpecialityNotFoundException(Speciality.class, id);
            specialityRepository.delete(speciality);
        }catch (SpecialityNotFoundException e){
            throw e;
        }

        return speciality;

    }

    @Override
    public Speciality findSpecialityById(Long id) throws SpecialityNotFoundException {
        Speciality speciality;

        try {
            speciality = specialityRepository.findSpecialityById(id);
            if (speciality == null) throw new SpecialityNotFoundException(Speciality.class, id);
        }catch (SpecialityNotFoundException e){
            throw e;
        }
        return speciality;
    }

    @Override
    public List<Speciality> findAllSpecialities() {
        return specialityRepository.findAll();
    }
}
