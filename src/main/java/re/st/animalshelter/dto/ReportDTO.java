package re.st.animalshelter.dto;

import re.st.animalshelter.entity.User;
import re.st.animalshelter.entity.animal.Animal;

public class ReportDTO {
    private  User user;
    private Animal animal;

    public ReportDTO(User user, Animal animal) {
        this.user = user;
        this.animal = animal;
    }

    public ReportDTO() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }
}
