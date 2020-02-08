package com.tw.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO {
    private List<QuestionDTO> questionDTOList;
    private Boolean showFirstPage;
    private Boolean showLastPage;
    private Boolean showPreviousPage;
    private Boolean showNextPage;
    private Integer page;
    private Integer totalPage;
    private List<Integer> pageIndex = new ArrayList<>();

    public void setPagination(Integer totalCount, Integer page, Integer size){
        this.page = page;
        int totalPage = ( totalCount + size - 1 ) / size;
        this.totalPage = totalPage;
        int leftIndex = Math.max(1, page - 3);
        int rightIndex = Math.min(totalPage, page + 3);
        for (int i = leftIndex; i <= rightIndex; i++)
            pageIndex.add(i);

        showFirstPage = !pageIndex.contains(1);
        showLastPage =  !pageIndex.contains(totalPage);

        showPreviousPage = page != 1;
        showNextPage = page != totalPage;
    }
}
