package ru.clevertec.ecl.pagination;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.clevertec.ecl.data.page.Paging;

@Component
@RequiredArgsConstructor
public class Pagination {

    @Value("${pagination.limit}")
    private int DEFAULT_LIMIT;

    public Paging getPaging(Integer limit, Integer offset, Integer currentPage, Integer totalEntities) {
        int definedLimit = getValidatedLimit(limit, totalEntities);
        int definedCurrentPage = getValidCurrentPage(currentPage);
        int definedOffset = getValidOffset(offset, definedLimit, definedCurrentPage);
        int totalPages = getTotalPages(totalEntities, definedLimit);
        int page = getPage(definedCurrentPage, totalPages);
        return new Paging(definedLimit, definedOffset, page, totalPages);
    }

    private int getValidatedLimit(Integer limit, Integer totalEntities) {
        if (limit == null || limit <= 0) {
            return DEFAULT_LIMIT;
        } else {
            return Math.min(limit, totalEntities);
        }
    }

    private int getValidCurrentPage(Integer currentPage) {
        if (currentPage == null || currentPage < 0) {
            return 0;
        } else {
            return currentPage;
        }
    }

    private int getValidOffset(Integer offset, Integer limit, Integer currentPage) {
        if (offset == null) {
            offset = 0;
        }
        if (offset <= 0 && currentPage > 0) {
            return (currentPage - 1) * limit;
        } else {
            return offset;
        }
    }

    private int getTotalPages(int totalEntities, int limit) {
        int pages = totalEntities / limit;
        int additionalPage = (totalEntities - (pages * limit) > 0) ? 1 : 0;
        return pages + additionalPage;
    }

    private int getPage(int currentPage, int totalPages) {
        int page;
        if (currentPage <= 0) {
            page = 1;
        } else {
            page = Math.min(currentPage, totalPages);
        }
        return page;
    }
}
