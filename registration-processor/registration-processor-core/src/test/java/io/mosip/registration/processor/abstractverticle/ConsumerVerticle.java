package io.mosip.registration.processor.abstractverticle;

import io.mosip.registration.processor.core.abstractverticle.MessageBusAddress;
import io.mosip.registration.processor.core.abstractverticle.MessageDTO;
import io.mosip.registration.processor.core.abstractverticle.MosipEventBus;
import io.mosip.registration.processor.core.abstractverticle.MosipVerticleManager;

public class ConsumerVerticle extends MosipVerticleManager {
	private MessageDTO messageDTO;
	private MosipEventBus mosipEventBus;

	public void start() {
		ConsumerVerticle consumerVerticle = new ConsumerVerticle();
		vertx.deployVerticle(consumerVerticle);
		this.mosipEventBus = new MosipEventBus(vertx);

		this.messageDTO = new MessageDTO();
		this.messageDTO.setRid("1001");
		this.messageDTO.setRetryCount(0);
		this.messageDTO.setMessageBusAddress(MessageBusAddress.PACKET_VALIDATOR_BUS_IN);
		this.messageDTO.setIsValid(true);
		this.messageDTO.setInternalError(false);

		this.consume(mosipEventBus, MessageBusAddress.PACKET_VALIDATOR_BUS_IN);
		this.send(mosipEventBus, MessageBusAddress.DEMO_DEDUPE_BUS_IN, this.messageDTO);
		this.consumeAndSend(mosipEventBus, MessageBusAddress.PACKET_VALIDATOR_BUS_OUT, MessageBusAddress.RETRY_BUS);

	}

	@Override
	public MessageDTO process(MessageDTO object) {

		return object;
	}

	public MosipEventBus deployVerticle() {
		MosipEventBus mosipEventBus = this.getEventBus(this.getClass(),"http://104.211.212.28:51000/*/dev/DEV/ignite.xml");
		return mosipEventBus;
	}

}