package re.st.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.st.animalshelter.dto.animal.CatDTO;
import re.st.animalshelter.dto.animal.DogDTO;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.entity.animal.Cat;
import re.st.animalshelter.entity.animal.Dog;
import re.st.animalshelter.enumeration.breed.CatBreed;
import re.st.animalshelter.enumeration.breed.DogBreed;
import re.st.animalshelter.repository.animal.AnimalRepository;
import re.st.animalshelter.repository.animal.CatRepository;
import re.st.animalshelter.repository.animal.DogRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

@Service
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final CatRepository catRepository;
    private final DogRepository dogRepository;

    @Autowired
    public AnimalService(AnimalRepository animalRepository, CatRepository catRepository, DogRepository dogRepository) {
        this.animalRepository = animalRepository;
        this.catRepository = catRepository;
        this.dogRepository = dogRepository;
    }

    public void saveAnimal(Animal animal) {
        animalRepository.save(animal);
    }

    public void saveCat(int age, CatBreed breed, int amount) {
        for (int i = 0; i < amount; i++) {
            saveAnimal(new Cat(age, breed));
        }
    }

    public void saveDog(int age, DogBreed breed, int amount) {
        for (int i = 0; i < amount; i++) {
            saveAnimal(new Dog(age, breed));
        }
    }

    public Animal getAnimalById(long id) {
        return animalRepository.findById(id).orElseThrow(RuntimeException::new); //TODO
    }

    public void updateExpirationTime(long animalId, int time) {
        Animal animal = getAnimalById(animalId);
        LocalDateTime expirationDate = animal.getExpirationDate();
        animal.setExpirationDate(expirationDate.plusDays(time));
        animalRepository.save(animal);
    }

    public LinkedHashSet<CatDTO> getActiveCatsAsDTO() {
        return catRepository.findAllByActiveIsTrueAndUserIsNull().stream()
                .map(CatDTO::new)
                .sorted(Comparator.comparing(CatDTO::getBreed))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public LinkedHashSet<DogDTO> getActiveDogsAsDTO() {
        return dogRepository.findAllByActiveIsTrueAndUserIsNull().stream()
                .map(DogDTO::new)
                .sorted(Comparator.comparing(DogDTO::getBreed))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
