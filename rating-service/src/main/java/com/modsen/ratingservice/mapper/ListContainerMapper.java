package com.modsen.ratingservice.mapper;

import com.modsen.ratingservice.dto.ListContainerResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface ListContainerMapper {

    default <T> ListContainerResponseDto<T> toDto(Page<T> page) {
        return ListContainerResponseDto.<T>builder()
                .withValues(page.getContent())
                .withTotalElements(page.getTotalElements())
                .withCurrentOffset(page.getPageable().getPageNumber())
                .withCurrentLimit(page.getPageable().getPageSize())
                .withTotalPages(page.getTotalPages())
                .withSort(page.getSort().toString())
                .build();
    }

}
