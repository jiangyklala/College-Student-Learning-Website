package com.jxm.yiti.req;

import jakarta.validation.constraints.NotNull;

public class DownloadListSaveReq {

    private Long id;  // 有值时为更新, 没值时为新增

    @NotNull(message = "[名称] 不能为空")
    private String name;

    @NotNull(message = "[分类一] 不能为空")
    private Long categoryId1;

    private Long categoryId2;

    private String description;

    private Integer downloadCount;

    private String size;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId1() {
        return categoryId1;
    }

    public void setCategoryId1(Long categoryId1) {
        this.categoryId1 = categoryId1;
    }

    public Long getCategoryId2() {
        return categoryId2;
    }

    public void setCategoryId2(Long categoryId2) {
        this.categoryId2 = categoryId2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", categoryId1=").append(categoryId1);
        sb.append(", categoryId2=").append(categoryId2);
        sb.append(", description=").append(description);
        sb.append(", downloadCount=").append(downloadCount);
        sb.append(", size=").append(size);
        sb.append("]");
        return sb.toString();
    }
}