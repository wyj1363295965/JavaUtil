package com.wyj.javautil.es;

/**
 * @工程名称：es-demo
 * @程序包名：com.zt.report.config
 * @程序类名：ElasticsearchConfiguration.java
 * @创建日期：2018年2月6日
 */

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Configuration
@EnableScheduling
@Component
public class ElasticsearchConfiguration implements FactoryBean<TransportClient>, InitializingBean, DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfiguration.class);
    // 由于项目从2.1.1配置的升级到 5.6.2版本 原配置文件不想动还是指定原来配置参数
    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    @Value("${spring.data.es.max.result.window}")
    private Integer maxResultWindow;

    private TransportClient client;

    private static RestClient restClient;

    @Override
    public void destroy() throws Exception {
        try {
            logger.info("Closing elasticSearch client");
            if (client != null) {
                client.close();
            }
        } catch (final Exception e) {
            logger.error("Error closing ElasticSearch client: ", e);
        }
    }

    @Override
    public TransportClient getObject() throws Exception {
        return client;
    }

    @Override
    public Class<TransportClient> getObjectType() {
        return TransportClient.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        buildClient();
    }

    protected void buildClient() {
        try {
            PreBuiltTransportClient preBuiltTransportClient = new PreBuiltTransportClient(settings());
            if (!"".equals(clusterNodes)) {
                for (String nodes : clusterNodes.split(",")) {
                    String InetSocket[] = nodes.split(":");
                    String Address = InetSocket[0];
                    Integer port = Integer.valueOf(InetSocket[1]);
                    preBuiltTransportClient.addTransportAddress(new TransportAddress(InetAddress.getByName(Address),
                            port));
                }
                client = preBuiltTransportClient;
            }
        } catch (UnknownHostException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 初始化默认的client
     */
    private Settings settings() {
        Settings settings = Settings.builder().put("cluster.name", clusterName).put("client.transport.sniff", true).build();
        client = new PreBuiltTransportClient(settings);
        return settings;
    }

    /**
     * @函数名称：initResult
     * @创建日期：2018年4月24日
     * @功能说明：由于es中bro数据的索引实时变化，不能一次性设置max_result_window
     * @参数说明：
     * @返回说明：void
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void initResult() {
        logger.info("time nows is : " + System.currentTimeMillis());
        //max_result_window 这个值的设置运维会在操作系统的定时任务中去做，这里不用处理了 -Tiger 2019-7-25
		/*
		client.admin().indices().prepareUpdateSettings("bro*")
				.setSettings(Settings.builder().put("max_result_window", maxResultWindow)) // 将副本数更新为2
				.execute().actionGet();
		*/
    }

    // @Bean
    // public RestClient getRestClientObject() {
    // List<HttpHost> hosts = new ArrayList<HttpHost>();
    // if (!"".equals(clusterNodes)) {
    // for (String nodes : clusterNodes.split(",")) {
    // String InetSocket[] = nodes.split(":");
    // String Address = InetSocket[0];
    // Integer port = Integer.valueOf(InetSocket[1]);
    // hosts.add(new HttpHost(Address, port, "http"));
    // }
    // }
    // HttpHost[] strings = new HttpHost[hosts.size()];
    // hosts.toArray(strings);
    //
    // RestClient restClient = RestClient.builder(strings).build();
    // return restClient;
    // }

    // public void getRestClient() {
    // List<HttpHost> hosts = new ArrayList<HttpHost>();
    // if (!"".equals(clusterNodes)) {
    // for (String nodes : clusterNodes.split(",")) {
    // String InetSocket[] = nodes.split(":");
    // String Address = InetSocket[0];
    // Integer port = Integer.valueOf(InetSocket[1]);
    // hosts.add(new HttpHost(Address, port, "http"));
    // }
    // }
    // HttpHost[] strings = new HttpHost[hosts.size()];
    // hosts.toArray(strings);
    // final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    // credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic",
    // "changeme"));
    //
    // restClient = RestClient.builder(strings)
    // .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
    // @Override
    // public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
    // return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
    // }
    // }).build();
    // }

    // @Before
    // public void getRest(){
    // restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();
    // }
}
