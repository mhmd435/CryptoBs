package com.example.crypto_app.model.CryptoMarketModel;

public class CryptoMarketDataModel {

	private String Cryptos;
	private String Exchanges;
	private String MarketCap;
	private String Vol_24h;
	private String BTC_Dominance;
	private String ETH_Dominance;
	private String MarketCap_change;
	private String vol_change;
	private String BTCD_change;

	public CryptoMarketDataModel(String cryptos, String exchanges, String marketCap, String vol_24h, String BTC_Dominance, String ETH_Dominance, String marketCap_change, String vol_change, String BTCD_change) {
		Cryptos = cryptos;
		Exchanges = exchanges;
		MarketCap = marketCap;
		Vol_24h = vol_24h;
		this.BTC_Dominance = BTC_Dominance;
		this.ETH_Dominance = ETH_Dominance;
		MarketCap_change = marketCap_change;
		this.vol_change = vol_change;
		this.BTCD_change = BTCD_change;
	}

	public String getCryptos() {
		return Cryptos;
	}

	public String getExchanges() {
		return Exchanges;
	}

	public String getMarketCap() {
		return MarketCap;
	}

	public String getVol_24h() {
		return Vol_24h;
	}

	public String getBTC_Dominance() {
		return BTC_Dominance;
	}

	public String getETH_Dominance() {
		return ETH_Dominance;
	}

	public String getMarketCap_change() {
		return MarketCap_change;
	}

	public String getVol_change() {
		return vol_change;
	}

	public String getBTCD_change() {
		return BTCD_change;
	}
}