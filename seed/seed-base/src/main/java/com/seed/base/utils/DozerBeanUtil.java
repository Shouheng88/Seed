package com.seed.base.utils;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Dozer utils
 *
 * @author <a href="mailto:shouheng2015@gmail.com">Shouheng.W</a>
 * @version 1.0
 * @date 2020/11/5 17:30
 */
public final class DozerBeanUtil {

    private Mapper mapper;

    private static class DozerBeanUtilHolder {

        private static DozerBeanUtil dozerBeanUtil = new DozerBeanUtil(getMapper());

        private static Mapper getMapper() {
            return DozerBeanMapperBuilder.buildDefault();
        }
    }

    public static DozerBeanUtil get() {
        return DozerBeanUtilHolder.dozerBeanUtil;
    }

    private DozerBeanUtil(Mapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Clone given object
     *
     * @param base the source
     * @param <P>  the type
     * @return     the cloned object
     */
    @SuppressWarnings("unchecked")
    public <P> P clone(P base) {
        if (base == null) {
            return null;
        } else {
            return (P) mapper.map(base, base.getClass());
        }
    }

    /**
     * Clone a list
     *
     * @param baseList the source list
     * @param <P>      the element type
     * @return         the cloned list
     */
    public <P> List<P> cloneList(List<P> baseList) {
        if (baseList == null) {
            return null;
        } else {
            List<P> targetList = new ArrayList<P>();
            for (P p : baseList) {

                targetList.add((P) clone(p));
            }
            return targetList;
        }
    }

    /**
     * Convert object from one type to another.
     *
     * @param base   the source object
     * @param target the target type
     * @param <V>    the source type
     * @param <P>    the target type
     * @return       the converted value
     */
    public <V, P> P convert(V base, P target) {
        if (base != null) {
            mapper.map(base, target);
            return target;
        }
        return target;
    }

    /**
     * Convert object to given type
     *
     * @param base   object
     * @param target target type
     * @param <V>    from type
     * @param <P>    to type
     * @return       converted object
     */
    public <V, P> P convert(V base, Class<P> target) {
        if (base == null) {
            return null;
        } else {
            return mapper.map(base, target);
        }
    }

    /**
     * Convert list from one type to another
     *
     * @param baseList base list
     * @param target   target type of element
     * @param <V>      from element type
     * @param <P>      to element type
     * @return         result list
     */
    public <V, P> List<P> convertList(List<V> baseList, Class<P> target) {
        if (baseList == null) {
            return null;
        } else {
            List<P> targetList = new ArrayList<>();
            for (V vo : baseList) {
                targetList.add(convert(vo, target));
            }
            return targetList;
        }
    }
}
