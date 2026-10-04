package com.springbatch.demo.config;

import com.springbatch.demo.decider.MyJobExecutionDecider;
import com.springbatch.demo.listener.MyStepExecutionListener;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

//	@Autowired
//	public JobBuilderFactory jobBuilderFactory;
//
//	@Autowired
//	public StepBuilderFactory stepBuilderFactory;

    @Bean
    public StepExecutionListener myStepExecutionListener(){
        return new MyStepExecutionListener();
    }

    @Bean
    public JobExecutionDecider decider(){
        return new MyJobExecutionDecider();
    }

    @Bean
    public Step step1(){
        return new StepBuilder("step1", jobRepository).tasklet(new Tasklet() {
            @Override
            public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("step1 executed!!");
                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

    @Bean
    public Step step2(){
        return new StepBuilder("step2", jobRepository).tasklet(new Tasklet() {
            @Override
            public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

                boolean isFailure=false;
                if(isFailure){
                    throw new Exception("Test Exception");
                }

                System.out.println("step2 executed!!");
                return RepeatStatus.FINISHED;
            }
        }, transactionManager)
//                .listener(myStepExecutionListener())
                .build();
    }

    @Bean
    public Step step3(){
        return new StepBuilder("step3", jobRepository).tasklet(new Tasklet() {
            @Override
            public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("step3 executed!!");
                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

    @Bean
    public Step step4(){
        return new StepBuilder("step4", jobRepository).tasklet(new Tasklet() {
            @Override
            public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("step4 executed!!");
                return RepeatStatus.FINISHED;
            }
        }, transactionManager).build();
    }

    @Bean
    public Job firstJob(){
        // 1. Sequential flow
//        return new JobBuilder("job1", jobRepository)
//                .preventRestart()
//                .start(step1())
//                .next(step2())
//                .next(step3())
//                .build();
//    }

        // 2. Conditional flow using StepExecutionListener
//        return new JobBuilder("job1", jobRepository)
//                .start(step1())
//                    .on("COMPLETED").to(step2())
//                .from(step2())
//                    .on("TEST_STATUS").to(step3())
//                .from(step2())
//                    .on("*").to(step4())              // (* means all (STOPPED, FAILED...etc)
//                .end()
//                .build();


        // 2. Conditional flow using JobExecutionDecider
        return new JobBuilder("job1", jobRepository)
                .start(step1())
                    .on("COMPLETED").to(decider())
                        .on("TEST_STATUS").to(step2())
                .from(decider())
                    .on("*").to(step3())
                .end()
                .build();
    }
}
