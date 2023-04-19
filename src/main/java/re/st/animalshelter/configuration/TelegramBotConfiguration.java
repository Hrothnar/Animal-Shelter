package re.st.animalshelter.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TelegramBotConfiguration {
    private final String token;


    @Autowired //внедрение токена бота из переменной среды
    public TelegramBotConfiguration(@Value("${telegram.bot.token}") String token) {
        this.token = token;
    }

    @Bean //создание bean класса TelegramBot
    public TelegramBot getBot() {
        return new TelegramBot(token);
    }

}
