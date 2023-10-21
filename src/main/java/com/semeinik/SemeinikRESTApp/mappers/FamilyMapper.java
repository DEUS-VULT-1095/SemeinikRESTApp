package com.semeinik.SemeinikRESTApp.mappers;

import com.semeinik.SemeinikRESTApp.dto.FamilyDTO;
import com.semeinik.SemeinikRESTApp.models.Family;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class FamilyMapper {
    private final ModelMapper modelMapper ;

    @Autowired
    private FamilyMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Family convertToFamily(FamilyDTO familyDTO) {
        return modelMapper.map(familyDTO, Family.class);
    }

    public FamilyDTO convertToFamilyDTO(Family family) {
        return modelMapper.map(family, FamilyDTO.class);
    }
}
