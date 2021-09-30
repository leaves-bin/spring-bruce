###CAP
分布式系统中， Consistency（一致性）、 Availability（可用性）、Partition tolerance（分区容错性），三者不可得兼。

C（一致性）: 分布式系统中同一时刻的数据备份是否一样，所有节点的数据状态是否一样

A（可用性）: 集群中一部分节点故障是否还能响应客户端请求

P（分区容错性）: 节点故障引起的分区后数据状态不一致是否能够容忍，

eureka 在CAP中选择的AP, eureka集群节点是互相注册的，部分节点故障后，其余节点还可以继续，没有选举过程导致的不可用。zookeeper 节点故障后会有选举过程导致在该过程中服务是不可用的，所以zookeeper保证的是C

###服务注册
eureka server 中维护着一个 ConcurrentHashMap<String, Map<String, Lease<InstanceInfo>>> registry
= new ConcurrentHashMap<String, Map<String, Lease<InstanceInfo>>>(); ，map的第一层key是注册的应用名称（spring.application.name），第二层key是实例id，属性为实例的具体注册信息。

