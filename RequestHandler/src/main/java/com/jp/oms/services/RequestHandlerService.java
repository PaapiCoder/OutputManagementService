package com.jp.oms.services;

import com.jp.oms.dtos.OnlineRequestDto;
import com.jp.oms.dtos.RequestMessage;
import com.jp.oms.processors.CsvProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class RequestHandlerService {

	@Autowired
	private CsvProcessor csvProcessor;
	public String handleOnlineRequest(OnlineRequestDto onlineRequest) {
		RequestMessage message = onlineRequest.toRequestMessage();
		this.pushMessagesToKafkaTopic(message);
		return "Success";
	}

	public String handleBulkRequest(MultipartFile file) throws IOException {
		List<RequestMessage> messages = csvProcessor.processCsvFile(file.getInputStream());
		messages.forEach(this::pushMessagesToKafkaTopic);
		return "Success";
	}

	private void pushMessagesToKafkaTopic(RequestMessage messages) {
		System.out.println(messages);;
	}
}
