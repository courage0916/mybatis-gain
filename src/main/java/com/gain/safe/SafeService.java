package com.gain.safe;

import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class SafeService {

    private final AllowUpdateTables allowUpdateTables;

    public SafeService(AllowUpdateTables allowUpdateTables) {
        this.allowUpdateTables = allowUpdateTables;
    }

    public void check(SqlCommandType type, String sql) throws SQLException {

        if (!allowUpdateTables.isEnable()) return;

        if (SqlCommandType.UPDATE.equals(type) && determine(sql, allowUpdateTables.getUpdate())) return;

        if (SqlCommandType.DELETE.equals(type) && determine(sql, allowUpdateTables.getDelete())) return;

        if ((SqlCommandType.DELETE.equals(type) || SqlCommandType.UPDATE.equals(type))
                && !sql.toLowerCase().contains("where"))
            throw new SQLException(String.format("DELETE OR UPDATE SQL HAVING WHERE CONDITION , ERROR SQL : %S", sql));
    }

    private boolean determine(String str, List<String> list) {
        str = str.toLowerCase();
        for (String item : list) {
            if (str.contains(item.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
