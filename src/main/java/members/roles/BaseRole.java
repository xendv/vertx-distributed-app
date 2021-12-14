package members.roles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.Clan;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Map;

public class BaseRole extends AbstractVerticle {
    protected String name;
    protected String role;
    protected JsonObject USER_INFO;

    protected BaseRole(){}

    protected BaseRole(String name, String role){
        this.name = name;
        this.role = role;
        this.USER_INFO = new JsonObject().put("name", name).put("role", role);
    }

    protected void printActiveClans(JsonArray jsonArray){
        for(int n = 0; n < jsonArray.size(); n++)
        {
            JsonObject jsonObject = jsonArray.getJsonObject(n);
            System.out.println(jsonObject.getString("name") + " clan is active, ClanInfo: "
                    + jsonObject);
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

    /*
    protected void printActiveClans0(Map<Object, Object> clans){
        clans.forEach((name, clanInfo) ->
                {
                    JsonObject jsonObject = new JsonObject(clanInfo.toString());
                    Clan tempClan = jsonObject.mapTo(Clan.class);
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        System.out.println(tempClan.getName() + " clan is active, ClanInfo: "
                                + mapper.writeValueAsString(tempClan));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
        );
    }*/
}
