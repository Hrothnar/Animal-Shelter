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
import org.springframework.web.servlet.ModelAndView;
import re.st.animalshelter.entity.Action;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.entity.Volunteer;
import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.entity.animal.Cat;
import re.st.animalshelter.entity.animal.Dog;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Position;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.breed.CatBreed;
import re.st.animalshelter.enumeration.breed.DogBreed;
import re.st.animalshelter.enumeration.shelter.Shelter;
import re.st.animalshelter.service.AnimalService;
import re.st.animalshelter.service.UserService;
import re.st.animalshelter.service.VolunteerService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;

@TestPropertySource(locations = "classpath:test.properties")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerTest {
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
    private final static long USER_ID = 33;

    @AfterEach
    public void clean() {

    }

    @Test
    @Transactional
    public void receive_shouldRedirectToUserPage() throws Exception {
        User user = createUserAndAnimals();
        String userName = user.getUserName();
        long userId = user.getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/user/receive")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("user_id", "0")
                        .param("userName", userName))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/" + userId));
        mockMvc.perform(MockMvcRequestBuilders.post("/user/receive")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("user_id", String.valueOf(userId))
                        .param("userName", ""))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/" + userId));

        Assertions.assertTrue(userId != -1L);
    }

    @Test
    @Transactional
    public void receive_shouldShowUserNotFoundPage() throws Exception {
        long userId = userService.getUserId(0, "TJ User Name");
        mockMvc.perform(MockMvcRequestBuilders.post("/user/receive")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("user_id", "0")
                        .param("userName", "TJ User Name"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/not_found"));

        Assertions.assertTrue(userId == -1L);
    }

    @Test
    public void getUserAction_shouldShowAvailableUserActions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}", USER_ID))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/actions"));
    }

    @Test
    @Transactional
    public void updateUser_shouldFindUserAndShowUpdatePage() throws Exception {
        long userId = createUserAndAnimals().getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}/update", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/update"));
    }

    @Test
    @Transactional
    public void saveUpdate_shouldSaveUpdatedUser() throws Exception {
        User user = createUserAndAnimals();
        String phoneNumber = user.getPhoneNumber();
        String passport = user.getPassport();
        String userName = user.getUserName();
        String email = user.getEmail();
        String fullName = user.getFullName();
        long userId = user.getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/user/{id}/save_update", userId)
                        .param("fullName", user.getFullName())
                        .param("email", "tom_johnson@yandex.ru")
                        .param("userName", user.getUserName())
                        .param("phoneNumber", "+7(933)233-59-99")
                        .param("passport", user.getPassport()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/" + userId));

        User foundUser = userService.getUserById(userId);

        Assertions.assertNotEquals(phoneNumber, foundUser.getPhoneNumber());
        Assertions.assertNotEquals(email, foundUser.getEmail());
        Assertions.assertEquals(fullName, foundUser.getFullName());
        Assertions.assertEquals(userName, foundUser.getUserName());
        Assertions.assertEquals(passport, foundUser.getPassport());
    }

    @Test
    @Transactional
    public void addTime_shouldShowAddTimePage() throws Exception {
        User user = createUserAndAnimals();
        long userId = user.getId();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}/time", userId))
                .andExpect(MockMvcResultMatchers.model().attributeExists("animals", "id"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/add_time"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        HashSet<Animal> animals = (HashSet<Animal>) modelAndView.getModel().get("animals");

        Assertions.assertArrayEquals(user.getActiveAnimals().toArray(), animals.toArray());
    }

    @Test
    @Transactional
    public void saveTime_shouldAddTimeToChosenAnimal() throws Exception {
        User user = createUserAndAnimals();
        Animal animal = user.getActiveAnimals().stream().findAny().get();
        LocalDateTime expectedExpirationDate = animal.getExpirationDate();
        long animalId = animal.getId();
        long userId = user.getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/user/{id}/save_time", userId)
                        .param("animal_id", String.valueOf(animalId))
                        .param("time", "7"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/" + userId));

        User foundUser = userService.getUserById(userId);
        Animal foundAnimal = foundUser.getActiveAnimals().stream().filter(e -> e.getId() == animalId).findFirst().get();

        Assertions.assertTrue(expectedExpirationDate.plusDays(6).isBefore(foundAnimal.getExpirationDate()));
        Assertions.assertNotSame(expectedExpirationDate, foundAnimal.getExpirationDate());
    }

    @Test
    @Transactional
    public void attachAnimal_shouldShowCorrectPage() throws Exception {
        User user = createUserAndAnimals();
        long userId = user.getId();
        Volunteer volunteer = attendVolunteer(user);
        animalService.saveCat(2, CatBreed.ABYSSINIAN, 2);
        animalService.saveDog(3, DogBreed.BEAGLE, 2);
        LinkedList<Volunteer> volunteers = volunteerService.getAllVolunteers();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/user/{id}/animal", userId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("user/attach_animal"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("id", "volunteers", "dogs", "cats"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        long userIdFromModel = (long) modelAndView.getModel().get("id");
        LinkedList<Volunteer> volunteersFromModel = (LinkedList<Volunteer>) modelAndView.getModel().get("volunteers");
        LinkedList<Dog> dogsFromModel = (LinkedList<Dog>) modelAndView.getModel().get("dogs");
        LinkedList<Cat> catsFromModel = (LinkedList<Cat>) modelAndView.getModel().get("cats");
        LinkedList<Animal> animalsFromModel = new LinkedList<>();
        animalsFromModel.addAll(dogsFromModel);
        animalsFromModel.addAll(catsFromModel);

        Assertions.assertEquals(userId, userIdFromModel);
        Assertions.assertEquals(4, animalsFromModel.toArray().length);
        Assertions.assertArrayEquals(volunteers.toArray(), volunteersFromModel.toArray());
        Assertions.assertEquals(volunteer.getUserName(), volunteersFromModel.stream().findFirst().get().getUserName());
    }

    @Test
    @Transactional
    public void saveAttachedAnimal_shouldAttachAnimalAndVolunteer() throws Exception {
        User user = createUserAndAnimals();
        Animal animal = user.getActiveAnimals().stream().findFirst().get();
        Volunteer volunteer = attendVolunteer(user);
        long animalId = animal.getId();
        long volunteerId = volunteer.getId();
        long userId = user.getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/user/{id}/animal_attach", userId)
                        .param("animal_id", String.valueOf(animalId))
                        .param("volunteer_id", String.valueOf(volunteerId)))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/" + userId));

        User founUser = userService.getUserById(userId);
        Volunteer foundVolunteer = volunteerService.getVolunteerById(volunteerId);
        Animal foundAnimal = animalService.getAnimalById(animalId);

        Assertions.assertEquals(founUser, foundAnimal.getUser());
        Assertions.assertEquals(founUser, foundVolunteer.getUsers().stream().findAny().get());
        Assertions.assertEquals(foundAnimal, foundVolunteer.getAnimals().stream().findAny().get());
        Assertions.assertEquals(Status.REPORT_WAS_NOT_SENT, foundAnimal.getReportStatus());
        Assertions.assertTrue(founUser.isOwner());
    }

    private User createUserAndAnimals() {
        User user = new User();
        user.setUserName("TJ User Name");
        user.setFullName("Tom Johnson");
        user.setChatId(CHAT_ID);
        user.setOwner(true);
        user.setPosition(Position.USER);
        user.setEmail("johnson@mail.ru");
        user.setPassport("3324 9943");
        user.setPhoneNumber("+79293341122");
        user.setCurrentStatus(Status.NONE);
        user.addAction(new Action(MESSAGE_ID, Button.START, Shelter.NONE));

        Cat cat = new Cat(2, CatBreed.ABYSSINIAN);
        cat.setExpirationDate(LocalDateTime.now().plusDays(30));
        Dog dog = new Dog(4, DogBreed.POODLE);
        dog.setExpirationDate(LocalDateTime.now().plusDays(30));

        user.addAnimal(cat);
        user.addAnimal(dog);
        userService.saveUser(user);
        return user;
    }

    private Volunteer attendVolunteer(User user) {
        Volunteer volunteer = new Volunteer();
        volunteer.setChatId(user.getChatId());
        volunteer.setUserName(user.getUserName());
        volunteer.setFullName(user.getFullName());
        volunteerService.saveVolunteer(volunteer);
        return volunteer;
    }
}
