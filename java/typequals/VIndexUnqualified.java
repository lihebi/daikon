package typequals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TypeQualifier;

/** Not an index into a ValueTuple nor into a list of VarInfo objects. */
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
@TypeQualifier
@SubtypeOf(VIndexTop.class)
@DefaultQualifierInHierarchy
public @interface VIndexUnqualified {}
