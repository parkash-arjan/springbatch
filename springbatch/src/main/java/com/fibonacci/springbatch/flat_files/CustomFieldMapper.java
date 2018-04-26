package com.fibonacci.springbatch.flat_files;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustomFieldMapper implements FieldSetMapper<Student> {

	@Override
	public Student mapFieldSet(FieldSet fieldSet) throws BindException {

		//@formatter:off
		Student student = new Student(fieldSet.readString("id"), 
																fieldSet.readString("name"), 
																fieldSet.readString("email")/*, date format is invalid in inout file
																fieldSet.readDate("birthDate", "yyyy-MM-dd HH:mm:ss")*/);
		//@formatter:on		
		return student;
	}

}
