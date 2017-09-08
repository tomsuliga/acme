package org.suliga.acme.model.primegen;

import java.math.BigInteger;

public class PrimeNumberResult {
	private BigInteger primeNumber;
	private int numTries;
	private int numBits;
	private long millis;
	private String threadName;
	private long threadId;
	private String primeNumberDisplay;
	
	public PrimeNumberResult() {}
	
	public PrimeNumberResult(BigInteger primeNumber, int numBits, int numTries, long millis) {
		super();
		this.primeNumber = primeNumber;
		this.numBits = numBits;
		this.numTries = numTries;
		this.millis = millis;
		this.threadName = Thread.currentThread().getName();
		this.threadId = Thread.currentThread().getId();
		this.primeNumberDisplay = getPrimeNumber10();
	}

	public long getMillis() {
		return millis;
	}
	
	public int getNumDigits() {
		return primeNumber.toString().length();
	}
	
	public BigInteger getPrimeNumber() {
		return primeNumber;
	}
	
	public String getPrimeNumber10() {
		return primeNumber.toString(10);
	}

	public String getPrimeNumber16() {
		return primeNumber.toString(16);
	}

	public void setPrimeNumber(BigInteger primeNumber) {
		this.primeNumber = primeNumber;
	}

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public int getNumTries() {
		return numTries;
	}

	public void setNumTries(int numTries) {
		this.numTries = numTries;
	}
	public int getNumBits() {
		return numBits;
	}
	public void setNumBits(int numBits) {
		this.numBits = numBits;
	}

	public String getPrimeNumberDisplay() {
		return primeNumberDisplay;
	}
	public void setPrimeNumberDisplay(String primeNumberDisplay) {
		this.primeNumberDisplay = primeNumberDisplay;
	}
}
