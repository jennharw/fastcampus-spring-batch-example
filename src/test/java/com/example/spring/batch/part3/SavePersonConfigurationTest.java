package com.example.spring.batch.part3;

import com.example.spring.batch.TestConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBatchTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes={SavePersonConfiguration.class, TestConfiguration.class})
class SavePersonConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PersonRepository personRepository;

    @After
    public void tearDown() throws Exception {
        personRepository.deleteAll();
    }

    @Test
    void savePersonJob() throws Exception {
        //given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("allow_duplicate", "false")
                .toJobParameters();
        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        //then
        Assertions.assertThat(jobExecution.getStepExecutions().stream()
                .mapToInt(StepExecution::getWriteCount)
                .sum())
                .isEqualTo(personRepository.count())  /// DB저장 되었는지
                .isEqualTo(3);
    }

    @Test
    void savePersonStep() {
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("SavePersonStep");

        Assertions.assertThat(jobExecution.getStepExecutions().stream()
                        .mapToInt(StepExecution::getWriteCount)
                        .sum())
                .isEqualTo(personRepository.count())
                .isEqualTo(3);
    }
}