package me.Der_s.Restables;

import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.entity.*;
import net.minestom.server.event.instance.*;
import net.minestom.server.event.inventory.*;
import net.minestom.server.event.item.*;
import net.minestom.server.event.player.*;
import net.minestom.server.event.server.ClientPingServerEvent;
import net.minestom.server.event.server.ServerListPingEvent;

import java.util.*;
import java.util.function.Function;

public abstract class Restable {

    protected static Map<Class<? extends Event>, HashMap<UUID, Function>> callbacks = new HashMap<>();
    public static EventNode<Event> node = EventNode.all("node");

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
            callbacks.put(aClass, new HashMap<>());
            node.addListener(aClass, Restable::handle);
        });
    }

    public static void registerCustomEvent(Class<? extends Event> event) {
        callbacks.put(event, new HashMap<>());
        node.addListener(event, Restable::handle);
    }

    public enum Result {
        CONTINUE,
        REMOVE;
    }

    private static <T extends Event> void handle(T Event) {
        Class<? extends net.minestom.server.event.Event> clazz = Event.getClass();
        if(callbacks.get(clazz).isEmpty()) return;
        HashMap<UUID, Function> ev = callbacks.get(clazz);

        try {
            ev.entrySet().forEach(functionEntry -> {
                UUID uuid = functionEntry.getKey();
                Function f = functionEntry.getValue();
                if (uuid == null) {
                    if (f.apply(Event) == Result.REMOVE) {
                        ev.remove(f);
                    }
                } else {
                    if (f.apply(Event) == Result.REMOVE) {
                        ev.remove(uuid, f);
                    }
                }
            });
        } catch (ConcurrentModificationException e) {
            //ok
        }
    }

}
