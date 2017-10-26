package ru.mail.park.websocket;

import org.jetbrains.annotations.NotNull;
import ru.mail.park.model.Id;
import ru.mail.park.model.UserProfile;

/**
 * Created by Solovyev on 06/04/16.
 */
public interface MessageHandlerContainer {

    void handle(@NotNull Message message, @NotNull Id<UserProfile> forUser) throws HandleException;

    <T extends Message> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler);
}
