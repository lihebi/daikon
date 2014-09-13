//
// Generated by JTB 1.3.2
//

package jtb.visitor;
import jtb.syntaxtree.*;
import java.util.*;

/**
 * All GJ visitors with no argument must implement this interface.
 */

public interface GJNoArguVisitor<R> {

   //
   // GJ Auto class visitors with no argument
   //

   public R visit(NodeList n);
   public R visit(NodeListOptional n);
   public R visit(NodeOptional n);
   public R visit(NodeSequence n);
   public R visit(NodeToken n);

   //
   // User-generated visitor methods below
   //

   // f0 -> [ PackageDeclaration() ]
   // f1 -> ( ImportDeclaration() )*
   // f2 -> ( TypeDeclaration() )*
   // f3 -> <EOF>
   public R visit(CompilationUnit n);

   // f0 -> "package"
   // f1 -> Name()
   // f2 -> ";"
   public R visit(PackageDeclaration n);

   // f0 -> "import"
   // f1 -> [ "static" ]
   // f2 -> Name()
   // f3 -> [ "." "*" ]
   // f4 -> ";"
   public R visit(ImportDeclaration n);

   // f0 -> ( ( "public" | "static" | "protected" | "private" | "final" | "abstract" | "synchronized" | "native" | "transient" | "volatile" | "strictfp" | Annotation() ) )*
   public R visit(Modifiers n);

   // f0 -> ";"
   //       | Modifiers() ( ClassOrInterfaceDeclaration(modifiers) | EnumDeclaration(modifiers) | AnnotationTypeDeclaration(modifiers) )
   public R visit(TypeDeclaration n);

   // f0 -> ( "class" | "interface" )
   // f1 -> <IDENTIFIER>
   // f2 -> [ TypeParameters() ]
   // f3 -> [ ExtendsList(isInterface) ]
   // f4 -> [ ImplementsList(isInterface) ]
   // f5 -> ClassOrInterfaceBody(isInterface)
   public R visit(ClassOrInterfaceDeclaration n);

   // f0 -> "extends"
   // f1 -> ClassOrInterfaceType()
   // f2 -> ( "," ClassOrInterfaceType() )*
   public R visit(ExtendsList n);

   // f0 -> "implements"
   // f1 -> ClassOrInterfaceType()
   // f2 -> ( "," ClassOrInterfaceType() )*
   public R visit(ImplementsList n);

   // f0 -> "enum"
   // f1 -> <IDENTIFIER>
   // f2 -> [ ImplementsList(false) ]
   // f3 -> EnumBody()
   public R visit(EnumDeclaration n);

   // f0 -> "{"
   // f1 -> [ EnumConstant() ( "," EnumConstant() )* ]
   // f2 -> [ "," ]
   // f3 -> [ ";" ( ClassOrInterfaceBodyDeclaration(false) )* ]
   // f4 -> "}"
   public R visit(EnumBody n);

   // f0 -> <IDENTIFIER>
   // f1 -> [ Arguments() ]
   // f2 -> [ ClassOrInterfaceBody(false) ]
   public R visit(EnumConstant n);

   // f0 -> "<"
   // f1 -> TypeParameter()
   // f2 -> ( "," TypeParameter() )*
   // f3 -> ">"
   public R visit(TypeParameters n);

   // f0 -> <IDENTIFIER>
   // f1 -> [ TypeBound() ]
   public R visit(TypeParameter n);

   // f0 -> "extends"
   // f1 -> ClassOrInterfaceType()
   // f2 -> ( "&" ClassOrInterfaceType() )*
   public R visit(TypeBound n);

   // f0 -> "{"
   // f1 -> ( ClassOrInterfaceBodyDeclaration(isInterface) )*
   // f2 -> "}"
   public R visit(ClassOrInterfaceBody n);

   // f0 -> Initializer()
   //       | Modifiers() ( ClassOrInterfaceDeclaration(modifiers) | EnumDeclaration(modifiers) | ConstructorDeclaration() | FieldDeclaration(modifiers) | MethodDeclaration(modifiers) )
   //       | ";"
   public R visit(ClassOrInterfaceBodyDeclaration n);

