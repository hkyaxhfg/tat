package org.hkyaxhfg.tat.lang.util;

import org.hkyaxhfg.tat.lang.util.function.Deserializer;
import org.hkyaxhfg.tat.lang.util.function.Serializer;

import java.io.*;
import java.util.Objects;

/**
 * 处理对象序列化与反序列化的操作类.
 *
 * @param <T> {@link T}.
 *
 * @author: wjf
 * @date: 2021/9/23 11:08
 */
public class Serialization<T extends Serializable> {

    /**
     * 序列化器.
     */
    private final Serializer<T> serializer;

    /**
     * 反序列化器.
     */
    private final Deserializer<T> deserializer;

    /**
     * 默认的双向序列化.
     */
    public Serialization() {
        serializer = t -> {
            if (t == null) {
                return null;
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
            try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
                oos.writeObject(t);
                oos.flush();
            } catch (IOException ex) {
                throw new IllegalArgumentException("Failed to serialize object of type: " + t.getClass(), ex);
            }
            return os.toByteArray();
        };
        deserializer = t -> {
            if (t == null) {
                return null;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(t))) {
                return Unaware.castUnaware(ois.readObject());
            }
            catch (IOException ex) {
                throw new IllegalArgumentException("Failed to deserialize object", ex);
            }
            catch (ClassNotFoundException ex) {
                throw new IllegalStateException("Failed to deserialize object type", ex);
            }
        };
    }

    /**
     * 自定义的双向序列化.
     *
     * @param serializer 序列号器.
     * @param deserializer 反序列化器.
     */
    public Serialization(Serializer<T> serializer, Deserializer<T> deserializer) {
        Objects.requireNonNull(serializer);
        Objects.requireNonNull(deserializer);
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    /**
     * 序列化对象{@link T}为字节数组.
     *
     * @param t 对象 {@link T}.
     * @return byte[].
     */
    public byte[] serialize(T t) {
        return this.serializer.apply(t);
    }

    /**
     * 将字节数组反序列化为对象{@link T}.
     *
     * @param t byte[].
     * @return 对象 {@link T}.
     */
    public T deserialize(byte[] t) {
        return this.deserializer.apply(t);
    }

}
