package re.st.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.st.animalshelter.dto.NotifyDTO;
import re.st.animalshelter.entity.Report;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.repository.ReportRepository;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    private final static String PASS = "pass";

    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public void saveReport(Report report) {
        reportRepository.save(report);
    }

    public Report getReportById(long id) {
        return reportRepository.findById(id).orElseThrow(RuntimeException::new);//TODO
    }

    public NotifyDTO updateReport(long chatId, long reportId, String button) {
        NotifyDTO notifyDTO = new NotifyDTO();
        Report report = getReportById(reportId);
        if (button.equals(PASS)) {
            report.setStatus(Status.PASSED);
            notifyDTO.setUserChatId(chatId);
            notifyDTO.setStatus(Status.PASSED);
        } else {
            report.setStatus(Status.FAILED);
            notifyDTO.setUserChatId(chatId);
            notifyDTO.setStatus(Status.FAILED);
        }
        saveReport(report);
        return notifyDTO;
    }
}
