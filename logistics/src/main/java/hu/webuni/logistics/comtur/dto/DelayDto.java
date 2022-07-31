package hu.webuni.logistics.comtur.dto;

import javax.validation.constraints.Positive;

/**
 * Delay data transfer object.
 * 
 * @author comtur
 */
public class DelayDto {

	private long milestoneId;

	@Positive
	private int delay;

	public long getMilestoneId() {
		return milestoneId;
	}

	public void setMilestoneId(long milestoneId) {
		this.milestoneId = milestoneId;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
}
