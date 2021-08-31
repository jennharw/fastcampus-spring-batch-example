package com.example.spring.batch.part3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.core.annotation.BeforeStep;

@Slf4j
public class SavePersonListener {

    public static class SavePersonStepAnnotationListener{
        @BeforeStep
        public void beforeStep(StepExecution stepExecution){
            log.info("before Step");
        }
        @AfterStep
        public ExitStatus afterStep(StepExecution stepExecution){
            log.info("afterStep {}",stepExecution.getWriteCount());
            return stepExecution.getExitStatus();
        }
    }

    public static class SavePersonJobExecutionListener implements JobExecutionListener {


        @Override
        public void beforeJob(JobExecution jobExecution) {
            log.info("before Job");
        }

        @Override
        public void afterJob(JobExecution jobExecution) {
            int sum = jobExecution.getStepExecutions().stream()
                    .mapToInt(StepExecution::getWriteCount)
                    .sum();
            log.info("after job: {}", sum);
        }
    }

    public static class SavePersonAnnotationJobExecutionListener{
        @BeforeJob
        public void beforeJob(JobExecution jobExecution) {
            log.info("before Annotation Job");
        }
        @AfterJob
        public void afterJob(JobExecution jobExecution) {
            int sum = jobExecution.getStepExecutions().stream()
                    .mapToInt(StepExecution::getWriteCount)
                    .sum();
            log.info("after annotation job: {}", sum);
        }
    }
}
