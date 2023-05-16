package re.st.animalshelter.model;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import re.st.animalshelter.listener.TelegramBotListener;
import re.st.animalshelter.model.handler.CallBackQueryHandler;
import re.st.animalshelter.model.handler.DocumentHandler;
import re.st.animalshelter.model.handler.PhotoHandler;
import re.st.animalshelter.model.handler.TextHandler;

import java.util.Objects;

@Component
public class Distributor {
    private final CallBackQueryHandler callBackQueryHandler;
    private final TextHandler textHandler;
    private final PhotoHandler photoHandler;
    private final DocumentHandler documentHandler;

    public static final String BACK_RESPONSE = "BACK_RESPONSE";
    public static final String EDIT_MULTIMEDIA_RESPONSE = "EDIT_MULTIMEDIA_RESPONSE";
    public static final String EDIT_TEXT_RESPONSE = "EDIT_TEXT_RESPONSE";
    public static final String PHOTO_RESPONSE = "PHOTO_RESPONSE";
    public static final String SIMPLE_TEXT_RESPONSE = "TEXT_RESPONSE";
    public static final String STATUS_RESPONSE = "STATUS_RESPONSE";

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


}
