package com.niyanchun;

import com.beust.jcommander.JCommander;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

/**
 * @description:
 * @author: NiYanchun
 * @version: 1.0
 * @create: 2019-07-18
 **/
@Slf4j
public class Main {

    public static void main(String[] argv) throws Exception {
        Args args = new Args();
        JCommander jCommander = JCommander.newBuilder()
                .addObject(args)
                .build();
        jCommander.parse(argv);

        if (args.isHelp()) {
            jCommander.usage();
            return;
        }

        log.info("args: {}", args);

        String data = null;
        if (args.getFilename() != null) {
            data = readFile(args.getFilename());
        }

        int parallelism = args.getParallelism();
        CountDownLatch latch = new CountDownLatch(parallelism);

        long startTime = System.currentTimeMillis();
        while (parallelism > 0) {
            Worker worker = new Worker("thread-" + parallelism, args, latch, data);
            worker.start();
            parallelism--;
        }

        // wait all worker thread finish
        latch.await();

        long endTime = System.currentTimeMillis();
        long sendData = args.getBulkSize() * args.getBulkCount() * args.getParallelism();
        long usedTime = (endTime - startTime) / 1000;
        log.info("all thread has exit, summary:\nthread: {}, send data: {} records, used time: {} s, speed: {} records/s",
                args.getParallelism(), sendData, usedTime, usedTime > 0 ? (sendData / usedTime) : "NA");

        System.exit(0);
    }

    private static String readFile(String filename) throws Exception {
        File file = new File(filename);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];

        try (InputStream inputStream = new FileInputStream(filename)) {
            inputStream.read(fileContent);
        }

        return new String(fileContent);
    }
}
