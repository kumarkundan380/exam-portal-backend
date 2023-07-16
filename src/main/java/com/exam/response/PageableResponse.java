package com.exam.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageableResponse<T> {

    private T content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElement;
    private Integer totalPages;
    private Boolean isLast;
    private Boolean isFirst;

}
