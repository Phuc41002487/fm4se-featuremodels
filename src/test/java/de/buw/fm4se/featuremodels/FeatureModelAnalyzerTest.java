package de.buw.fm4se.featuremodels;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import de.buw.fm4se.featuremodels.fm.Feature;
import org.junit.jupiter.api.Test;

import de.buw.fm4se.featuremodels.fm.FeatureModel;

class FeatureModelAnalyzerTest {
//
//  @Test
//  void testCheckConsistentCarFM() throws Exception {
//    assertTrue(FeatureModelAnalyzer.checkConsistent(ExampleFmCreator.getSimpleFm()));
//  }

//  @Test
//  void testCheckConsistentBadFM() throws Exception {
//    assertFalse(FeatureModelAnalyzer.checkConsistent(ExampleFmCreator.getBadFm()));
//  }

//  @Test
//  void testCheckDeadFeaturesBadFM() throws Exception {
//    FeatureModel fm = ExampleFmCreator.getBadFm();
//    List<String> deadFeatures = FeatureModelAnalyzer.deadFeatureNames(fm);
//    System.out.println("list of dead features");
//    for (String deadFeature : deadFeatures) {
//      System.out.println(deadFeature);
//    }
//    System.out.println(fm.getRoot().getName());
//    assertTrue(deadFeatures.contains(fm.getRoot().getName()));
//    //assertTrue(deadFeatures.contains(fm.getRoot().getChildren().get(0).getName()));
//  }

  @Test
  void testCheckDeadFeaturesCarFM() throws Exception {
    FeatureModel fm = ExampleFmCreator.getSimpleFm();
    List<String> deadFeatures = FeatureModelAnalyzer.deadFeatureNames(fm);

    assertFalse(deadFeatures.contains(fm.getRoot().getName()));
  }

//  @Test
//  void testCheckMandatoryCarFM() {
//    FeatureModel fm = ExampleFmCreatorCopy.getSimpleFm();
//    List<String> mandatoryFeatures = FeatureModelAnalyzer.mandatoryFeatureNames(fm);
//    System.out.println(mandatoryFeatures);
//  }
}
