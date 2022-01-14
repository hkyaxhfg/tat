package org.hkyaxhfg.tat.springfox;

import springfox.documentation.schema.ScalarType;

/**
 * springfox 数据类型.
 *
 * @author: wjf
 * @date: 2022/1/12
 */
public enum DataType {
    /**
     * Int
     */
    Int(ScalarType.INTEGER),
    /**
     * Long
     */
    Long(ScalarType.LONG),
    /**
     * 日期
     */
    Date(ScalarType.DATE),
    /**
     * 日期时间
     */
    DateTime(ScalarType.DATE_TIME),
    /**
     * 字符串
     */
    String(ScalarType.STRING),
    /**
     * 字节
     */
    Byte(ScalarType.BYTE),
    /**
     * 二进制
     */
    Binary(ScalarType.BINARY),
    /**
     * 密码
     */
    Password(ScalarType.PASSWORD),
    /**
     * 布尔
     */
    Boolean(ScalarType.BOOLEAN),
    /**
     * Double
     */
    Double(ScalarType.DOUBLE),
    /**
     * Float
     */
    Float(ScalarType.FLOAT),
    /**
     * BigInt
     */
    BigInt(ScalarType.BIGINTEGER),
    /**
     * BigDecimal
     */
    BigDecimal(ScalarType.BIGDECIMAL),
    /**
     * Uuid
     */
    Uuid(ScalarType.UUID),
    /**
     * 邮箱
     */
    Email(ScalarType.EMAIL),
    /**
     * 货币
     */
    Currency(ScalarType.CURRENCY),
    /**
     * Uri
     */
    Uri(ScalarType.URI),
    /**
     * Url
     */
    Url(ScalarType.URL),
    /**
     * Object
     */
    Object(ScalarType.OBJECT)

    ;

    private final ScalarType scalarType;

    DataType(ScalarType scalarType) {
        this.scalarType = scalarType;
    }

    public ScalarType getScalarType() {
        return scalarType;
    }
}
