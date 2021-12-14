package app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.Clan;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Map;

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
                    final String role = user.getString("role");
                    System.out.println(name + " of role '" + role + "' joins the GS");

                    switch (role){
                        case "user": break;
                        case "admin":{
                            printActiveClans();
                        }
                        break;
                        case "moderator": break;
                        default: System.out.println("ERROR No such role: " + role);
                            break;
                    }
                }
        ).completionHandler(result -> {
            if (result.succeeded()) {
                startPromise.complete();
                return;
            }
            startPromise.fail(result.cause());
        });

        vertx.eventBus().<JsonObject>consumer(
                "gameservice.exit",
                event -> {
                    final JsonObject user = event.body();
                    final String name = user.getString("name");
                    final String role = user.getString("role");
                    System.out.println(name + " exits");
                    if (role.equals("admin")){
                        printActiveClans();
                    }
                }
        ).completionHandler(result -> {
            if (result.succeeded()) {
                startPromise.complete();
                return;
            }
            startPromise.fail(result.cause());
        });
    }

    private void printActiveClans(){
        vertx.sharedData().getClusterWideMap("activeClans", map ->
                map.result().entries(clans -> {
                    System.out.println("Active clans:");
                    if (clans.result().isEmpty()) System.out.println("No active clans :(");
                    else {
                       printActiveClansFromJsonArray(getActiveClansJsonArray(clans.result()));
                    }
                })
        );
    }

    protected void printActiveClansFromJsonArray(JsonArray jsonArray){
        for(int n = 0; n < jsonArray.size(); n++)
        {
            JsonObject jsonObject = jsonArray.getJsonObject(n);
            System.out.println(jsonObject.getString("name") + " clan is active, ClanInfo: "
                    + jsonArray);
        }
    }

    protected JsonArray getActiveClansJsonArray(Map<Object, Object> clans){
        JsonArray jsonArray = new JsonArray();
        clans.forEach((name, clanInfo) ->
                {
                    JsonObject jsonObject = (JsonObject)clanInfo;
                    jsonArray.add(jsonObject);
                }
        );
        return jsonArray;
    }
}
