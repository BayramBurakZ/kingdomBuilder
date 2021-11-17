package kingdomBuilder.network.internal;

@Response(topic = "WELCOME_TO_SERVER")
public record IAmResponse (
    int clientId,
    String clientName,
    int gameId
){}
