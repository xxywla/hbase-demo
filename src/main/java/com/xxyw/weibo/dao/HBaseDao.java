package com.xxyw.weibo.dao;

import com.xxyw.weibo.constants.Constants;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 1.发布微博
 * 2. 删除微博
 * 3. 关注用户
 * 4. 取关用户
 * 5. 获取用户微博详情
 * 6. 获取用户的初始化页面
 */
public class HBaseDao {

    // 1. 发布微博
    public static void publishWeiBo(String uid, String content) throws IOException {

        // 1. 获取Connection对象
        Connection connection = ConnectionFactory.createConnection(Constants.CONFIGURATION);

        // 第一部分：操作微博内容表
        // 1.获取微博内容表对象
        Table contTable = connection.getTable(TableName.valueOf(Constants.CONTENT_TABLE));

        // 2.获取当前时间戳
        long ts = System.currentTimeMillis();

        // 3.获取RowKey
        String rowKey = uid + "_" + ts;

        // 4. 创建Put对象
        Put contPut = new Put(Bytes.toBytes(rowKey));

        // 5.给Put对象赋值
        contPut.addColumn(Bytes.toBytes(Constants.CONTENT_TABLE_CF), Bytes.toBytes("content"), Bytes.toBytes(content));

        //6. 执行插入数据操作
        contTable.put(contPut);

        // 第二部分：操作微博收件箱表
        // 1.获取用户关系表对象
        Table relTable = connection.getTable(TableName.valueOf(Constants.RELATION_TABLE));

        // 2.获取当前发布微博人的fans列族数据
        Get get = new Get(Bytes.toBytes(uid));
        get.addFamily(Bytes.toBytes(Constants.RELATION_TABLE_CF2));
        Result result = relTable.get(get);

        // 3.创建一个集合，用于存放微博内容表的Put对象
        ArrayList<Put> inboxPuts = new ArrayList<>();

        // 4.遍历粉丝
        for (Cell cell : result.rawCells()) {

            // 5.构建微博收件箱表的Put对象
            Put inboxPut = new Put(CellUtil.cloneQualifier(cell));

            //6.给收件箱表的Put对象赋值
            inboxPut.addColumn(Bytes.toBytes(Constants.INBOX_TABLE_CF), Bytes.toBytes(uid), Bytes.toBytes(rowKey));

            // 7.将收件箱表的Put对象存入集合
            inboxPuts.add(inboxPut);
        }
        // 8.判断是否有粉丝
        if (inboxPuts.size() > 0) {
            // 获取收件箱表对象
            Table inboxTable = connection.getTable(TableName.valueOf(Constants.INBOX_TABLE));

            // 执行收件箱表数据插入操作
            inboxTable.put(inboxPuts);

            //关闭收件箱表
            inboxTable.close();
        }

        // 关闭资源
        relTable.close();
        contTable.close();
        connection.close();
    }

    // 2.关注用户
    public static void addAttends(String uid, String... attends) throws IOException {

        //校验是否添加了待关注的人
        if (attends.length <= 0) {
            System.out.println("请选择待关注的人！！！");
            return;
        }

        // 获取Connection对象
        Connection connection = ConnectionFactory.createConnection(Constants.CONFIGURATION);

        // 第一部分：操作用户关系表
        //1.获取用户关系表对象
        Table relTable = connection.getTable(TableName.valueOf(Constants.RELATION_TABLE));

        // 2.创建一个集合，用于存放用户关系表的Put对象
        ArrayList<Put> relPuts = new ArrayList<>();

        // 3. 创建操作者的Put对象
        Put uidPut = new Put(Bytes.toBytes(uid));

        // 4.循环创建被关注者的Put对象
        for (String attend : attends) {
            // 5. 给操作者的Put对象赋值
            uidPut.addColumn(Bytes.toBytes(Constants.RELATION_TABLE_CF1), Bytes.toBytes(attend), Bytes.toBytes(attend));

            //6. 创建被关注者的Put对象
            Put attendPut = new Put(Bytes.toBytes(attend));

            //7.给被关注者的Put对象赋值
            attendPut.addColumn(Bytes.toBytes(Constants.RELATION_TABLE_CF2), Bytes.toBytes(uid), Bytes.toBytes(uid));

            //8.将被关注者的Put对象放入集合
            relPuts.add(attendPut);
        }

        // 9.将操作者的Put对象添加至集合
        relPuts.add(uidPut);

        // 10. 执行用户关系表的插入数据操作
        relTable.put(relPuts);

        // 第二部分：操作收件箱表
        //1. 获取微博内容表的对象

        // 2. 创建收件箱表的Put对象

        // 3. 循环attends，获取每个被关注者的近期发布的微博

        {
            // 4. 获取当前被关注着的近期发布的微博（scan） startRow A_ stopRow B|

            // 5. 对获取的值进行遍历
            {
                // 6.给收件箱表的Put对象赋值

            }
        }

        // 7. 判断当前的Put对象是否为空
        if (true) {
            // 获取收件箱表对象

            // 插入数据

            // 关闭收件箱表连接
        }

        // 关闭资源

    }
}
