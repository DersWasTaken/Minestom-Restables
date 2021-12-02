package me.Der_s.Restables;

import net.minestom.server.event.trait.CancellableEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

public class ServerRestable extends Restable{

    public static <E extends CancellableEvent> void addCallback(Class<E> event_class, Function<E, Result> callback) {
        HashMap<UUID, Function> map = new HashMap<>();
        map.put(null, callback);
        callbacks.put(event_class, map);
    }

}
