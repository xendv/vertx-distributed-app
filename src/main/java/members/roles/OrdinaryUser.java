package members.roles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Verticle;
import io.vertx.core.impl.JavaVerticleFactory;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ThreadLocalRandom;

public class OrdinaryUser extends AbstractVerticle {
    private final String name;

    public OrdinaryUser(long number) {
        this.name = "user#" + number;
    }

    @Override
    public void start() {
        subscribe();
    }

    private void subscribe() {
        final JsonObject message = new JsonObject().put("name", name);
        System.out.println(name + " wants to join the service");

        vertx.eventBus().consumer("gameservice.started", event -> vertx.eventBus().send("gameservice.join", message));

        vertx.setPeriodic(2000, timer ->
                vertx.sharedData().getClusterWideMap("activeClans", map ->
                        map.result().entries(clans -> {
                            if (clans.result().isEmpty()) System.out.println("No active clans :(");
                            else {
                                System.out.println("Active clans:");
                                clans.result().forEach((name, admin) ->
                                        System.out.println(name + " clan is active, admin - " + admin)
                                );
                            }
                        })
                )
        );
    }

    public static final class Factory extends JavaVerticleFactory {
        private long number;

        @Override
        public String prefix() {
            return "service";
        }

        @SuppressWarnings("ProhibitedExceptionDeclared")
        //@Override
        public Verticle createVerticle(String verticleName, ClassLoader classLoader) {
            return new OrdinaryUser(number++);
        }
    }

}
