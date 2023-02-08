package com.jxm.yiti.resp;

/**
 * 不直接返回 DownloadList, 防止其中含有敏感字段
 */
public class DownloadListQueryResp {

    private Long id;

    private String name;

    private Long categoryId1;

    private Long categoryId2;

    private Integer downloadCount;

    private String size;

    private String downloadLink;


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

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    @Override
    public String toString() {
        return "DownloadListQueryResp{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId1=" + categoryId1 +
                ", categoryId2=" + categoryId2 +
                ", downloadCount=" + downloadCount +
                ", size='" + size + '\'' +
                ", downloadLink='" + downloadLink + '\'' +
                '}';
    }
}