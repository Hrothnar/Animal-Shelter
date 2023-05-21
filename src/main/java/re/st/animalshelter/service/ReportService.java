package re.st.animalshelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import re.st.animalshelter.entity.Report;
import re.st.animalshelter.enumeration.Status;
import re.st.animalshelter.response.particular.status.ReportFailedStatus;
import re.st.animalshelter.repository.ReportRepository;
import re.st.animalshelter.utility.Distributor;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReportFailedStatus reportFailedStatus;


    @Autowired
    public ReportService(ReportRepository reportRepository, ReportFailedStatus reportFailedStatus) {
        this.reportRepository = reportRepository;
        this.reportFailedStatus = reportFailedStatus;
    }

    public void saveReport(Report report) {
        reportRepository.save(report);
    }

    public Report getReportById(long id) {
        return reportRepository.findById(id).orElseThrow(RuntimeException::new);//TODO
    }

    public void updateReport(long chatId, long reportId, String button) {
        Report report = getReportById(reportId);
        if (button.equals(Distributor.PASS)) {
            report.setReportCode(Status.REPORT_PASSED.getCode());
        } else {
            report.setReportCode(Status.REPORT_FAILED.getCode());
            reportFailedStatus.execute(chatId);
        }
        saveReport(report);
    }
}
