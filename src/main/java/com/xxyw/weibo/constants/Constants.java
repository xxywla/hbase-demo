package com.xxyw.weibo.constants;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

public class Constants {

    // HBase配置信息
    public static final Configuration CONFIGURATION = HBaseConfiguration.create();

    // 命名空间
    public static final String NAMESPACE = "weibo7";

    // 微博内容表
    public static final String CONTENT_TABLE = NAMESPACE + ":content";
    public static final String CONTENT_TABLE_CF = "info";
    public static final int CONTENT_TABLE_VERSIONS = 1;

    //用户关系表
    public static final String RELATION_TABLE = NAMESPACE + ":relation";
    public static final String RELATION_TABLE_CF1 = "attends";
    public static final String RELATION_TABLE_CF2 = "fans";
    public static final int RELATION_TABLE_VERSIONS = 1;

    // 收件箱表
    public static final String INBOX_TABLE = NAMESPACE + ":inbox";
    public static final String INBOX_TABLE_CF = "info";
    public static final int INBOX_TABLE_VERSIONS = 2;
}
