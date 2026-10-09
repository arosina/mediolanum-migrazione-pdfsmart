package prgm.pdfwebformsutil.drivers.service;

import java.util.concurrent.Semaphore;

public class HolderCachedObject<K, M> {
	private Semaphore semaphore = new Semaphore(1);
	private String cacheKey;
	private K key;
	private M value;

	public HolderCachedObject() {
	}

	public HolderCachedObject(M value) {
		this.value = value;
	}

	public Semaphore getSemaphore() {
		return semaphore;
	}

	public M getValue() {
		return value;
	}

	public void setValue(M value) {
		this.value = value;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}
}
