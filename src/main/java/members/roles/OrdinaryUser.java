package members.roles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.Clan;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class OrdinaryUser extends BaseRole {
    private String clanName;
    private String clanToJoin;
    static int counter = 1;

    public OrdinaryUser() {
        super("user#" + counter,"user");
        counter ++;
    }

    @Override
    public void start() {
        subscribe();
        chooseActiveClan("joinRandomClan");
    }

    private void subscribe() {
        System.out.println(name + " wants to join the service");

        ThreadLocalRandom random = ThreadLocalRandom.current();
        final int delay = random.nextInt(3000, 5000);
        vertx.eventBus().consumer("gameservice.started",
                event -> vertx.eventBus().send("gameservice.join", USER_INFO));
        long timerID = vertx.setTimer(delay, id ->
                vertx.eventBus().send("gameservice.join", USER_INFO));

        vertx.setPeriodic(delay, timer -> System.out.println(clanName));
        //vertx.setPeriodic(10000, timer -> getActiveClans("joinClan"));
    }

    public void chooseActiveClan(String action){
          vertx.sharedData().getClusterWideMap("activeClans", map ->
                map.result().entries(clans -> {
                    System.out.println("Active clans:");
                    if (clans.result().isEmpty()) System.out.println("No active clans :(");
                    else {
                        printActiveClans(getActiveClansJsonArray(clans.result()));

                        Set<String> keys = new HashSet<>();
                        for (Object o : clans.result().keySet()) {
                            keys.add((String) o);
                        }
                        List<String> clansList = new ArrayList<>(keys);
                        switch (action){
                            case "joinRandomClan": joinRandomClan(clansList);
                            break;
                            case "joinClan": {
                                if (clansList.contains(clanToJoin)) joinClan(this.clanToJoin);
                                else {
                                    System.out.println("Clan is not active, choosing another...");
                                    joinRandomClan(clansList);
                                }
                            }
                            break;
                            default: System.out.println("Wrong action");
                            break;
                        }
                    }
                })
        );
    }

    public void joinRandomClan(List<String> clans){
        ThreadLocalRandom random = ThreadLocalRandom.current();
        final int itemNumber = random.nextInt(clans.size());
        clanToJoin = clans.get(itemNumber);
        joinClan(clanToJoin);
    }

    public void joinClan(String clanName){
        vertx.eventBus().send("gameservice.clans.join", clanName);
    }
}
