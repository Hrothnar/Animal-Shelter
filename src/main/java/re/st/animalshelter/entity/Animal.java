package re.st.animalshelter.model.entity;

import re.st.animalshelter.enumeration.Stage;
import re.st.animalshelter.enumeration.animal.Breed;
import re.st.animalshelter.enumeration.animal.Shelter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity(name = "animals")
public class Animal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Volunteer volunteer;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Shelter shelter;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Breed breed;
    @Column(nullable = false)
    private int age;
    @Enumerated(value = EnumType.STRING)
    private Stage stage;
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;
    @Column(name = "last_report_date")
    private LocalDateTime lastReportDate;

}
