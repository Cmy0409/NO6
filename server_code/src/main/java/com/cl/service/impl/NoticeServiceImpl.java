package com.cl.service.impl;

import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.service.JiuzhentongzhiService;
import com.cl.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {
    
    @Autowired
    private JiuzhentongzhiService jiuzhentongzhiService;
    
    @Override
    public void sendNotice(JiuzhentongzhiEntity notice) {
        System.out.println("NoticeService.sendNotice 被调用");
        try {
            // 等待一小段时间，确保数据库事务已提交
            Thread.sleep(100);
            
            // 重新从数据库获取通知记录
            JiuzhentongzhiEntity dbNotice = jiuzhentongzhiService.selectById(notice.getId());
            if (dbNotice == null) {
                System.out.println("通知记录不存在: " + notice.getId());
                return;
            }
            
            // 获取原始通知内容
            String originalContent = dbNotice.getTongzhibeizhu();
            System.out.println("原始通知内容: " + originalContent);
            System.out.println("手机号: " + dbNotice.getShouji());
            
            // 检查渠道状态
            if (!checkChannelStatus(dbNotice.getShouji())) {
                System.out.println("渠道检查失败");
                // 渠道不可用，记录失败状态
                updateNoticeStatus(dbNotice, originalContent + " [发送失败-渠道不可用]");
                return;
            }
            System.out.println("渠道检查通过");
            
            // 模拟发送通知
            // 实际项目中这里会调用短信API、邮件API等
            System.out.println("发送通知到 " + dbNotice.getShouji() + ": " + originalContent);
            
            // 模拟发送成功（实际项目中根据API返回结果判断）
            // 发送成功，保留原始内容并添加状态
            updateNoticeStatus(dbNotice, originalContent + " [发送成功]");
            System.out.println("通知状态已更新为发送成功");
        } catch (Exception e) {
            // 发送异常
            System.out.println("发送异常: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean retrySendNotice(Long noticeId) {
        JiuzhentongzhiEntity notice = jiuzhentongzhiService.selectById(noticeId);
        if (notice != null) {
            // 清除之前的状态标记，重新发送
            String content = notice.getTongzhibeizhu();
            // 移除之前的状态标记
            content = content.replaceAll(" \\[发送成功\\]", "")
                           .replaceAll(" \\[发送失败-[^\\]]*\\]", "");
            notice.setTongzhibeizhu(content);
            sendNotice(notice);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean checkChannelStatus(String phone) {
        // 检查用户接收渠道状态
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
