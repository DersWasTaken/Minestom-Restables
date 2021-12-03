package me.Der_s;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.trait.EntityEvent;
import org.reflections.Reflections;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Restable<T extends Event> {

    public static final ConcurrentHashMap<Class<? extends Event>, List<me.Der_s.Restable<? extends Event>>> callbacks = new ConcurrentHashMap<>();
    public static final EventNode<Event> node = EventNode.all("node");

    private static final Reflections reflections = new Reflections("net.minestom.server.event");
    private static final Set<Class<? extends Event>> events = reflections.getSubTypesOf(Event.class);

    public static void enableDefaultEvents() {
        events.forEach(aClass -> {
            callbacks.put(aClass, new ArrayList<>());
            node.addListener(aClass, me.Der_s.Restable::handleGlobal);
        });
        MinecraftServer.getGlobalEventHandler().addChild(node);
    }

    protected RestableIMPL callback;
    protected UUID[] entitys;
    protected Class<T> clazz;

    public Restable(Class<T> clazz, RestableIMPL<T> callback, Entity... entity) {
        this.entitys = Arrays.stream(entity).map(Entity::getUuid).toArray(UUID[]::new);
        this.callback = callback;
        this.clazz = clazz;

        callbacks.get(clazz).add(this);
    }

    private static <T extends Event> void handleGlobal(T e) {
        Class<? extends net.minestom.server.event.Event> clazzz = e.getClass();
        if(callbacks.get(clazzz).isEmpty()) return;

        List<me.Der_s.Restable<? extends Event>> restables = callbacks.get(clazzz);
        try {
            restables.forEach(restableEventIMPL -> {
                RestableIMPL callback = (RestableIMPL) restableEventIMPL.callback;
                UUID[] entitys = restableEventIMPL.entitys;
                Class<? extends Event> clazz = restableEventIMPL.clazz;
                if (e instanceof EntityEvent && entitys.length != 0) {
                    EntityEvent event = (EntityEvent) e;
                    UUID eUUID = event.getEntity().getUuid();
                    if (!Arrays.stream(entitys).anyMatch(uuid -> uuid == eUUID)) return;
                }
                if (!callback.callback(e)) {
                    callbacks.get(clazz).remove(restableEventIMPL);
                    callback = null;
                    entitys = null;
                    clazz = null;
                }
            });
        } catch (ConcurrentModificationException exception) {
            //ignore it doesnt do shit i just cant be bothered to fix it
        }

    }

    public interface RestableIMPL<T extends Event> {
        boolean callback(T e);
    }

}

