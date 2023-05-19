package re.st.animalshelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.st.animalshelter.entity.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

}
