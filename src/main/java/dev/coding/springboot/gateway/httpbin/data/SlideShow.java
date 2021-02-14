package dev.coding.springboot.gateway.httpbin.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SlideShow implements Serializable {
    private String author;
    private String date;
    private String title;
    private List<Slide> slides;
}
