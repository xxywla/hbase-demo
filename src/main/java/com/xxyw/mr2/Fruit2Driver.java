package com.xxyw.mr2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.zookeeper.ZKUtil;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class Fruit2Driver implements Tool {
    private Configuration configuration = new Configuration();

    @Override
    public int run(String[] strings) throws Exception {
        Job job = Job.getInstance(configuration);

        job.setJarByClass(Fruit2Driver.class);

        Scan scan = new Scan();
        scan.setCaching(500);
        scan.setCacheBlocks(false);

        TableMapReduceUtil.initTableMapperJob(
                "fruit2",
                scan,
                Fruit2Mapper.class,
                ImmutableBytesWritable.class,
                Put.class,
                job);
        TableMapReduceUtil.initTableReducerJob(
                "fruit3",
                Fruit2Reducer.class,
                job
        );

        boolean result = job.waitForCompletion(true);

        return result ? 0 : 1;
    }

    @Override
    public void setConf(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Configuration getConf() {
        return configuration;
    }

    public static void main(String[] args) {

        try {
//            Configuration configuration = new Configuration();
            Configuration configuration = HBaseConfiguration.create();
            configuration.set("hbase.rootdir", "hdfs://mycluster/hbase");
            ToolRunner.run(configuration, new Fruit2Driver(), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
