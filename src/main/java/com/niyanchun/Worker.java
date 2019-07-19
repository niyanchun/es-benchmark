package com.niyanchun;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @description: Work send data to es async.
 * @author: NiYanchun
 * @version: 1.0
 * @create: 2019-07-18
 **/
@Slf4j
public class Worker extends Thread {
    private static RestHighLevelClient client = null;
    private String threadName;
    private String data;

    private Args args;
    private CountDownLatch latch;


    public Worker(String threadName, Args args, CountDownLatch latch, String data) {
        super(threadName);

        this.threadName = threadName;
        this.args = args;
        this.latch = latch;
        this.data = data;
    }

    private static BulkRequest getBulkRequest(String index, int bulkSize, String message) {
        BulkRequest request = new BulkRequest();

        while (bulkSize > 0) {
            request.add(new IndexRequest(index, "doc")
                    .source(XContentType.JSON, "message", message));
            bulkSize--;
        }

        return request;
    }

    @Override
    public void run() {

        try {
            String index = args.getIndex();
            int bulkCount = args.getBulkCount();
            int bulkSize = args.getBulkSize();
            int messageSize = args.getMessageSize();

            String message;
            if (this.data != null) {
                message = this.data;
            } else if (StringUtils.isBlank(args.getMessage())) {
                message = RandomStringUtils.randomAlphanumeric(messageSize);
            } else {
                message = args.getMessage();
            }

            log.info("thread: {} start, index: {}, bulkCount: {}, bulkSize: {}, message(length:{}): {}",
                    currentThread().getName(), index, bulkCount, bulkSize, message.length(), message.substring(0, 100));

            while (bulkCount > 0) {
                BulkRequest request = getBulkRequest(index, bulkSize, message);

                if (args.isSync()) {
                    try {
                        getClient().bulk(request, RequestOptions.DEFAULT);
                    } catch (IOException e) {
                        log.error("bulk error:", e);
                    }
                } else {
                    getClient().bulkAsync(request, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
                        @Override
                        public void onResponse(BulkResponse bulkItemResponses) {

                        }

                        @Override
                        public void onFailure(Exception e) {
                            log.error("thread:{}, index error", currentThread().getName(), e);
                        }
                    });
                }
                bulkCount--;
            }

            log.info("{} exit.", currentThread().getName());
        } finally {
            latch.countDown();
        }
    }

    private RestHighLevelClient getClient() {
        if (client == null) {
            List<HttpHost> httpHosts = new ArrayList<>();
            for (String host : args.getHosts().split(",")) {
                httpHosts.add(new HttpHost(host, 9200, "http"));
            }

            client = new RestHighLevelClient(
                    RestClient.builder(httpHosts.toArray(new HttpHost[0])));
        }

        return client;
    }

}
