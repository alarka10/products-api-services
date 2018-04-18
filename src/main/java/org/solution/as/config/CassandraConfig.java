package org.solution.as.config;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.solution.as.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.cassandra.config.DataCenterReplication;
import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.CassandraOperations;

/**
 * This class configures Cassandra by creating a table in a keyspace and adding values to it.
 *
 */

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

	@Autowired
	private CassandraOperations cassandraTemplate;
	
	@Bean
	@Override
	public CassandraCqlClusterFactoryBean cluster() {
		CassandraCqlClusterFactoryBean bean = new CassandraCqlClusterFactoryBean();
		bean.setKeyspaceCreations(getKeyspaceCreations());
		bean.setContactPoints(Constants.NODE);
		return bean;
	}
	
	@Override
	public String getKeyspaceName() {
		return Constants.KEYSPACE;
	}
	
	@Override
	public String[] getEntityBasePackages() {
		return new String[]{"org.solution"};
	}
	
	protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
		List<CreateKeyspaceSpecification> keySpaceSpecifications = new ArrayList<CreateKeyspaceSpecification>();
		keySpaceSpecifications.add(getKeyspaceSpecification());
		return keySpaceSpecifications;
	}
	
	@SuppressWarnings("static-access")
	private CreateKeyspaceSpecification getKeyspaceSpecification() {
		CreateKeyspaceSpecification keySpace = new CreateKeyspaceSpecification();
		DataCenterReplication dataCenter = new DataCenterReplication("datacenter1", 3L);
		keySpace.name(Constants.KEYSPACE);
		keySpace.ifNotExists(true).createKeyspace().withNetworkReplication(dataCenter);
		return keySpace;
	}
	/**
	 * This method creates a table and adds data to it.
	 * @throws FileNotFoundException
	 */
	@PostConstruct
	public void createAndAddTable() throws FileNotFoundException {
		
		// Schema
		cassandraTemplate.execute("CREATE TABLE IF NOT EXISTS price(id bigint PRIMARY KEY, value decimal, currency_code text);");
		
		// Data
		cassandraTemplate.execute("INSERT INTO price (id, value, currency_code) VALUES (15117729, 25.25, 'USD');");
		cassandraTemplate.execute("INSERT INTO price (id, value, currency_code) VALUES (16483589, 5000.50, 'USD');");
		cassandraTemplate.execute("INSERT INTO price (id, value, currency_code) VALUES (16696652, 0.75, 'USD');");
		cassandraTemplate.execute("INSERT INTO price (id, value, currency_code) VALUES (16752456, 99, 'USD');");
		cassandraTemplate.execute("INSERT INTO price (id, value, currency_code) VALUES (15643793, 99.99, 'USD');");
		cassandraTemplate.execute("INSERT INTO price (id, value, currency_code) VALUES (13860428, 16.65, 'USD');");
				
	}
	
	/**
	 * This method drops a table once the application is closed.
	 * @throws FileNotFoundException
	 */
	@PreDestroy
	public void dropTable() throws FileNotFoundException {
		cassandraTemplate.execute("DROP TABLE IF EXISTS price;");		
	}

}
