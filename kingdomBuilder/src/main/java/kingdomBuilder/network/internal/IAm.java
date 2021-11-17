package kingdomBuilder.network.internal;

@Message(format = "iam #{name}", response = IAmResponse.class)
public record IAm(String name) {}


