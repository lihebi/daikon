package daikon.derive.binary;

import daikon.*;
import daikon.derive.*;

import utilMDE.*;

// *****
// Do not edit this file directly:
// it is automatically generated from SequenceSubscript.java.jpp
// *****

public final class SequenceStringSubscript  extends BinaryDerivation {

  // Variables starting with dkconfig_ should only be set via the
  // daikon.config.Configuration interface.
  public static boolean dkconfig_enabled = true;

  // base1 is the sequence
  // base2 is the scalar
  public VarInfo seqvar() { return base1; }
  public VarInfo sclvar() { return base2; }

  // Indicates whether the subscript is an index of valid data or a limit
  // (one element beyond the data of interest).
  // Value is -1 or 0.
  public final int index_shift;

  public SequenceStringSubscript (VarInfo vi1, VarInfo vi2, boolean less1) {
    super(vi1, vi2);
    if (less1)
      index_shift = -1;
    else
      index_shift = 0;
  }

  public ValueAndModified computeValueAndModified(ValueTuple full_vt) {
    int mod1 = base1.getModified(full_vt);
    if (mod1 == ValueTuple.MISSING)
      return ValueAndModified.MISSING;
    int mod2 = base2.getModified(full_vt);
    if (mod2 == ValueTuple.MISSING)
      return ValueAndModified.MISSING;
    Object val1 = base1.getValue(full_vt);
    if (val1 == null)
      return ValueAndModified.MISSING;
    String [] val1_array = (String []) val1;
    int val2 = base2.getIndexValue(full_vt) + index_shift;
    if ((val2 < 0) || (val2 >= val1_array.length))
      return ValueAndModified.MISSING;
    String  val = val1_array[val2];
    int mod = (((mod1 == ValueTuple.UNMODIFIED)
		&& (mod2 == ValueTuple.UNMODIFIED))
	       ? ValueTuple.UNMODIFIED
	       : ValueTuple.MODIFIED);
    return new ValueAndModified( val  , mod);
  }

  protected VarInfo makeVarInfo() {
    VarInfo seqvar = seqvar();

    VarInfoName index = sclvar().name;
    switch (index_shift) {
    case 0:
      break;
    case -1:
      index = index.applyDecrement();
      break;
    default:
      throw new UnsupportedOperationException("Unsupported shift: " + index_shift);
    }

    VarInfoName name = seqvar.name.applySubscript(index);
    ProglangType type = seqvar.type.elementType();
    ProglangType file_rep_type = seqvar.file_rep_type.elementType();
    VarComparability compar = base1.comparability.elementType();
    return new VarInfo(name, type, file_rep_type, compar);
  }

  public  boolean isSameFormula(Derivation other) {
    return (other instanceof SequenceStringSubscript )
      && (((SequenceStringSubscript ) other).index_shift == this.index_shift);
  }

}

