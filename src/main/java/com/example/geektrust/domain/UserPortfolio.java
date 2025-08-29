package com.example.geektrust.domain;

import java.util.LinkedHashSet;
import java.util.Set;

public class UserPortfolio {
	private final String userName;
	private final Set<String> currentFunds = new LinkedHashSet<>();

	public UserPortfolio(String userName) {
		this.userName = userName;
	}

	public void addFund(String fundName) {
		currentFunds.add(fundName);
	}

	public String getUserName() {
		return userName;
	}

	public Set<String> getFunds() {
		return currentFunds;
	}
}
