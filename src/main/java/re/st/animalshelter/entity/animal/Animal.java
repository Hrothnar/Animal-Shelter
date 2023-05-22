package re.st.animalshelter.entity.animal;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import re.st.animalshelter.entity.Report;
import re.st.animalshelter.entity.User;
import re.st.animalshelter.entity.Volunteer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "animals")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;
    @Column(name = "report_code")
    private String reportCode;
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;
    private boolean active;
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "volunteer_id")
    private Volunteer volunteer;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "animal")
    @LazyCollection(value = LazyCollectionOption.TRUE)
    private Set<Report> reports = new HashSet<>();

    public long getId() {
        return id;
    }

    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportStatus) {
        this.reportCode = reportStatus;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Volunteer getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(Volunteer volunteer) {
        this.volunteer = volunteer;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    public String getProbationEnd() {
        int day = expirationDate.getDayOfMonth();
        int month = expirationDate.getMonthValue();
        int year = expirationDate.getYear();
        return day + "." + month + "." + year + "г.";
    }

    public abstract String getGeneralInfo();
}
