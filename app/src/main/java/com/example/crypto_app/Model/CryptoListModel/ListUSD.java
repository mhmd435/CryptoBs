package com.example.crypto_app.Model.CryptoListModel;

import com.google.gson.annotations.SerializedName;

public class ListUSD {

	@SerializedName("percent_change_1h")
	private double percentChange1h;

	@SerializedName("last_updated")
	private String lastUpdated;

	@SerializedName("percent_change_24h")
	private double percentChange24h;

	@SerializedName("market_cap")
	private double marketCap;

	@SerializedName("price")
	private double price;

	@SerializedName("volume_24h")
	private double volume24h;

	@SerializedName("percent_change_7d")
	private double percentChange7d;

	public double getPercentChange1h(){
		return percentChange1h;
	}

	public String getLastUpdated(){
		return lastUpdated;
	}

	public double getPercentChange24h(){
		return percentChange24h;
	}

	public double getMarketCap(){
		return marketCap;
	}

	public double getPrice(){
		return price;
	}

	public double getVolume24h(){
		return volume24h;
	}

	public double getPercentChange7d(){
		return percentChange7d;
	}
}