package com.fibonacci.springbatch.reader;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class StatelessItemReader implements ItemReader<String> {

	private final Iterator<String> data;

	public StatelessItemReader(final List<String> data) {
		this.data = data.iterator();
		System.out.println(String.format("Value of this %s =  ", this));
	}

	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (data.hasNext()) {
			System.out.println("Reading.....");
			return data.next();
		} else {
			return null;
		}
	}
}
