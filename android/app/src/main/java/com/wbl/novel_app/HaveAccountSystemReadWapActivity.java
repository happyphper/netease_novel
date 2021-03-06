package com.wbl.novel_app;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.netease.readwap.IHandlerCallback;
import com.netease.readwap.IPayResultListener;
import com.netease.readwap.IReadWapCallback;
import com.netease.readwap.IRegisterNativeFunctionCallback;
import com.netease.readwap.ISetSDKAuthListener;
import com.netease.readwap.view.ReadWebView;

public class HaveAccountSystemReadWapActivity extends AppCompatActivity {

    private ReadWebView mReadWebView;
    private ISetSDKAuthListener mISetSDKAuthListener;
    private IPayResultListener mIPayResultListener;
    private String mSDKAuth;

    public static void startHaveAccountSystemReadWapActivity(Context context, boolean isAnonymous, String url, String appChannel) {
        Intent intent = new Intent(context, HaveAccountSystemReadWapActivity.class);
        intent.putExtra("isAnonymous", isAnonymous);
        intent.putExtra("url", url);
        intent.putExtra("appChannel", appChannel);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        boolean isAnonymous = intent.getBooleanExtra("isAnonymous", true);
        String url = intent.getStringExtra("url");
        String appChannel =  intent.getStringExtra("appChannel");

        mSDKAuth = Account.sSDKAuth;

        TextView closeTV = findViewById(R.id.tv_close);
        closeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mReadWebView = findViewById(R.id.readwebView);

        // 设置IReadWapCallback
        mReadWebView.setReadWapCallback(mReadWapCallback);
        // 注册本地接口
        mReadWebView.registerNativeFunction(Constants.NATIVE_FUNCTION, mRegisterNativeFunctionCallback);
        // 开始加载
        if (isAnonymous) {
            mReadWebView.startLoad(url, appChannel, null);
        } else {
            mReadWebView.startLoad(url, appChannel, mSDKAuth);
        }
    }

    private IReadWapCallback mReadWapCallback = new IReadWapCallback() {

        @Override
        public void doLogin(ISetSDKAuthListener setSDKAuthListener, String from) {
            mISetSDKAuthListener = setSDKAuthListener;
            LoginActivity.startLoginActivityForResult(HaveAccountSystemReadWapActivity.this, LoginActivity.REQUEST_CODE);
        }

        @Override
        public void saveSDKAuth(String SDKAuth) {
            // 有帐号体系的app不用实现该方法
        }

        @Override
        public void doPay(String transactionId, int amount, int payment, IPayResultListener payResultListener) {
            // 支付，希望使用应用自己支付方式的app需要实现该方法，完成支付后使用IPayResultListener通知支付结果。如果不用自己支付方式不用实现该方法。
            mIPayResultListener = payResultListener;
            pay(transactionId, amount, payment);
        }

        @Override
        public void notifyThemeChanged(boolean isNightMode) {
            // 通知主题切换，如果无此需求不用实现该方法
            changeTheme(isNightMode);
        }

        @Override
        public void notifyCurrentBookProgress(String bookId, String bookName, double progress) {
            // 通知当前打开书籍的阅读进度
            setBookProgress(bookId, bookName, progress);
        }

    };

    private IRegisterNativeFunctionCallback mRegisterNativeFunctionCallback = new IRegisterNativeFunctionCallback() {
        @Override
        public void onHandle(String handlerName, String value, IHandlerCallback handlerCallback) {
            String result = handle(handlerName, value);
            handlerCallback.onCallback(result);
        }
    };

    private String handle(String handlerName, String value) {
        if (handlerName.equals(Constants.NATIVE_FUNCTION)) {
            // 调用本地方法的具体实现
        }

        return null;
    }

    public void pay(String transactionId, int amount, int payment) {
        if (mIPayResultListener != null) {
            // true表示支付成功，false表示支付失败
            mIPayResultListener.onPayResult(true, transactionId);
        }
    }

    public void changeTheme(boolean isNightMode) {

    }

    public void setBookProgress(String bookId, String bookName, double progress) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LoginActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // 获取sdkAuth
                new GetSDKAuthTask().execute();
            }
        }
    }

    class GetSDKAuthTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Account.sSDKAuth = Constants.YD_SDK_HAVE_ACCOUNT_SYSTEM_AUTH;
            mSDKAuth = Account.sSDKAuth;

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (mISetSDKAuthListener != null) {
                mISetSDKAuthListener.setSDKAuth(mSDKAuth);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mReadWebView.canGoBack()) {
                mReadWebView.goBack();

                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

}
