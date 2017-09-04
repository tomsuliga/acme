package org.suliga.acme.model.dailydiet;

public class NumServings {
	private String numServingsId;
	private boolean selected;
	private String dailyDietName;
	
	public String getNumServingsId() {
		return numServingsId;
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
