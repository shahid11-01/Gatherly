package com.social.gatherly.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class EventAllResponse<T> {
    private List<T>events;  //이벤트 목록(실제 데이터)
    private int page;      // 현재 페이지 번호
    private int size;     //한 페이지당 개수
    private long total;   //전체 이벤트 수
    private int totalPages; //전체 페이지 수
    private boolean last;  // 마직막 페이지 여부

}
