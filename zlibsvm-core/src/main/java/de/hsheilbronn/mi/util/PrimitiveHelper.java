/*-
 * ========================LICENSE_START=================================
 * zlibsvm-core
 * %%
 * Copyright (C) 2014 - 2016 Heilbronn University - Medical Informatics
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
package de.hsheilbronn.mi.util;

import de.hsheilbronn.mi.domain.SvmFeature;
import de.hsheilbronn.mi.domain.SvmFeatureImpl;
import libsvm.svm_node;

import java.util.*;

/**
 * A little helper to convert 2d arrays of double and {@link SvmFeature svm features} to maps and the other way around.
 *
 * @author rz
 */

//is their a better solution?
public class PrimitiveHelper {


    public static svm_node[][] svmFeatureMapTo2dArray(Map<Integer, List<SvmFeature>> svmFeatureMap) {
        Set<Integer> set = svmFeatureMap.keySet();
        svm_node[][] feature2dArray = new svm_node[set.size()][];
        for (Integer i : set) {
            List<SvmFeature> features = svmFeatureMap.get(i);
            feature2dArray[i] = new svm_node[svmFeatureMap.get(i).size()];
            for (int j = 0; j < features.size(); j++) {
                svm_node f = new svm_node();
                f.index = features.get(j).getIndex();
                f.value = features.get(j).getValue();
                feature2dArray[i][j] = f;
            }
        }
        return feature2dArray;
    }

    public static Map<Integer, List<SvmFeature>> svmFeature2dArrayToMap(svm_node[][] svmFeatures) {
        Map<Integer, List<SvmFeature>> map = new HashMap<>();
        for (int i = 0; i < svmFeatures.length; i++) {

            List<SvmFeature> feature = new ArrayList<>();

            for (int j = 0; j < svmFeatures[i].length; j++) {
                SvmFeature f = new SvmFeatureImpl(svmFeatures[i][j].index, svmFeatures[i][j].value);
                feature.add(f);
            }
            map.put(i, feature);
        }
        return map;
    }

    public static double[][] doubleMapTo2dArray(Map<Integer, List<Double>> doubleMap) {
        Set<Integer> set = doubleMap.keySet();
        double[][] double2dArray = new double[set.size()][];
        for (Integer i : set) {
            double2dArray[i] = new double[doubleMap.get(i).size()];
            List<Double> features = doubleMap.get(i);
            for (int j = 0; j < features.size(); j++) {
                double2dArray[i][j] = features.get(j);
            }
        }
        return double2dArray;
    }

    public static Map<Integer, List<Double>> double2dArrayToMap(double[][] doubleArray) {
        Map<Integer, List<Double>> map = new HashMap<>();
        for (int i = 0; i < doubleArray.length; i++) {
            List<Double> doubleList = new ArrayList<>();
            for (int j = 0; j < doubleArray[i].length; j++) {
                doubleList.add(doubleArray[i][j]);
            }
            map.put(i, doubleList);
        }
        return map;
    }
}
