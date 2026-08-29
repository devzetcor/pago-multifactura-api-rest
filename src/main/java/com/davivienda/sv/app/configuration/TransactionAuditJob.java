package com.davivienda.sv.app.configuration;

import com.davivienda.sv.app.services.datasource.AuditService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;


@Component
public class TransactionAuditJob {


    private static final Logger LOGGER = LogManager.getLogger(TransactionAuditJob.class);
    private final AuditService auditService;

	public TransactionAuditJob(
        AuditService auditService
    ) {
		this.auditService = auditService;
	}

    @Scheduled(cron = "${audit.job.cron.expression:0 0 23 * * ?}") 
    public void executeAuditDump() {
        try {
            Timestamp yesterday = new Timestamp(System.currentTimeMillis() - 86400000);
            Timestamp today = new Timestamp(System.currentTimeMillis());

            auditService.executeAudit(yesterday, today);

            LOGGER.info("Auditoría ejecutada exitosamente: " + new Date());
        } catch (Exception e) {
            LOGGER.error("Error ejecutando auditoría: " + e.getMessage(),e);
            e.printStackTrace();
        }
    }
}