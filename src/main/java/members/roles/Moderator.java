package members.roles;

import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

public class Moderator extends BaseRole {
    static int counter = 1;

    public Moderator() {
        super("moderator#" + counter,"moderator");
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

        vertx.eventBus().consumer("gameservice.considerRequest",
                event -> considerRequest());
    }

    private void considerRequest(){

    }

    private void approveRequest(){

    }

    private void declineRequest(){

    }

    @Override
    public void stop() {
        exit();
    }

    private void exit(){
        System.out.println(name + " exits");
    }
}
