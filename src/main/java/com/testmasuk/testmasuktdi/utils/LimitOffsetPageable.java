package com.testmasuk.testmasuktdi.utils;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.validation.constraints.NotNull;

public class LimitOffsetPageable {

    private String query;
    private String rowNumber;
    private MapSqlParameterSource parameterSource;

    public LimitOffsetPageable(@NotNull MapSqlParameterSource parameterSource) {
        this.parameterSource = parameterSource;
    }

    public String query(@NotNull String baseQuery, @NotNull String nameOfRowNumber) {
        this.query = baseQuery;
        this.rowNumber = nameOfRowNumber;
        return String.format("with data as (%s) select * from data where %s between (:start + 1) and (:page * :limit)", query, rowNumber);
    }

    public String query(@NotNull String baseQuery, @NotNull String nameOfRowNumber, @NotNull Long length) {
        this.query = baseQuery;
        this.rowNumber = nameOfRowNumber;
        if (length == -1)
            return String.format("with data as (%s) select * from data", query);
        else
            return String.format("with data as (%s) select * from data where %s between (:start + 1) and (:page * :limit)", query, rowNumber);
    }

    public MapSqlParameterSource parameter(Long start, Long limit) {
        if (start > 0) {
            parameterSource.addValue("page", (start + limit) / limit);
            parameterSource.addValue("start", start);
            parameterSource.addValue("limit", limit);
        } else if (limit == -1) {
// do nothing
        } else {
            parameterSource.addValue("page", 1);
            parameterSource.addValue("start", start);
            parameterSource.addValue("limit", limit);
        }

        return parameterSource;
    }

}
