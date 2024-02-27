package by.potapchuk.flatservice.service;

import by.potapchuk.flatservice.core.entity.FlatWebSite;
import by.potapchuk.flatservice.service.api.SaleContentRequester;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static by.potapchuk.flatservice.utils.DocumentRequester.requestHtmlDocument;

@Slf4j
@Component
public class KufarSaleContentRequester implements SaleContentRequester {

    @Value("${custom.rent-web-sites.kufar.base-url-sale}")
    private String baseUrl;

    @Override
    public FlatWebSite getType() {
        return FlatWebSite.KUFAR;
    }

    @Override
    public Element getHtmlDocument(String url) {
        return requestHtmlDocument(url == null ? baseUrl : url).getElementById("content");
    }
}
