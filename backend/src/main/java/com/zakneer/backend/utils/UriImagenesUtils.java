package com.zakneer.backend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UriImagenesUtils {

    @Value("${url.imagenes}")
    private String url;

    public String getUrlImagen(String uri){
        return url + uri;
    }
}
