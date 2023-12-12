package com.jp.oms.processors;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.jp.oms.dtos.RequestMessage;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class CsvProcessor {
	private static final CsvMapper mapper = new CsvMapper();

	public List<RequestMessage> processCsvFile(InputStream is) throws IOException {
		return this.read(RequestMessage.class, is);
	}

	private <T> List<T> read(Class<T> clazz, InputStream stream) throws IOException {
		CsvSchema schema = mapper.schemaFor(clazz).withHeader().withColumnReordering(false);
		ObjectReader reader = mapper.readerFor(clazz).with(schema);
		return reader.<T>readValues(stream).readAll();
	}
}
