# zlibsvm 

[![Build Status](https://travis-ci.org/rzo1/zlibsvm.svg?branch=master)](https://travis-ci.org/rzo1/zlibsvm)   ![Maven Central](https://img.shields.io/maven-central/v/de.hs-heilbronn.mi/zlibsvm.svg?style=flat-square)

**zlibsvm** is an object-oriented, easy to use and simple Java binding for the famous **LIBSVM** library hosted on [GitHub](https://github.com/cjlin1/libsvm).

It encapsulates the cross-compiled Java code from **LIBSVM** behind an object-oriented API which can be easily used via Apache Maven in your own projects.

### Using Maven

To use the latest release of **zlibsvm**, please use the following snippet in your `pom.xml`

#### Java 10+
```xml
    <dependency>
        <groupId>de.hs-heilbronn.mi</groupId>
        <artifactId>zlibsvm-core</artifactId>
        <version>2.0.2</version>
    </dependency>
```

#### Java 8+
```xml
    <dependency>
        <groupId>de.hs-heilbronn.mi</groupId>
        <artifactId>zlibsvm-core</artifactId>
        <version>1.3.1</version>
    </dependency>
```

### Quickstart

#### Check UI-based Example

A code example can be found [here](https://github.com/rzo1/zlibsvm-example).

#### Dataset Format

The dataset format for [LIBSVM](https://www.csie.ntu.edu.tw/~cjlin/libsvm/) is 

    label feature_id1:feature_value1 feature_id2:feature_value2 ...

Thus, every feature (or value) needs its own unique identifier.

For three different class labels `1,2,3` and a feature set consisting of `a(id=1),b(id=2),c=(id=3)`, a valid data representation for  three data points `d1,d2,d3` would be

    2 1:0.5325 3:0.523
    
    3 2:0.7853 3:0.6326
    
    1 1:0.53265 2:0.5422

Meaning:

 - `d1` contains feature `a(id=1)` and `c(id=3)`
 - `d2` contains feature `b(id=2)` and `c(id=3)`
 - `d3` contains feature `a(id=1)` and `b(id=2)`

Note, that it is not necessary to provide `feature_id1:feature_value1` for features, which are not contained in the given data point.

#### Training and Prediction

First of all, you need to implement your custom `SvmDocument` and a custom `SvmFeature`, which could be like:

```java
      public class SvmDocumentImpl implements SvmDocument {
      
          private final List<SvmFeature> features;
          private final List<SvmClassLabel> classLabels = new ArrayList<>();
      
          public SvmDocumentImpl(List<SvmFeature> features) {
              this.features = features;
          }
      
          public List<SvmFeature> getSvmFeatures() {
              return features;
          }
      
          public SvmClassLabel getClassLabelWithHighestProbability() {
              if (classLabels.isEmpty()) {
                  return null;
              }
      
              return Collections.max(classLabels);
          }
      
          @Override
          public List<SvmClassLabel> getAllClassLabels() {
              return Collections.unmodifiableList(classLabels);
          }
      
          @Override
          public void addClassLabel(SvmClassLabel classLabel) {
              assert (classLabel != null);
              this.classLabels.add(classLabel);
          }
            
      }
      
```

```java
     public class SvmFeatureImpl implements SvmFeature {
     
         private int index;
         private double value;
     
         public SvmFeatureImpl(int index, double value) {
             this.index = index;
             this.value = value;
         }
     
         public int getIndex() {
             return index;
         }
     
         public double getValue() {
             return value;
         }
     
         public void setIndex(int index) {
             this.index = index;
     
         }
     
         public void setValue(double value) {
             this.value = value;
     
         }
     
         @Override
         public int compareTo(SvmFeature o) {
             throw new UnsupportedOperationException("TODO: Implement this method for real use-cases");
         }
     }
      
```

To obtain an `SvmModel` the SVM needs to be trained. This is done via an `SvmConfigurationImpl.Builder()`, which is used to specify your custom SVM configuration.
 
The default configuration is the same as described [here](https://github.com/cjlin1/libsvm): C_SVC, RBF-Kernel with gamma 0 and cost 1.

```java
        SvmTrainer trainer = new SvmTrainerImpl(new SvmConfigurationImpl.Builder().build(),"my-custom-trained-model");

        SvmModel model = trainer.train(documentsForTraining);

```

After this step it is possible to use this `SvmModel` for prediction.

```java
         SvmClassifier classifier = new SvmClassifierImpl(model);
        
         List<SvmDocument> classified = classifier.classify(documentsForPrediction, true);
         
         for(SvmDocument d : classified) {
            System.out.println(d.toString() + " was classified as category:" + d.getClassLabelWithHighestProbability().getNumeric());
         }
                  
```


### License

Published under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0)
