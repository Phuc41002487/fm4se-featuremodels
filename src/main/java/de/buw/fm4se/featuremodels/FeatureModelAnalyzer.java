package de.buw.fm4se.featuremodels;

import java.util.ArrayList;
import java.util.List;

import de.buw.fm4se.featuremodels.exec.LimbooleExecutor;
import de.buw.fm4se.featuremodels.fm.CrossTreeConstraint;
import de.buw.fm4se.featuremodels.fm.Feature;
import de.buw.fm4se.featuremodels.fm.FeatureModel;

/**
 * This code needs to be implemented by translating FMs to input for Limboole
 * and interpreting the output
 *
 */
public class FeatureModelAnalyzer {

  public static boolean checkConsistent(FeatureModel fm) throws Exception {
    String formula = FeatureModelTranslator.translateToFormula(fm);
    String result;
    try {
      result = LimbooleExecutor.runLimboole(formula, true);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    if (result.contains("UNSATISFIABLE")) {
      return false;
    }
    return true;
  }

  public static List<String> deadFeatureNames(FeatureModel fm) throws Exception {
    List<String> deadFeatures = new ArrayList<>();
    //addDeadFeature(fm, fm.getRoot(), deadFeatures);
    addDeadFeature(fm.getRoot(), deadFeatures, fm.getConstraints());
    // TODO check for dead features
    return deadFeatures;
  }

  private static void addDeadFeature(FeatureModel featureModel, Feature feature, List<String> deadFeatures) throws Exception {
    featureModel.setRoot(feature);
    if(!checkConsistent(featureModel)){
      deadFeatures.add(feature.getName());
    }
    for (Feature child : feature.getChildren()) {
      addDeadFeature(featureModel, child, deadFeatures);
    }
  }

  private static void addDeadFeature(Feature feature, List<String> deadFeatures, List<CrossTreeConstraint> constraints) throws Exception {
    FeatureModel tempModel = new FeatureModel();
    tempModel.setRoot(feature);
    for(CrossTreeConstraint constraint : constraints) {
      tempModel.addConstraint(constraint);
    }
    if(!checkConsistent(tempModel)){
      deadFeatures.add(feature.getName());
    }
    for (Feature child : feature.getChildren()) {
      addDeadFeature(child, deadFeatures, constraints);
    }
  }

  public static List<String> mandatoryFeatureNames(FeatureModel fm) {
    List<String> mandatoryFeatures = new ArrayList<>();

    // TODO check for mandatory features
    addMandatoryChildren(fm.getRoot(), mandatoryFeatures);
    return mandatoryFeatures;
  }

  private static void addMandatoryChildren(Feature feature, List<String> mandatoryFeatures) {
    if(feature.isMandatory()) {
      mandatoryFeatures.add(feature.getName());
    }
    /* Check children */
    for (Feature childFeature : feature.getChildren()) {
      addMandatoryChildren(childFeature, mandatoryFeatures);
    }
  }
}
