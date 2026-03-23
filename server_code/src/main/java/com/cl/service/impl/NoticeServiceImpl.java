package com.cl.service.impl;

import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.service.JiuzhentongzhiService;
import com.cl.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {
    
    @Autowired
    private JiuzhentongzhiService jiuzhentongzhiService;
    
    @Override
    public boolean sendNotice(JiuzhentongzhiEntity notice) {
        try {
            // 检查渠道状态
            if (!checkChannelStatus(notice.getShouji())) {
                // 渠道不可用，记录失败状态
                updateNoticeStatus(notice, "渠道不可用");
                return false;
            }
            
            // 模拟发送通知
            // 实际项目中这里会调用短信API、邮件API等
            System.out.println("发送通知到 " + notice.getShouji() + ": " + notice.getTongzhibeizhu());
            
            // 模拟发送成功率90%
            if (new Random().nextInt(10) < 1) {
                // 发送失败
                updateNoticeStatus(notice, "发送失败");
                return false;
            }
            
            // 发送成功
            updateNoticeStatus(notice, "发送成功");
            return true;
        } catch (Exception e) {
            // 发送异常
            updateNoticeStatus(notice, "发送异常: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean retrySendNotice(Long noticeId) {
        JiuzhentongzhiEntity notice = jiuzhentongzhiService.selectById(noticeId);
        if (notice != null) {
            return sendNotice(notice);
        }
        return false;
    }
    
    @Override
    public boolean checkChannelStatus(String phone) {
        // 模拟检查渠道状态
        // 实际项目中这里会检查用户手机号是否有效、是否开启通知等
        return phone != null && phone.length() == 11;
    }
    
    /**
     * 更新通知状态
     */
    private void updateNoticeStatus(JiuzhentongzhiEntity notice, String status) {
        notice.setTongzhibeizhu(status);
        notice.setTongzhishijian(new Date());
        jiuzhentongzhiService.updateById(notice);
    }
}
