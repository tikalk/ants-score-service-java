package com.tikal.fuze.antscosmashing.scoreservice.handler.games;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tikal.fuze.antscosmashing.scoreservice.handler.response.ApiGatewayResponse;
import com.tikal.fuze.antscosmashing.scoreservice.repositories.GamesRepository;
import com.tikal.fuze.antscosmashing.scoreservice.service.PlayerScoresService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class PostGameHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger logger = LogManager.getLogger(PostGameHandler.class);

	private GamesRepository gamesRepository;

	public PostGameHandler() {
		if (gamesRepository == null)
			gamesRepository = new GamesRepository();
	}

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		logger.debug("received: " + input);
		Map<String,?> pathParameters = (Map<String, Object>) input.get("pathParameters");
		String gameId = pathParameters.get("gameId").toString();

		gamesRepository.put(Integer.valueOf(gameId));
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.build();
	}
}
