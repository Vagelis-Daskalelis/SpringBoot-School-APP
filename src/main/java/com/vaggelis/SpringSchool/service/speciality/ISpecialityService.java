package com.vaggelis.SpringSchool.service.speciality;

import com.vaggelis.SpringSchool.dto.speciality.SpecialityInsertDTO;
import com.vaggelis.SpringSchool.dto.speciality.SpecialityUpdateDTO;
import com.vaggelis.SpringSchool.exception.speciality.SpecialityAlreadyExistsException;
import com.vaggelis.SpringSchool.exception.speciality.SpecialityNotFoundException;
import com.vaggelis.SpringSchool.models.Speciality;

import java.util.List;

public interface ISpecialityService {
    Speciality addSpeciality(SpecialityInsertDTO dto) throws SpecialityAlreadyExistsException;
    Speciality deleteSpeciality(Long id)throws SpecialityNotFoundException;
    Speciality findSpecialityById(Long id)throws SpecialityNotFoundException;
    List<Speciality> findAllSpecialities();
    Speciality updateSpeciality(SpecialityUpdateDTO dto) throws SpecialityNotFoundException;
}
