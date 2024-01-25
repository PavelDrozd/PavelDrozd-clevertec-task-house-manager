package ru.clevertec.ecl.data.page;

public record Paging(

        int limit,

        int offset,

        int page,

        int totalPages
) {
}