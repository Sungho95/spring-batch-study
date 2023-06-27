package com.example.springbatch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class JobParameterConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob() {
        return this.jobBuilderFactory.get("Job")
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {

                    // Contribution 클래스에서 JobParameter 참조하기
                    JobParameters jobParameters1 = contribution.getStepExecution().getJobExecution().getJobParameters();
                    System.out.println("jobParameters1.getString(\"name\") = " + jobParameters1.getString("name"));
                    System.out.println("jobParameters1.getLong(\"seq\") = " + jobParameters1.getLong("seq"));
                    System.out.println("jobParameters1.getDate(\"date\") = " + jobParameters1.getDate("date"));
                    System.out.println("jobParameters1.getDouble(\"avg\") = " + jobParameters1.getDouble("avg"));

                    // ChunkContext 클래스에서 JobParameter 참조하기
                    JobParameters jobParameters2 = chunkContext.getStepContext().getStepExecution().getJobParameters();
                    System.out.println("jobParameters2.getString(\"name\") = " + jobParameters2.getString("name"));
                    System.out.println("jobParameters2.getLong(\"seq\") = " + jobParameters2.getLong("seq"));
                    System.out.println("jobParameters2.getDate(\"date\") = " + jobParameters2.getDate("date"));
                    System.out.println("jobParameters2.getDouble(\"avg\") = " + jobParameters2.getDouble("avg"));

                    Map<String, Object> jobParametersMap = chunkContext.getStepContext().getJobParameters();
                    System.out.println("jobParametersMap = " + jobParametersMap);

                    System.out.println("step1 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("step2 has executed");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
}
