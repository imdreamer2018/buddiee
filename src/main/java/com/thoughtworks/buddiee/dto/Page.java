package com.thoughtworks.buddiee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Page<T> {
    private int currentPage;
    private int totalPage;
    private List<T> data;
}
