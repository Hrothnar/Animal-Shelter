package re.st.animalshelter.enumeration.breed;

public enum DogBreed {
    LABRADOR_RETRIEVER("Лабрадор-ретривер"),
    GERMAN_SHEPHERD("Немецкая овчарка"),
    BULLDOG("Бульдог"),
    POODLE("Пудель"),
    BEAGLE("Бигль"),
    YORKSHIRE_TERRIER("Йоркширский терьер"),
    DACHSHUND("Такса"),
    BOXER("Боксёр"),
    ROTTWEILER("Ротвейлер"),
    GOLDEN_RETRIEVER("Золотистый ретривер");

    private final String text;


    DogBreed(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
