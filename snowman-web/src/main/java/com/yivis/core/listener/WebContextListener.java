package com.yivis.core.listener;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Created by XuGuang on 2017/2/14.
 */
public class WebContextListener extends ContextLoaderListener {
    @Override
    public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
        /*if (!SystemService.printKeyLoadMessage()){
			return null;
		}*/
        return super.initWebApplicationContext(servletContext);
    }
}
