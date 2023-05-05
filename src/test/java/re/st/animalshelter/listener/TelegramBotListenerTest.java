package re.st.animalshelter.listener;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.entity.Volunteer;
import re.st.animalshelter.repository.UserRepository;
import re.st.animalshelter.service.UserService;

@ExtendWith(MockitoExtension.class)
public class TelegramBotListenerTest {
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private TelegramBotListener telegramBotListener;

    @Test
    public void start() {

    }






}
