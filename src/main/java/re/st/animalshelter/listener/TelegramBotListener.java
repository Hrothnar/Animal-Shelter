package re.st.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import re.st.animalshelter.model.Distributor;
import re.st.animalshelter.utility.CommandUtil;

import javax.annotation.PostConstruct;
import java.util.List;

@EnableScheduling
@Component
public class TelegramBotListener implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final Distributor distributor;

    @Autowired
    public TelegramBotListener(TelegramBot telegramBot, Distributor distributor) {
        this.telegramBot = telegramBot;
        this.distributor = distributor;
    }

    @Override
    public int process(List<Update> updateList) {
        for (Update update : updateList) {
            distributor.catchUpdate(update);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @PostConstruct
    private void init() {
        telegramBot.setUpdatesListener(this);
        new CommandUtil().addCommands(telegramBot);
    }
}
