package ru.mail.park.websocket;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;


/**
 * Created by Solovyev on 06/04/16.
 */
public abstract class MessageHandler<T extends Message> {
    @NotNull
    private final Class<T> clazz;

    public MessageHandler(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    public void handleMessage(@NotNull Message message, @NotNull Id<UserProfile> forUser) throws HandleException {
        try {
            handle(clazz.cast(message), forUser);
        } catch (ClassCastException ex) {
            throw new HandleException("Can't read incoming message of type " + message.getClass(), ex);
        }
    }

    public abstract void handle(@NotNull T message, @NotNull Id<UserProfile> forUser) throws HandleException;
}
