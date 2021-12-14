package app;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

public class GameService extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) {
        vertx.eventBus().publish("gameservice.started", null);
        System.out.println("Game service Started");
        vertx.eventBus().<JsonObject>consumer(
                "gameservice.join",
                event -> {
                    final JsonObject user = event.body();
                    final String name = user.getString("name");
                    System.out.println(name + " joins the service");
                    vertx.eventBus().send("Member was connected to GS as ", name);
                }
        ).completionHandler(result -> {
            //vertx.eventBus().publish("gameservice.started", null);

            if (result.succeeded()) {
                startPromise.complete();
                return;
            }
            startPromise.fail(result.cause());
        });

        vertx.eventBus().<JsonObject>consumer(
                "gameservice.join.admin",
                event -> {
                    final JsonObject admin = event.body();
                    final String name = admin.getString("name");
                    System.out.println(name + " joins the service");
                    vertx.sharedData().getClusterWideMap("activeClans", map ->
                            {
                                map.result().entries(clans -> {
                                    System.out.println("Active clans:");
                                    clans.result().forEach((clan, adminName) ->
                                            System.out.println(clan + " clan is active, admin - " + admin + "(admin entered server)")
                                    );
                                });
                            }

                    );
                }
        ).completionHandler(result -> {
            if (result.succeeded()) {
                startPromise.complete();
                return;
            }
            startPromise.fail(result.cause());
        });

        vertx.eventBus().<JsonObject>consumer(
                "gameservice.exit.admin",
                event -> {
                    final JsonObject admin = event.body();
                    final String name = admin.getString("name");
                    System.out.println("Admin " + name + " exits");
                    vertx.sharedData().getClusterWideMap("activeClans", map ->
                            {
                                map.result().entries(clans -> {
                                    if (clans.result().isEmpty()) System.out.println("No active clans :(");
                                    else {
                                        System.out.println("Active clans:");
                                        clans.result().forEach((clan, adminName) ->
                                                System.out.println(clan + " clan is active, admin - " + admin)
                                        );
                                    }
                                });
                            }
                    );
                }
        ).completionHandler(result -> {
            if (result.succeeded()) {
                startPromise.complete();
                return;
            }
            startPromise.fail(result.cause());
        });

    }
}
