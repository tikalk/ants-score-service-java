package com.tikal.fuze.antscosmashing.scoreservice.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tikal.fuze.antscosmashing.scoreservice.service.PlayerScoresService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger logger = LogManager.getLogger(Handler.class);

	private PlayerScoresService playerScoresService;

	public Handler() {
		if (playerScoresService == null)
			playerScoresService = new PlayerScoresService();
	}

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		logger.info("received: " + input);
//		Response responseBody = new Response("Go Serverless v1.x! Your function executed successfully!", input);
		Object pathParameters = input.get("pathParameters");
		logger.debug("pathParameters class :{}",pathParameters.getClass());
		String scores = playerScoresService.getPlayersScores("6");
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(scores)
//				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
				.build();
	}
}
