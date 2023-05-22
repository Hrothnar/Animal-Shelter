package re.st.animalshelter.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import re.st.animalshelter.entity.Action;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.entity.Volunteer;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.entity.animal.Cat;
import re.st.animalshelter.entity.animal.Dog;
import re.st.animalshelter.enumeration.Command;
import re.st.animalshelter.enumeration.Position;
import re.st.animalshelter.enumeration.Shelter;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.breed.CatBreed;
import re.st.animalshelter.enumeration.breed.DogBreed;
import re.st.animalshelter.service.AnimalService;
import re.st.animalshelter.service.UserService;
import re.st.animalshelter.service.VolunteerService;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@TestPropertySource(locations = "classpath:test.properties")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class VolunteerControllerTest {
    @Autowired
    private UserService userService;
    @Autowired
    private VolunteerService volunteerService;
    @Autowired
    private AnimalService animalService;
    @Autowired
    private MockMvc mockMvc;

    private final static long CHAT_ID = 11;
    private final static int MESSAGE_ID = 22;

    @AfterEach
    public void clean() {

    }

    @Test
    @Transactional
    public void appoint_shouldShowPageWithAllUsers() throws Exception {
        User user = createUserAndPets();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/volunteer/appoint"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("volunteer/appoint"))
                .andReturn();

        LinkedList<User> users = (LinkedList<User>) mvcResult.getModelAndView().getModel().get("users");

        Assertions.assertEquals(user, users.getFirst());
    }

    @Test
    @Transactional
    public void saveVolunteer_shouldAppointVolunteerAndRedirect() throws Exception {
        User user = createUserAndPets();
        String userName = user.getUserName();
        long userId = user.getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/volunteer/save_appoint")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("user_id", "0")
                        .param("userName", userName))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/volunteer/menu"));

        User foundVolunteerAsUser = userService.getById(userId);
        Volunteer volunteer = volunteerService.getByChatId(foundVolunteerAsUser.getChatId());

        Assertions.assertEquals(Position.VOLUNTEER, foundVolunteerAsUser.getPosition());
        Assertions.assertNotNull(volunteer);
    }

    @Test
    @Transactional
    public void saveVolunteer_shouldNotAppointVolunteerAndShowNotFoundPage() throws Exception {
        User user = createUserAndPets();
        String userName = user.getUserName();
        long userId = user.getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/volunteer/save_appoint")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("user_id", "0")
                        .param("userName", userName + "some"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("not_found"));

        User foundVolunteerAsUser = userService.getById(userId);
        int volunteerSize = volunteerService.getAllVolunteers().size();

        Assertions.assertNotEquals(Position.VOLUNTEER, foundVolunteerAsUser.getPosition());
        Assertions.assertEquals(0, volunteerSize);
    }

    @Test
    @Transactional
    public void findVolunteer_shouldShowPageWithAllVolunteers() throws Exception {
        Volunteer volunteer = attendVolunteer(createUserAndPets());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/volunteer/find"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("volunteer/find"))
                .andReturn();

        LinkedList<Volunteer> volunteers = (LinkedList<Volunteer>) mvcResult.getModelAndView().getModel().get("volunteers");

        Assertions.assertEquals(volunteer, volunteers.getFirst());
    }

    @Test
    @Transactional
    public void foundVolunteer_shouldFindVolunteerAndRedirect() throws Exception {
        Volunteer volunteer = attendVolunteer(createUserAndPets());
        long volunteerId = volunteer.getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/volunteer/receive")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("volunteer_id", String.valueOf(volunteerId))
                        .param("userName", ""))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/volunteer/" + volunteerId));
    }

    @Test
    @Transactional
    public void foundVolunteer_shouldNotFindVolunteerAndShowNotFindPage() throws Exception {
        Volunteer volunteer = attendVolunteer(createUserAndPets());
        long volunteerId = volunteer.getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/volunteer/receive")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("volunteer_id", String.valueOf(volunteerId + 1))
                        .param("userName", ""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("not_found"));
    }

    @Test
    @Transactional
    public void getVolunteerAction_shouldShowAvailableActions() throws Exception {
        Volunteer volunteer = attendVolunteer(createUserAndPets());
        long volunteerId = volunteer.getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/volunteer/{volunteerId}", volunteerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("volunteer/actions"));
    }

    @Test
    @Transactional
    public void removeVolunteer_shouldRemoveVolunteerAndShowRemovedPage() throws Exception {
        Volunteer volunteer = attendVolunteer(createUserAndPets());
        long volunteerId = volunteer.getId();
        int volunteerSize = volunteerService.getAllVolunteers().size();
        mockMvc.perform(MockMvcRequestBuilders.get("/volunteer/{volunteerId}/remove", volunteerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("volunteer/removed"));

        int foundVolunteerSize = volunteerService.getAllVolunteers().size();

        Assertions.assertEquals(1, volunteerSize);
        Assertions.assertEquals(0, foundVolunteerSize);
    }

    @Test
    @Transactional
    public void attachAnimal_shouldShowPageWithAnimalsForAttaching() throws Exception {
        Volunteer volunteer = attendVolunteer(createUserAndPets());
        long volunteerId = volunteer.getId();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/volunteer/{volunteerId}/attach_animal", volunteerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("volunteer/attach_animal"))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        long id = (long) model.get("id");
        List<Cat> cats = (List<Cat>) model.get("cats");
        List<Dog> dogs = (List<Dog>) model.get("dogs");
        List<Cat> catsWithoutVolunteer = animalService.getCatsWithoutVolunteer();
        List<Dog> dogsWithoutVolunteer = animalService.getDogsWithoutVolunteer();

        Assertions.assertArrayEquals(catsWithoutVolunteer.toArray(), cats.toArray());
        Assertions.assertArrayEquals(dogsWithoutVolunteer.toArray(), dogs.toArray());
        Assertions.assertEquals(volunteerId, id);
    }

    @Test
    @Transactional
    public void saveAnimal_shouldAttachAnimalToVolunteerAndRedirect() throws Exception {
        Volunteer volunteer = attendVolunteer(createUserAndPets());
        long volunteerId = volunteer.getId();
        Cat cat = animalService.getCatsWithoutVolunteer().stream().findAny().get();
        Volunteer catVolunteer = cat.getVolunteer();
        long catId = cat.getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/volunteer/{volunteerId}/save_animal", volunteerId)
                        .param("animal_id", String.valueOf(catId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/volunteer/" + volunteerId));

        Animal foundAnimal = animalService.getById(catId);

        Assertions.assertNull(catVolunteer);
        Assertions.assertEquals(volunteer, foundAnimal.getVolunteer());
        Assertions.assertEquals(cat, volunteer.getAnimals().stream().findAny().get());
    }

    @Test
    @Transactional
    public void removeAnimal_shouldShowPageWithAnimalsForRemoving() throws Exception {
        Volunteer volunteer = attendVolunteer(createUserAndPets());
        long volunteerId = volunteer.getId();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/volunteer/{volunteerId}/remove_animal", volunteerId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("volunteer/remove_animal"))
                .andReturn();

        Map<String, Object> model = mvcResult.getModelAndView().getModel();
        Set<Animal> volunteerAnimals = volunteer.getAnimals();
        Set<Animal> animals = (Set<Animal>) model.get("animals");

        Assertions.assertArrayEquals(volunteerAnimals.toArray(), animals.toArray());
    }

    @Test
    @Transactional
    public void saveRemovedAnimal_shouldRemoveAnimalAndRedirect() throws Exception {
        User user = createUserAndPets();
        Animal animal = user.getActiveAnimals().stream().findAny().get();
        Volunteer volunteer = attendVolunteer(user);
        volunteer.addAnimal(animal);
        volunteerService.save(volunteer);
        long volunteerId = volunteer.getId();
        long animalId = animal.getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/volunteer/{volunteerId}/save_removed_animal", volunteerId)
                        .param("animal_id", String.valueOf(animalId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/volunteer/" + volunteerId));

        Assertions.assertEquals(0, volunteer.getAnimals().size());
        Assertions.assertNull(animal.getVolunteer());
    }


    private User createUserAndPets() {
        User user = new User();
        user.setUserName("TJ User Name");
        user.setFullName("Tom Johnson");
        user.setChatId(CHAT_ID);
        user.setOwner(true);
        user.setPosition(Position.USER);
        user.setEmail("johnson@mail.ru");
        user.setPassport("3324 9943");
        user.setPhoneNumber("+79293341122");
        user.setCurrentCode(Status.NONE.getCode());
        user.addAction(new Action(MESSAGE_ID, Command.START.getCode(), Shelter.NONE));

        Cat cat = new Cat(2, CatBreed.ABYSSINIAN);
        cat.setExpirationDate(LocalDateTime.now().plusDays(30));
        cat.setActive(true);
        Dog dog = new Dog(4, DogBreed.POODLE);
        dog.setActive(true);
        dog.setExpirationDate(LocalDateTime.now().plusDays(30));

        user.addAnimal(cat);
        user.addAnimal(dog);
        animalService.save(cat);
        animalService.save(dog);
        userService.save(user);
        return user;
    }

    private Volunteer attendVolunteer(User user) {
        Volunteer volunteer = new Volunteer();
        volunteer.setChatId(user.getChatId());
        volunteer.setUserName(user.getUserName());
        volunteer.setFullName(user.getFullName());
        volunteerService.save(volunteer);
        return volunteer;
    }
}
