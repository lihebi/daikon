package daikon.inv.filter;

import daikon.inv.*;
import daikon.inv.filter.*;
import daikon.VarInfo;
import daikon.PrintInvariants;
import daikon.VarInfoAux;

/**
 * Filter for not printing an Invariant if its VarInfos return
 * isDerivedParameterAndUninteresting == true.
 **/
public class DerivedParameterFilter extends InvariantFilter {
  public String getDescription() {
    return "Suppress parameter-derived postcondition invariants";
  }

  /**
   * Returns true if the invariant describes changes made to
   * pass-by-value parameters that shouldn't be part of a routine's
   * visible interface. E.g, suppose that "param" is a parameter to a
   * Java method. If "param" itself is modified, that change won't be
   * visible to a caller, so we shouldn't print it. If "param" points
   * to an object, and that object is changed, that is visible, but
   * only if "param" hasn't changed; otherwise, we're reporting a
   * change in some object other than the one that was passed in.
   *
   * More specifically, return true if the invariant is a post-state
   * invariant, and a variable in it is either a parameter, or a
   * variable derived from a parameter, when we think that the
   * parameter itself may have changed by virtue of not having a
   * "param == orig(param)" invariant. */

  boolean shouldDiscardInvariant( Invariant inv ) {
    if (inv.ppt.ppt_name.isExitPoint()) {
      PrintInvariants.debugFiltering.fine ("\tconsidering DPF for vars " + inv.ppt.var_infos.toString() + "\n");
      for (int i = 0; i < inv.ppt.var_infos.length; i++) {
        VarInfo vi = inv.ppt.var_infos[i];
        // ppt has to be a PptSlice, not a PptTopLevel
        PrintInvariants.debugFiltering.fine ("\tconsidering DPF for " + vi.name.name() + "\n");
        if (vi.isDerivedParamAndUninteresting()) {
          inv.discardCode = DiscardCode.derived_param;
          inv.discardString = "Derived and uninteresting VarInfo: "+vi.name.name();
          return true;
        }
      }
    }
    return false;
  }
}