   // f0 -> Type()
   // f1 -> VariableDeclarator()
   // f2 -> ( "," VariableDeclarator() )*
   // f3 -> ";"
   public R visit(FieldDeclaration n);

   // f0 -> VariableDeclaratorId()
   // f1 -> [ "=" VariableInitializer() ]
   public R visit(VariableDeclarator n);

   // f0 -> <IDENTIFIER>
   // f1 -> ( "[" "]" )*
   public R visit(VariableDeclaratorId n);

   // f0 -> ArrayInitializer()
   //       | Expression()
   public R visit(VariableInitializer n);

   // f0 -> "{"
   // f1 -> [ VariableInitializer() ( "," VariableInitializer() )* ]
   // f2 -> [ "," ]
   // f3 -> "}"
   public R visit(ArrayInitializer n);

   // f0 -> [ TypeParameters() ]
   // f1 -> ResultType()
   // f2 -> MethodDeclarator()
   // f3 -> [ "throws" NameList() ]
   // f4 -> ( Block() | ";" )
   public R visit(MethodDeclaration n);

   // f0 -> <IDENTIFIER>
   // f1 -> FormalParameters()
   // f2 -> ( "[" "]" )*
   public R visit(MethodDeclarator n);

   // f0 -> "("
   // f1 -> [ FormalParameter() ( "," FormalParameter() )* ]
   // f2 -> ")"
   public R visit(FormalParameters n);

   // f0 -> [ "final" ]
   // f1 -> Type()
   // f2 -> [ "..." ]
   // f3 -> VariableDeclaratorId()
   public R visit(FormalParameter n);

   // f0 -> [ TypeParameters() ]
   // f1 -> <IDENTIFIER>
   // f2 -> FormalParameters()
   // f3 -> [ "throws" NameList() ]
   // f4 -> "{"
   // f5 -> [ ExplicitConstructorInvocation() ]
   // f6 -> ( BlockStatement() )*
   // f7 -> "}"
   public R visit(ConstructorDeclaration n);

   // f0 -> "this" Arguments() ";"
   //       | [ PrimaryExpression() "." ] "super" Arguments() ";"
   public R visit(ExplicitConstructorInvocation n);

   // f0 -> [ "static" ]
   // f1 -> Block()
   public R visit(Initializer n);

   // f0 -> ReferenceType()
   //       | PrimitiveType()
   public R visit(Type n);

   // f0 -> PrimitiveType() ( "[" "]" )+
   //       | ( ClassOrInterfaceType() ) ( "[" "]" )*
   public R visit(ReferenceType n);

   // f0 -> <IDENTIFIER>
   // f1 -> [ TypeArguments() ]
   // f2 -> ( "." <IDENTIFIER> [ TypeArguments() ] )*
   public R visit(ClassOrInterfaceType n);

   // f0 -> "<"
   // f1 -> TypeArgument()
   // f2 -> ( "," TypeArgument() )*
   // f3 -> ">"
   public R visit(TypeArguments n);

   // f0 -> ReferenceType()
   //       | "?" [ WildcardBounds() ]
   public R visit(TypeArgument n);

   // f0 -> "extends" ReferenceType()
   //       | "super" ReferenceType()
   public R visit(WildcardBounds n);

   // f0 -> "boolean"
   //       | "char"
   //       | "byte"
   //       | "short"
   //       | "int"
   //       | "long"
   //       | "float"
   //       | "double"
   public R visit(PrimitiveType n);

   // f0 -> "void"
   //       | Type()
   public R visit(ResultType n);

   // f0 -> <IDENTIFIER>
   // f1 -> ( "." <IDENTIFIER> )*
   public R visit(Name n);

   // f0 -> Name()
   // f1 -> ( "," Name() )*
   public R visit(NameList n);

   // f0 -> ConditionalExpression()
   // f1 -> [ AssignmentOperator() Expression() ]
   public R visit(Expression n);

