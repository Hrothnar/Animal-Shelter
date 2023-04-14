package re.st.animalshelter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AnimalShelterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimalShelterApplication.class, args);
    }

}
