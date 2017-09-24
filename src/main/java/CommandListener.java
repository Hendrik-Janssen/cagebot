import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class CommandListener {

    private Map<String, CommandInterface> defaultCommands = new HashMap<>();
    private Map<String, String> customCommands = new HashMap<>();

    public CommandListener() {
        defaultCommands.put("!cmd", new CommandInterface() {
            @Override
            public void execute(MessageReceivedEvent event) {
                String message = "**Default commands:** ";
                for (String cmd : defaultCommands.keySet()) {
                    message = message.concat(cmd).concat(", ");
                }
                message = message.concat("\n\n**Custom commands:** ");
                for (String cmd : customCommands.keySet()) {
                    message = message.concat(cmd).concat(", ");
                }
                if(!customCommands.keySet().isEmpty()) {
                    BotUtils.sendMessage(event.getChannel(), message.substring(0, message.length() - 2));
                } else {
                    BotUtils.sendMessage(event.getChannel(), message.concat("There are no custom commands."));
                }
            }
        });

        defaultCommands.put("!ping", new CommandInterface() {
            @Override
            public void execute(MessageReceivedEvent event) {
                BotUtils.sendMessage(event.getChannel(), event.getAuthor() + " Pong!");
            }
        });

        defaultCommands.put("!addcmd", new CommandInterface() {
            @Override
            public void execute(MessageReceivedEvent event) {
                String[] parsedCommand = event.getMessage().getContent().split(" ");
                if (parsedCommand.length < 3) {
                    BotUtils.sendMessage(event.getChannel(), event.getAuthor() + "\n**Error**: !addcmd takes 2 arguments - e.g. !addcmd !suckmydick Suck my dick.");
                } else if (!parsedCommand[1].startsWith("!")) {
                    BotUtils.sendMessage(event.getChannel(), event.getAuthor() + "\n**Error**: 1st argument must start with '!'");
                } else if (defaultCommands.containsKey(parsedCommand[1])) {
                    BotUtils.sendMessage(event.getChannel(), event.getAuthor() + "\n**Error**: cannot override default command");
                } else {
                    if (!customCommands.containsKey(parsedCommand[1])) {
                        customCommands.put(parsedCommand[1], concatinate(parsedCommand));
                        BotUtils.sendMessage(event.getChannel(), "Command '" + parsedCommand[1] + "' has been successfully added!");
                    } else {
                        customCommands.remove(parsedCommand[1]);
                        customCommands.put(parsedCommand[1], concatinate(parsedCommand));
                        BotUtils.sendMessage(event.getChannel(), "Command '" + parsedCommand[1] + "' has been successfully overridden!");
                    }
                }
            }
        });

        defaultCommands.put("!delcmd", new CommandInterface() {
            @Override
            public void execute(MessageReceivedEvent event) {
                if(!(event.getAuthor().getName().equals("NicolasCage") || event.getAuthor().getName().equals("Kildohr") || event.getAuthor().getName().equals("saltymeat"))) {
                    BotUtils.sendMessage(event.getChannel(), event.getAuthor() + "\n**Error**: you have no permission for this action!");
                }
                String[] parsedCommand = event.getMessage().getContent().split(" ");
                if(parsedCommand.length != 2) {
                    BotUtils.sendMessage(event.getChannel(), event.getAuthor() + "\n**Error**: !delcmd takes 1 argument - e.g. !delcmd !suckmydick");
                } else if (!parsedCommand[1].startsWith("!")) {
                    BotUtils.sendMessage(event.getChannel(), event.getAuthor() + "\n**Error**: 1st argument must start with '!'");
                } else if (!customCommands.containsKey(parsedCommand[1])) {
                    BotUtils.sendMessage(event.getChannel(), event.getAuthor() + "\n**Error**: command does not exist.");
                } else {
                    customCommands.remove(parsedCommand[1]);
                    BotUtils.sendMessage(event.getChannel(), "Commmand '" + parsedCommand[1] + "' has been successfully removed!");
                }
            }
        });

        defaultCommands.put("!about", new CommandInterface() {
            @Override
            public void execute(MessageReceivedEvent event) {
                BotUtils.sendMessage(event.getChannel(), "**Author:** NicolasCage\n\n**Description:** The bot is still under development, please don't abuse :open_mouth: also don't waste your time adding commands yet cuz they aren't stored anywhere which means they get lost once I restart, imma get to that tomorrow.");
            }
        });
    }

    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        String command = event.getMessage().getContent().split(" ")[0];

        if (command.charAt(0) != '!')
            return;

        if (defaultCommands.containsKey(command)) { // handle default commands
            defaultCommands.get(command).execute(event);
        } else if (customCommands.containsKey(command)) { // handle custom commands
            BotUtils.sendMessage(event.getChannel(), customCommands.get(command));
        } else {
            BotUtils.sendMessage(event.getChannel(), event.getAuthor() + "\nUnknown command, use !cmd for a list of available commands.");
        }
    }

    private String concatinate(String[] split) {
        String result = "";
        for (int i = 2; i < split.length; i++) {
            result = result.concat(split[i]).concat(" ");
        }
        return result;
    }

    private interface CommandInterface {
        void execute(MessageReceivedEvent event);
    }
}
