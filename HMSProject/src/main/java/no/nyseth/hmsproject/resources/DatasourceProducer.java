/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package no.nyseth.hmsproject.resources;

import javax.annotation.Resource;
import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Singleton;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.sql.DataSource;
import javax.ws.rs.Produces;
import static no.nyseth.hmsproject.resources.DatasourceProducer.JNDI_NAME;

/**
 *
 * @author nyseth
 */
@Singleton
@DataSourceDefinition(
        name = JNDI_NAME,
        className = "org.postgresql.xa.PGXADataSource",
        serverName = "168.63.55.26",  // set the property
        portNumber = 5432,        // set the property
        databaseName = "hmsdb",    // set the property
        user = "hmsprojectuser",
        password = "SterktPassord303911",
        minPoolSize = 10,
        maxPoolSize = 500)
/**@Singleton
@DataSourceDefinition(
    name = JNDI_NAME,
    className = "org.h2.jdbcx.JdbcDataSource",
    url = "jdbc:h2:~/hmsdb.db")*/
public class DatasourceProducer {
    public static final String JNDI_NAME =  "java:app/jdbc/default";

    @Resource(lookup=JNDI_NAME)
    DataSource ds;

    @javax.enterprise.inject.Produces
    public DataSource getDatasource() {
        return ds;
    }
}