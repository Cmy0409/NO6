package com.cl.service;

import com.cl.entity.JiuzhentongzhiEntity;

public interface NoticeService {
    /**
     * 发送通知
     * @param notice 通知实体
     * @return 是否发送成功
     */
    boolean sendNotice(JiuzhentongzhiEntity notice);
    
    /**
     * 重试发送通知
     * @param noticeId 通知ID
     * @return 是否发送成功
     */
    boolean retrySendNotice(Long noticeId);
    
    /**
     * 检查用户接收渠道状态
     * @param phone 用户手机号
     * @return 渠道是否可用
     */
    boolean checkChannelStatus(String phone);
}
