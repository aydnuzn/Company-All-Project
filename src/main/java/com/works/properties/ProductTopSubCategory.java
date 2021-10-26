package com.works.properties;

import lombok.Data;

import java.util.List;

@Data
public class ProductTopSubCategory {
    private String top_title;
    private List<String> sub_titles;
}
