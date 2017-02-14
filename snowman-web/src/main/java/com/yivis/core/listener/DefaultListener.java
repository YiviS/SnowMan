package com.yivis.core.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Created by XuGuang on 2017/2/14.
 *
 * @Description: 系统默认listener
 */
public class DefaultListener implements ApplicationListener<ContextRefreshedEvent> {
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            //TODO 这里写下将要初始化的内容
        }
    }
}
