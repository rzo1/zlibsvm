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
package de.hsheilbronn.mi;

import de.hsheilbronn.mi.configuration.SvmConfigurationBuilder;
import de.hsheilbronn.mi.configuration.SvmConfigurationImpl;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * @author rz
 */
public class SvmConfigurationTestCase {

    @Test
    public void testBuildConfigurationWithInvalidParameters() {
        boolean failed = false;
        SvmConfigurationBuilder builder = new SvmConfigurationImpl.Builder();

        try {
            builder.setDegree(-1);
            failed = true;
        } catch (AssertionError ae) {

        } catch (Exception e) {
            e.printStackTrace();
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("setting a degree < 0 worked");
        failed = false;

        try {
            builder.setGamma(-1.0d);
            failed = true;
        } catch (AssertionError ae) {

        } catch (Exception e) {
            e.printStackTrace();
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("setting a gamma < 0 worked");
        failed = false;

        try {
            builder.setNu(-1.0d);
            failed = true;
        } catch (AssertionError ae) {

        } catch (Exception e) {
            e.printStackTrace();
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("setting a nu < 0 worked");
        failed = false;


        try {
            builder.setNu(1.1d);
            failed = true;
        } catch (AssertionError ae) {

        } catch (Exception e) {
            e.printStackTrace();
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("setting a nu > 1 worked");
        failed = false;

        try {
            builder.setCacheSize(-1);
            failed = true;
        } catch (AssertionError ae) {

        } catch (Exception e) {
            e.printStackTrace();
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("setting a cache size < 0 worked");
        failed = false;

        try {
            builder.setCost(0.0d);
            failed = true;
        } catch (AssertionError ae) {

        } catch (Exception e) {
            e.printStackTrace();
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("setting cost <= 0 worked");
        failed = false;

        try {
            builder.setEpsilon(0.0d);
            failed = true;
        } catch (AssertionError ae) {

        } catch (Exception e) {
            e.printStackTrace();
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("setting eps <= 0 worked");
        failed = false;

        try {
            builder.setP(-1.0d);
            failed = true;
        } catch (AssertionError ae) {

        } catch (Exception e) {
            e.printStackTrace();
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("setting a p value < 0 worked");
        failed = false;

        try {
            builder.setCrossValidation(true, 1);
            failed = true;
        } catch (AssertionError ae) {

        } catch (Exception e) {
            e.printStackTrace();
            fail("caught Exception of type "
                    + e.getClass().getName()
                    + " instead of an AssertionError. See stacktrace for further information.");
        }

        if (failed)
            fail("setting crossValidation with nfold < 2 worked");
    }

}
