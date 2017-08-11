package com.flipkart.mdm.guice;

import com.flipkart.mdm.client.MHttpClient;
import com.flipkart.mdm.client.SHttpClient;
import com.flipkart.mdm.client.VHttpClient;
import com.flipkart.mdm.client.ZHttpClient;
import com.flipkart.mdm.config.MDMConfiguration;
import com.flipkart.mdm.model.*;
import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.hibernate.SessionFactory;

import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

@SuppressWarnings("unused")
public class MDMModule extends AbstractModule {

    private HibernateBundle hibernateBundle = new HibernateBundle<MDMConfiguration>(
            User.class, Images.class, Role.class, Task.class, Trend.class, QCFSN.class, CuratedFSN.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(MDMConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public MDMModule(Bootstrap<MDMConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    protected void configure() {
        bind(VHttpClient.class);
        bind(SHttpClient.class);
        bind(ZHttpClient.class);
        bind(MHttpClient.class);
        bind(Gson.class).in(Singleton.class);
    }

    @Singleton
    @Provides
    public SessionFactory getSessionFactory() {
        return hibernateBundle.getSessionFactory();
    }


    @Provides
    Client provideClient() throws Exception {
        ClientConfig cc = new ClientConfig();
        cc.property(ClientProperties.ASYNC_THREADPOOL_SIZE, 5);
        cc.property(ClientProperties.CONNECT_TIMEOUT, 5 * 1000);
        cc.property(ClientProperties.READ_TIMEOUT, 5 * 1000);
        return ClientBuilder.newClient(cc);
    }
}
