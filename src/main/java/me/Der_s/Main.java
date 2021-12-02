package me.Der_s;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.ping.ResponseData;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        MinecraftServer MINECRAFT_SERVER = MinecraftServer.init();
        MINECRAFT_SERVER.start("localhost", 25565);
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer world = instanceManager.createInstanceContainer();
        world.setChunkGenerator(new VoidGen());
        Restables.enableDefaultEvents();


        /*
        EXAMPLE OF ENTITY/PLAYER EVENT (NO SPECIFIED PLAYER)
         */
        Restables<PlayerLoginEvent> playerLogin = new Restables<>(PlayerLoginEvent.class, e -> {
            e.setSpawningInstance(world);
            if(MinecraftServer.getConnectionManager().getOnlinePlayers().size() <= 1) {
                e.getPlayer().sendMessage("why did you spend 6 hours to get slightly prettier code");
                return false; // false removes the restable
            }
            return true;
        });

        /*
        EXAMPLE OF NON ENITY/PLAYER EVENT
         */
        Restables<ServerListPingEvent> serverPing = new Restables<>(ServerListPingEvent.class, e -> {
            ResponseData responseData = e.getResponseData();
            responseData.setOnline(1000);
            e.setResponseData(responseData);
            return true;
        });

    }

    public static class VoidGen implements ChunkGenerator {

        @Override
        public void generateChunkData(@NotNull ChunkBatch chunkBatch, int i, int i1) {

        }

        @Override
        public void fillBiomes(@NotNull Biome[] biomes, int i, int i1) {
            Arrays.fill(biomes, Biome.PLAINS);
        }

        @Override
        public @Nullable List<ChunkPopulator> getPopulators() {
            return null;
        }
    }

}
