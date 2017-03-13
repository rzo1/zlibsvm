/*-
 * ========================LICENSE_START=================================
 * zlibsvm-core
 * %%
 * Copyright (C) 2014 - 2017 Heilbronn University - Medical Informatics
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

import de.hsheilbronn.mi.domain.*;
import de.hsheilbronn.mi.mock.SvmDocumentMock;
import libsvm.svm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author rz
 */
public class TestInputReader {


    public List<SvmDocument> readFileProblem(String problem, boolean skipClassLabel) throws IOException {

        BufferedReader fp = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream
                (problem), StandardCharsets.UTF_8));
        List<SvmDocument> documents = new ArrayList<>();

        String line;
        fp.readLine(); //skip the first line because of header information
        while ((line = fp.readLine()) != null) {

            StringTokenizer st = new StringTokenizer(line, " \t\n\r\f:");
            SvmClassLabelImpl classLabel = new SvmClassLabelImpl(Double.parseDouble(st.nextToken()));

            int m = st.countTokens() / 2;

            List<SvmFeature> features = new ArrayList<>();

            for (int j = 0; j < m; j++) {
                String featureIndex = st.nextToken();
                String featureValue = st.nextToken();
                SvmFeature sfi = new SvmFeatureImpl(Integer.parseInt(featureIndex), Double.parseDouble(featureValue));
                features.add(sfi);
            }
            SvmDocument mock = new SvmDocumentMock(features);

            if (!skipClassLabel)
                mock.addClassLabel(classLabel);

            documents.add(mock);
        }

        fp.close();

        return documents;

    }

    public SvmModel readSvmModel(String svmModel) throws IOException {
        BufferedReader fp = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream
                (svmModel), StandardCharsets.UTF_8));
        fp.readLine(); //skip the first line because of header information

        //using unmodified libsvm core method to load a model
        return new SvmModelImpl("TEST", new NativeSvmModelWrapper(new svm().svm_load_model(fp)));
    }

    public List<SvmDocument> readClassifiedDocuments(String classifiedDocs, boolean predict) throws IOException {
        BufferedReader fp = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream
                (classifiedDocs), StandardCharsets.UTF_8));
        fp.readLine(); //skip the first line because of header information
        List<SvmDocument> classifiedDocuments = new ArrayList<>();

        String line;

        while ((line = fp.readLine()) != null) {
            String[] split = line.split(" ");
            double estimatedClassLabel = Double.parseDouble(split[0]);

            SvmDocument doc = new SvmDocumentMock(new ArrayList<>());

            SvmClassLabel label = new SvmClassLabelImpl(estimatedClassLabel);
            if (predict) {
                List<Double> probability = new ArrayList<>();

                for (int i = 1; i < split.length; i++) {
                    probability.add(Double.parseDouble(split[i]));
                }
                label.setProbability(Collections.max(probability));
            }
            doc.addClassLabel(label);
            classifiedDocuments.add(doc);
        }
        return classifiedDocuments;
    }
}
