package re.st.animalshelter.utility;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import org.springframework.stereotype.Component;
import re.st.animalshelter.enumeration.Command;

@Component
public class CommandUtil {

    public void addCommands(TelegramBot bot) {
        BotCommand start = new BotCommand(Command.START.getText(), Command.START.getDescription());
        BotCommand finish = new BotCommand(Command.FINISH.getText(), Command.FINISH.getDescription());
        SetMyCommands commandsSet = new SetMyCommands(start, finish);
        bot.execute(commandsSet);
    }
}
