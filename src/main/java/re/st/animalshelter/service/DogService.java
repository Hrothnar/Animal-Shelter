package re.st.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.st.animalshelter.repository.DogRepository;

@Service
public class DogService {
    private final DogRepository repository;

    @Autowired
    public DogService(DogRepository repository) {
        this.repository = repository;

    }
}
