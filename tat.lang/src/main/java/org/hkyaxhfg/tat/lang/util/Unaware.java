package org.hkyaxhfg.tat.lang.util;

/**
 * 无感知, 主要用于消除编译期异常, 使jvm编译器能够正常通过编译, 类型转换等操作.
 *
 * @author: wjf
 * @date: 2022/1/6
 */
public class Unaware {

    private Unaware() {}

    /**
     * 异常无感知, 用于消除编译期异常.
     * @param exceptionUnaware 无参无返回的function.
     */
    public static void exceptionUnaware(ExceptionUnaware exceptionUnaware) {
        try {
            exceptionUnaware.unaware();
        } catch (Exception exception) {
            TatException.throwEx(exception.toString());
        }
    }

    /**
     * 类型转换无感知, 便于适用类型转换而无编译器警告.
     * @param arg arg.
     * @param <T> 要转换的类型.
     * @return T.
     */
    @SuppressWarnings("unchecked")
    public static <T> T castUnaware(Object arg) {
        try {
            return (T) arg;
        } catch (ClassCastException exception) {
            throw TatException.newEx(exception.getMessage());
        }
    }

    /**
     * 非空无感知, 当arg不为空时, 执行代码.
     * @param arg arg.
     */
    public static void notNullUnaware(Object arg, NotNullUnaware notNullUnaware) {
        notNullUnaware.run(arg);
    }

    /**
     * 异常无感知专用接口.
     */
    @FunctionalInterface
    @SuppressWarnings("all")
    public interface ExceptionUnaware {

        /**
         * 异常无感知时使用.
         * @throws Exception 抛出任意异常, 使jvm通过过编译.
         */
        void unaware() throws Exception;

    }

    /**
     * 非空无感知专用接口.
     */
    @FunctionalInterface
    public interface NotNullUnaware {

        /**
         * 非空无感知时使用.
         */
        void unaware();

        /**
         * 当target非空时执行.
         *
         * @param arg 目标.
         */
        default void run(Object arg) {
            if (arg != null) {
                unaware();
            }
        }

    }

}
