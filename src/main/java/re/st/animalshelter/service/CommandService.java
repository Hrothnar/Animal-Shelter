package re.st.animalshelter.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import org.springframework.stereotype.Service;
import re.st.animalshelter.enumeration.Command;

@Service
public class CommandService {

    public void addCommands(TelegramBot bot) {
        BotCommand start = new BotCommand(Command.START.getValue(), Command.START.getDescription());
        BotCommand finish = new BotCommand(Command.FINISH.getValue(), Command.FINISH.getDescription());
        SetMyCommands commandsSet = new SetMyCommands(start, finish);
        bot.execute(commandsSet);
    }
}
