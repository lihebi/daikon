package daikon.derive.binary;

import daikon.*;
import daikon.derive.*;

import utilMDE.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;

import org.apache.log4j.Category;

/**
 * Derived variable representing the "join" of two sequences.  That
 * is, if the two sequences came from the same original data structure
 * (like an array of multi-field objects) then we join the two
 * sequences and generate a hashcode of the join value.  This allows
 * us to detect uniqueness and equality style invariants across the
 * data structure rather than just one slice of it.  Works for number
 * and string arrays.
 *
 **/

public final class SequencesJoin  extends BinaryDerivation {

  /**
   * Debugging logger
   *
   **/
  public static final Category debug = Category.getInstance(SequencesJoin.class.getName());

  // Variables starting with dkconfig_ should only be set via the
  // daikon.config.Configuration interface.
  public static boolean dkconfig_enabled = true;

  public VarInfo var1() { return base1; }
  public VarInfo var2() { return base2; }


  /**
   * Create a new SequencesJoin derivation.
   * @param vi1 
   * @param vi2 The two variables this is based on
   **/
  public SequencesJoin (VarInfo vi1, VarInfo vi2) {
    super(vi1, vi2);
  }

  /**
   * Returns a new sequence of hashcodes that is the join of the
   * hashcodes of the component sequences.  This is modified whenever
   * either component sequence is modified.
   * @param full_vt the value tuple of a program point to compute the
   * derived value from.
   **/
  public ValueAndModified computeValueAndModified(ValueTuple full_vt) {
    Object val1 = var1().getValue(full_vt);
    Object val2 = var2().getValue(full_vt);

    int length1 = -1;
    int length2 = -2; // They must equal in the end

    if (val1 == null) {
      length1 = 0;
    }

    if (val2 == null) {
      length2 = 0;
    }

    if (val1 instanceof long[]) {
      length1 = ((long[]) val1).length;
    }

    if (val2 instanceof long[]) {
      length2 = ((long[]) val2).length;
    }

    if (val1 instanceof Object[]) {
      length1 = ((long[]) val1).length;
    }

    if (val2 instanceof Object[]) {
      length2 = ((long[]) val2).length;
    }

    Assert.assert(length1 == length2);

    /**
       debug.debug ("var1 name: " + var1().name);
       if (val1 != null) debug.debug ("val1 type: " + val1.getClass().getName());
       debug.debug ("var2 name: " + var2().name);
       if (val2 != null) debug.debug ("val2 type: " + val2.getClass().getName());
    **/

    long[] result = new long[length1];


    for (int i = 0; i < length1; i++) {
      Object e1 = null;
      Object e2 = null;
      if (val1 instanceof long[]) {
	e1 = new Long (((long[]) val1) [i]);
      }
      if (val2 instanceof long[]) {
	e2 = new Long (((long[]) val2) [i]);
      }
      if (val1 instanceof Object[]) {
	e1 = ((Object[]) val1) [i];
      }
      if (val2 instanceof Object[]) {
	e2 = ((Object[]) val2) [i];
      }
      if (e1 == null) e1 = new Long(0);
      if (e2 == null) e2 = new Long(0);
      result[i] = e1.hashCode() ^ (e2.hashCode() + 214);
    }

    int mod = ValueTuple.UNMODIFIED;
    if (var1().getModified(full_vt) == ValueTuple.MODIFIED) mod = ValueTuple.MODIFIED;
    if (var2().getModified(full_vt) == ValueTuple.MODIFIED) mod = ValueTuple.MODIFIED;
    if (var1().getModified(full_vt) == ValueTuple.MISSING) mod = ValueTuple.MISSING;
    if (var2().getModified(full_vt) == ValueTuple.MISSING) mod = ValueTuple.MISSING;

    return new ValueAndModified(Intern.intern(result), mod);
  }
  


  protected VarInfo makeVarInfo() {
    VarInfo var1 = var1();
    VarInfo var2 = var2();
    String newTypeName = "Join(" + var1.type.toString() + "," + var2.type.toString() + ")";
    ProglangType decltype = 
      ProglangType.parse (newTypeName + "[]");

    if (debug.isDebugEnabled()) {
      debug.debug ("New decl type is " + decltype.toString());
    }

    VarComparability indexType = var1.comparability.indexType(0);

    // Generate a new comparability based on base comparabilities' indices
    VarComparability comparability = VarComparability.makeComparabilitySameIndices (newTypeName,
										    var1.comparability);

    return new VarInfo(var1.name.applyFunctionOfTwo("join", var2.name),
		       decltype,
		       ProglangType.HASHCODE_ARRAY,
		       comparability
		       );
  }

  public String toString() {
    return "[SequencesJoin of " + var1().toString() + " " + var2().toString() + "]";

  }

  public  boolean isSameFormula(Derivation other) {
    return (other instanceof SequencesJoin);
  }

}
