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
package de.hsheilbronn.mi;

import de.hsheilbronn.mi.configuration.SvmConfiguration;
import de.hsheilbronn.mi.configuration.SvmConfigurationImpl;
import de.hsheilbronn.mi.domain.SvmDocument;
import de.hsheilbronn.mi.domain.SvmFeature;
import de.hsheilbronn.mi.domain.SvmModel;
import de.hsheilbronn.mi.process.SvmTrainer;
import de.hsheilbronn.mi.process.SvmTrainerImpl;
import de.hsheilbronn.mi.util.TestInputReader;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author rz
 */
public class SvmTrainingTestCase {

    private static final String MODEL_NAME = "TEST";
    private static final double DELTA = 0.005d;
    private static List<SvmDocument> svmDocuments;
    private static SvmModel referenceModel;

    @BeforeClass
    public static void setup() throws IOException {
        svmDocuments = new TestInputReader().readFileProblem("mushroom", false);
        referenceModel = new TestInputReader().readSvmModel("mushroom-reference");

    }

    @Test
    public void testInvalidParameters() {

        SvmConfiguration config = new SvmConfigurationImpl.Builder().build();
        boolean failed = false;

        try {
            new SvmTrainerImpl(null, null);
            failed = true;
        } catch (AssertionError ae) {

        } catch (Exception e) {
            e.printStackTrace();
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("creating a svm trainer with nulled config worked");
        failed = false;

        try {
            new SvmTrainerImpl(config, MODEL_NAME).train(null);
            failed = true;
        } catch (AssertionError ae) {

        } catch (Exception e) {
            e.printStackTrace();
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("training with nulled list worked");
        failed = false;

        try {
            new SvmTrainerImpl(config, MODEL_NAME).train(new ArrayList<>());
            failed = true;
        } catch (AssertionError ae) {

        } catch (Exception e) {
            e.printStackTrace();
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("training with empty list worked");
    }

    /**
     * Performs a tw.edu.ntu.csie.libsvm.reference test using the mushroom data taken from <a href="http://www.csie
     * .ntu.edu .tw/~cjlin/libsvmtools/datasets/"> the LIBSVM data set collection</a>  in which a pre calculated model
     * by java ported LIBSVM v3.17 is tested against a freshly calculated model based on the same data. <p/> To pass the
     * test, the implementation must provide the same as the pre calculated model since the refactoring process did not
     * affect the LIBSVM internals.
     */
    @Test
    public void testAgainstReferenceTrainingModel() {

        SvmTrainer trainer = new SvmTrainerImpl(new SvmConfigurationImpl.Builder().build(),
                MODEL_NAME);

        SvmModel ownModel = trainer.train(svmDocuments);

        SvmConfiguration ownConfig = ownModel.getMetaInformation().getSvmConfiguration();
        SvmConfiguration referenceConfig = ownModel.getMetaInformation().getSvmConfiguration();

        assertEquals(ownConfig, referenceConfig);

        //checking equality between the two models without using an own object equals method because of defining an
        // DELTA for floating point issues.

        assertEquals(referenceModel.getMetaInformation().getNumberOfClasses(),
                ownModel.getMetaInformation().getNumberOfClasses());

        assertEquals(referenceModel.getMetaInformation().getAmountOfSupportVectors(),
                ownModel.getMetaInformation().getAmountOfSupportVectors());

        List<Double> rhoReference = referenceModel.getMetaInformation().getRhoConstants();
        List<Double> ownRhoReference = ownModel.getMetaInformation().getRhoConstants();

        assertEquals(rhoReference.size(), ownRhoReference.size());

        for (int i = 0; i < ownRhoReference.size(); i++) {
            assertEquals(rhoReference.get(i), ownRhoReference.get(i), DELTA);
        }

        List<Integer> labelForEachClassReference = referenceModel.getMetaInformation().getLabelForEachClass();
        List<Integer> ownLabelForEachClass = ownModel.getMetaInformation().getLabelForEachClass();

        assertEquals(labelForEachClassReference, ownLabelForEachClass);

        List<Double> probabilityAReference = referenceModel.getMetaInformation().getRhoConstants();
        List<Double> ownProbabilityA = ownModel.getMetaInformation().getRhoConstants();

        assertEquals(probabilityAReference.size(), ownProbabilityA.size());

        for (int i = 0; i < ownProbabilityA.size(); i++) {
            assertEquals(probabilityAReference.get(i), ownProbabilityA.get(i), DELTA);
        }


        List<Double> probabilityBReference = referenceModel.getMetaInformation().getRhoConstants();
        List<Double> ownProbabilityB = ownModel.getMetaInformation().getRhoConstants();

        assertEquals(probabilityBReference.size(), ownProbabilityB.size());

        for (int i = 0; i < ownProbabilityB.size(); i++) {
            assertEquals(probabilityBReference.get(i), ownProbabilityB.get(i), DELTA);
        }


        List<Integer> numberOfSupportVectorsForEachClassReference = referenceModel.getMetaInformation()
                .getNumberOfSupportVectorsForEachClass();
        List<Integer> ownNumberOfSupportVectorsForEachClass = ownModel.getMetaInformation()
                .getNumberOfSupportVectorsForEachClass();

        assertEquals(numberOfSupportVectorsForEachClassReference, ownNumberOfSupportVectorsForEachClass);


        Map<Integer, List<Double>> supportVectorCoefficientsReference = referenceModel.getSvCoefficients();
        Map<Integer, List<Double>> ownSupportVectorCoefficients = ownModel.getSvCoefficients();

        assertEquals(supportVectorCoefficientsReference.keySet().size(), ownSupportVectorCoefficients.keySet().size());

        for (Integer i : supportVectorCoefficientsReference.keySet()) {
            List<Double> reference = supportVectorCoefficientsReference.get(i);
            List<Double> own = ownSupportVectorCoefficients.get(i);
            assertEquals(reference.size(), own.size());
            for (int j = 0; j < reference.size(); j++) {
                assertEquals(reference.get(j), own.get(j), DELTA);
            }
        }

        Map<Integer, List<SvmFeature>> supportVectorsReference = referenceModel.getSupportVectors();
        Map<Integer, List<SvmFeature>> ownSupportVectors = ownModel.getSupportVectors();

        for (Integer i : supportVectorsReference.keySet()) {
            List<SvmFeature> reference = supportVectorsReference.get(i);
            List<SvmFeature> own = ownSupportVectors.get(i);
            assertEquals(reference.size(), own.size());
            for (int j = 0; j < reference.size(); j++) {
                assertEquals(reference.get(j).getIndex(), own.get(j).getIndex());
                assertEquals(reference.get(j).getValue(), own.get(j).getValue(), DELTA);
            }
        }
    }


}
