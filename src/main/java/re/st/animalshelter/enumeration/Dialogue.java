package re.st.animalshelter.enumeration;

import re.st.animalshelter.enumeration.animal.Shelter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public enum Dialogue {
    START,
    MENU,
    INFO,
    MAP,
    NONE,
    DRIVER_PERMIT,
    CONTACT_INFORMATION,
    REPORT_TEXT,
    REPORT_PHOTO,
    REPORTED;

//    COMMUNICATION_DATA,
//    START_MENU,
//    CHOOSE_AN_ANIMAL,
//    CHOOSE_ITS_AGE,
//    RULES,
//    LICENCE,
//    DOCUMENTS,
//    PHOTO_WAITING,
//    TEXT_WAITING,

    private String text = ""; // названия файлов должны соответствовать названиям Enums, с учётом префиксов типа приюта и статуса держателя
    private File file;
    private static final String TEXT = "text/";
    private static final String PHOTO = "photo/";
    private static final String RESOURCES = "src/main/resources/";

    public String getText(Shelter shelter, boolean isOwner, Extension extension) {
        getData(TEXT, shelter, isOwner, extension.getExtension());
        return text;
    }

    public File getPhoto(Shelter shelter, boolean isOwner, Extension extension) {
        getData(PHOTO, shelter, isOwner, extension.getExtension());
        return file;
    }

    private void getData(String mediaType, Shelter shelter, boolean isOwner, String extension) {
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
                    if (toFile.exists()) text = Files.readString(toFile.toPath());
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
