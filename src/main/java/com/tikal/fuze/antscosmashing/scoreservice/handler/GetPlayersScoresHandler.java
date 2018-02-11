package com.tikal.fuze.antscosmashing.scoreservice.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tikal.fuze.antscosmashing.scoreservice.service.PlayerScoresService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class GetPlayersScoresHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger logger = LogManager.getLogger(GetPlayersScoresHandler.class);

	private PlayerScoresService playerScoresService;

	public GetPlayersScoresHandler() {
		if (playerScoresService == null)
			playerScoresService = new PlayerScoresService();
	}

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		logger.debug("received: " + input);
		Map<String,?> pathParameters = (Map<String, Object>) input.get("pathParameters");
		String scores = playerScoresService.getPlayersScores(pathParameters.get("gameId").toString());
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(scores)
				.build();
	}
}
