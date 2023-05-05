package re.st.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
import java.util.LinkedList;
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

    public void saveCat(int age, CatBreed breed, int amount) {
        for (int i = 0; i < amount; i++) {
            animalRepository.save(new Cat(age, breed));
        }
    }

    public void saveDog(int age, DogBreed breed, int amount) {
        for (int i = 0; i < amount; i++) {
            animalRepository.save(new Dog(age, breed));
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

    public LinkedList<Cat> getActiveCats() {
        return catRepository.findAllByActiveIsTrueAndUserIsNull().stream()
                .sorted(Comparator.comparing(Cat::getBreed))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public LinkedList<Dog> getActiveDogs() {
        return dogRepository.findAllByActiveIsTrueAndUserIsNull().stream()
                .sorted(Comparator.comparing(Dog::getBreed))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void saveAnimal(Animal animal) {
        animalRepository.save(animal);
    }



}