   // f0 -> "="
   //       | "*="
   //       | "/="
   //       | "%="
   //       | "+="
   //       | "-="
   //       | "<<="
   //       | ">>="
   //       | ">>>="
   //       | "&="
   //       | "^="
   //       | "|="
   public R visit(AssignmentOperator n);

   // f0 -> ConditionalOrExpression()
   // f1 -> [ "?" Expression() ":" Expression() ]
   public R visit(ConditionalExpression n);

   // f0 -> ConditionalAndExpression()
   // f1 -> ( "||" ConditionalAndExpression() )*
   public R visit(ConditionalOrExpression n);

   // f0 -> InclusiveOrExpression()
   // f1 -> ( "&&" InclusiveOrExpression() )*
   public R visit(ConditionalAndExpression n);

   // f0 -> ExclusiveOrExpression()
   // f1 -> ( "|" ExclusiveOrExpression() )*
   public R visit(InclusiveOrExpression n);

   // f0 -> AndExpression()
   // f1 -> ( "^" AndExpression() )*
   public R visit(ExclusiveOrExpression n);

   // f0 -> EqualityExpression()
   // f1 -> ( "&" EqualityExpression() )*
   public R visit(AndExpression n);

   // f0 -> InstanceOfExpression()
   // f1 -> ( ( "==" | "!=" ) InstanceOfExpression() )*
   public R visit(EqualityExpression n);

   // f0 -> RelationalExpression()
   // f1 -> [ "instanceof" Type() ]
   public R visit(InstanceOfExpression n);

   // f0 -> ShiftExpression()
   // f1 -> ( ( "<" | ">" | "<=" | ">=" ) ShiftExpression() )*
   public R visit(RelationalExpression n);

   // f0 -> AdditiveExpression()
   // f1 -> ( ( "<<" | RSIGNEDSHIFT() | RUNSIGNEDSHIFT() ) AdditiveExpression() )*
   public R visit(ShiftExpression n);

   // f0 -> MultiplicativeExpression()
   // f1 -> ( ( "+" | "-" ) MultiplicativeExpression() )*
   public R visit(AdditiveExpression n);

   // f0 -> UnaryExpression()
   // f1 -> ( ( "*" | "/" | "%" ) UnaryExpression() )*
   public R visit(MultiplicativeExpression n);

   // f0 -> ( "+" | "-" ) UnaryExpression()
   //       | PreIncrementExpression()
   //       | PreDecrementExpression()
   //       | UnaryExpressionNotPlusMinus()
   public R visit(UnaryExpression n);

   // f0 -> "++"
   // f1 -> PrimaryExpression()
   public R visit(PreIncrementExpression n);

   // f0 -> "--"
   // f1 -> PrimaryExpression()
   public R visit(PreDecrementExpression n);

   // f0 -> ( "~" | "!" ) UnaryExpression()
   //       | CastExpression()
   //       | PostfixExpression()
   public R visit(UnaryExpressionNotPlusMinus n);

   // f0 -> "(" PrimitiveType()
   //       | "(" Type() "[" "]"
   //       | "(" Type() ")" ( "~" | "!" | "(" | <IDENTIFIER> | "this" | "super" | "new" | Literal() )
   public R visit(CastLookahead n);

   // f0 -> PrimaryExpression()
   // f1 -> [ "++" | "--" ]
   public R visit(PostfixExpression n);

   // f0 -> "(" Type() ")" UnaryExpression()
   //       | "(" Type() ")" UnaryExpressionNotPlusMinus()
   public R visit(CastExpression n);

   // f0 -> PrimaryPrefix()
   // f1 -> ( PrimarySuffix() )*
   public R visit(PrimaryExpression n);

   // f0 -> "."
   // f1 -> TypeArguments()
   // f2 -> <IDENTIFIER>
   public R visit(MemberSelector n);

   // f0 -> Literal()
   //       | "this"
   //       | "super" "." <IDENTIFIER>
   //       | "(" Expression() ")"
   //       | AllocationExpression()
   //       | ResultType() "." "class"
   //       | Name()
   public R visit(PrimaryPrefix n);

