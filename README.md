# es-benchmark

This is an efficient ElasticSearch benchmark program using bulk API. 

# Usage

```sh
Usage: <main class> [options]
  Options:
    -h, --help

    -bulkCount
      how many bulks will send
      Default: 1
    -bulkSize
      how many request in one bulk
      Default: 500
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
    -p
      how many thread to send data
      Default: 1
    -sync
      send to es in sync mode or async mode
      Default: true
``` 
