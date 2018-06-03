package net.wmann.fileanalyser.converter;

import lombok.extern.slf4j.Slf4j;
import net.wmann.fileanalyser.exception.InvalidUrlException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UriConverter {

    public static List<URI> convert(List<String> urls) throws InvalidUrlException {
        List<String> invalidUrls = new ArrayList<>();
        List<URI> result = new ArrayList<>();
        for(String urlRaw : urls) {
            try {
                URL url = new URL(urlRaw);
                result.add(url.toURI());
            } catch (MalformedURLException|URISyntaxException e) {
                log.debug("Invalid url found: " + urlRaw, e);
                invalidUrls.add(urlRaw);
            }
        }
        if(!invalidUrls.isEmpty()) {
            throw new InvalidUrlException(invalidUrls);
        }
        return result;
    }

}
