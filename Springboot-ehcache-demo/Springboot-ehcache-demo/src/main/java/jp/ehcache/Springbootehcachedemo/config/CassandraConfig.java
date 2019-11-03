package jp.ehcache.Springbootehcachedemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.QueryOptions;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.policies.ConstantSpeculativeExecutionPolicy;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.SpeculativeExecutionPolicy;

@Configuration
public class CassandraConfig {
	
	@Value("${cassandra-config.contact-points}")
	private String[] contactPoints;

	@Value("${cassandra-config.port}")
	private Integer port;

	@Value("${cassandra-config.keyspace-name}")
	private String keyspaceName;

	@Value("${cassandra-config.username}")
	private String userName;

	@Value("${cassandra-config.password}")
	private String password;

	@Value("${cassandra-config.datacenter}")
	private String datacenter;

	private Cluster cluster;
	private Session session;
	
	@Bean(name = "cassandraSessionBean")
	public Session getSession() {
		connect();
		return session;
	}

	public void connect() {

		final PoolingOptions poolingOptions = new PoolingOptions();
		poolingOptions.setMaxRequestsPerConnection(HostDistance.LOCAL, 32768);
		poolingOptions.setMaxRequestsPerConnection(HostDistance.REMOTE, 2000);
		poolingOptions.setNewConnectionThreshold(HostDistance.LOCAL, 30000);
		poolingOptions.setNewConnectionThreshold(HostDistance.REMOTE, 400);
		poolingOptions.setConnectionsPerHost(HostDistance.LOCAL, 2, 4); 
		poolingOptions.setPoolTimeoutMillis(0);

		SpeculativeExecutionPolicy mySpeculativeExecutionPolicy = new ConstantSpeculativeExecutionPolicy(50, 2);

		cluster = Cluster.builder().addContactPoints(contactPoints).withPort(port).withoutJMXReporting()
				.withLoadBalancingPolicy(DCAwareRoundRobinPolicy.builder().withLocalDc(datacenter).build())
				.withQueryOptions(new QueryOptions().setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM))
				.withAuthProvider(new PlainTextAuthProvider(userName, password)).withPoolingOptions(poolingOptions)
				.withSpeculativeExecutionPolicy(mySpeculativeExecutionPolicy).build();

		// to print queries which are taking more than 2 ms
		//cluster.register(QueryLogger.builder().withConstantThreshold(slowquerythreshold).build());

		Metadata metadata = cluster.getMetadata();
		//System.out.println("Cluster name: {}", metadata.getClusterName());
		//System.out.println("PROTOCOL VERSION {}", cluster.getConfiguration().getProtocolOptions().getProtocolVersion());

	//	for (Host host : metadata.getAllHosts()) {
			//log.info("Datacenter: {}, Host: {}, Rack: {}", host.getDatacenter(), host.getAddress(), host.getRack());
	//	}
		session = cluster.connect(keyspaceName);
	}
}

