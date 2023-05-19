package re.st.animalshelter.enumeration;

import java.util.Collections;
import java.util.LinkedList;

public enum Status {
    NONE("s0", "Без статуса"),

    CONTACT_INFO("s1", "В ожидании контактных данных"),
    CONTACT_INFO_RECEIVED("s2", "Контактные данные получены"),

    REPORT_TEXT("s3", "В ожидании текстового отчёта о питомце"),
    REPORT_PHOTO("s4", "В ожидании фото-отчёта о питомце"),
    REPORTED("s5", "Отчёт получен"),
    REPORT_PASSED("s6", "Полученный отчёт успешно принят"),
    REPORT_FAILED("s7", "Полученный отчёт завален"),

    DIALOG_PREPARED("s8", "В ожидании начала диалога с волонтёром"),
    DIALOG("s9", "Диалог"),
    DIALOG_FINISHED("s10", "Закончить диалог с волонтёром"),

    PROBATION_SUCCESSFULLY_COMPLETED("s11", "Испытательный период успешно окончен"),
    PROBATION_NOT_COMPLETED("s12", "Испытательный период не пройден"),
    ADD_TIME("s13",  "Увеличить испытательный срок");


    private final String code;
    private final String description;

    Status(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static LinkedList<Status> getValidStatuses() {
        LinkedList<Status> statuses = new LinkedList<>();
        Collections.addAll(statuses, CONTACT_INFO, CONTACT_INFO_RECEIVED, REPORT_TEXT, REPORT_PHOTO, REPORTED);
        Collections.addAll(statuses, REPORT_PASSED, REPORT_FAILED, DIALOG_PREPARED, DIALOG_FINISHED, PROBATION_SUCCESSFULLY_COMPLETED);
        Collections.addAll(statuses, PROBATION_NOT_COMPLETED, ADD_TIME);
        return statuses;
    }
}
