package net.devk.batch;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.data.domain.PageRequest;

public class AggregateItemReader extends AbstractPaginatedDataItemReader<List<Person>> {

	private final PersonRepository personRepository;

	public AggregateItemReader(PersonRepository personRepository) {
		this.personRepository = personRepository;
		setExecutionContextName(AggregateItemReader.class.getName());
	}

	@Override
	protected Iterator<List<Person>> doPageRead() {
		List<Person> content = personRepository.findAll(PageRequest.of(page, 5)).getContent();
		return content.isEmpty() ? null : Collections.singletonList(content).iterator();
	}

}