package re.st.animalshelter.utility;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.TelegramBotListener;
import re.st.animalshelter.handler.CallBackQueryHandler;
import re.st.animalshelter.handler.DocumentHandler;
import re.st.animalshelter.handler.PhotoHandler;
import re.st.animalshelter.handler.TextHandler;

import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class Distributor {
    private final CallBackQueryHandler callBackQueryHandler;
    private final TextHandler textHandler;
    private final PhotoHandler photoHandler;
    private final DocumentHandler documentHandler;

    public final static String PASS = "pass";
    public final static Logger LOGGER = Logger.getLogger(TelegramBotListener.class);

    @Autowired
    protected Distributor(CallBackQueryHandler callBackQueryHandler,
                          TextHandler textHandler,
                          PhotoHandler photoHandler,
                          DocumentHandler documentHandler) {
        this.callBackQueryHandler = callBackQueryHandler;
        this.textHandler = textHandler;
        this.photoHandler = photoHandler;
        this.documentHandler = documentHandler;
    }

    public void catchUpdate(Update update) {
        if (Objects.nonNull(update.message())) {
            Message message = update.message();
            if (Objects.nonNull(message.text())) {
                textHandler.processTextMessage(message);
            } else if (Objects.nonNull(message.photo())) {
                photoHandler.processPhotoMessage(message);
            } else if (Objects.nonNull(message.document())) {
                documentHandler.processDocumentMessage(message);
            } else {
                LOGGER.error("Необрабатываемый запрос"); //TODO не забыть добавить ещё логики
                throw new RuntimeException();
            }
        } else if (Objects.nonNull(update.callbackQuery())) {
            callBackQueryHandler.processCallBackQuery(update.callbackQuery());
        }
    }

    public static String conjugate(int age) {
        String years = " года";
        if (Pattern.matches("(.*[5-9]|.*1[0-9]|.*0$)", String.valueOf(age))) {
            years = " лет";
        }
        if (Pattern.matches("(.*1$)", String.valueOf(age))) {
            years = " год";
        }
        return years;
    }
}
