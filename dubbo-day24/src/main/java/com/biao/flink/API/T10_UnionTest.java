package com.biao.flink.API;

import com.biao.flink.Producer;
import com.biao.flink.Vehicle;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootApplication
public class T10_UnionTest {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        environment.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        // 设置环境全局并行数，将作用于所有operator
        environment.setParallelism(1);
        List<Integer> list1 = Arrays.asList(1,3,5,7,9);
        List<Integer> list2 = Arrays.asList(2,4,6,8,10);
        DataStream<Integer> dataStream1 = environment.fromCollection(list1);
        DataStream<Integer> dataStream2 = environment.fromCollection(list2);
        dataStream1.union(dataStream2)
                .writeAsText("D:/API/union", FileSystem.WriteMode.OVERWRITE);
        dataStream1.union(dataStream1)
                .writeAsText("D:/API/union2", FileSystem.WriteMode.OVERWRITE);
        environment.execute();
    }
}
