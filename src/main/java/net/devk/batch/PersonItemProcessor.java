package net.devk.batch;

import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	@Override
	public Person process(Person item) throws Exception {
		item.setProcessed(Boolean.TRUE);
		return item;
	}

}
