package members.launchers;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import members.roles.Moderator;

@SuppressWarnings("NotNullNullableValidation")
public final class ModeratorsLauncher {
    public static void main(String[] args) {
        System.setProperty("hazelcast.ignoreXxeProtectionFailures", "true");

        Vertx.clusteredVertx(
                new VertxOptions(),
                vertxResult -> {
                    final var vertx = vertxResult.result();
                    final DeploymentOptions optionsMember = new DeploymentOptions().setWorker(true).setInstances(1);
                    vertx.deployVerticle(
                            Moderator.class.getName(),
                            optionsMember,
                            res -> System.out.println("Moderators deploy result: " + res.succeeded())
                    );
                }
        );
    }
}
