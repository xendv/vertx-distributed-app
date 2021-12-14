package members.roles;

import common.Clan;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

public class Admin extends BaseRole {
    static int counter = 1;
    private final Clan clan;

    public Admin() {
        super("admin#" + counter,"admin");
        this.clan = new Clan("CLAN#" + counter, name);
        counter++;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        vertx.eventBus().send("gameservice.join", USER_INFO);
        subscribe();
        long timerID = vertx.setTimer(30000, id -> exit());
    }

    private void subscribe() {
        System.out.println(name + " wants to join the service");

        vertx.eventBus().consumer("gameservice.started",
                event -> vertx.eventBus().send("gameservice.join", USER_INFO));

        vertx.eventBus().<String>consumer("exit.admin", event ->
                {
                    System.out.println("Admin " + name + "had to exit");
                    final String adminToExit = event.body();
                    if (adminToExit.equals(name)) {
                        vertx.eventBus().send("gameservice.exit", USER_INFO);
                    }
                });

        final JsonObject clanInfo = new JsonObject().put("name", clan.getName())
                .put("admin", name).put("limit", clan.DEFAULT_LIMIT);
        vertx.sharedData().getClusterWideMap("activeClans", map ->
                map.result().put(clan.getName(), clanInfo));
    }

    @Override
    public void stop() {
        exit();
    }

    private void exit(){
        System.out.println(name + " exits");

        vertx.sharedData().getClusterWideMap("activeClans", map ->
                map.result().remove(clan.getName()));
        vertx.eventBus().send("gameservice.exit", USER_INFO);
    }
}
