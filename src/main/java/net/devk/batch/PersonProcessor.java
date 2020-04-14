package net.devk.batch;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;

public class PersonProcessor implements ItemProcessor<List<Person>, List<Person>> {

	@Override
	public List<Person> process(List<Person> items) throws Exception {
		items.forEach(c -> c.setProcessed(Boolean.TRUE));
		return items;
	}

}
