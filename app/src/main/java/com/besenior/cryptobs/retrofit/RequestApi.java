package com.besenior.cryptobs.retrofit;

import com.besenior.cryptobs.model.SliderImageModel;
import com.besenior.cryptobs.model.cryptolistmodel.AllMarketModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface RequestApi {

    @GET("https://api.coinmarketcap.com/data-api/v3/cryptocurrency/listing?start=1&limit=1000&sortBy=market_cap&sortType=desc&convert=USD&cryptoType=all&tagType=all&audited=false&aux=ath,atl,high24h,low24h,num_market_pairs,cmc_rank,date_added,tags,platform,max_supply,circulating_supply,total_supply,volume_7d,volume_30d")
    Observable<AllMarketModel> makeMarketLatestListCall();


    @GET("https://besenior.ir/CryptoApi/PageViewAsset/pageViewImg.json")
    Observable<SliderImageModel> makePageViewCall();
}
