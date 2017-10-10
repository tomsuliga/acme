package org.suliga.acme.service.javatest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class JavaTestServiceImpl implements JavaTestService {

	@Override
	public String factorial(int n) {
		return Integer.toString(recursiveFactorial(n));
	}
	
	private int recursiveFactorial(int n) {
		if (n == 1) {
			return 1;
		}
		
		return n * recursiveFactorial(n-1);
	}

	@Override
	public String anagram(String s) {
		Set<String> words = new HashSet<>();
		byte[] bytes = s.getBytes();
		recursiveAnagram(bytes, 0, words, 0);
		return words.toString();
	}

	private void recursiveAnagram(byte[] bytes, int numRotations, Set<String> words, int firstPos) {
		if (firstPos >= bytes.length-1) {
			return;
		}
		
		for (int i=firstPos; i<bytes.length; i++) {
			words.add(new String(bytes));
			byte[] bytesSub = copyBytes(bytes);
			rotateLeft(bytesSub, firstPos+1);
			recursiveAnagram(bytesSub, firstPos+1, words, firstPos+1);
			rotateLeft(bytes, firstPos);
		}
	}

	private void rotateLeft(byte[] bytes, int firstPos) {
		byte temp = bytes[firstPos];
		
		for (int i=firstPos;i<bytes.length-1;i++) {
			bytes[i] = bytes[i+1];			
		}
		bytes[bytes.length-1] = temp;		
	}
	
	private byte[] copyBytes(byte[] bytes) {
		byte[] b = new byte[bytes.length];
		for (int i=0;i<bytes.length;i++) {
			b[i] = bytes[i];
		}
		return b;
	}

	@Override
	public String priorityQueue(int... values) {
		PriorityQueue<Integer> pq = new PriorityQueue<>(16, (a,b) -> a-b);
		for (int i:values) {
			pq.offer(i);
		}
		StringBuilder sb = new StringBuilder();
		while (!pq.isEmpty()) {
			sb.append(pq.poll() + " ");
		}
		return sb.toString();
	}
}

