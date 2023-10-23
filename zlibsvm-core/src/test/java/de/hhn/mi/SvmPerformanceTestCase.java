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
import de.hhn.mi.process.SvmTrainerImpl;
import de.hhn.mi.util.TestInputReader;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import tw.edu.ntu.csie.libsvm.svm_train;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class SvmPerformanceTestCase {

    private static final String MODE_NAME = "TEST";
    private static final Logger logger = org.slf4j.LoggerFactory
            .getLogger(SvmPerformanceTestCase.class);
    private static final double DELTA = 2.2d; // in seconds

    private double referenceTrainingTime = 0.0d;
    private double referenceVariance = 0.0d;

    @Before
    public void obtainReferenceTrainingTime() throws IOException {
        String[] args = {"-q", "mushroom"};
        double[] referenceMeasuredValues = new double[5];
        double referenceMeanValue = 0.0d;
        for (int i = 0; i < referenceMeasuredValues.length; i++) {

            long startTime = System.currentTimeMillis();

            svm_train.main(args);

            long endTime = System.currentTimeMillis();

            referenceMeasuredValues[i] = (endTime - startTime) / 1000d;
            referenceMeanValue += referenceMeasuredValues[i];

            logger.info("{}. reference run, elapsed time: {}", (i + 1),
                    referenceMeasuredValues[i]);
        }
        referenceMeanValue /= referenceMeasuredValues.length;
        referenceTrainingTime = referenceMeanValue;
        referenceVariance = round(calculateStandardDeviation(referenceMeasuredValues, referenceMeanValue));
    }

    /**
     * Performs a test on  the mushroom data taken from <a href="http://www.csie.ntu.edu
     * .tw/~cjlin/libsvmtools/datasets/"> the libsvm data set collection</a>  in which the time elapsed for training a
     * model is measured. <p/> To pass the test, the implementation must beat a predefined time (with a delta of {@value
     * #DELTA} seconds) taken from the original java based LIBSVM implementation.
     */
    @Test
    public void performanceTrainingTime() throws IOException {
        SvmTrainerImpl trainer = new SvmTrainerImpl(new SvmConfigurationImpl.Builder().build(),
                MODE_NAME);
        double[] measuredValues = new double[5];
        double meanValue = 0.0d;
        for (int i = 0; i < measuredValues.length; i++) {

            long startTime = System.currentTimeMillis();
            trainer.train(new TestInputReader().readFileProblem("mushroom", false));

            long endTime = System.currentTimeMillis();

            measuredValues[i] = (endTime - startTime) / 1000d;
            meanValue += measuredValues[i];

            logger.info("{}. run, elapsed time: {}", (i + 1), measuredValues[i]);
        }
        meanValue /= measuredValues.length;
        double ownVariance = round(calculateStandardDeviation(measuredValues, meanValue));

        logger.info("Mean value (reference): {} + / - {}", round(referenceTrainingTime),
                round(referenceVariance));
        logger.info("Mean value (own): {} + / - {}", round(meanValue), ownVariance);
        logger.info("Difference: {}", round((meanValue - referenceTrainingTime)));

        if (meanValue > referenceTrainingTime)
            assertEquals(referenceTrainingTime, meanValue, DELTA);

    }

    /**
     * Example: round(3.4181) = 3.148
     *
     * @param value the value to round
     * @return the rounded value
     */
    private double round(double value) {
        return Math.round(value * Math.pow(10, 3)) / Math.pow(10, 3);
    }

    /**
     * Calculates the standard deviation of a given array of double values and a given mean value.
     *
     * @param values    values to calculated deviation
     * @param meanValue the mean value corresponding to the given values.
     * @return the standard deviation
     */
    private double calculateStandardDeviation(double[] values, double meanValue) {
        double variance = 0.0d;
        for (double value : values) {
            variance += Math.pow(meanValue - value, 2);
        }
        variance /= (values.length - 1);
        return Math.sqrt(variance);
    }


}
