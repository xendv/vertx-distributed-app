package common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Clan {
    @JsonIgnore
    public final int DEFAULT_LIMIT = 1;
    @JsonIgnore
    public final int DEFAULT_MODS_LIMIT = 2;

    @NotNull
    private String name;
    @NotNull
    private String admin;
    @Nullable
    private Integer limit = DEFAULT_LIMIT;
    @Nullable
    private Integer modsLimit = DEFAULT_LIMIT;
    @JsonProperty(value = "membersCount")
    private int membersCount = 0;

    public Clan(@NotNull String name, @NotNull String admin){
        this.name = name;
        this.admin = admin;
    }

    @JsonCreator
    public Clan(@JsonProperty(value = "name", required = true) @NotNull String name,
                @JsonProperty(value = "admin", required = true) @NotNull String admin,
                @JsonProperty(value = "limit") @Nullable Integer limit,
                @JsonProperty(value = "modsLimit") @Nullable Integer modsLimit,
                @JsonProperty(value = "membersCount", required = true) Integer membersCount){
        this.name = name;
        if (limit != null) this.limit = limit;
        if (modsLimit != null) this.modsLimit = modsLimit;
        this.admin = admin;
        if (membersCount != null) this.membersCount = membersCount;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getName(){
        return name;
    }

    public void setLimit(@Nullable Integer limit) {
        this.limit = limit;
    }

    public @Nullable Integer getLimit(){
        return limit;
    }

    public void setAdmin(@NotNull String admin){
        this.admin = admin;
    }

    public @NotNull String getAdmin(){
        return this.admin;
    }

    public void setMembersCount(int membersCount) {
        this.membersCount = membersCount;
    }

    public Integer getMembersCount(){
        return this.membersCount;
    }
}
