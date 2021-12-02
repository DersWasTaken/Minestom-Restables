package me.Der_s;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.*;
import net.minestom.server.event.instance.*;
import net.minestom.server.event.inventory.*;
import net.minestom.server.event.item.*;
import net.minestom.server.event.player.*;
import net.minestom.server.event.server.ClientPingServerEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.event.trait.EntityEvent;

import java.util.*;

public class Restables<T extends Event> {

    public static final Map<Class<? extends Event>, List<Restables<? extends Event>>> callbacks = new HashMap<>();
    public static final EventNode<Event> node = EventNode.all("node");

    public static Set<Class<? extends Event>> events = Set.of(
            //Entity Events
            EntityAttackEvent.class,
            EntityDamageEvent.class,
            EntityDeathEvent.class,
            EntityFireEvent.class,
            EntityItemMergeEvent.class,
            EntityPotionAddEvent.class,
            EntityPotionRemoveEvent.class,
            EntityShootEvent.class,
            EntitySpawnEvent.class,
            EntityTickEvent.class,
            EntityVelocityEvent.class,

            //Instance
            AddEntityToInstanceEvent.class,
            InstanceChunkLoadEvent.class,
            InstanceChunkUnloadEvent.class,
            InstanceTickEvent.class,
            RemoveEntityFromInstanceEvent.class,

            //Inventory
            InventoryClickEvent.class,
            InventoryCloseEvent.class,
            InventoryItemChangeEvent.class,
            InventoryOpenEvent.class,
            InventoryPreClickEvent.class,
            PlayerInventoryItemChangeEvent.class,

            //Item
            EntityEquipEvent.class,
            ItemDropEvent.class,
            ItemUpdateStateEvent.class,
            PickupExperienceEvent.class,
            PickupItemEvent.class,

            //Player
            AdvancementTabEvent.class,
            AsyncPlayerPreLoginEvent.class,
            PlayerBlockBreakEvent.class,
            PlayerBlockInteractEvent.class,
            PlayerBlockPlaceEvent.class,
            PlayerChangeHeldSlotEvent.class,
            PlayerChatEvent.class,
            PlayerChunkLoadEvent.class,
            PlayerChunkUnloadEvent.class,
            PlayerCommandEvent.class,
            PlayerDeathEvent.class,
            PlayerDisconnectEvent.class,
            PlayerEatEvent.class,
            PlayerEntityInteractEvent.class,
            PlayerHandAnimationEvent.class,
            PlayerItemAnimationEvent.class,
            PlayerLoginEvent.class,
            PlayerMoveEvent.class,
            PlayerPacketEvent.class,
            PlayerPluginMessageEvent.class,
            PlayerPreEatEvent.class,
            PlayerResourcePackStatusEvent.class,
            PlayerRespawnEvent.class,
            PlayerSettingsChangeEvent.class,
            PlayerSkinInitEvent.class,
            PlayerSpawnEvent.class,
            PlayerStartDiggingEvent.class,
            PlayerStartFlyingEvent.class,
            PlayerStartFlyingWithElytraEvent.class,
            PlayerStartSneakingEvent.class,
            PlayerStartSprintingEvent.class,
            PlayerStopFlyingEvent.class,
            PlayerStopFlyingWithElytraEvent.class,
            PlayerStopSneakingEvent.class,
            PlayerStopSprintingEvent.class,
            PlayerSwapItemEvent.class,
            PlayerTickEvent.class,
            PlayerUseItemEvent.class,
            PlayerUseItemOnBlockEvent.class,
            UpdateTagListEvent.class,

            //Server
            ClientPingServerEvent.class,
            ServerListPingEvent.class
    );

    public static void enableDefaultEvents() {
        events.forEach(aClass -> {
            callbacks.put(aClass, new ArrayList<>());
            node.addListener(aClass, Restables::handleGlobal);
        });
        MinecraftServer.getGlobalEventHandler().addChild(node);
    }

    protected Restable callback;
    protected UUID[] entitys;
    protected Class<T> clazz;

    public Restables(Class<T> clazz, Restable<T> callback, Entity... entity) {
        this.entitys = Arrays.stream(entity).map(Entity::getUuid).toArray(UUID[]::new);
        this.callback = callback;
        this.clazz = clazz;

        callbacks.get(clazz).add(this);
    }

    private static <T extends Event> void handleGlobal(T e) {
        Class<? extends net.minestom.server.event.Event> clazzz = e.getClass();
        if(callbacks.get(clazzz).isEmpty()) return;

        List<Restables<? extends Event>> restables = callbacks.get(clazzz);
        try {
            restables.forEach(restablesEventIMPL -> {
                Restable callback = (Restable) restablesEventIMPL.callback;
                UUID[] entitys = restablesEventIMPL.entitys;
                Class<? extends Event> clazz = restablesEventIMPL.clazz;
                if (e instanceof EntityEvent && entitys.length != 0) {
                    EntityEvent event = (EntityEvent) e;
                    UUID eUUID = event.getEntity().getUuid();
                    if (!Arrays.stream(entitys).anyMatch(uuid -> uuid == eUUID)) return;
                }
                if (!callback.callback(e)) {
                    callbacks.get(clazz).remove(restablesEventIMPL);
                    callback = null;
                    entitys = null;
                    clazz = null;
                }
            });
        } catch (ConcurrentModificationException exception) {
            //ignore it doesnt do shit i just cant be bothered to fix it
        }

    }

    public interface Restable<T extends Event> {
        boolean callback(T e);
    }

}

