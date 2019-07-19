# es-benchmark

This is an efficient ElasticSearch benchmark program using bulk API. 



# Usage

## get uber jar

```bash
mvn clean package
```

## run uber jar and see help

```bash
# java -jar es-benchmark-1.0-uber.jar -h

Usage: <main class> [options]
  Options:
    -f, --filename
      read data from file and send it as message, if not null, -message will 
      be ignored
    -h, --help

    -p, --parallelism
      how many thread to send data
      Default: 1
    -bulkCount
      how many bulks will send
      Default: 1
    -bulkSize
      how many request in one bulk
      Default: 500
    -bulkTimeout
      bulk request timeout, es default sets it to 30s, unit is second
      Default: 30
    -hosts
      host1,host2,host3,...(not include port, port is 9200, and cannot change 
      for now!!!)
      Default: 127.0.0.1
    -index
      index name
      Default: test
    -message
      message to send, if null, random message(length is -messageSize) will be 
      used 
    -messageSize
      length of message, if you specify -message, this parameter will be 
      ignored 
      Default: 100
    -sync
      send to es in sync mode or async mode
      Default: true

``` 

## run benchmark

1. Read data from file, if your data to send is large, this will be convenient:

```bash
java -jar es-benchmark-1.0-uber.jar  -hosts 192.168.9.1,192.168.9.2  -index benchmark-test -bulkCount 1000 -bulkSize 100 -p 16 -f data.txt
```

2. Or data to send is small, just specify in command line: 

```bash
java -jar es-benchmark-1.0-uber.jar  -hosts 192.168.9.1,192.168.9.2  -index benchmark-test -bulkCount 1000 -bulkSize 100 -p 16 -messageSize 100
```

3. Or you can just give a message length, random message will be produce automatically:

```bash
java -jar es-benchmark-1.0-uber.jar  -hosts 192.168.9.1,192.168.9.2  -index benchmark-test -bulkCount 1000 -bulkSize 100 -p 16 -f data.txt
```