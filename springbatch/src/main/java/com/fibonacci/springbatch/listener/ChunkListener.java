package com.fibonacci.springbatch.listener;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ChunkListener {

	@BeforeChunk
	public void beforeChunk(ChunkContext context) {
		System.out.println("beforeChunk");

	}

	@AfterChunk
	public void afterChunk(ChunkContext context) {
		System.out.println("afterChunk");

	}
}
