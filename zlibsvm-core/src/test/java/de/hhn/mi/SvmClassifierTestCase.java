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
package de.hhn.mi;

import de.hhn.mi.configuration.SvmConfigurationImpl;
import de.hhn.mi.domain.SvmDocument;
import de.hhn.mi.domain.SvmModel;
import de.hhn.mi.process.SvmClassifier;
import de.hhn.mi.process.SvmClassifierImpl;
import de.hhn.mi.process.SvmTrainerImpl;
import de.hhn.mi.util.TestInputReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class SvmClassifierTestCase {

    private static final int DELTA = 5;
    private static final double DELTA_PREDICTION = 0.025d;
    private static final String MODEL_NAME ="TEST";
    private SvmModel ownPredictionModel;
    private SvmModel ownModel;
    private List<SvmDocument> trainingDocuments = new ArrayList<>();
    private List<SvmDocument> referenceClassifiedDocuments;
    private List<SvmDocument> referenceClassifiedPredictionDocuments;

    @Before
    public void setup() throws IOException {
        trainingDocuments = new TestInputReader().readFileProblem("mushroom.t", true);        // skip class labels
        // because they are not needed.
        List<SvmDocument> modelDocuments = new TestInputReader().readFileProblem("mushroom", false);

        ownModel = new SvmTrainerImpl(new SvmConfigurationImpl.Builder().build(), MODEL_NAME).train
                (modelDocuments);
        ownPredictionModel = new SvmTrainerImpl(new SvmConfigurationImpl.Builder().setProbability
                (true).build(), MODEL_NAME).train(modelDocuments);

        referenceClassifiedDocuments = new TestInputReader().readClassifiedDocuments("mushroom-outcome", false);

        referenceClassifiedPredictionDocuments = new TestInputReader().readClassifiedDocuments
                ("mushroom-outcome-predict", true);


    }

    @Test
    public void testInvalidParameters() {

        boolean failed = false;

        try {
            new SvmClassifierImpl(null);
            failed = true;
        } catch (AssertionError ignored) {

        } catch (Exception e) {
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("creating a svm classifier with nulled model worked");
        failed = false;

        try {
            new SvmClassifierImpl(ownModel).classify(null, false);
            failed = true;
        } catch (AssertionError ignored) {

        } catch (Exception e) {
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("classifying with nulled list worked");
        failed = false;

        try {
            new SvmClassifierImpl(ownModel).classify(new ArrayList<>(), false);
            failed = true;
        } catch (AssertionError ignored) {

        } catch (Exception e) {
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("classifying with empty list worked");

    }


    /**
     * Performs a tw.edu.ntu.csie.libsvm.reference test using the mushroom data taken from <a href="http://www.csie
     * .ntu.edu .tw/~cjlin/libsvmtools/datasets/"> the LIBSVM data set collection</a>  in which a pre-calculated
     * classification result by java ported LIBSVM is tested against a freshly calculated classification result
     * based on the same data. <p/> To pass the test, the implementation must provide the same as the pre-calculated
     * model (with a delta value of {@value #DELTA}) since the refactoring process did not affect the LIBSVM internals.
     */
    @Test
    public void testAgainstReferenceClassificationResults() {
        SvmClassifier classifier = new SvmClassifierImpl(ownModel);

        List<SvmDocument> classified = classifier.classify(trainingDocuments, false);

        assertEquals(referenceClassifiedDocuments.size(), classified.size());

        int similarityCount = 0;

        for (int i = 0; i < referenceClassifiedDocuments.size(); i++) {
            SvmDocument ref = referenceClassifiedDocuments.get(i);
            SvmDocument cla = classified.get(i);
            if (ref.getClassLabelWithHighestProbability().getNumeric() == cla.getClassLabelWithHighestProbability()
                    .getNumeric())
                similarityCount++;
        }

        int difference = referenceClassifiedDocuments.size() - similarityCount;

        assertTrue(difference < DELTA);
    }


    /**
     * @see #testAgainstReferenceClassificationResults()
     */
    @Test
    public void testAgainstReferencedPredictionClassificationResults() {
        SvmClassifier classifier = new SvmClassifierImpl(ownPredictionModel);

        List<SvmDocument> classified = classifier.classify(trainingDocuments, true);

        assertEquals(referenceClassifiedPredictionDocuments.size(), classified.size());

        int similarityCount = 0;

        for (int i = 0; i < referenceClassifiedPredictionDocuments.size(); i++) {
            SvmDocument ref = referenceClassifiedPredictionDocuments.get(i);
            SvmDocument cla = classified.get(i);
            if (ref.getClassLabelWithHighestProbability().getNumeric() == cla.getClassLabelWithHighestProbability()
                    .getNumeric())
                similarityCount++;
            assertEquals(ref.getClassLabelWithHighestProbability().getProbability(),
                    cla.getClassLabelWithHighestProbability().getProbability(), DELTA_PREDICTION);
        }

        int difference = referenceClassifiedPredictionDocuments.size() - similarityCount;

        assertTrue(difference < DELTA);
    }

}
