package by.potapchuk.flatservice.service.api;

import by.potapchuk.flatservice.core.entity.FlatWebSite;
import org.jsoup.nodes.Element;

public interface ContentRequester {

    FlatWebSite getType();

    Element getHtmlDocument(String url);
}
