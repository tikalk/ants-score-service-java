package com.tikal.fuze.antscosmashing.scoreservice.handler.hittrials;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.tikal.fuze.antscosmashing.scoreservice.domain.HitTrial;
import com.tikal.fuze.antscosmashing.scoreservice.handler.response.ApiGatewayResponse;
import com.tikal.fuze.antscosmashing.scoreservice.service.PlayerScoresService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static com.tikal.fuze.antscosmashing.scoreservice.domain.HitTrial.createHitTrial;

public class PostHitTrialHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger logger = LogManager.getLogger(PostHitTrialHandler.class);

	private PlayerScoresService playerScoresService;

	public PostHitTrialHandler(PlayerScoresService playerScoresService){
		this.playerScoresService=playerScoresService;
	}

	public PostHitTrialHandler() {
		if (playerScoresService == null)
			playerScoresService = new PlayerScoresService();
	}

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		logger.debug("received: " + input);
		String body = (String) input.get("body");
		HitTrial hitTrial = createHitTrial(body);
		playerScoresService.savePlayerScore(hitTrial);
		return ApiGatewayResponse.builder()
				.setObjectBody(hitTrial)
				.setStatusCode(200)
				.build();
	}
}
