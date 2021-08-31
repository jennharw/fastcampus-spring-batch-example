package com.example.spring.batch.part4;

import com.example.spring.batch.TestConfiguration;
import com.example.spring.batch.part3.SavePersonConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBatchTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes={UserConfiguration.class, TestConfiguration.class})
class UserConfigurationTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void userJob() throws Exception {
        //given
        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        //then
        int size = userRepository.findAllByUpdatedDate(LocalDate.now()).size();
 // -> update 로서 하면 안돼요!!
        Assertions.assertThat(jobExecution.getStepExecutions().stream()
                .filter(x -> x.getStepName().equals("userLevelUpStep"))
                .mapToInt(StepExecution::getWriteCount)
                .sum())
                .isEqualTo(size)
                .isEqualTo(300);
        Assertions.assertThat(userRepository.count())
                .isEqualTo(400);

    }

    @Test
    void saveUserStep() {
    }

    @Test
    void levelUpStep() {
    }
}