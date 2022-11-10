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

  public static boolean checkConsistent(FeatureModel fm) {
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

  public static List<String> deadFeatureNames(FeatureModel fm) {
    List<String> deadFeatures = new ArrayList<>();
    // TODO check for dead features
    addDeadFeature(fm, fm.getRoot(), deadFeatures);
    return deadFeatures;
  }


  /**
   * @param featureModel feature model
   * @param feature feature
   * @param deadFeatures list of dead features
   * The function add dead feature to the list
   */
  private static void addDeadFeature(FeatureModel featureModel, Feature feature, List<String> deadFeatures) {
    featureModel.setRoot(feature);
    if(!checkConsistent(featureModel)){
      addAllFeatureToDead(feature, deadFeatures);
    } else {
      for (Feature child : feature.getChildren()) {
        addDeadFeature(featureModel, child, deadFeatures);
      }
    }
  }

  /**
   * @param feature input
   * @param deadFeatures list of dead feature
   * The function add the feature and all children of it to dead feature list
   */
  private static void addAllFeatureToDead(Feature feature, List<String> deadFeatures) {
    deadFeatures.add(feature.getName());
    for (Feature child : feature.getChildren()) {
      addAllFeatureToDead(child, deadFeatures);
    }
  }

  public static List<String> mandatoryFeatureNames(FeatureModel fm) {
    List<String> mandatoryFeatures = new ArrayList<>();

    mandatoryFeatures.add(fm.getRoot().getName());
    // TODO check for mandatory features
    for (Feature child : fm.getRoot().getChildren()) {
      addMandatoryChildren(child, mandatoryFeatures);
    }
    return mandatoryFeatures;
  }


  /**
   * @param feature feature
   * @param mandatoryFeatures mandatory feature list
   * The function add mandatory feature to the list
   */
  private static void addMandatoryChildren(Feature feature, List<String> mandatoryFeatures) {
    if(feature.isMandatory()) {
      mandatoryFeatures.add(feature.getName());
      /* Check children */
      for (Feature childFeature : feature.getChildren()) {
        addMandatoryChildren(childFeature, mandatoryFeatures);
      }
    }
  }
}
