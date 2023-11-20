/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.hhn.mi.shade.org.apache.commons.lang3;

import java.util.Arrays;
import java.util.function.IntFunction;

/**
 * Forked from commons-lang3 in version 3.13.0
 */
public class ArrayUtils {

    public static final double[] EMPTY_DOUBLE_ARRAY = {};

    public static final int[] EMPTY_INT_ARRAY = {};

    public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = {};

    public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = {};

    /**
     * Converts an array of object Double to primitives.
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     * </p>
     *
     * @param array  a {@link Double} array, may be {@code null}
     * @return an {@code int} array, {@code null} if null array input
     * @throws NullPointerException if an array element is {@code null}
     */
    public static double[] toPrimitive(final Double[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return EMPTY_DOUBLE_ARRAY;
        }
        final double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].doubleValue();
        }
        return result;
    }

    /**
     * Converts an array of object Integers to primitives.
     * <p>
     * This method returns {@code null} for a {@code null} input array.
     * </p>
     *
     * @param array  a {@link Integer} array, may be {@code null}
     * @return an {@code int} array, {@code null} if null array input
     * @throws NullPointerException if an array element is {@code null}
     */
    public static int[] toPrimitive(final Integer[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return EMPTY_INT_ARRAY;
        }
        final int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i].intValue();
        }
        return result;
    }


    /**
     * Converts an array of primitive doubles to objects.
     *
     * <p>This method returns {@code null} for a {@code null} input array.</p>
     *
     * @param array  a {@code double} array
     * @return a {@link Double} array, {@code null} if null array input
     */
    public static Double[] toObject(final double[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return EMPTY_DOUBLE_OBJECT_ARRAY;
        }
        return setAll(new Double[array.length], i -> array[i]);
    }

    /**
     * Converts an array of primitive ints to objects.
     *
     * <p>This method returns {@code null} for a {@code null} input array.</p>
     *
     * @param array  an {@code int} array
     * @return an {@link Integer} array, {@code null} if null array input
     */
    public static Integer[] toObject(final int[] array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return EMPTY_INTEGER_OBJECT_ARRAY;
        }
        return setAll(new Integer[array.length], i -> array[i]);
    }


    /**
     * Sets all elements of the specified array, using the provided generator supplier to compute each element.
     * <p>
     * If the generator supplier throws an exception, it is relayed to the caller and the array is left in an indeterminate
     * state.
     * </p>
     *
     * @param <T> type of elements of the array.
     * @param array array to be initialized.
     * @param generator a function accepting an index and producing the desired value for that position.
     * @return the input array
     * @since 3.13.0
     */
    public static <T> T[] setAll(final T[] array, final IntFunction<? extends T> generator) {
        if (array != null && generator != null) {
            Arrays.setAll(array, generator);
        }
        return array;
    }
}
