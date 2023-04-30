package re.st.animalshelter.model;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.entity.Animal1;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.animal.Breed;
import re.st.animalshelter.enumeration.animal.Shelter;
import re.st.animalshelter.repository.Animal1Repository;
import re.st.animalshelter.service.Animal1Service;

@Component
public class Distributor {
//    private final UserService userService;
//    private final ActionService actionService;
//    private final EditTextResponse editTextResponse;
//    private final TextResponse textResponse;
//    private final UserRepository userRepository;
//    private final VolunteerRepository volunteerRepository;
//    private final AnimalRepository animalRepository;
    private final Animal1Repository animal1Repository;
    private final Animal1Service animal1Service;


    public static final String BACK_RESPONSE = "BACK_RESPONSE";
    public static final String EDIT_MULTIMEDIA_RESPONSE = "EDIT_MULTIMEDIA_RESPONSE";
    public static final String EDIT_TEXT_RESPONSE = "EDIT_TEXT_RESPONSE";
    public static final String PHOTO_RESPONSE = "PHOTO_RESPONSE";
    public static final String TEXT_RESPONSE = "TEXT_RESPONSE";

    @Autowired
    public Distributor(Animal1Repository animal1Repository, Animal1Service animal1Service) {
        this.animal1Repository = animal1Repository;
        this.animal1Service = animal1Service;
    }

    public void processCallBackQuery(CallbackQuery callbackQuery) {
        long chatId = callbackQuery.message().chat().id();
        int messageId = callbackQuery.message().messageId();
        Button button = Button.getButton(callbackQuery.data());
        switch (button.getResponseType()) {
            case BACK_RESPONSE:

                break;
            case EDIT_MULTIMEDIA_RESPONSE:

                break;
            case EDIT_TEXT_RESPONSE:
//                Action action = dialogService.saveDialog(chatId, messageId, button);
//                editTextResponse.send(action);
                break;
            case PHOTO_RESPONSE:

                break;
            case TEXT_RESPONSE:


                break;
            default:
                throw new RuntimeException("Нет подходящего обработчика"); //TODO
        }
    }

    public void processTextMessage(Message message) {
//        long chatId = message.chat().id();
//        boolean exist = userService.isExist(chatId);
//        int messageId = message.messageId();
//        String text = message.text();
        Animal1 animal1 = new Animal1();
        animal1.setAnimal("DOG");
        animal1.setBreed("DOG");
        animal1.setAge(2);
        animal1Repository.save(animal1);
        Animal1 animal2 = animal1Service.getAnimal1(2L);
        System.out.println(animal2.getAge());


//        User user = userService.getUser(100);
//        System.out.println("Here");
//        Animal animal = animalRepository.getById(1L);
//        System.out.println();
//        System.out.println(animal.getAge());
//        System.out.println();
//        user.addAnimal(animal);
//        userRepository.save(user);



//        if (Objects.equals(Button.START .getText(), text)) {
//            ++messageId;
//            if (exist) {
//                userService.createDialog(chatId, messageId);
//            } else {
//                userService.createUserAndStartDialog(chatId, messageId, message);
//            }
//            textResponse.send();
//        } else {
//            Stage stage = userService.getUser(chatId).getPhase();
//            switch (stage) {
//                case REPORT_TEXT:
//                    userService.updatePhase(chatId, Stage.REPORT_PHOTO);
//                    fileService.saveText(chatId, text);
//                    sendNewTextResponse(chatId, Stage.REPORT_PHOTO, Shelter.NONE);
//                    break;
//                case CONTACT_INFO:
//                    if (text.length() < 15 && text.length() > 6) {
//                        userService.updatePhase(chatId, Stage.CONTACT_INFO_RECEIVED);
//                        userService.updatePhoneNumber(chatId, text);
//                        sendNewTextResponse(chatId, Stage.CONTACT_INFO_RECEIVED, Shelter.NONE);
//                    }
//                    break;
//            }
//        }
    }


    public void processPhotoMessage(Message message) {

    }

    public void processDocumentMessage(Message message) {

    }

}
