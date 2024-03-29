package com.jp.oms.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jp.oms.dtos.OnlineRequestDto;
import com.jp.oms.dtos.RequestMessage;
import com.jp.oms.exceptions.UnsupportedFileTypeException;
import com.jp.oms.kafka.producer.SendMessageToKafka;
import com.jp.oms.utils.RequestHandlerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
@Service
public class RequestHandlerService {

	private static final String [] allowed_File_Type = {"text/csv","application/vnd.ms-excel",
			"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"};

	@Autowired
	private SendMessageToKafka messageToKafka;

	/**
	 * Handle the online request and send the message to kafka topic.
	 *
	 * @param onlineRequest
	 * @return
	 * @throws JsonProcessingException
	 */
	public String handleOnlineRequest(OnlineRequestDto onlineRequest) throws JsonProcessingException {
		this.pushMessagesToKafkaTopic(onlineRequest.toRequestMessage());
		return "Success";
	}

	/**
	 * Handle bulk file request, parse file and send the messages to kafka one by one.
	 *
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws UnsupportedFileTypeException
	 */
	public String handleBulkRequest(MultipartFile file) throws IOException, UnsupportedFileTypeException {
		if(!Arrays.stream(allowed_File_Type).toList().contains(file.getContentType())){
			throw new UnsupportedFileTypeException("Uploaded file type is not supported");
		}
		for(var message:RequestHandlerUtils.read(RequestMessage.class,file.getInputStream())) {
			this.pushMessagesToKafkaTopic(message);
		}
		return "Success";
	}

	/**
	 * Send message to kafka.
	 *
	 * @param messages
	 * @throws JsonProcessingException
	 */
	private void pushMessagesToKafkaTopic(RequestMessage messages) throws JsonProcessingException {
		messageToKafka.sendMessageToTopic(RequestHandlerUtils.convertToJson(messages));
	}
}
