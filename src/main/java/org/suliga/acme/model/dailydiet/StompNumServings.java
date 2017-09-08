package org.suliga.acme.model.dailydiet;

public class StompNumServings {
	private String numServingsId;
	private boolean selected;
	private String dailyDietName;
	
	public String getNumServingsId() {
		// foodItem-1-count-2
		String temp = numServingsId.substring(0,numServingsId.indexOf("-count-"));
		return temp.substring("foodItem-".length());
	}
	public void setNumServingsId(String numServingsId) {
		this.numServingsId = numServingsId;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getDailyDietName() {
		return dailyDietName;
	}
	public void setDailyDietName(String dailyDietName) {
		this.dailyDietName = dailyDietName;
	}
	
	public int getCount() {
		int count = Integer.parseInt(numServingsId.substring(numServingsId.length()-1));
		return count;
	}
}
