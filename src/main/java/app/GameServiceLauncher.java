package app;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.internal.util.XmlUtil;
import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
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
