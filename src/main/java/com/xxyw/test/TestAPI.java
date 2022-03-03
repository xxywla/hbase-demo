package com.xxyw.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos;

import java.io.IOException;

/**
 * DDL:
 * 1. 判断表是否存在
 * 2. 创建表
 * 3. 创建命名空间
 * 4. 删除表
 * <p>
 * DML:
 * 5. 插入数据
 * 6. 查数据 get
 * 7. 查数据 scan
 * 8. 删除数据
 */
public class TestAPI {
    private static Connection connection = null;
    private static Admin admin = null;

    static {
        try {
            // 1 获取配置信息
            Configuration configuration = HBaseConfiguration.create();
            configuration.set("hbase.zookeeper.quorum", "hadoop102,hadoop103,hadoop104");

            //2.创建连接对象
            connection = ConnectionFactory.createConnection(configuration);

            //3. 创建Admin对象
            admin = connection.getAdmin();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void close() throws IOException {
        if (admin != null) {
            admin.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    //1. 判断表是否存在
    public static boolean isTableExist(String tableName) throws IOException {

        return admin.tableExists(TableName.valueOf(tableName));
    }

    // 2. 创建表
    public static void createTable(String tableName, String... cfs) throws IOException {
        // 1. 判断是否存在列族信息
        if (cfs.length <= 0) {
            System.out.println("请设置列族信息！");
            return;
        }
        //2. 判断表是否存在
        if (isTableExist(tableName)) {
            System.out.println(tableName + "表已存在！");
            return;
        }
        //3. 创建表描述器
        HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));

        // 4. 循环添加列族信息
        for (String cf : cfs) {
            // 5. 创建列族描述器
            HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(cf);

            // 6. 添加具体的列族信息
            hTableDescriptor.addFamily(hColumnDescriptor);
        }
        //7. 创建表
        admin.createTable(hTableDescriptor);
    }

    // 3. 删除表
    public static void dropTable(String tableName) throws IOException {
        // 1. 判断表是否存在
        if (!isTableExist(tableName)) {
            System.out.println(tableName + "表不存在！！！");
        }
        // 2. 使表下线
        admin.disableTable(TableName.valueOf(tableName));
        //3. 删除表
        admin.deleteTable(TableName.valueOf(tableName));
    }

    //4. 创建命名空间
    public static void createNamespace(String ns) {
        //1. 创建命名空间描述器
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(ns).build();
        //2.创建命名空间
        try {
            admin.createNamespace(namespaceDescriptor);
        } catch (NamespaceExistException e) {
            System.out.println(ns + "命名空间已存在！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // 1. 测试表是否存在
//        System.out.println(isTableExist("stu5"));
        // 2. 创建表测试
        createTable("0408:stu5", "info1", "info2");
        // 3. 删除表测试
//        dropTable("stu5");

        //4.创建命名空间测试
//        createNamespace("0408");

//        System.out.println(isTableExist("stu5"));
        // 关闭资源
        close();
    }
}
