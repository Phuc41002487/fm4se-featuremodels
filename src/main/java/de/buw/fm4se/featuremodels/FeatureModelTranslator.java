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
  public static String translateToFormula(FeatureModel fm) throws Exception {
    
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
    System.out.println(result);
    return result;
  }

  private static void printFeature (Feature feature, List<String> formulaMembers) throws Exception {
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

  private static String printGroupChildNone (String parent, String feature, boolean mandatory) {
    if(mandatory) {
      return MessageFormat.format("({0} <-> {1})", feature, parent);
    } else {
      return MessageFormat.format("({0} -> {1})", feature, parent);
    }
  }

  private static String printGroupChildOr (Feature feature) {
    String temp = MessageFormat.format("({0} -> (", feature.getName());
    for (int i=0; i<feature.getChildren().size(); i++) {
      if (i < feature.getChildren().size() - 1) {
        temp += feature.getChildren().get(i).getName() + " | ";
      } else {
        temp += feature.getChildren().get(i).getName();
      }
    }
    return temp + "))";
  }

  private static String printGroupChildXor (Feature feature) throws Exception {
    if (feature.getChildren().size() > 2) {
      throw new Exception("Cannot print formula for group child Xor if number of children is more than 2");
    } else {
      List<Feature> children = feature.getChildren();
      String result = MessageFormat.format("({0} -> (!{1} & {2}) | ({1} & !{2}))", feature.getName(), children.get(0).getName(), children.get(1).getName());
      return result;
    }
  }

  private static String printConstraint (String left, Kind kind, String right) {
    if(kind.equals(Kind.EXCLUDES)) {
      return MessageFormat.format("!({0} & {1})", left, right);
    } else {
      return MessageFormat.format("({0} -> {1})", left, right);
    }
  }
}
