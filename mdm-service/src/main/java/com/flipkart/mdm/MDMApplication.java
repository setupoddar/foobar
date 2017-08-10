package com.flipkart.mdm;

import com.flipkart.mdm.config.MDMConfiguration;
import com.flipkart.mdm.guice.MDMModule;
import com.google.inject.Stage;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.glassfish.jersey.media.multipart.MultiPartFeature;


public class MDMApplication extends Application<MDMConfiguration> {

    private GuiceBundle<MDMConfiguration> guiceBundle;

    public static void main(String[] args) throws Exception {
            new MDMApplication().run(args);
    }

    @Override
    public String getName() {
        return "FooBar";
    }

    @Override
    public void initialize(Bootstrap<MDMConfiguration> bootstrap) {

        guiceBundle = new GuiceBundle.Builder<MDMConfiguration>()
                .setConfigClass(MDMConfiguration.class)
                .addModule(new MDMModule(bootstrap))
                .enableAutoConfig(MDMApplication.class.getPackage().getName())
                .build(Stage.DEVELOPMENT);

        bootstrap.addBundle(guiceBundle);

        bootstrap.addBundle(new SwaggerBundle<MDMConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(MDMConfiguration configuration) {
                return configuration.getSwaggerBundleConfiguration();
            }
        });
        bootstrap.addBundle(new AssetsBundle("/assets", "/", "home.html"));
    }

    @Override
    public void run(MDMConfiguration configuration, Environment environment) throws Exception {;
        environment.jersey().register(MultiPartFeature.class);
        environment.jersey().setUrlPattern("/api/*");

    }
}
