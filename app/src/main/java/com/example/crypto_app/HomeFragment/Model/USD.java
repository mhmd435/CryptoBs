package com.example.crypto_app.HomeFragment.Model;

import com.google.gson.annotations.SerializedName;

public class USD{

	@SerializedName("percent_change_30d")
	private double percentChange30d;

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

	@SerializedName("percent_change_60d")
	private double percentChange60d;

	@SerializedName("volume_24h")
	private double volume24h;

	@SerializedName("percent_change_90d")
	private double percentChange90d;

	@SerializedName("percent_change_7d")
	private double percentChange7d;

	public double getPercentChange30d(){
		return percentChange30d;
	}

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

	public double getPercentChange60d(){
		return percentChange60d;
	}

	public double getVolume24h(){
		return volume24h;
	}

	public double getPercentChange90d(){
		return percentChange90d;
	}

	public double getPercentChange7d(){
		return percentChange7d;
	}
}