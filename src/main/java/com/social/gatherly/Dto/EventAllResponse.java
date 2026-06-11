package com.social.gatherly.Dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EventAllResponse<T> {
    private List<T>events;
    private int page;
    private int size;
    private long total;
    private int totalPages;
    private boolean last;

}
