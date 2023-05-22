package re.st.animalshelter.entity;

import re.st.animalshelter.entity.animal.Animal;
import re.st.animalshelter.enumeration.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "time", nullable = false)
    private LocalDateTime time;
    @Column(name = "photo_path", length = 64)
    private String photoPath;
    @Column(name = "text_path", length = 64)
    private String textPath;
    @Column(name = "report_code", nullable = false, length = 4)
    private String reportCode;
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    private Animal animal;

    public long getId() {
        return id;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.time = LocalDateTime.now();
        this.photoPath = photoPath;
    }

    public String getTextPath() {
        return textPath;
    }

    public void setTextPath(String textPath) {
        this.time = LocalDateTime.now();
        this.textPath = textPath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getTimeAsString() {
        int dayOfMonth = time.getDayOfMonth();
        int monthValue = time.getMonthValue();
        int year = time.getYear();
        return dayOfMonth + "." + monthValue + "." + year;
    }

    public String getAnimalInfo() {
        return animal.getGeneralInfo();
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String status) {
        this.reportCode = status;
    }

    public String getStatusAsString() {
        if (reportCode.equals(Status.NONE.getCode())) {
            return "Не просмотрен";
        } else if (reportCode.equals(Status.REPORT_PASSED.getCode())) {
            return "Принят";
        } else {
            return "Завален";
        }
    }
}
