package re.st.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.st.animalshelter.entity.Animal1;
import re.st.animalshelter.repository.Animal1Repository;

import javax.persistence.Transient;

@Service
public class Animal1Service {
    private final Animal1Repository animal1Repository;

    @Autowired
    public Animal1Service(Animal1Repository animal1Repository) {
        this.animal1Repository = animal1Repository;
    }

    public Animal1 getAnimal1(long id) {
        return animal1Repository.getById(id);
    }
}
