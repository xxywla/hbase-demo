package com.xxyw.weibo.constants;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class Constants {

    // HBase配置信息
    public static Configuration CONFIGURATION = HBaseConfiguration.create();

    // 命名空间
    public static String NAMESPACE = "weibo";

    // 微博内容表
    public static String CONTENT_TABLE = "weibo:content";
    public static String CONTENT_TABLE_CF = "info";
    public static int CONTENT_TABLE_VERSIONS = 1;

    //用户关系表
    public static String RELATION_TABLE = "weibo:relation";
    public static String RELATION_TABLE_CF1 = "attends";
    public static String RELATION_TABLE_CF2 = "fans";
    public static int RELATION_TABLE_VERSIONS = 1;

    // 收件箱表
    public static String INBOX_TABLE = "weibo:inbox";
    public static String INBOX_TABLE_CF = "info";
    public static int INBOX_TABLE_VERSIONS = 2;
}
