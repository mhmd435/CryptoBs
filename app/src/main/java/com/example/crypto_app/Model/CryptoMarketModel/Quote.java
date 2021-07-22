package com.example.crypto_app.Model.CryptoMarketModel;

import com.google.gson.annotations.SerializedName;

public class Quote{

	@SerializedName("eth_dominance_yesterday")
	private double ethDominanceYesterday;

	@SerializedName("last_updated")
	private String lastUpdated;

	@SerializedName("defi_volume_24h")
	private double defiVolume24h;

	@SerializedName("active_exchanges")
	private int activeExchanges;

	@SerializedName("derivatives_24h_percentage_change")
	private double derivatives24hPercentageChange;

	@SerializedName("defi_market_cap")
	private double defiMarketCap;

	@SerializedName("derivatives_volume_24h_reported")
	private double derivativesVolume24hReported;

	@SerializedName("total_cryptocurrencies")
	private int totalCryptocurrencies;

	@SerializedName("btc_dominance_24h_percentage_change")
	private double btcDominance24hPercentageChange;

	@SerializedName("btc_dominance_yesterday")
	private double btcDominanceYesterday;

	@SerializedName("total_exchanges")
	private int totalExchanges;

	@SerializedName("stablecoin_volume_24h")
	private double stablecoinVolume24h;

	@SerializedName("stablecoin_volume_24h_reported")
	private double stablecoinVolume24hReported;

	@SerializedName("defi_volume_24h_reported")
	private double defiVolume24hReported;

	@SerializedName("active_market_pairs")
	private int activeMarketPairs;

	@SerializedName("btc_dominance")
	private double btcDominance;

	@SerializedName("stablecoin_24h_percentage_change")
	private double stablecoin24hPercentageChange;

	@SerializedName("quote")
	private Quote quote;

	@SerializedName("eth_dominance")
	private double ethDominance;

	@SerializedName("active_cryptocurrencies")
	private int activeCryptocurrencies;

	@SerializedName("stablecoin_market_cap")
	private double stablecoinMarketCap;

	@SerializedName("derivatives_volume_24h")
	private double derivativesVolume24h;

	@SerializedName("defi_24h_percentage_change")
	private double defi24hPercentageChange;

	@SerializedName("eth_dominance_24h_percentage_change")
	private double ethDominance24hPercentageChange;

	@SerializedName("USD")
	private USD uSD;

	public double getEthDominanceYesterday(){
		return ethDominanceYesterday;
	}

	public String getLastUpdated(){
		return lastUpdated;
	}

	public double getDefiVolume24h(){
		return defiVolume24h;
	}

	public int getActiveExchanges(){
		return activeExchanges;
	}

	public double getDerivatives24hPercentageChange(){
		return derivatives24hPercentageChange;
	}

	public double getDefiMarketCap(){
		return defiMarketCap;
	}

	public double getDerivativesVolume24hReported(){
		return derivativesVolume24hReported;
	}

	public int getTotalCryptocurrencies(){
		return totalCryptocurrencies;
	}

	public double getBtcDominance24hPercentageChange(){
		return btcDominance24hPercentageChange;
	}

	public double getBtcDominanceYesterday(){
		return btcDominanceYesterday;
	}

	public int getTotalExchanges(){
		return totalExchanges;
	}

	public double getStablecoinVolume24h(){
		return stablecoinVolume24h;
	}

	public double getStablecoinVolume24hReported(){
		return stablecoinVolume24hReported;
	}

	public double getDefiVolume24hReported(){
		return defiVolume24hReported;
	}

	public int getActiveMarketPairs(){
		return activeMarketPairs;
	}

	public double getBtcDominance(){
		return btcDominance;
	}

	public double getStablecoin24hPercentageChange(){
		return stablecoin24hPercentageChange;
	}

	public Quote getQuote(){
		return quote;
	}

	public double getEthDominance(){
		return ethDominance;
	}

	public int getActiveCryptocurrencies(){
		return activeCryptocurrencies;
	}

	public double getStablecoinMarketCap(){
		return stablecoinMarketCap;
	}

	public double getDerivativesVolume24h(){
		return derivativesVolume24h;
	}

	public double getDefi24hPercentageChange(){
		return defi24hPercentageChange;
	}

	public double getEthDominance24hPercentageChange(){
		return ethDominance24hPercentageChange;
	}

	public USD getUSD(){
		return uSD;
	}
}