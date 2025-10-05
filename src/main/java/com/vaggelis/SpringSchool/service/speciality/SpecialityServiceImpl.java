package com.vaggelis.SpringSchool.service.speciality;

import com.vaggelis.SpringSchool.dto.speciality.SpecialityInsertDTO;
import com.vaggelis.SpringSchool.dto.speciality.SpecialityUpdateDTO;
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

    /**Creates a speciality
     *
     * @param dto
     * @return
     * @throws SpecialityAlreadyExistsException
     */
    @Override
    public Speciality addSpeciality(SpecialityInsertDTO dto) throws SpecialityAlreadyExistsException {
        Speciality speciality;

        try {
            //Maps the specialitydto to the speciality
            speciality = Mapper.mapSpecialityToInsertDto(dto);

            //Checks if the speciality exists if not it creates one
            Optional<Speciality> returnedSpeciality = specialityRepository.findByName(dto.getName());

            if (returnedSpeciality.isPresent())throw new SpecialityAlreadyExistsException(Speciality.class, speciality.getName());

            specialityRepository.save(speciality);
        }catch (SpecialityAlreadyExistsException e){
            throw e;
        }return speciality;
    }

    /**Deletes a speciality by the id
     *
     * @param id
     * @return
     * @throws SpecialityNotFoundException
     */
    @Override
    public Speciality deleteSpeciality(Long id) throws SpecialityNotFoundException {
        Speciality speciality;

        try {
            //Finds the speciality by the id
            speciality = specialityRepository.findSpecialityById(id);
            //if the speciality exists it gets deleted
            if (speciality == null) throw new SpecialityNotFoundException(Speciality.class, id);
            specialityRepository.delete(speciality);
        }catch (SpecialityNotFoundException e){
            throw e;
        }

        return speciality;

    }

    /**Finds a speciality by the id
     *
     * @param id
     * @return
     * @throws SpecialityNotFoundException
     */
    @Override
    public Speciality findSpecialityById(Long id) throws SpecialityNotFoundException {
        Speciality speciality;

        try {
            //Finds the speciality by the id
            speciality = specialityRepository.findSpecialityById(id);

            if (speciality == null) throw new SpecialityNotFoundException(Speciality.class, id);
        }catch (SpecialityNotFoundException e){
            throw e;
        }
        return speciality;
    }

    /**Finds all specialities
     *
     * @return
     */
    @Override
    public List<Speciality> findAllSpecialities() {
        return specialityRepository.findAll();
    }

    /**Updates Speciality
     *
     * @param dto
     * @return
     * @throws SpecialityNotFoundException
     */
    @Override
    public Speciality updateSpeciality(SpecialityUpdateDTO dto) throws SpecialityNotFoundException {
        Speciality speciality;

        try {
            //finds the speciality by the id and if it exists it gets updated
            speciality = specialityRepository.findSpecialityById(dto.getId());
            if (speciality == null) throw new SpecialityNotFoundException(Speciality.class, dto.getId());

            //Maps the speciality to the specialityUpdatedto
            Mapper.mapSpecialityToUpdateDto(speciality, dto);
            specialityRepository.save(speciality);
        }catch (SpecialityNotFoundException e){
            throw e;
        }
        return speciality;
    }
}
