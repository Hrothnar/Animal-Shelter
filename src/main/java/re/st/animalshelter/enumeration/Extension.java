package re.st.animalshelter.enumeration;

public enum Extension {
    PNG(".png"),
    JPEG(".jpeg"),
    TXT(".txt"),
    MD(".md");

    private final String extension;

    Extension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

}
