package members.launchers;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import members.roles.Admin;

@SuppressWarnings("NotNullNullableValidation")
public final class AdminLauncher {
    public static void main(String[] args) {

        System.setProperty("hazelcast.ignoreXxeProtectionFailures", "true");
        int QUANTITY = 1;

        Vertx.clusteredVertx(
                new VertxOptions(),
                vertxResult -> {
                    final var vertx = vertxResult.result();
                    final DeploymentOptions optionsMember = new DeploymentOptions().setWorker(true).setInstances(1);
                    vertx.deployVerticle(Admin.class.getName(), optionsMember);
                }
        );

    }
}
