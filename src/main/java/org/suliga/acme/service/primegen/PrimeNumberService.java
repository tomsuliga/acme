package org.suliga.acme.service.primegen;

import org.springframework.messaging.simp.SimpMessagingTemplate;

public interface PrimeNumberService {
	void generatePrimeNumber(int numBits, int numThreads, SimpMessagingTemplate simpMessagingTemplate);
	void stopPreviousThreads();
}
