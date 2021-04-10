package dnt.spring.reddit.service;

import org.springframework.stereotype.Service;

@Service
public class SlugService {
    public static String toSlug(String title) {
        return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\'|\"|\\s\\?\\,\\.]+", "-");
    }
}
