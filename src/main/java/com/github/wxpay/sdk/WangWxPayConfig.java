package com.github.wxpay.sdk;

import java.io.InputStream;

public class WangWxPayConfig extends WXPayConfig {
    @Override
    public String getAppID() {
        return "wxd898fcb01713c658";
    }

    @Override
    String getMchID() {
        return "1483469312";
    }

    @Override
    public String getKey() {
        return "098F6BCD4621D373CADE4E832627B4F6";
    }

    @Override
    InputStream getCertStream() {
        return null;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
    }
}
