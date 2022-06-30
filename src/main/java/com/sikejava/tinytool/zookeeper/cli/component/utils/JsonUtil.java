package com.sikejava.tinytool.zookeeper.cli.component.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.TypeUtils;

import java.lang.reflect.Type;

/**
 * json工具类
 *
 * @author skjv2014@163.com
 * @date 2022-06-26 14:59:22
 */
public class JsonUtil {

    public enum SFeature {

        /**
         * 无 SerializerFeature
         */
        EMPTY,
        /**
         * 解决乱码
         */
        BROWSER_COMPATIBLE_ONLY(SerializerFeature.BrowserCompatible),
        /**
         * 字段为null时保留为null
         */
        WRITE_NULL_VALUE(SerializerFeature.WriteMapNullValue),
        /**
         * 格式化输出
         */
        PRETTY_FORMAT(SerializerFeature.PrettyFormat),
        /**
         * 字段为空时使用默认值
         *
         * @see JsonUtil.SFeature#WRITE_NULL_VALUE
         */
        WRITE_NULL_AS_DEFAULT_VALUE_AND_BROWSER_COMPATIBLE(
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.BrowserCompatible);

        private SerializerFeature[] features;

        SFeature(SerializerFeature... features) {
            this.features = features;
        }

        private SerializerFeature[] getFeatures() {
            return features;
        }
    }

    /**
     * 通过子类来获取泛型类型
     */
    public abstract static class AbstractJsonReference<T> extends TypeReference<T> {
    }

    /**
     * 把java对象转换成json字符串
     *
     * @param object java对象
     * @return String 字符串
     * @author skjv2014@163.com
     * @date 2022-06-28 10:06:34
     */
    public static String toJson(Object object) {
        return toJson(object, SFeature.EMPTY);
    }

    /**
     * 把java对象转换成json字符串
     *
     * @param object java对象
     * @param features SFeature
     * @return String 字符串
     * @author skjv2014@163.com
     * @date 2022-06-28 10:06:34
     */
    public static String toJson(Object object, SFeature features) {
        return toJson(object, new SFeature[] {features});
    }

    /**
     * 把java对象转换成json字符串
     *
     * @param object java对象
     * @param features SFeature数组
     * @return String 字符串
     * @author skjv2014@163.com
     * @date 2022-06-28 10:06:34
     */
    public static String toJson(Object object, SFeature... features) {
        SerializerFeature[] serializerFeatures = SFeature.EMPTY.getFeatures();
        if (null != features && features.length == 1) {
            serializerFeatures = features[0].getFeatures();
        }
        if (null != features && features.length > 1) {
            int featureCount = 0;
            for (SFeature feature : features) {
                featureCount += feature.features.length;
            }
            final SerializerFeature[] newFeatures = new SerializerFeature[featureCount];
            int point = 0;
            for (SFeature feature : features) {
                System.arraycopy(feature.features, 0, newFeatures, point, feature.features.length);
                point += feature.features.length;
            }
            serializerFeatures = newFeatures;
        }

        return JSON.toJSONString(object, serializerFeatures);
    }

    /**
     * 把json字符串转换成java对象
     *
     * @param json json字符串
     * @param clazz java对象的类
     * @return T java对象
     * @author skjv2014@163.com
     * @date 2022-06-28 10:06:34
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        return toObject(json, (Type) clazz);
    }

    /**
     * 把json字符串转换成java对象
     *
     * @param json json字符串
     * @param jsonReference AbstractJsonReference
     * @return T java对象
     * @author skjv2014@163.com
     * @date 2022-06-28 10:06:34
     */
    public static <T> T toObject(String json, AbstractJsonReference<T> jsonReference) {
        return toObject(json, jsonReference.getType());
    }

    /**
     * 把json字符串转换成java对象
     *
     * @param json json字符串
     * @param type Type
     * @return T java对象
     * @author skjv2014@163.com
     * @date 2022-06-28 10:06:34
     */
    public static <T> T toObject(String json, Type type) {
        try {
            return JSON.parseObject(json, type);
        } catch (JSONException ex) {
            throw new JSONException(json + "转换成" + type.getTypeName() + "错误", ex);
        }
    }

    /**
     * 把Java对象转换成另一个java对象
     *
     * @param obj Java对象
     * @param clazz Class<T>
     * @return T 另一个java对象
     * @author skjv2014@163.com
     * @date 2022-06-28 10:06:34
     */
    public static <T> T caseObject(Object obj, Class<T> clazz) {
        return TypeUtils.cast(obj, clazz, ParserConfig.getGlobalInstance());
    }
}
