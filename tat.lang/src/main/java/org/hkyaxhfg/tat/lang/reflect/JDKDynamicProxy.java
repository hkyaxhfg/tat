package org.hkyaxhfg.tat.lang.reflect;

import org.hkyaxhfg.tat.lang.util.Container;
import org.hkyaxhfg.tat.lang.util.Unaware;
import org.hkyaxhfg.tat.lang.util.function.FourConsumer;
import org.hkyaxhfg.tat.lang.util.function.ThreeConsumer;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 一个简易的JDK动态代理类.
 *
 * @param <I> JDK动态代理所需的公共接口.
 *
 * @author: wjf
 * @date: 2022/1/8
 */
public abstract class JDKDynamicProxy<I> {

    /**
     * 被代理的对象.
     */
    private I target;

    /**
     * 被代理的对象的字节码.
     */
    private Class<I> targetClass;

    /**
     * 代理对象.
     */
    private I proxy;

    /**
     * 构造函数, 需要提供被代理的对象, 前置和后置都可以为空.
     *
     * @param target 被代理的对象.
     */
    public JDKDynamicProxy(I target) {
        this.target = target;
        this.targetClass = Unaware.castUnaware(this.target.getClass());
        this.proxy = newProxy();
    }

    /**
     * 前置通知.
     * @param <P1> JDKDynamicProxy.this.target.
     * @param <P2> method.
     * @param <P3> args.
     * @return ThreeConsumer<P1, P2, P3>
     */
    public abstract <P1, P2, P3> ThreeConsumer<P1, P2, P3> before();

    /**
     * 后置通知.
     * @param <P1> this.target.
     * @param <P2> method.
     * @param <P3> args.
     * @param <P4> result.
     * @return FourConsumer<P1, P2, P3, P4>
     */
    public abstract <P1, P2, P3, P4> FourConsumer<P1, P2, P3, P4> after();

    /**
     * 异常通知.
     * @param <P1> this.target.
     * @param <P2> method.
     * @param <P3> args.
     * @param <P4> exception.
     * @return FourConsumer<P1, P2, P3, P4>
     */
    public abstract <P1, P2, P3, P4> FourConsumer<P1, P2, P3, P4> throwing();

    /**
     * 最终通知.
     * @param <P1> this.target.
     * @param <P2> method.
     * @param <P3> args.
     * @param <P4> result.
     * @return FourConsumer<P1, P2, P3, P4>
     */
    public abstract <P1, P2, P3, P4> FourConsumer<P1, P2, P3, P4> ultimate();

    /**
     * 获取代理对象.
     * @return proxy.
     */
    public I getProxy() {
        return this.proxy;
    }

    /**
     * 生成代理对象.
     *
     * @param before 前置.
     * @param after 后置.
     * @return I.
     */
    @SuppressWarnings("all")
    private I newProxy() {
        return Unaware.castUnaware(
                Proxy.newProxyInstance(
                        JDKDynamicProxy.class.getClassLoader(),
                        this.targetClass.getInterfaces(),
                        (proxy, method, args) -> {
                            Container<Object> container = new Container<>();
                            try {

                                ThreeConsumer<I, Method, Object[]> before = before();
                                Unaware.notNullUnaware(
                                        before,
                                        () -> before.accept(
                                                JDKDynamicProxy.this.target,
                                                method,
                                                args
                                        )
                                );

                                // put result
                                container.put(method.invoke(JDKDynamicProxy.this.target, args));

                                FourConsumer<I, Method, Object[], Object> after = after();
                                Unaware.notNullUnaware(
                                        after,
                                        () -> after.accept(
                                                JDKDynamicProxy.this.target,
                                                method,
                                                args,
                                                container.get()
                                        )
                                );
                            } catch (Exception e) {
                                FourConsumer<I, Method, Object[], Exception> throwing = throwing();
                                Unaware.notNullUnaware(
                                        throwing,
                                        () -> throwing.accept(
                                                JDKDynamicProxy.this.target,
                                                method,
                                                args,
                                                e
                                        )
                                );
                            } finally {
                                FourConsumer<I, Method, Object[], Object> ultimate = ultimate();
                                final Object result = container.get();
                                Unaware.notNullUnaware(
                                        ultimate,
                                        () -> ultimate.accept(
                                                JDKDynamicProxy.this.target,
                                                method,
                                                args,
                                                result
                                        )
                                );
                            }
                            return container.get();
                        }
                )
        );
    }

}
