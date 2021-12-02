package me.Der_s;

import net.minestom.server.MinecraftServer;

public class Main {

    public static void main(String[] args) {
        MinecraftServer MINECRAFT_SERVER = MinecraftServer.init();
        MINECRAFT_SERVER.start("localhost", 25565);
    }

}