   // f0 -> "." "this"
   //       | "." "super"
   //       | "." AllocationExpression()
   //       | MemberSelector()
   //       | "[" Expression() "]"
   //       | "." <IDENTIFIER>
   //       | Arguments()
   public R visit(PrimarySuffix n);

   // f0 -> <INTEGER_LITERAL>
   //       | <FLOATING_POINT_LITERAL>
   //       | <CHARACTER_LITERAL>
   //       | <STRING_LITERAL>
   //       | BooleanLiteral()
   //       | NullLiteral()
   public R visit(Literal n);

   // f0 -> "true"
   //       | "false"
   public R visit(BooleanLiteral n);

   // f0 -> "null"
   public R visit(NullLiteral n);

   // f0 -> "("
   // f1 -> [ ArgumentList() ]
   // f2 -> ")"
   public R visit(Arguments n);

   // f0 -> Expression()
   // f1 -> ( "," Expression() )*
   public R visit(ArgumentList n);

   // f0 -> "new" PrimitiveType() ArrayDimsAndInits()
   //       | "new" ClassOrInterfaceType() [ TypeArguments() ] ( ArrayDimsAndInits() | Arguments() [ ClassOrInterfaceBody(false) ] )
   public R visit(AllocationExpression n);

   // f0 -> ( "[" Expression() "]" )+ ( "[" "]" )*
   //       | ( "[" "]" )+ ArrayInitializer()
   public R visit(ArrayDimsAndInits n);

   // f0 -> LabeledStatement()
   //       | AssertStatement()
   //       | Block()
   //       | EmptyStatement()
   //       | StatementExpression() ";"
   //       | SwitchStatement()
   //       | IfStatement()
   //       | WhileStatement()
   //       | DoStatement()
   //       | ForStatement()
   //       | BreakStatement()
   //       | ContinueStatement()
   //       | ReturnStatement()
   //       | ThrowStatement()
   //       | SynchronizedStatement()
   //       | TryStatement()
   public R visit(Statement n);

   // f0 -> "assert"
   // f1 -> Expression()
   // f2 -> [ ":" Expression() ]
   // f3 -> ";"
   public R visit(AssertStatement n);

   // f0 -> <IDENTIFIER>
   // f1 -> ":"
   // f2 -> Statement()
   public R visit(LabeledStatement n);

   // f0 -> "{"
   // f1 -> ( BlockStatement() )*
   // f2 -> "}"
   public R visit(Block n);

   // f0 -> LocalVariableDeclaration() ";"
   //       | Statement()
   //       | ClassOrInterfaceDeclaration(0)
   public R visit(BlockStatement n);

   // f0 -> [ "final" ]
   // f1 -> Type()
   // f2 -> VariableDeclarator()
   // f3 -> ( "," VariableDeclarator() )*
   public R visit(LocalVariableDeclaration n);

   // f0 -> ";"
   public R visit(EmptyStatement n);

   // f0 -> PreIncrementExpression()
   //       | PreDecrementExpression()
   //       | PrimaryExpression() [ "++" | "--" | AssignmentOperator() Expression() ]
   public R visit(StatementExpression n);

   // f0 -> "switch"
   // f1 -> "("
   // f2 -> Expression()
   // f3 -> ")"
   // f4 -> "{"
   // f5 -> ( SwitchLabel() ( BlockStatement() )* )*
   // f6 -> "}"
   public R visit(SwitchStatement n);

   // f0 -> "case" Expression() ":"
   //       | "default" ":"
   public R visit(SwitchLabel n);

   // f0 -> "if"
   // f1 -> "("
   // f2 -> Expression()
   // f3 -> ")"
   // f4 -> Statement()
   // f5 -> [ "else" Statement() ]
   public R visit(IfStatement n);

   // f0 -> "while"
   // f1 -> "("
   // f2 -> Expression()
   // f3 -> ")"
   // f4 -> Statement()
   public R visit(WhileStatement n);

   // f0 -> "do"
   // f1 -> Statement()
   // f2 -> "while"
   // f3 -> "("
   // f4 -> Expression()
   // f5 -> ")"
   // f6 -> ";"
   public R visit(DoStatement n);

