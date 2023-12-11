package com.gain.safe;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "allow-tables")
public class AllowUpdateTables {

    /**
     * 启用
     */
    private boolean enable = false;

    /**
     * 允许全表删除的表
     */
    private List<String> delete;

    /**
     * 允许全表更新的表
     */
    private List<String> update;


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<String> getDelete() {
        return delete;
    }

    public void setDelete(List<String> delete) {
        this.delete = delete;
    }

    public List<String> getUpdate() {
        return update;
    }

    public void setUpdate(List<String> update) {
        this.update = update;
    }
}
