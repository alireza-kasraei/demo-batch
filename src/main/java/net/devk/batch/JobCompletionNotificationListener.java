package net.devk.batch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	private final PersonRepository personRepository;

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			personRepository.findAll().forEach(System.out::println);

		}
	}
}