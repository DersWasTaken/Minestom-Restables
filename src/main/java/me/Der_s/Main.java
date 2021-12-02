package me.Der_s;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static final InstanceManager instanceManager = MinecraftServer.getInstanceManager();

    public static void main(String[] args) {
        MinecraftServer MINECRAFT_SERVER = MinecraftServer.init();
        MINECRAFT_SERVER.start("localhost", 25565);
        InstanceContainer world = instanceManager.createInstanceContainer();
        world.setChunkGenerator(new VoidGen());
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(PlayerLoginEvent.class, event -> {
            event.setSpawningInstance(world);
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
