package net.devk.batch;

import java.util.Collections;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public ItemReader<Person> itemReader(PersonRepository personRepository) {
		RepositoryItemReader<Person> reader = new RepositoryItemReader<>();
		reader.setRepository(personRepository);
		reader.setMethodName("findAll");
		reader.setSort(Collections.singletonMap("id", Direction.ASC));
		return reader;
	}

	@Bean(name = "aggregateItemReader")
	public AggregateItemReader aggregateItemReader(PersonRepository personRepository) {
		return new AggregateItemReader(personRepository);
	}

	@Bean
	public ItemWriter<List<Person>> aggregateItemWriter(PersonRepository personRepository) {
		return items -> items.stream().forEach(c -> personRepository.saveAll(c));

	}

	@Bean
	public ItemWriter<Person> itemWriter(PersonRepository personRepository) {
		return items -> personRepository.saveAll(items);

	}

	@Bean
	public ItemProcessor<List<Person>, List<Person>> aggregateItemProcessor() {
		return new PersonProcessor();
	}

	@Bean
	public ItemProcessor<Person, Person> itemProcessor() {
		return new PersonItemProcessor();
	}

	@Bean
	public Step step(PersonRepository personRepository) {
		return stepBuilderFactory.get("step").<List<Person>, List<Person>>chunk(1)
				.reader(aggregateItemReader(personRepository)).processor(aggregateItemProcessor())
				.writer(aggregateItemWriter(personRepository)).build();
	}

//	@Bean
//	public Step step(PersonRepository personRepository) {
//		return stepBuilderFactory.get("step").<Person, Person>chunk(5).reader(itemReader(personRepository))
//				.processor(itemProcessor()).writer(itemWriter(personRepository)).build();
//	}

	@Bean
	public Job processJob(Step step, JobCompletionNotificationListener listener) {
		return jobBuilderFactory.get("importUserJob").incrementer(new RunIdIncrementer()).listener(listener).flow(step)
				.end().build();
	}

}