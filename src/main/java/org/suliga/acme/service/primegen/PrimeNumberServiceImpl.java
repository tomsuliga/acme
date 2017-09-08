package org.suliga.acme.service.primegen;

import java.math.BigInteger;
import java.time.Instant;
import java.util.BitSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.suliga.acme.model.primegen.PrimeNumberResult;

@Service
public class PrimeNumberServiceImpl implements PrimeNumberService {
	private static final Logger logger = LoggerFactory.getLogger(PrimeNumberServiceImpl.class);
	private static boolean canRun;
	
	private static final int[] intPrimes = { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43,
			47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137,
			139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229,
			233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331,
			337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433,
			439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547,
			557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647,
			653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761,
			769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881,
			883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997 };
	private static BigInteger[] bigPrimes = new BigInteger[intPrimes.length];

	static {
		for (int i=0;i<intPrimes.length;i++) {
			bigPrimes[i] = BigInteger.valueOf(intPrimes[i]);
		}		
	}
	
	@Override
	public void generatePrimeNumber(int numBits, int numThreads, SimpMessagingTemplate simpMessagingTemplate) {		
		
		ExecutorService es = Executors.newFixedThreadPool(numThreads);
		canRun= true;
		
		for (int i=0;i<numThreads;i++) {
			es.execute(() -> {
				while (canRun) {
					PrimeNumberResult pnr = generatePrimeNumber(numBits);
					if (canRun) {
						simpMessagingTemplate.convertAndSend("/topic/primeNumberResult", pnr);
					}
				}
			});
		}
		
		es.shutdown();
	}
	
	@Override
	public void stopPreviousThreads() {
		logger.debug("stopPreviousThreads called");
		canRun = false;
	}
	
	private PrimeNumberResult generatePrimeNumber(int numBits) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		boolean good = false;
		BigInteger bigResult = null;
		int numTries = 0;
		Instant instantStart = Instant.now();
		while (!good && canRun) {
			numTries++;
			BitSet bs = new BitSet(numBits);
			for (int i=0;i<numBits;i++) {
				if (random.nextInt(2) == 1) {
					bs.set(i);
				}
			}
			bigResult = new BigInteger(bs.toByteArray());
			if (bigResult.signum() == -1) {
				bigResult = bigResult.negate();
			}
			good = true;
			// test initial set of known primes
			for (int i=0;i<bigPrimes.length;i++) {
				BigInteger[] bigTest = bigResult.divideAndRemainder(bigPrimes[i]);
				if (bigTest[1].intValue() == 0) {
					good = false;
					break;
				}				
			}
			// still good? try more odd numbers
			if (good) {
				for (int i=1001;i<25_000_000;i+=2) {
					BigInteger bigAdditional = BigInteger.valueOf(i);
					BigInteger[] bigTest = bigResult.divideAndRemainder(bigAdditional);
					if (bigTest[1].intValue() == 0 || !canRun) {
						good = false;
						break;
					}				
				}
			}
		}
		if (canRun) {
			Instant instantEnd = Instant.now();
			PrimeNumberResult pnr = new PrimeNumberResult(bigResult, numBits, numTries, instantEnd.toEpochMilli() - instantStart.toEpochMilli());
			return pnr;
		}
		return null;
	}
}


