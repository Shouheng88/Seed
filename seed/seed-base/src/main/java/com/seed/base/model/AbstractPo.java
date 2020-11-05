package com.seed.base.model;

import com.seed.base.annotation.ColumnInfo;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 14:29
 */
public abstract class AbstractPo implements Serializable {

    private static final long serialVersionUID = 6982434571375510313L;

    @Id
    @Column(name = "id", nullable = false)
    @ColumnInfo(comment = "ID")
    private Long id;

    @Column(name = "remark")
    @ColumnInfo(comment = "remark for column")
    private String remark;

    @Version
    @Column(name = "lock_version", nullable = false)
    @ColumnInfo(comment = "Optimistic Concurrency Control version")
    private Long lockVersion = 0L;

    @Column(name = "updated_time", nullable = false)
    @ColumnInfo(comment = "last updated time of record")
    private Date updatedTime;

    @Column(name = "created_time", nullable = false)
    @ColumnInfo(comment = "Created time of record")
    private Date createdTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(Long lockVersion) {
        this.lockVersion = lockVersion;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "AbstractPo{" +
                "id=" + id +
                ", remark='" + remark + '\'' +
                ", lockVersion=" + lockVersion +
                ", updatedTime=" + updatedTime +
                ", createdTime=" + createdTime +
                '}';
    }
}
