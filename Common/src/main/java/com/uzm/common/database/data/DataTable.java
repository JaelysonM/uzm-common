package com.uzm.common.database.data;

import com.uzm.common.database.data.interfaces.DataTableInfo;
import com.uzm.common.database.solutions.DatabaseSolution;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A complete and upgradable plugin for <strong>any</strong> use for any project..
 *
 * @author JotaMPê (UzmStudio)
 * @version 2.0.6
 */

@AllArgsConstructor
@Getter
public abstract class DataTable {

    private final DatabaseSolution databaseSolution;

    public abstract void init();

    public abstract Map<String, DataContainer> getDefaultValues();

    public DataTableInfo getInfo() {
        return this.getClass().getAnnotation(DataTableInfo.class);
    }

    private static final List<DataTable> TABLES = new ArrayList<>();

    public static void registerTable(DataTable table) {
        TABLES.add(table);
    }

    public static Collection<DataTable> listTables() {
        return TABLES;
    }
}