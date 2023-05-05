package re.st.animalshelter.enumeration.breed;

public enum CatBreed {
    SIAMESE("Сиамская"),
    PERSIAN("Персидская"),
    BENGAL("Бенгальская"),
    MAINE_COON("Мейн-кун"),
    RUSSIAN_BLUE("Русская голубая"),
    SCOTTISH_FOLD("Шотландская вислоухая"),
    SPHYNX("Сфинкс"),
    BRITISH("Британская короткошёрстная"),
    RAGDOLL("Рэгдолл"),
    ABYSSINIAN("Абиссинская");

    private final String text;


    CatBreed(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
