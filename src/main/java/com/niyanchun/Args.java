package com.niyanchun;

import com.beust.jcommander.Parameter;
import lombok.Data;

/**
 * @description:
 * @author: NiYanchun
 * @version: 1.0
 * @create: 2019-07-18
 **/
@Data
public class Args {
    @Parameter(names = {"-h", "--help"}, help = true)
    private boolean help;

    @Parameter(names = "-hosts",
            description = "host1,host2,host3,...(not include port, port is 9200, and cannot change for now!!!)")
    private String hosts = "127.0.0.1";

    @Parameter(names = {"-p", "--parallelism"}, description = "how many thread to send data")
    private Integer parallelism = 1;

    @Parameter(names = "-sync", description = "send to es in sync mode or async mode")
    private boolean sync = true;

    @Parameter(names = "-index", description = "index name")
    private String index = "test";

    @Parameter(names = "-bulkCount", description = "how many bulks will send")
    private Integer bulkCount = 1;

    @Parameter(names = "-bulkSize", description = "how many request in one bulk")
    private Integer bulkSize = 500;

    @Parameter(names = "-message",
            description = "message to send, if null, random message(length is -messageSize) will be used")
    private String message;

    @Parameter(names = "-messageSize",
            description = "length of message, if you specify -message, this parameter will be ignored")
    private Integer messageSize = 100;

    @Parameter(names = {"-f", "--filename"},
            description = "read data from file and send it as message, if not null, -message will be ignored")
    private String filename;

    @Parameter(names = "-bulkTimeout", description = "bulk request timeout, es default sets it to 30s, unit is second")
    private int bulkTimeout = 30;
}
