package re.st.animalshelter.utility;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import org.springframework.stereotype.Component;

@Component
public class CommandUtil {

    public void addCommands(TelegramBot bot) {
        BotCommand start = new BotCommand("/start", "начать общение");
        SetMyCommands commandsSet = new SetMyCommands(start);
        bot.execute(commandsSet);
    }
}
