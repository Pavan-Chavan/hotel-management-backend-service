package com.teams.entity.models;

import com.teams.entity.Permission;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class HotelManagementListResponse<T> implements Serializable {

    private Integer pageNumber;
    private Integer pageSize;
    private Long totalCount;
    private List<T> response;


    public static <T> HotelManagementListResponse getResponse(Page<T> page, Integer pageNumber, Integer pageSize) {
        HotelManagementListResponse listResponse = new HotelManagementListResponse<>();

        listResponse.setPageNumber(pageNumber);
        listResponse.setPageSize(pageSize);
        listResponse.setTotalCount(page.getTotalElements());
        listResponse.setResponse(page.getContent());
        return listResponse;
    }

    public static <T> HotelManagementListResponse getResponse(List<T> list, Integer pageNumber, Integer pageSize) {
        HotelManagementListResponse<T> listResponse = new HotelManagementListResponse<>();

        listResponse.setPageNumber(pageNumber);
        listResponse.setPageSize(pageSize);
        listResponse.setTotalCount(0L);
        listResponse.setResponse(list);
        return listResponse;
    }

    public static <T> HotelManagementListResponse getResponse(T data, Integer pageNumber, Integer pageSize) {
        HotelManagementListResponse<T> listResponse = new HotelManagementListResponse<>();

        listResponse.setPageNumber(pageNumber);
        listResponse.setPageSize(pageSize);
        listResponse.setTotalCount(0L);
        listResponse.setResponse(Arrays.asList(data));
        return listResponse;
    }
}
