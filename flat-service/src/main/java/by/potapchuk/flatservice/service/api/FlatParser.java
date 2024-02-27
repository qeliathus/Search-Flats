package by.potapchuk.flatservice.service.api;

import by.potapchuk.flatservice.core.entity.Flat;
import by.potapchuk.flatservice.core.entity.FlatWebSite;
import org.jsoup.nodes.Element;

import java.util.List;

public interface FlatParser {

    FlatWebSite getType();

    List<Flat> parseFlats(Element content);

    String parseNextPageLink(Element content);
}
