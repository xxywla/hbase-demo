package com.xxyw.weibo.test;

import com.xxyw.weibo.constants.Constants;
import com.xxyw.weibo.dao.HBaseDao;
import com.xxyw.weibo.utils.HBaseUtil;

import javax.security.auth.login.Configuration;
import java.io.IOException;

public class TestWeiBo {
    public static void init() {

        try {
            // 创建命名空间
            HBaseUtil.createNameSpace(Constants.NAMESPACE);

            // 创建微博内容表
            HBaseUtil.createTable(Constants.CONTENT_TABLE, Constants.CONTENT_TABLE_VERSIONS, Constants.CONTENT_TABLE_CF);

            // 创建用户关系表
            HBaseUtil.createTable(Constants.RELATION_TABLE, Constants.RELATION_TABLE_VERSIONS, Constants.RELATION_TABLE_CF1, Constants.RELATION_TABLE_CF2);

            // 创建收件箱表
            HBaseUtil.createTable(Constants.INBOX_TABLE, Constants.INBOX_TABLE_VERSIONS, Constants.INBOX_TABLE_CF);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {

        // 初始化
        init();

        // 1001发布微博
        HBaseDao.publishWeiBo("1001", "1001的第一条微博");

        // 1002 关注 1001 和 1003
        HBaseDao.addAttends("1002", "1001", "1003");

        // 获取 1002 初始化页面
        HBaseDao.getInit("1002");

        System.out.println("===============111==============");

        // 1003 发布3条微博, 1001 发布2条微博
        HBaseDao.publishWeiBo("1003", "1003的第一条微博");
        Thread.sleep(10);

        HBaseDao.publishWeiBo("1001", "1001的第二条微博");
        Thread.sleep(10);

        HBaseDao.publishWeiBo("1003", "1003的第二条微博");
        Thread.sleep(10);

        HBaseDao.publishWeiBo("1001", "1001的第三条微博！！！");
        Thread.sleep(10);

        HBaseDao.publishWeiBo("1003", "1003的第三条微博！！！");

        // 获取1002初始化页面
        HBaseDao.getInit("1002");

        System.out.println("===============222==============");

        // 1002 取关 1003
        HBaseDao.deleteAttends("1002", "1003");

        // 获取1002初始化页面
        HBaseDao.getInit("1002");

        System.out.println("==============333===============");

        // 1002再次关注1003
        HBaseDao.addAttends("1002", "1003");

        // 获取1002初始化页面
        HBaseDao.getInit("1002");

        System.out.println("==============444===============");

        // 获取1001微博详情
        HBaseDao.getWeiBo("1001");

    }
}
