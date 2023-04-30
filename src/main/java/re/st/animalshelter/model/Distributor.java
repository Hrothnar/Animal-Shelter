package re.st.animalshelter.model;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.entity.Animal;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.model.response.EditTextResponse;
import re.st.animalshelter.model.response.TextResponse;
import re.st.animalshelter.repository.AnimalRepository;
import re.st.animalshelter.repository.UserRepository;
import re.st.animalshelter.repository.VolunteerRepository;
import re.st.animalshelter.service.ActionService;
import re.st.animalshelter.service.UserService;

@Component
public class Distributor {
    private final UserService userService;
    private final ActionService actionService;
    private final EditTextResponse editTextResponse;
    private final TextResponse textResponse;
    private final UserRepository userRepository;
    private final VolunteerRepository volunteerRepository;
    private final AnimalRepository animalRepository;


    public static final String BACK_RESPONSE = "BACK_RESPONSE";
    public static final String EDIT_MULTIMEDIA_RESPONSE = "EDIT_MULTIMEDIA_RESPONSE";
    public static final String EDIT_TEXT_RESPONSE = "EDIT_TEXT_RESPONSE";
    public static final String PHOTO_RESPONSE = "PHOTO_RESPONSE";
    public static final String TEXT_RESPONSE = "TEXT_RESPONSE";

    @Autowired
    public Distributor(UserService userService,
                       ActionService actionService,
                       EditTextResponse editTextResponse,
                       TextResponse textResponse, UserRepository userRepository, VolunteerRepository repository, AnimalRepository animalRepository) {
        this.userService = userService;
        this.actionService = actionService;
        this.textResponse = textResponse;
        this.editTextResponse = editTextResponse;
        this.userRepository = userRepository;
        this.volunteerRepository = repository;
        this.animalRepository = animalRepository;
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
        long chatId = message.chat().id();
//        boolean exist = userService.isExist(chatId);
        int messageId = message.messageId();
        String text = message.text();



//        User user = userService.getUser(100);
//        System.out.println("Here");
        Animal animal = animalRepository.getById(1L);
        System.out.println();
        System.out.println(animal.getAge());
        System.out.println();
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
