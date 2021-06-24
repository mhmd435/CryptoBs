package com.example.crypto_app.HomeFragment.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class AllMarketModel implements Parcelable {

	@SerializedName("data")
	private List<DataItem> data;

	@SerializedName("status")
	private Status status;

	protected AllMarketModel(Parcel in) {
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<AllMarketModel> CREATOR = new Creator<AllMarketModel>() {
		@Override
		public AllMarketModel createFromParcel(Parcel in) {
			return new AllMarketModel(in);
		}

		@Override
		public AllMarketModel[] newArray(int size) {
			return new AllMarketModel[size];
		}
	};

	public List<DataItem> getData(){
		return data;
	}

	public Status getStatus(){
		return status;
	}





	public static class Status{

		@SerializedName("error_message")
		private String errorMessage;

		@SerializedName("elapsed")
		private int elapsed;

		@SerializedName("total_count")
		private int totalCount;

		@SerializedName("credit_count")
		private int creditCount;

		@SerializedName("error_code")
		private int errorCode;

		@SerializedName("timestamp")
		private String timestamp;

		@SerializedName("notice")
		private Object notice;

		public String getErrorMessage(){
			return errorMessage;
		}

		public int getElapsed(){
			return elapsed;
		}

		public int getTotalCount(){
			return totalCount;
		}

		public int getCreditCount(){
			return creditCount;
		}

		public int getErrorCode(){
			return errorCode;
		}

		public String getTimestamp(){
			return timestamp;
		}

		public Object getNotice(){
			return notice;
		}
	}
}