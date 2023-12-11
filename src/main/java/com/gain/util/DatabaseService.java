package com.gain.util;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DatabaseService {

    @Resource
    private Database database;



    private static final Map<String, List<String>> tables = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        try {
            Connection conn = DriverManager.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
            DatabaseMetaData metaData = conn.getMetaData();
            String databaseType = metaData.getDatabaseProductName();

            //针对MySQL数据库进行相关生成操作
            if (databaseType.equals("MySQL")) {
                //获取所有表结构
                ResultSet tableResultSet = metaData.getTables(null, "%", "%", new String[]{"TABLE"});
                String databaseName = conn.getCatalog();
                //循环所有表信息
                while (tableResultSet.next()) {
                    String tableName = tableResultSet.getString("TABLE_NAME");
                    List<String> columns = new ArrayList<>();
                    ResultSet columnsSet = metaData.getColumns(databaseName, database.getUsername(), tableName, null);
                    while (columnsSet.next()){
                        String columnName = columnsSet.getString("COLUMN_NAME");
                        columns.add(columnName);
                    }
                    tables.put(tableName,columns);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> findByTableName(String tableName){

        if(tables.containsKey(tableName)){
            return tables.get(tableName);
        }

        return new ArrayList<>();
    }

    public Boolean isExist(String tableName,String fileName){

        return findByTableName(tableName).contains(fileName);
    }
}
