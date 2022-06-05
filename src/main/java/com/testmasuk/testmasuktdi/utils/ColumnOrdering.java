package com.testmasuk.testmasuktdi.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.Assert;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ColumnOrdering {

    private List<String> values = new ArrayList<>();

    public ColumnOrdering(@NotNull String... columnsName) {
        Assert.noNullElements(columnsName, "column names should be not null");
        Assert.notEmpty(columnsName, "column names fields is required");
        this.values.clear();
        this.values = Arrays.asList(columnsName);
    }

    public String orderBy(String dir, @NotNull @Min(0) Long index) {
        String columnName = null;

        try {
            index = NumberUtils.toLong(index.toString(), 0L);
            columnName = this.values.get(index.intValue());
        } catch (IndexOutOfBoundsException var5) {
            columnName = this.values.get(0);
        } catch (NullPointerException var6) {
            columnName = this.values.get(0);
        } catch (Exception ex) {
            columnName = this.values.get(0);
        }
        return String.format(" order by %s %s ", columnName, StringUtils.isNotBlank(dir) ? dir : "asc");
    }
}
