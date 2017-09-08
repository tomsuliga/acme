package org.suliga.acme.model.primegen;

public class PrimegenStart {
	private int numBits;
	private int numThreads;
	public int getNumBits() {
		return numBits;
	}
	public void setNumBits(int numBits) {
		this.numBits = numBits;
	}
	public int getNumThreads() {
		return numThreads;
	}
	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}
	
	@Override
	public String toString() {
		return "PrimegenStart: " + "numBits=" + numBits + ", numThreads=" + numThreads;
	}
}

