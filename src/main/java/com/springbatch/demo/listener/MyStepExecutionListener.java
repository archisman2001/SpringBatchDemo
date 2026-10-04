package com.springbatch.demo.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.step.StepExecution;

public class MyStepExecutionListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution){

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution){
        return new ExitStatus("TEST_STATUS");
    }
}
