package members.launchers;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import members.roles.OrdinaryUser;

@SuppressWarnings("NotNullNullableValidation")
public final class OrdinaryUsersLauncher {
    public static void main(String[] args) {

        System.setProperty("hazelcast.ignoreXxeProtectionFailures", "true");
        int QUANTITY = 2;

        Vertx.clusteredVertx(
                new VertxOptions(),
                vertxResult -> {
                    final var vertx = vertxResult.result();
                    final DeploymentOptions optionsMember = new DeploymentOptions().setWorker(true).setInstances(QUANTITY);
                    vertx.deployVerticle(OrdinaryUser.class.getName(), optionsMember);
                }
        );
    }
}
