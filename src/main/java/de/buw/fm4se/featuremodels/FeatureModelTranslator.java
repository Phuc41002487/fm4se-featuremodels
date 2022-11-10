package de.buw.fm4se.featuremodels;

import de.buw.fm4se.featuremodels.fm.CrossTreeConstraint.Kind;

import de.buw.fm4se.featuremodels.fm.CrossTreeConstraint;
import de.buw.fm4se.featuremodels.fm.Feature;
import de.buw.fm4se.featuremodels.fm.FeatureModel;
import de.buw.fm4se.featuremodels.fm.GroupKind;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class FeatureModelTranslator {
  public static String translateToFormula(FeatureModel fm) {

    // TODO implement a real translation
    String result = "";
    String rootName = fm.getRoot().getName();
    List<String> temp = new ArrayList<>();
    temp.add(rootName);
    printFeature(fm.getRoot(), temp);
    for (String member : temp) {
      if(member.equals(temp.get(0))) {
        result += member;
      } else {
        result += " & " + member;
      }
    }
    /* add constraints */
    for (CrossTreeConstraint constraint : fm.getConstraints()) {
      result += " & " + printConstraint(constraint.getLeft().getName(), constraint.getKind(), constraint.getRight().getName());
    }
    return result;
  }

  /**
   * @param feature input feature
   * @param formulaMembers List of String contains member of the Semantic formula
   */
  private static void printFeature (Feature feature, List<String> formulaMembers) {
    GroupKind groupKind = feature.getChildGroupKind();
    if (groupKind.equals(GroupKind.NONE)) {
      for (Feature child : feature.getChildren()) {
        formulaMembers.add(printGroupChildNone(feature.getName(), child.getName(), child.isMandatory()));
      }
    } else if (groupKind.equals(GroupKind.OR)) {
      formulaMembers.add(printGroupChildOr(feature));
    } else {
      formulaMembers.add(printGroupChildXor(feature));
    }
    /* add Children if applicable */
    for (Feature child : feature.getChildren()) {
      printFeature(child, formulaMembers);
    }
  }

  /**
   * @param parent parent feature
   * @param feature child feature
   * @param mandatory mandatory attribute
   * @return semantic String describe relationship of parent and child
   */
  private static String printGroupChildNone (String parent, String feature, boolean mandatory) {
    if(mandatory) {
      return MessageFormat.format("({0} <-> {1})", feature, parent);
    } else {
      return MessageFormat.format("({0} -> {1})", feature, parent);
    }
  }

  /**
   * @param feature input feature
   * @return semantic String group OR
   */
  private static String printGroupChildOr (Feature feature) {
    StringBuilder temp = new StringBuilder(MessageFormat.format("({0} -> (", feature.getName()));
    for (int i=0; i<feature.getChildren().size(); i++) {
      if (i < feature.getChildren().size() - 1) {
        temp.append(feature.getChildren().get(i).getName()).append(" | ");
      } else {
        temp.append(feature.getChildren().get(i).getName());
      }
    }
    return temp + "))";
  }

  /**
   * @param feature input feature
   * @return semantic String Group XOR
   */
  private static String printGroupChildXor (Feature feature) {
    List<String> finalList = new ArrayList<>();
    for(int i = 0; i < feature.getChildren().size(); i++) {
      List<String> tempList = new ArrayList<>();
      Feature positiveChild = feature.getChildren().get(i);
      tempList.add(positiveChild.getName());
      for (int j = 0; j<feature.getChildren().size(); j++) {
        Feature negativeChild = feature.getChildren().get(j);
        if(!negativeChild.equals(positiveChild)){
          tempList.add("!" + negativeChild.getName());
        }
      }
      finalList.add(concatenateTempListStringXOR(tempList));
    }
    return MessageFormat.format("({0} -> {1})", feature.getName(), concatenateFinalListStringXOR(finalList));
  }

  /**
   * @param tempList input list which contains the positive child and negative child
   * @return a semantic String which be used to construct the final list
   */
  private static String concatenateTempListStringXOR (List<String> tempList) {
    StringBuilder result = new StringBuilder();
    result.append("(");
    for(int i = 0; i < tempList.size(); i++) {
      if (i < tempList.size() - 1) {
        result.append(tempList.get(i)).append(" & ");
      } else {
        result.append(tempList.get(i));
      }
    }
    result.append(")");
    return result.toString();
  }

  /**
   * @param finalList input list which contains the all the sub-member of group XOR
   * @return semantic String of XOR group
   */
  private static String concatenateFinalListStringXOR (List<String> finalList) {
    StringBuilder result = new StringBuilder();
    result.append("(");
    for(int i = 0; i < finalList.size(); i++) {
      if (i < finalList.size() - 1) {
        result.append(finalList.get(i)).append(" | ");
      } else {
        result.append(finalList.get(i));
      }
    }
    result.append(")");
    return result.toString();
  }

  /**
   * @param left left
   * @param kind type of constraint
   * @param right right
   * @return Constraint Semantic String for Constraint
   */
  private static String printConstraint (String left, Kind kind, String right) {
    if(kind.equals(Kind.EXCLUDES)) {
      return MessageFormat.format("!({0} & {1})", left, right);
    } else {
      return MessageFormat.format("({0} -> {1})", left, right);
    }
  }
}
