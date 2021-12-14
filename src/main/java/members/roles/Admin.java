package members.roles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

public class Admin extends AbstractVerticle {
    static int counter = 1;
    private final String name;
    private final String clan;

    public Admin() {
        this.name = "admin#" + counter;
        this.clan = "CLAN#" + counter;
        counter ++;
    }

    public Admin(long number) {
        this.name = "admin#" + number;
        this.clan = "CLAN#" + number;
        counter ++;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        subscribe();
        long timerID = vertx.setTimer(3000, id -> {
            exit();
            //vertx.eventBus().send("gameservice.exit.admin", name);
        });
    }

    private void subscribe() {
        final JsonObject message = new JsonObject().put("name", name);
        System.out.println(name + " wants to join the service");

        vertx.eventBus().consumer("gameservice.started", event -> vertx.eventBus().send("gameservice.join", message));
        vertx.eventBus().send("gameservice.join.admin", message);

        vertx.eventBus().<String>consumer("exit.admin", event ->
                {
                    System.out.println("Admin " + name + "wants to exit");
                    final String adminToExit = event.body();
                    if (adminToExit.equals(name)) {
                        vertx.eventBus().send("gameservice.exit.admin", message);
                    }
                });

        vertx.sharedData().getClusterWideMap("activeClans", map ->
                map.result().put(clan, name));
    }

    @Override
    public void stop() {
        exit();
    }

    private void exit(){
        final JsonObject message = new JsonObject().put("name", name);
        System.out.println(name + " exits");

        vertx.sharedData().getClusterWideMap("activeClans", map ->
                map.result().remove(clan));
        vertx.eventBus().send("gameservice.exit.admin", message);
    }
}
