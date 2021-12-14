package members.roles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

public class OrdinaryUser extends AbstractVerticle {
    private final String name;
    private String clanName;
    static int counter = 1;

    public OrdinaryUser(long number) {
        this.name = "user#" + number;
        counter ++;
    }

    public OrdinaryUser() {
        this.name = "user#" + counter;
        counter ++;
    }

    @Override
    public void start() {
        subscribe();
    }

    private void subscribe() {
        final JsonObject message = new JsonObject().put("name", name);
        System.out.println(name + " wants to join the service");

        vertx.eventBus().consumer("gameservice.started", event -> vertx.eventBus().send("gameservice.join", message));
        vertx.eventBus().send("gameservice.join", message);

        vertx.setPeriodic(10000, timer ->
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
}
