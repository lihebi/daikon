// ***** This file is automatically generated from SeqComparison.java.jpp

package daikon.inv.binary.twoSequence;

import daikon.*;
import daikon.inv.*;
import daikon.inv.binary.twoScalar.*;
import daikon.inv.binary.twoString.*;

import utilMDE.*;

import java.util.*;

/**
 * Compares two sequences.  If order does matter, then sequences are
 * compared lexically.  We assume that if repeats don't matter, then
 * the given data contains only one instance of an element.  If order
 * doesn't matter, then we do a double subset comparison to test
 * equality.  If the two Aux fields of the VarInfos are not identical,
 * then we don't compare at all.
 **/
public class SeqComparison
  extends TwoSequence
  implements Comparison
{
  // We are Serializable, so we specify a version to allow changes to
  // method signatures without breaking serialization.  If you add or
  // remove fields, you should change this number to the current date.
  static final long serialVersionUID = 20020122L;

  // Variables starting with dkconfig_ should only be set via the
  // daikon.config.Configuration interface.
  /**
   * Boolean.  True iff SeqComparison invariants should be considered.
   **/
  public static boolean dkconfig_enabled = true;

  static Comparator comparator = new ArraysMDE.LongArrayComparatorLexical();

  public final boolean only_check_eq;

  public boolean can_be_eq = false;
  public boolean can_be_lt = false;
  public boolean can_be_gt = false;

  boolean orderMatters;

  private ValueTracker values_cache = new ValueTracker(8);

  protected SeqComparison(PptSlice ppt, boolean only_eq,
                      boolean order, boolean excludeEquality) {
    super(ppt);
    only_check_eq = only_eq;
    orderMatters = order;
    if (excludeEquality) can_be_eq = true;
  }

  //   public static SeqComparison instantiate(PptSlice ppt) {
  //     return instantiate (ppt, false);
  //   }

  public static SeqComparison instantiate(PptSlice ppt, boolean onlyEq,
                                      boolean excludeEquality) {
    if (!dkconfig_enabled) return null;

    VarInfo var1 = ppt.var_infos[0];
    VarInfo var2 = ppt.var_infos[1];

    // System.out.println("Ppt: " + ppt.name);
    // System.out.println("vars[0]: " + var1.type.format());
    // System.out.println("vars[1]: " + var2.type.format());

    ProglangType type1 = var1.type;
    ProglangType type2 = var2.type;
    // This intentonally checks dimensions(), not pseudoDimensions.
    boolean only_eq = (! ((type1.dimensions() == 1)
                          && type1.baseIsIntegral()
                          && (type2.dimensions() == 1)
                          && type2.baseIsIntegral()));
    if (onlyEq) only_eq = true;
    // System.out.println("only_eq: " + only_eq);
    if (var1.aux.getFlag(VarInfoAux.HAS_ORDER)
        && var2.aux.getFlag(VarInfoAux.HAS_ORDER)) {
      return new SeqComparison(ppt, only_eq, true, excludeEquality);
    } else {
      return new SeqComparison(ppt, true, false, excludeEquality);
    }
  }

  protected Object clone() {
    SeqComparison result = (SeqComparison) super.clone();
    result.values_cache = (ValueTracker) values_cache.clone();
    return result;
  }

  protected Invariant resurrect_done_swapped() {
    boolean tmp = can_be_lt;
    can_be_lt = can_be_gt;
    can_be_gt = tmp;
    return this;
  }

  public String repr() {
    return "SeqComparison" + varNames() + ": "
      + "can_be_eq=" + can_be_eq
      + ",can_be_lt=" + can_be_lt
      + ",can_be_gt=" + can_be_gt
      + ",only_check_eq=" + only_check_eq
      + ",orderMatters=" + orderMatters
      + ",enoughSamples=" + enoughSamples()
      ;
  }

  public String format_using(OutputFormat format) {
    // System.out.println("Calling SeqComparison.format for: " + repr());
    String comparator = IntComparisonCore.format_comparator
      (format, can_be_lt, can_be_eq, can_be_gt);

    if ((format == OutputFormat.DAIKON)
        || (format == OutputFormat.JAVA))
    {
      String name1 = var1().name.name_using(format);
      String name2 = var2().name.name_using(format);
      String lexically = (var1().aux.getFlag(VarInfoAux.HAS_ORDER)
                          ? " (lexically)"
                          : "");
      return name1 + " " + comparator + " " + name2 + lexically;
    }

    if (format == OutputFormat.IOA) {
      if (var1().isIOASet() || var2().isIOASet()) {
        return "Not valid for Sets: " + format();
      }
      String name1 = var1().name.name_using(format);
      String name2 = var2().name.name_using(format);
      return name1 + " " + comparator + " " + name2 + " ***";
    }

    if (format == OutputFormat.JML) { // Must complete
      String quantResult[] = VarInfoName.QuantHelper.format_jml(new VarInfoName[] {var1().name,var2().name},true);
      return quantResult[0] + quantResult[1] + comparator + quantResult[2] + quantResult[3];
    }
    return format_unimplemented(format);
  }

  public void add_modified(long[] v1, long[] v2, int count) {
    /// This does not do the right thing; I really want to avoid comparisons
    /// if one is missing, but not if one is zero-length.
    // // Don't make comparisons with empty arrays.
    // if ((v1.length == 0) || (v2.length == 0)) {
    //   return;
    // }
    int comparison = 0;
    if (orderMatters) {
      // Standard element wise comparison
       comparison = comparator.compare(v1, v2);
    } else {
      // Do a double subset comparison
      comparison = ArraysMDE.isSubset (v1, v2) && ArraysMDE.isSubset (v2, v1) ? 0 : -1;
    }

    // System.out.println("SeqComparison" + varNames() + ": "
    //                    + "compare(" + ArraysMDE.toString(v1)
    //                    + ", " + ArraysMDE.toString(v2) + ") = " + comparison);

    boolean new_can_be_eq = can_be_eq;
    boolean new_can_be_lt = can_be_lt;
    boolean new_can_be_gt = can_be_gt;
    boolean changed = false;
    if (comparison == 0) {
      new_can_be_eq = true;
      if (!can_be_eq) changed = true;
    } else if (comparison < 0) {
      new_can_be_lt = true;
      if (!can_be_lt) changed = true;
    } else {
      new_can_be_gt = true;
      if (!can_be_gt) changed = true;
    }

    if (! changed) {
      values_cache.add(v1, v2);
      return;
    }

    if ((new_can_be_lt && new_can_be_gt)
        || (only_check_eq && (new_can_be_lt || new_can_be_gt))) {
      destroyAndFlow();
      return;
    }

    // changed but didn't die
    cloneAndFlow();
    can_be_eq = new_can_be_eq;
    can_be_lt = new_can_be_lt;
    can_be_gt = new_can_be_gt;

    values_cache.add(v1, v2);
    Assert.assertTrue (!(can_be_lt && can_be_gt));
    Assert.assertTrue (can_be_gt || can_be_lt || can_be_eq);
  }

  protected double computeProbability() {
    if (falsified) {
      return Invariant.PROBABILITY_NEVER;
    } else if (can_be_lt || can_be_gt) {
      // System.out.println("prob = " + Math.pow(.5, ppt.num_values()) + " for " + format());
      return Math.pow(.5, values_cache.num_values());
    } else if (ppt.num_samples() == 0) {
      return Invariant.PROBABILITY_UNJUSTIFIED;
    } else {
      return Invariant.PROBABILITY_JUSTIFIED;
    }
  }

  // For Comparison interface
  public double eq_probability() {
    if (can_be_eq && (!can_be_lt) && (!can_be_gt))
      return computeProbability();
    else
      return Invariant.PROBABILITY_NEVER;
  }

  public boolean isSameFormula(Invariant o)
  {
    SeqComparison other = (SeqComparison) o;
    return
      (can_be_eq == other.can_be_eq) &&
      (can_be_lt == other.can_be_lt) &&
      (can_be_gt == other.can_be_gt);
  }

  public boolean isExclusiveFormula(Invariant o)
  {
    if (o instanceof SeqComparison) {
      SeqComparison other = (SeqComparison) o;
      return (! ((can_be_eq && other.can_be_eq)
                 || (can_be_lt && other.can_be_lt)
                 || (can_be_gt && other.can_be_gt)));
    }
    return false;
  }

  /**
   *  Since this invariant can be a postProcessed equality, we have to
   *  handle isObvious especially to avoid circular isObvious
   *  relations.
   **/
  public VarInfo[] isObviousStatically_SomeInEquality() {
    if (var1().equalitySet == var2().equalitySet) {
      if (isObviousStatically (this.ppt.var_infos)) {
        return this.ppt.var_infos;
      } else {
        return null;
      }
    } else {
      return super.isObviousStatically_SomeInEquality();
    }
  }

  /**
   *  Since this invariant can be a postProcessed equality, we have to
   *  handle isObvious especially to avoid circular isObvious
   *  relations.
   **/
  public VarInfo[] isObviousDynamically_SomeInEquality() {
    if (var1().equalitySet == var2().equalitySet) {
      if (isObviousDynamically (this.ppt.var_infos)) {
        return this.ppt.var_infos;
      } else {
        return null;
      }
    } else {
      return super.isObviousDynamically_SomeInEquality();
    }
  }

  public boolean isObviousStatically(VarInfo[] vis) {
    VarInfo var1 = vis[0];
    VarInfo var2 = vis[1];
    if ((SubSequence.isObviousSubSequence(var1, var2))
        || (SubSequence.isObviousSubSequence(var2, var1))) {
      return true;
    }
    return super.isObviousStatically (vis);
  }
  
  public boolean isObviousDynamically(VarInfo[] vis) {
    PptSlice ppt = this.ppt.parent.findSlice_unordered (vis);
    if (ppt != null) {
       PairwiseIntComparison pic = PairwiseIntComparison.find(ppt);
       if ((pic != null)
           && (pic.core.can_be_eq == can_be_eq)
           && (pic.core.can_be_lt == can_be_lt)
           && (pic.core.can_be_gt == can_be_gt)) {
         return true;
       }
    }
    return super.isObviousDynamically(vis);
  }

  public void repCheck() {
    super.repCheck();
    if (! (this.can_be_eq || this.can_be_lt || this.can_be_gt)
        && ppt.num_samples() != 0) {
      System.err.println (this.repr());
      System.err.println (this.ppt.num_samples());
      throw new Error();
    }
  }
}
