package com.jxm.yiti.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jxm.yiti.domain.DownloadList;
import com.jxm.yiti.domain.DownloadListExample;
import com.jxm.yiti.mapper.DownloadListMapper;
import com.jxm.yiti.mapper.cust.DownloadListMapperCust;
import com.jxm.yiti.rabbitmq.MessageInfoRabbit;
import com.jxm.yiti.rabbitmq.ReceiverRabbit;
import com.jxm.yiti.rabbitmq.SenderRabbit;
import com.jxm.yiti.req.DownloadListQueryReq;
import com.jxm.yiti.req.DownloadListSaveReq;
import com.jxm.yiti.resp.DownloadListQueryResp;
import com.jxm.yiti.resp.PageResp;
import com.jxm.yiti.utils.CopyUtil;
import com.jxm.yiti.utils.SnowFlakeIdWorker;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Service
public class DownloadListService {

    @Resource
    private DownloadListMapper downloadListMapper;

    @Resource
    private DownloadListMapperCust downloadListMapperCust;

    private static final Logger LOG = LoggerFactory.getLogger(DownloadListService.class);


    @PostConstruct
    public void init() throws IOException, TimeoutException {
        ReceiverRabbit receiverRabbit = new ReceiverRabbit("yiti_new_download_item");  // 启用 RabbitMQ Receiver
         new Thread(receiverRabbit).start();
    }


    public DownloadListService() throws IOException, TimeoutException { }

    /**
     * 查询下载列表的所有数据, 带有模糊匹配功能
     */
    public PageResp<DownloadListQueryResp> selectAll(DownloadListQueryReq req) {
        DownloadListExample downloadListExample = new DownloadListExample();
        DownloadListExample.Criteria criteria = downloadListExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {                                      // 动态 SQL
            criteria.andNameLike("%" + req.getName() + "%");                            // 模糊匹配条件
        }

        PageHelper.startPage(req.getPage(), req.getSize(), true);
        List<DownloadList> downloadLists = downloadListMapper.selectByExample(downloadListExample);

        PageInfo<DownloadList> downloadListPageInfo = new PageInfo<>(downloadLists);
        LOG.info("当前页: " + downloadListPageInfo.getPageNum()
                + ", 总页数: " + downloadListPageInfo.getPages()
                + " , 总记录数: " + downloadListPageInfo.getTotal());
        PageResp<DownloadListQueryResp> resp = new PageResp<>();
        resp.setList(CopyUtil.copyList(downloadLists, DownloadListQueryResp.class));
        resp.setTotal(downloadListPageInfo.getTotal());

        return resp;

    }

    /**
     * 新增或者更新一个下载项
     */
    public int save(DownloadListSaveReq req) {
        SnowFlakeIdWorker snowFlakeIdWorker = new SnowFlakeIdWorker(0, 0);
        DownloadList res = CopyUtil.copy(req, DownloadList.class);

        try {
            if (ObjectUtils.isEmpty(req.getId())) {
                res.setId(snowFlakeIdWorker.nextId());
                int insertRes = downloadListMapper.insert(res);
//                rocketMQ.convertAndSend("yiti_newItem_notifyAll", "有新文档发布");
                if (insertRes == 1) {
                    new SenderRabbit("yiti_new_download_item").sendMessage(new MessageInfoRabbit("lala", "有新的下载资料发布呦~"));
                }
                return insertRes;
            } else {
                return downloadListMapper.updateByPrimaryKey(res);
            }
        } catch (DataIntegrityViolationException e) {
            LOG.info("错误: 插入或更新错误");
            e.printStackTrace();
            return -1;
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除 1 个下载项
     */
    public int delete(Long id) {
        int res = downloadListMapper.deleteByPrimaryKey(id);
        if (res != 1) {
            LOG.info("删除 1 个课程项失败");
            return 0;
        } else {
            return res;
        }
    }

    /**
     * 根据 categoryId2 查询: 在同一分类下的所有下载项
     */
    public PageResp<DownloadListQueryResp> selectByCategoryId(DownloadListQueryReq req) {
        DownloadListExample downloadListExample = new DownloadListExample();
        if (req.getCategoryId() != -1) {
            DownloadListExample.Criteria criteria = downloadListExample.createCriteria();
            criteria.andCategoryId2EqualTo(req.getCategoryId());                            // 匹配 categoryId2 相同的的下载项
        }

        PageHelper.startPage(req.getPage(), req.getSize(), true);
        List<DownloadList> downloadLists = downloadListMapper.selectByExample(downloadListExample);

        PageInfo<DownloadList> downloadListPageInfo = new PageInfo<>(downloadLists);
        LOG.info("当前页: " + downloadListPageInfo.getPageNum()
                + ", 总页数: " + downloadListPageInfo.getPages()
                + " , 总记录数: " + downloadListPageInfo.getTotal());
        PageResp<DownloadListQueryResp> resp = new PageResp<>();
        resp.setList(CopyUtil.copyList(downloadLists, DownloadListQueryResp.class));
        resp.setTotal(downloadListPageInfo.getTotal());

        return resp;
    }

    public int incrDownloadCount(Long id) {
        return downloadListMapperCust.incrDownloadCount(id);
    }
}
