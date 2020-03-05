package it.eng.fimind.model;

import java.util.List;

public class NotificationContent {
	private String subscriptionId;
	private List<Entity> data;
	
	public NotificationContent() {}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public List<Entity> getData() {
		return data;
	}

	public void setData(List<Entity> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "NotificationContent [subscriptionId=" + subscriptionId + "]";
	}
	
	
	
}
