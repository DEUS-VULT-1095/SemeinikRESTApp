package com.semeinik.SemeinikRESTApp.mappers;

import com.semeinik.SemeinikRESTApp.dto.PersonDTO;
import com.semeinik.SemeinikRESTApp.models.Person;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class PersonMapper {
    private final ModelMapper modelMapper;

    @Autowired
    private PersonMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    public PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
