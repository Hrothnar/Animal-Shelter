package re.st.animalshelter.enumeration;

import re.st.animalshelter.enumeration.animal.Shelter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public enum Stage {
    // состояния диалога
    NONE(".md"),
    START(".md"),
    MENU(".md"),
    INFO(".md"),
    ANIMAL(".md"),
    RULES(".md"),
    DOCUMENTS(".md"),
    DRIVER_PERMIT(".md"),
    DISABLED_ANIMAL(".md"),
    CYNOLOGIST(".md"),

    //состояния отправки информации,
    MAP(".png"),

    // состояния отчётности
    REPORTED(".md"),
    REPORT_TEXT(".md"),
    REPORT_PHOTO(".md"),
    REPORT_WAS_NOT_SENT(".md"),

    // состояния приёма контактной информации
    CONTACT_INFO(".md"),
    CONTACT_INFO_RECEIVED(".md");

    private String text = ""; // названия файлов должны соответствовать названиям Enums, с учётом префиксов типа приюта и статуса держателя
    private File file;
    private final String extension;
    private static final String TEXT = "text/";
    private static final String PHOTO = "photo/";
    private static final String RESOURCES = "src/main/resources/";

    Stage(String extension) {
        this.extension = extension;
    }

    public String getText(Shelter shelter, boolean isOwner) {
        getData(TEXT, shelter, isOwner);
        return text;
    }

    public File getPhoto(Shelter shelter, boolean isOwner) {
        getData(PHOTO, shelter, isOwner);
        return file;
    }


    private void getData(String mediaType, Shelter shelter, boolean isOwner) {
        String fileName = this.toString().toLowerCase();
        String shelterPath = shelter.getPath();
        String shelterPrefix = shelter.getPrefix();
        String personPath = "user/";
        String personPrefix = "user_";
        if (isOwner) {
            personPath = "owner/";
            personPrefix = "owner_";
        }
        try {
            switch (mediaType) {
                case TEXT:
                    File toFile = new File("./" + RESOURCES + mediaType + personPath + shelterPath + personPrefix + shelterPrefix + fileName + extension);
                    if (toFile.exists()) {
                        text = Files.readString(toFile.toPath());
                    }
                    break;
                case PHOTO:
                    file = new File(RESOURCES + mediaType + personPath + shelterPath + personPrefix + shelterPrefix + fileName + extension);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
