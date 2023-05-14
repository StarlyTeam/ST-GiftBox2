package net.starly.giftbox.util;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class EncodeUtil {

    public static byte[] encode(Object obj) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); BukkitObjectOutputStream boos = new BukkitObjectOutputStream(bos)) {
            boos.writeObject(obj);
            return bos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static <T> T decode(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes); BukkitObjectInputStream bois = new BukkitObjectInputStream(bis)) {
            return clazz.cast(bois.readObject());
        } catch (NullPointerException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
