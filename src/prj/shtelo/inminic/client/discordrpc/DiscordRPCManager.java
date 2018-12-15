package prj.shtelo.inminic.client.discordrpc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordRPCManager {
    private String applicationId, steamId;

    private DiscordRPC lib;
    private DiscordEventHandlers handlers;
    private DiscordRichPresence presence;

    private String state = "고정";
    private String largeImageKey = "logo";
    private String playerInformation;
    private String smallImageKey = "logo";
    private String mapInformation;

    public DiscordRPCManager(String applicationId, String steamId) {
        this.applicationId = applicationId;
        this.steamId = steamId;

        init();
    }

    private void init() {
        lib = DiscordRPC.INSTANCE;
        handlers = new DiscordEventHandlers();
        lib.Discord_Initialize(applicationId, handlers, true, steamId);

        presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        lib.Discord_UpdatePresence(presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                lib.Discord_RunCallbacks();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }

    public void update() {
        presence.state = state;
        presence.largeImageKey = largeImageKey;
        presence.largeImageText = playerInformation;
        presence.smallImageKey = smallImageKey;
        presence.smallImageText = mapInformation;

        lib.Discord_UpdatePresence(presence);
    }

    public void shutdown() {
        lib.Discord_Shutdown();
    }

    public void setPlayerInformation(String playerInformation) {
        this.playerInformation = playerInformation;
    }

    public void setMapInformation(String mapInformation) {
        this.mapInformation = mapInformation;
    }

    public void setSmallImageKey(String smallImageKey) {
        this.smallImageKey = smallImageKey;
    }

    public void setState(String state) {
        this.state = state;
    }
}
