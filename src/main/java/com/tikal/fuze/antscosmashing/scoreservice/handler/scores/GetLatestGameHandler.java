package com.tikal.fuze.antscosmashing.scoreservice.handler.scores;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tikal.fuze.antscosmashing.scoreservice.handler.response.ApiGatewayResponse;
import com.tikal.fuze.antscosmashing.scoreservice.repositories.TeamsScoresRepository;
import com.tikal.fuze.antscosmashing.scoreservice.service.PlayerScoresService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class GetLatestGameHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger logger = LogManager.getLogger(GetLatestGameHandler.class);

	private TeamsScoresRepository teamsScoresRepository;

	public GetLatestGameHandler() {
		if (teamsScoresRepository == null)
			teamsScoresRepository = new TeamsScoresRepository();
	}

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		logger.debug("startHandleRequest... ");
		Integer latestGame = teamsScoresRepository.getLatestGame();
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(latestGame)
				.build();
	}
}
