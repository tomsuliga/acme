package org.suliga.acme.model.rss;

import java.util.ArrayList;
import java.util.List;

public class Channel {
	private List<Item> items = new ArrayList<>();

	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	@Override
	public String toString() {
		return "Channel: " + items.toString();
	}
}

