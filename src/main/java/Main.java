import sx.blah.discord.api.IDiscordClient;

public class Main {

    public static final String TOKEN = "MzYwNTYwNDY3ODg3NzgzOTM2.DKXVoA.FlOdZhhxfZ6sQOypo87P1Razrb0";

    public static void main(String[] args) {

        IDiscordClient client = BotUtils.getBuiltDiscordClient(TOKEN);
        client.getDispatcher().registerListener(new CommandListener());
        client.login();
    }
}
