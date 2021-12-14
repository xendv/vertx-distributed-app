package members.roles;

import common.Clan;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class Moderator extends BaseRole {
    static int counter = 1;
    private String clanName;

    public Moderator() {
        super("moderator#" + counter,"moderator");
        /*vertx.sharedData().getClusterWideMap("activeClans", map ->
                map.result().entries(clans -> {
                    this.clanName = "CLAN#" +
                            getActiveClansJsonArray(clans.result()).getJsonObject(counter);
                }));*/
        this.clanName = "CLAN#" + counter;
        counter ++;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        final JsonObject message = new JsonObject().put("name", name).put("role", "moderator");
        subscribe();
        vertx.eventBus().send("gameservice.join", message);
    }

    private void subscribe() {
        System.out.println(name + " wants to join the service");

        vertx.eventBus().consumer("gameservice.started",
                event -> vertx.eventBus().send("gameservice.join", USER_INFO));

        vertx.eventBus().<JsonObject>consumer("gameservice.request.join",
                event -> {
                    final JsonObject user = event.body();
                    if (user.getString("clanName").equals(clanName)){
                        considerRequest(user.getString("user"));
                    }
                });
    }

    private void considerRequest(String userName){
        vertx.sharedData().getClusterWideMap("activeClans", map ->
                map.result().entries(clans -> {
                    JsonArray activeClans = getActiveClansJsonArray(clans.result());
                    for(int n = 0; n < activeClans.size(); n++){
                        JsonObject clan = activeClans.getJsonObject(n);
                        if (clan.getString("name").equals(clanName))
                        {
                            int tempMemCount = clan.getInteger("membersCount");
                            if (tempMemCount < clan.getInteger("limit")){
                                ThreadLocalRandom random = ThreadLocalRandom.current();
                                final int guess = random.nextInt(1, 11);
                                if (guess < 4) {
                                    declineRequest(userName, "Because.");
                                    System.out.println("Request on "+ clanName + " declined");
                                }
                                else {
                                    tempMemCount++;
                                    JsonObject clanUpdated = clan.put("membersCount", tempMemCount);
                                    map.result().put(clanName,clanUpdated);
                                    map.result().entries(clans2 -> printActiveClans(getActiveClansJsonArray(clans2.result())));
                                    System.out.println("Request on "+ clanName + " approved");
                                    //System.out.println(map.result().entries().result().get(clanName));
                                    approveRequest(userName);
                                }
                            }
                            else{
                                declineRequest(userName, "Clan is full");
                            }
                        }
                    }
                }));
    }

    private void approveRequest(String userName){

        vertx.eventBus().send("gameservice.request.approved", null);
    }

    private void declineRequest(String userName, String reason){
        vertx.eventBus().send("gameservice.request.declined", reason);
    }

    @Override
    public void stop() {
        exit();
    }

    private void exit(){
        System.out.println(name + " exits");
    }
}
