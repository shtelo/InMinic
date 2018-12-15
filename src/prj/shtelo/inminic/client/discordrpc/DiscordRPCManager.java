package prj.shtelo.inminic.client.discordrpc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordRPCManager {
    private String applicationId, steamId;

    private DiscordRPC lib;
    private DiscordEventHandlers handlers;
    private DiscordRichPresence presence;

    private String details;
    private String state;
    private String largeImageKey;
    private String playerInformation;
    private String mapInformation;
    private String smallImageText;

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

        largeImageKey = "logo";
        mapInformation = "logo";

        details = "싱글 플레이";
        state = "로비에서 대기중";
        playerInformation = "플레이어 정보";
        mapInformation = "맵 정보";
    }

    public void update() {
        presence.details = details;
        presence.state = state;
        presence.largeImageKey = largeImageKey;
        presence.largeImageText = playerInformation;
        presence.smallImageKey = mapInformation;
        presence.smallImageText = smallImageText;

        lib.Discord_UpdatePresence(presence);
    }

    public void shutdown() {
        lib.Discord_Shutdown();
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setLargeImageKey(String largeImageKey) {
        this.largeImageKey = largeImageKey;
    }

    public void setPlayerInformation(String playerInformation) {
        this.playerInformation = playerInformation;
    }

    public void setMapInformation(String mapInformation) {
        this.mapInformation = mapInformation;
    }

    public void setSmallImageText(String smallImageText) {
        this.smallImageText = smallImageText;
    }
}
