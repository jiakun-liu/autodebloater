package org.jkliu.debloater.instrument;

import org.jkliu.debloater.core.MethodFinder;
import soot.*;
import soot.jimple.*;
import soot.util.Chain;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MethodTransformer extends BodyTransformer {
    FastHierarchy fastHierarchy = Scene.v().getOrMakeFastHierarchy();
    Set<String> toRemoveMethods;
    Set<String> toRemoveClasses;
    static Set<String> removedCounter = new HashSet<>();
    static Set<String> totalCounter = new HashSet<>();
    String packageName;

    public MethodTransformer(MethodFinder methodFinder, String packageName) {
        this.toRemoveMethods = methodFinder.getToDebloatMethods();
        this.toRemoveClasses = methodFinder.getToDebloatClasses();

        this.packageName = packageName;
    }

    @Override
    protected void internalTransform(Body body, String arg0, @SuppressWarnings("rawtypes") Map arg1) {
        SootMethod method = body.getMethod();
        SootClass declaringClass = method.getDeclaringClass();
        if (!declaringClass.getPackageName().contains(packageName)) {
            return;
        }
        String methodSignature = method.getSignature();
        totalCounter.add(methodSignature);
        if (toRemoveMethods.contains(methodSignature) || toRemoveClasses.contains(declaringClass.getName())) {
            JimpleBody jimpleBody = (JimpleBody) body;
            UnitPatchingChain units = jimpleBody.getUnits();
            // TODO: we do not remove the method that has traps
            Chain<Trap> traps = body.getTraps();
            if (traps.size() > 0) {
                return;
            }
            removedCounter.add(methodSignature);
            HashSet<Stmt> toRemoveStmts = new HashSet<>();
            for (Unit unit : units) {
                if (unit instanceof Stmt) {
                    Stmt stmt = (Stmt) unit;
                    if (stmt instanceof ReturnVoidStmt) {
                        continue;
                    }
                    if (stmt instanceof ReturnStmt) {
                        ReturnStmt returnStmt = (ReturnStmt) stmt;
                        Value v = NullConstant.v();
                        Type returnType = method.getReturnType();
                        if (returnType instanceof RefType) {
                            v = NullConstant.v();
                        } else if (returnType instanceof IntType) {
                            v = IntConstant.v(0);
                        } else if (returnType instanceof LongType) {
                            v = LongConstant.v(0);
                        } else if (returnType instanceof DoubleType) {
                            v = DoubleConstant.v(0);
                        } else if (returnType instanceof FloatType) {
                            v = FloatConstant.v(0);
                        } else if (returnType instanceof BooleanType) {
                            v = IntConstant.v(0);
                        } else if (returnType instanceof CharType) {
                            v = IntConstant.v(0);
                        } else if (returnType instanceof ByteType) {
                            v = IntConstant.v(0);
                        } else if (returnType instanceof ShortType) {
                            v = IntConstant.v(0);
                        } else if (returnType instanceof VoidType) {
                            v = NullConstant.v();
                        }
                        returnStmt.setOp(v);
                        continue;
                    }
                    toRemoveStmts.add(stmt);
                }
            }
            for (Stmt stmt : toRemoveStmts) {
                units.remove(stmt);
            }
        }
    }

    public Set<String> getRemovedCounter() {
        return removedCounter;
    }

    public Set<String> getTotalCounter() {
        return totalCounter;
    }
}
