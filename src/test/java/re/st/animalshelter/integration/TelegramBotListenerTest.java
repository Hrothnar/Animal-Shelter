package re.st.animalshelter.integration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import re.st.animalshelter.dto.ActionDTO;
import re.st.animalshelter.entity.Action;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.enumeration.Button;
import re.st.animalshelter.enumeration.Command;
import re.st.animalshelter.enumeration.Position;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.enumeration.shelter.Shelter;
import re.st.animalshelter.listener.TelegramBotListener;
import re.st.animalshelter.service.StorageService;
import re.st.animalshelter.service.UserService;
import re.st.animalshelter.utility.ButtonUtil;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@TestPropertySource(locations = "classpath:test.properties")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TelegramBotListenerTest {
    @MockBean
    private TelegramBot telegramBot;
    @Autowired
    private TelegramBotListener telegramBotListener;
    @Autowired
    private StorageService storageService;
    @Autowired
    private UserService userService;
    @Autowired
    private ButtonUtil buttonUtil;
    @Mock
    private BaseResponse baseResponse;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @Mock
    private Chat chat;

    private final static long CHAT_ID = 11;
    private final static int MESSAGE_ID = 22;

    @AfterEach
    public void clean() {

    }

    @Test
    @Transactional
    public void createUserOrAction_shouldCreateUserAndGiveCorrectAnswer() {
        ActionDTO actionDTO = new ActionDTO(CHAT_ID, MESSAGE_ID, false, Button.START, Shelter.NONE);
        InlineKeyboardMarkup expectedKeyboard = buttonUtil.getKeyboard(actionDTO);
        createAndSaveTextToStorage1();
        String expectedText = storageService.getText(actionDTO);
        Mockito.when(telegramBot.execute(Mockito.any())).then(invocation -> {
            SendMessage sendMessage = invocation.getArgument(0);
            String text = (String) sendMessage.getParameters().get("text");
            InlineKeyboardMarkup keyboard = (InlineKeyboardMarkup) sendMessage.getParameters().get("reply_markup");

            Assertions.assertEquals(expectedText, text);
            Assertions.assertEquals(expectedKeyboard, keyboard);
            return baseResponse;
        });
        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(message.text()).thenReturn(Command.START.getText());
        Mockito.when(message.messageId()).thenReturn(MESSAGE_ID);
        Mockito.when(chat.firstName()).thenReturn("Tom");
        Mockito.when(chat.lastName()).thenReturn("Johnson");
        Mockito.when(chat.username()).thenReturn("TJ User Name");
        Mockito.when(chat.id()).thenReturn(CHAT_ID);

        telegramBotListener.process(List.of(update));
        User foundUser = userService.getByChatId(CHAT_ID);
        Set<Action> actions = foundUser.getActions();

        Assertions.assertEquals("TJ User Name", foundUser.getUserName());
        Assertions.assertEquals("Tom Johnson", foundUser.getFullName());
        Assertions.assertNotNull(foundUser.getStage());
        Assertions.assertEquals(1, actions.size());
        Assertions.assertNotNull(actions.stream().filter(action -> action.getMessageId() == MESSAGE_ID + 1));
        Assertions.assertEquals(Button.START, actions.stream().findAny().get().getButton());
        Assertions.assertEquals(foundUser, actions.stream().findAny().get().getUser());
    }

    @Test
    @Transactional
    public void createUserOrAction_shouldFindOwnerAndGiveCorrectAnswer() {
        ActionDTO actionDTO = new ActionDTO(CHAT_ID, MESSAGE_ID, true, Button.START, Shelter.NONE);
        InlineKeyboardMarkup expectedKeyboard = buttonUtil.getKeyboard(actionDTO);
        createAndSaveTextToStorage2();
        String expectedText = storageService.getText(actionDTO);
        Mockito.when(telegramBot.execute(Mockito.any())).then(invocation -> {
            SendMessage sendMessage = invocation.getArgument(0);
            String text = (String) sendMessage.getParameters().get("text");
            InlineKeyboardMarkup keyboard = (InlineKeyboardMarkup) sendMessage.getParameters().get("reply_markup");

            Assertions.assertEquals(expectedText, text);
            Assertions.assertEquals(expectedKeyboard, keyboard);
            return baseResponse;
        });
        User user = createUser();

        Mockito.when(update.message()).thenReturn(message);
        Mockito.when(message.chat()).thenReturn(chat);
        Mockito.when(message.text()).thenReturn(Command.START.getText());
        Mockito.when(message.messageId()).thenReturn(MESSAGE_ID);
        Mockito.when(chat.firstName()).thenReturn("Tom");
        Mockito.when(chat.lastName()).thenReturn("Johnson");
        Mockito.when(chat.username()).thenReturn("TJ User Name");
        Mockito.when(chat.id()).thenReturn(CHAT_ID);

        telegramBotListener.process(List.of(update));
        User foundUser = userService.getByChatId(CHAT_ID);
        Set<Action> actions = foundUser.getActions();

        Assertions.assertEquals("TJ User Name", foundUser.getUserName());
        Assertions.assertEquals("Tom Johnson", foundUser.getFullName());
        Assertions.assertEquals(2, actions.size());
        Assertions.assertNotNull(actions.stream().filter(action -> action.getMessageId() == MESSAGE_ID + 1));
        Assertions.assertEquals(user, actions.stream().findAny().get().getUser());
        Assertions.assertEquals(Button.TAKE_AN_ANIMAL, actions.stream()
                .min(Comparator.comparing(Action::getLastUpdate))
                .get()
                .getButton());
    }


    private User createUser() {
        User user = new User();
        user.setUserName("TJ User Name");
        user.setFullName("Tom Johnson");
        user.setChatId(CHAT_ID);
        user.setPosition(Position.USER);
        user.setOwner(true);
        user.setCurrentStatus(Status.NONE);
        user.addAction(new Action(MESSAGE_ID, Button.TAKE_AN_ANIMAL, Shelter.CAT));
        userService.saveUser(user);
        return user;
    }

    private void createAndSaveTextToStorage1() {
        String importText = "Приветствую! Если вы ищете себе или своим близким нового друга, мы можем вам помочь!  \n" +
                "Наш приют располагает большим числом животных, среди которых вы не несомненно найдёте нового члена семьи, будь то всем привычные пушистые друзья или кто-то экзотический...  \n" +
                "Пожалуйста, выберите какой приют вас сейчас интересует:\n";
        storageService.saveInformation(Button.START, Shelter.NONE, Status.NONE, false, importText, new MockMultipartFile("file", new byte[]{}));
    }

    private void createAndSaveTextToStorage2() {
        String importText = "Доброго времени суток. Выберите интересующий вас приют.";
        storageService.saveInformation(Button.START, Shelter.NONE, Status.NONE, true, importText, new MockMultipartFile("file", new byte[]{}));
    }


}


