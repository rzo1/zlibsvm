/*-
 * ========================LICENSE_START=================================
 * zlibsvm-core
 * %%
 * Copyright (C) 2014 - 2019 Heilbronn University - Medical Informatics
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package de.hhn.mi.util;

import de.hhn.mi.domain.SvmFeature;
import de.hhn.mi.domain.SvmFeatureImpl;
import libsvm.svm_node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility for converting between LIBSVM's native 2D arrays ({@link svm_node[][]}, {@code double[][]})
 * and the higher-level {@link Map}-based representations used by the zlibsvm domain model.
 */
public class PrimitiveHelper {

    /**
     * Converts a map of {@link SvmFeature} lists to a 2D {@link svm_node} array
     * suitable for LIBSVM. Keys are sorted in ascending order.
     *
     * @param svmFeatureMap the feature map, keyed by row index
     * @return a 2D array of native LIBSVM nodes
     */
    public static svm_node[][] svmFeatureMapTo2dArray(Map<Integer, List<SvmFeature>> svmFeatureMap) {
        final List<Integer> sortedKeys = new ArrayList<>(svmFeatureMap.keySet());
        Collections.sort(sortedKeys);
        final svm_node[][] feature2dArray = new svm_node[sortedKeys.size()][];
        for (int idx = 0; idx < sortedKeys.size(); idx++) {
            final List<SvmFeature> features = svmFeatureMap.get(sortedKeys.get(idx));
            final int size = features.size();
            feature2dArray[idx] = new svm_node[size];
            for (int j = 0; j < size; j++) {
                final svm_node f = new svm_node();
                f.index = features.get(j).getIndex();
                f.value = features.get(j).getValue();
                feature2dArray[idx][j] = f;
            }
        }
        return feature2dArray;
    }

    /**
     * Converts a 2D {@link svm_node} array to a map of {@link SvmFeature} lists, keyed by row index (0-based).
     *
     * @param svmFeatures the native LIBSVM 2D node array
     * @return a map where each key is a row index and each value is the corresponding feature list
     */
    public static Map<Integer, List<SvmFeature>> svmFeature2dArrayToMap(svm_node[][] svmFeatures) {
        final Map<Integer, List<SvmFeature>> map = new HashMap<>();
        for (int i = 0; i < svmFeatures.length; i++) {

            final List<SvmFeature> feature = new ArrayList<>();

            for (int j = 0; j < svmFeatures[i].length; j++) {
                final SvmFeature f = new SvmFeatureImpl(svmFeatures[i][j].index, svmFeatures[i][j].value);
                feature.add(f);
            }
            map.put(i, feature);
        }
        return map;
    }

    /**
     * Converts a map of {@link Double} lists to a 2D {@code double} array. Keys are sorted in ascending order.
     *
     * @param doubleMap the double map, keyed by row index
     * @return a 2D primitive double array
     */
    public static double[][] doubleMapTo2dArray(Map<Integer, List<Double>> doubleMap) {
        final List<Integer> sortedKeys = new ArrayList<>(doubleMap.keySet());
        Collections.sort(sortedKeys);
        final double[][] double2dArray = new double[sortedKeys.size()][];
        for (int idx = 0; idx < sortedKeys.size(); idx++) {
            final List<Double> values = doubleMap.get(sortedKeys.get(idx));
            double2dArray[idx] = new double[values.size()];
            for (int j = 0; j < values.size(); j++) {
                double2dArray[idx][j] = values.get(j);
            }
        }
        return double2dArray;
    }

    /**
     * Converts a 2D {@code double} array to a map of {@link Double} lists, keyed by row index (0-based).
     *
     * @param doubleArray the 2D primitive double array
     * @return a map where each key is a row index and each value is the corresponding double list
     */
    public static Map<Integer, List<Double>> double2dArrayToMap(double[][] doubleArray) {
        final Map<Integer, List<Double>> map = new HashMap<>();
        for (int i = 0; i < doubleArray.length; i++) {
            final List<Double> doubleList = new ArrayList<>();
            for (int j = 0; j < doubleArray[i].length; j++) {
                doubleList.add(doubleArray[i][j]);
            }
            map.put(i, doubleList);
        }
        return map;
    }
}
