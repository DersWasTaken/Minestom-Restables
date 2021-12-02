package me.Der_s.Restables;

import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.PlayerEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

public class PlayerRestable extends Restable {

    public static <E extends PlayerEvent> void addCallback(Class<E> event_class, Function<E, Result> callback, Player triggerFor) {
        HashMap<UUID, Function> map = new HashMap<>();
        if(triggerFor == null) {
            map.put(null, callback);
        } else {
            map.put(triggerFor.getUuid(), callback);
        }
        callbacks.put(event_class, map);
    }

}
