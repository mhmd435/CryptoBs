package com.besenior.cryptobs.viewmodel;

import android.app.Application;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.besenior.cryptobs.R;

import org.jetbrains.annotations.NotNull;

public class CryptoDetailViewModel extends AndroidViewModel {


    public CryptoDetailViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    // update chart with that Interval
    public void OnIntervalBtnClickByNumber(View view, int interval, WebView webView, String Symbol, Button btn,Button btn2,Button btn3,Button btn4,Button btn5){
        disableAllBtn(btn,btn2,btn3,btn4,btn5);

        // Active Button's Layout (Bottom Border)
        view.setBackgroundResource(R.drawable.borderbutton_active);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + Symbol + "USD&interval=" + interval + "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT");
    }

    // update chart with that Interval
    public void OnIntervalBtnClickByChar(View view, String interval, WebView webView,String Symbol, Button btn,Button btn2,Button btn3,Button btn4,Button btn5){
        disableAllBtn(btn,btn2,btn3,btn4,btn5);

        // Active Button's Layout (Bottom Border)
        view.setBackgroundResource(R.drawable.borderbutton_active);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + Symbol + "USD&interval=" + interval + "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT");
    }

    // inActive Button's Layout (No Border)
    private void disableAllBtn(Button btn, Button btn2, Button btn3, Button btn4, Button btn5){
        btn.setBackgroundResource(R.drawable.borderbutton_notactive);
        btn2.setBackgroundResource(R.drawable.borderbutton_notactive);
        btn3.setBackgroundResource(R.drawable.borderbutton_notactive);
        btn4.setBackgroundResource(R.drawable.borderbutton_notactive);
        btn5.setBackgroundResource(R.drawable.borderbutton_notactive);

    }
}
