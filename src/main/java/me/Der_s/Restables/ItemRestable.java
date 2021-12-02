package me.Der_s.Restables;

import net.minestom.server.entity.Entity;
import net.minestom.server.event.trait.ItemEvent;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Function;

public class ItemRestable extends Restable {

        public static <E extends ItemEvent> void addCallback(Class<E> event_class, Function<E, Result> callback, Entity triggerFor) {
                HashMap<UUID, Function> map = new HashMap<>();
                if(triggerFor == null) {
                        map.put(null, callback);
                } else {
                        map.put(triggerFor.getUuid(), callback);
                }
                callbacks.put(event_class, map);
        }

}
