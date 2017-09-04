package org.suliga.acme.model.dailydiet;

public class NutrientDisplaySummary {
	private String totalId;
	private double totalAmount;
	private String percentId;
	private int percentAmount;
	
	public NutrientDisplaySummary(String totalId, double totalAmount, String percentId, double percentAmount) {
		this.totalId = totalId;
		this.totalAmount = totalAmount;
		this.percentId = percentId;
		this.setPercentAmount(percentAmount);
	}

	public String getTotalId() {
		return totalId;
	}

	public void setTotalId(String totalId) {
		this.totalId = totalId;
	}

	public double getTotalAmount() {
		return Math.round (totalAmount * 1000.0) / 1000.0;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPercentId() {
		return percentId;
	}

	public void setPercentId(String percentId) {
		this.percentId = percentId;
	}

	public int getPercentAmount() {
		return percentAmount;
	}

	public void setPercentAmount(double percentAmount) {
		this.percentAmount = (int) (percentAmount);
	}
	
	@Override
	public String toString() {
		return "NutrientDisplaySummary: " + totalId + ", " + totalAmount + ", " + percentId + ", " + percentAmount;
	}
}
