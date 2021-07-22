package com.example.crypto_app.Model.CryptoMarketModel;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("last_updated")
	private String lastUpdated;

	@SerializedName("quote")
	private Quote quote;

	@SerializedName("active_exchanges")
	private int activeExchanges;

	@SerializedName("eth_dominance")
	private double ethDominance;

	@SerializedName("active_cryptocurrencies")
	private int activeCryptocurrencies;

	@SerializedName("total_cryptocurrencies")
	private int totalCryptocurrencies;

	@SerializedName("total_exchanges")
	private int totalExchanges;

	@SerializedName("btc_dominance")
	private double btcDominance;

	@SerializedName("active_market_pairs")
	private int activeMarketPairs;

	public String getLastUpdated(){
		return lastUpdated;
	}

	public Quote getQuote(){
		return quote;
	}

	public int getActiveExchanges(){
		return activeExchanges;
	}

	public double getEthDominance(){
		return ethDominance;
	}

	public int getActiveCryptocurrencies(){
		return activeCryptocurrencies;
	}

	public int getTotalCryptocurrencies(){
		return totalCryptocurrencies;
	}

	public int getTotalExchanges(){
		return totalExchanges;
	}

	public double getBtcDominance(){
		return btcDominance;
	}

	public int getActiveMarketPairs(){
		return activeMarketPairs;
	}
}