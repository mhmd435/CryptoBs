package com.example.crypto_app.Model.CryptoListModel;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DataItem{

	@SerializedName("symbol")
	private String symbol;

	@SerializedName("circulating_supply")
	private double circulatingSupply;

	@SerializedName("last_updated")
	private String lastUpdated;

	@SerializedName("total_supply")
	private double totalSupply;

	@SerializedName("cmc_rank")
	private int cmcRank;

	@SerializedName("platform")
	private Object platform;

	@SerializedName("tags")
	private List<String> tags;

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("quote")
	private ListQuote listQuote;

	@SerializedName("num_market_pairs")
	private int numMarketPairs;

	@SerializedName("name")
	private String name;

	@SerializedName("max_supply")
	private double maxSupply;

	@SerializedName("id")
	private int id;

	@SerializedName("slug")
	private String slug;

	public String getSymbol(){
		return symbol;
	}

	public double getCirculatingSupply(){
		return circulatingSupply;
	}

	public String getLastUpdated(){
		return lastUpdated;
	}

	public double getTotalSupply(){
		return totalSupply;
	}

	public int getCmcRank(){
		return cmcRank;
	}

	public Object getPlatform(){
		return platform;
	}

	public List<String> getTags(){
		return tags;
	}

	public String getDateAdded(){
		return dateAdded;
	}

	public ListQuote getQuote(){
		return listQuote;
	}

	public int getNumMarketPairs(){
		return numMarketPairs;
	}

	public String getName(){
		return name;
	}

	public double getMaxSupply(){
		return maxSupply;
	}

	public int getId(){
		return id;
	}

	public String getSlug(){
		return slug;
	}




	public static class ListQuote {

		@SerializedName("USD")
		private ListUSD uSD;

		public ListUSD getUSD(){
			return uSD;
		}
	}
}