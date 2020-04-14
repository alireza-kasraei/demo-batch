package net.devk.batch;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "PEOPLE")
@Entity
@NoArgsConstructor
@Data
public class Person {

	@Id
	private Long id;
	private String name;
	private Integer age;
	private Boolean processed;

}
