package app;

import com.hazelcast.config.Config;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.ConfigUtil;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

public class GameServiceLauncher {

    public static void main(String[] args) {
        // !!! Because of the "org.xml.sax.SAXNotRecognizedException:
        // Property 'http://javax.xml.XMLConstants/property/accessExternalSchema' is not recognized."
        System.setProperty("hazelcast.ignoreXxeProtectionFailures", "true");

        Config hazelcastConfig = ConfigUtil.loadConfig();
        hazelcastConfig.setClusterName("my-cluster-name");

        ClusterManager mgr = new HazelcastClusterManager();

        Vertx.clusteredVertx(
                new VertxOptions().setClusterManager(mgr),
                vertxResult -> {

                    final var options = new DeploymentOptions().setWorker(true);
                    vertxResult.result().deployVerticle(new GameService(), options);
                }
        );
    }

}