   // f0 -> "for"
   // f1 -> "("
   // f2 -> ( Type() <IDENTIFIER> ":" Expression() | [ ForInit() ] ";" [ Expression() ] ";" [ ForUpdate() ] )
   // f3 -> ")"
   // f4 -> Statement()
   public R visit(ForStatement n);

   // f0 -> LocalVariableDeclaration()
   //       | StatementExpressionList()
   public R visit(ForInit n);

   // f0 -> StatementExpression()
   // f1 -> ( "," StatementExpression() )*
   public R visit(StatementExpressionList n);

   // f0 -> StatementExpressionList()
   public R visit(ForUpdate n);

   // f0 -> "break"
   // f1 -> [ <IDENTIFIER> ]
   // f2 -> ";"
   public R visit(BreakStatement n);

   // f0 -> "continue"
   // f1 -> [ <IDENTIFIER> ]
   // f2 -> ";"
   public R visit(ContinueStatement n);

   // f0 -> "return"
   // f1 -> [ Expression() ]
   // f2 -> ";"
   public R visit(ReturnStatement n);

   // f0 -> "throw"
   // f1 -> Expression()
   // f2 -> ";"
   public R visit(ThrowStatement n);

   // f0 -> "synchronized"
   // f1 -> "("
   // f2 -> Expression()
   // f3 -> ")"
   // f4 -> Block()
   public R visit(SynchronizedStatement n);

   // f0 -> "try"
   // f1 -> Block()
   // f2 -> ( "catch" "(" FormalParameter() ")" Block() )*
   // f3 -> [ "finally" Block() ]
   public R visit(TryStatement n);

   // f0 -> ( ">" ">" ">" )
   public R visit(RUNSIGNEDSHIFT n);

   // f0 -> ( ">" ">" )
   public R visit(RSIGNEDSHIFT n);

   // f0 -> NormalAnnotation()
   //       | SingleMemberAnnotation()
   //       | MarkerAnnotation()
   public R visit(Annotation n);

   // f0 -> "@"
   // f1 -> Name()
   // f2 -> "("
   // f3 -> [ MemberValuePairs() ]
   // f4 -> ")"
   public R visit(NormalAnnotation n);

   // f0 -> "@"
   // f1 -> Name()
   public R visit(MarkerAnnotation n);

   // f0 -> "@"
   // f1 -> Name()
   // f2 -> "("
   // f3 -> MemberValue()
   // f4 -> ")"
   public R visit(SingleMemberAnnotation n);

   // f0 -> MemberValuePair()
   // f1 -> ( "," MemberValuePair() )*
   public R visit(MemberValuePairs n);

   // f0 -> <IDENTIFIER>
   // f1 -> "="
   // f2 -> MemberValue()
   public R visit(MemberValuePair n);

   // f0 -> Annotation()
   //       | MemberValueArrayInitializer()
   //       | ConditionalExpression()
   public R visit(MemberValue n);

   // f0 -> "{"
   // f1 -> MemberValue()
   // f2 -> ( "," MemberValue() )*
   // f3 -> [ "," ]
   // f4 -> "}"
   public R visit(MemberValueArrayInitializer n);

   // f0 -> "@"
   // f1 -> "interface"
   // f2 -> <IDENTIFIER>
   // f3 -> AnnotationTypeBody()
   public R visit(AnnotationTypeDeclaration n);

   // f0 -> "{"
   // f1 -> ( AnnotationTypeMemberDeclaration() )*
   // f2 -> "}"
   public R visit(AnnotationTypeBody n);

   // f0 -> Modifiers() ( Type() <IDENTIFIER> "(" ")" [ DefaultValue() ] ";" | ClassOrInterfaceDeclaration(modifiers) | EnumDeclaration(modifiers) | AnnotationTypeDeclaration(modifiers) | FieldDeclaration(modifiers) )
   //       | ( ";" )
   public R visit(AnnotationTypeMemberDeclaration n);

   // f0 -> "default"
   // f1 -> MemberValue()
   public R visit(DefaultValue n);

}

