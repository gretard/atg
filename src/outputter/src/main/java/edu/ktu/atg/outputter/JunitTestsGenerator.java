package edu.ktu.atg.outputter;

import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.AssignExpr.Operator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import edu.ktu.atg.common.executables.ExecutableArray;
import edu.ktu.atg.common.executables.ExecutableFieldWriter;
import edu.ktu.atg.common.executables.ExecutableValue;
import edu.ktu.atg.common.executables.ExecutableVoid;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.executables.IExecutableWithReturnValue;
import edu.ktu.atg.common.executables.ValueType;
import edu.ktu.atg.common.execution.CandidateSolution;
import edu.ktu.atg.common.execution.GenerationData;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.execution.models.ExecutablePair;
import edu.ktu.atg.common.execution.models.ResultValue;
import edu.ktu.atg.common.execution.models.ResultValue.CheckType;
import edu.ktu.atg.common.models.ClasszInfo;

public class JunitTestsGenerator {

	public CompilationUnit generate(GenerationData data) {
		final ClasszInfo ci = data.getInfo();
		final CompilationUnit cu = new CompilationUnit();
		cu.setPackageDeclaration(ci.getPackageName());
		final String newName = String.format("%sAtgTest", ci.getShortName());
		final ClassOrInterfaceDeclaration type = cu.addClass(newName);

		int counter = 0;
		for (CandidateSolution solutionData : data.getSolutions()) {
			SolutionExecutionData executionData = solutionData.data;
			OutputContext context = work(executionData);

			final BlockStmt block = new BlockStmt();
			block.getStatements().addAll(context.getFinalStatements());
			final MethodDeclaration main2 = type.addMethod("test" + counter++);
			main2.getModifiers().add(Modifier.publicModifier());
			main2.getModifiers().add(Modifier.finalModifier());
			main2.getThrownExceptions().add(new ClassOrInterfaceType(Throwable.class.getName()));

			final NormalAnnotationExpr nae = new NormalAnnotationExpr();
			nae.setName(new Name("org.junit.Test"));
			if (!executionData.getExceptionsThrown().isEmpty()) {
				String e = Throwable.class.getName();
				nae.addPair("expected", e + ".class");
			}
			main2.addAnnotation(nae);
			main2.setBody(block);
			main2.getAllContainedComments().clear();
			main2.getOrphanComments().clear();
		}
		return cu;

	}

	public OutputContext work(SolutionExecutionData executionData) {
		OutputContext context = new OutputContext();
		GeneratingVisitor visitor = new GeneratingVisitor(context, executionData);

		// generate names
		executionData.getExecutedPairs().forEach(pair -> {
			context.generateName(pair.getRoot());
			context.generateName(pair.getItem());
			context.generateName(pair.getItem(), pair.getReturnValue());
		});
		// generate statements
		executionData.getExecutedPairs().forEach(pair -> {
			try {
				visitor.innerVisit(pair.getRoot(), pair.getItem());
			} catch (Throwable e) {
				e.printStackTrace();
			}

		});

		List<String> generated = new LinkedList<>();
		// generate final statement invocations
		boolean shouldStop = false;
		for (ExecutablePair pair : executionData.getExecutedPairs()) {
			IExecutable item = pair.getItem();
			IExecutable returnValue = pair.getReturnValue();
			if (shouldStop) {
				break;
			}
			if (!pair.isOk) {
				shouldStop = true;
			}
			if (!context.getStatements().containsKey(item.getId()) || context.canBeInlined(item)
					|| generated.contains(item.getId())) {
				continue;
			}
			
		
			
			generated.add(item.getId());
			Node right = context.getStatements().get(item.getId());
			String name = context.getNames().getOrDefault(item.getId(), null);

			
			if (item instanceof ExecutableFieldWriter) {
				Node right0 = context.getStatements().get(((ExecutableFieldWriter) item).getInputValue().getId());
				AssignExpr assignEx = new AssignExpr((Expression) right, (Expression) right0, Operator.ASSIGN);
				ExpressionStmt ex = new ExpressionStmt(assignEx);
				context.getFinalStatements().add(ex);

				continue;
			}

			if (item instanceof IExecutableWithReturnValue) {
				if (returnValue != null && !(returnValue instanceof ExecutableVoid) && pair.isOk) {
					VariableDeclarationExpr vd = new VariableDeclarationExpr(new VariableDeclarator(
							GenerationHelper.generateType(returnValue), name, (Expression) right));
					vd.addModifier(Keyword.FINAL);
					ExpressionStmt ex = new ExpressionStmt(vd);
					context.getFinalStatements().add(ex);
				} else {
					context.getFinalStatements().add(new ExpressionStmt((Expression) right));
				}
				continue;
			}
			VariableDeclarationExpr vd = new VariableDeclarationExpr(
					new VariableDeclarator(GenerationHelper.generateType(item), name, (Expression) right));
			vd.addModifier(Keyword.FINAL);
			ExpressionStmt ex = new ExpressionStmt(vd);
			context.getFinalStatements().add(ex);

		}
		if (shouldStop) {
			return context;
		}
		// generate assertions
		executionData.getExecutedPairs().forEach(pair -> {

			IExecutable executablePair = pair.getItem();

			if (!executionData.getResults().containsKey(executablePair.getId())
					|| !generated.contains(executablePair.getId())) {
				return;
			}
			List<ResultValue> vals = executionData.getResults().get(executablePair.getId());
			String name = context.getNames().get(executablePair.getId());
			vals.forEach(v -> {
				CheckType type = v.getCheckType();
				Expression ex = null;
				IExecutable item = v.getItem();
				switch (type) {
				case SIMPLE: {
					if (!(item instanceof ExecutableValue)) {
						break;
					}
					ExecutableValue value = (ExecutableValue) v.getItem();
					MethodCallExpr exp = new MethodCallExpr(new NameExpr("org.junit.Assert"), "assertEquals");
					exp.addArgument(GenerationHelper.executableValueToNode(value, v.getValue().toString()));
					exp.addArgument(name);
					if (value.getType() == ValueType.DOUBLE) {
						exp.addArgument("0.001d");
					}
					if (value.getType() == ValueType.FLOAT) {
						exp.addArgument("0.001f");
					}
					ex = exp;
					break;
				}

				case NOTNULL: {
					MethodCallExpr exp = new MethodCallExpr(new NameExpr("org.junit.Assert"), "assertNotNull");
					exp.addArgument(new StringLiteralExpr(String.format("Expected item %s to be NOT null", name)));
					exp.addArgument(name);
					ex = exp;
					break;
				}
				case ARRAY: {
					if (!(item instanceof ExecutableArray)) {
						break;
					}

					IExecutable componentType = ((ExecutableArray) item).getComponentType();
					if (!(componentType instanceof ExecutableValue)) {
						break;
					}
					MethodCallExpr exp = new MethodCallExpr(new NameExpr("org.junit.Assert"), "assertArrayEquals");
					Object values = v.getValue();

					ArrayInitializerExpr e = new ArrayInitializerExpr();
					ArrayCreationExpr cr = new ArrayCreationExpr();
					cr.setElementType(GenerationHelper.generateNameIfArray(item));
					cr.setInitializer(e);

					for (int i = 0; i < Array.getLength(values); i++) {

						Object objValue = Array.get(values, i);
						if (objValue == null) {
							exp.addArgument(GenerationHelper.castTo(GenerationHelper.generateType(componentType),
									new NullLiteralExpr()));
							continue;
						}
						e.getValues().add(GenerationHelper.executableValueToNode((ExecutableValue) componentType,
								objValue.toString()));

					}

					exp.addArgument(cr);
					exp.addArgument(name);
					ex = exp;
					break;
				}
				case LEN: {
					if (!(item instanceof ExecutableArray)) {
						break;
					}
					MethodCallExpr exp = new MethodCallExpr(new NameExpr("org.junit.Assert"), "assertEquals");
					exp.addArgument(new IntegerLiteralExpr(v.getValue().toString()));
					exp.addArgument(new FieldAccessExpr(new NameExpr(name), "length"));
					ex = exp;
					break;
				}
				case NULL: {
					MethodCallExpr exp = new MethodCallExpr(new NameExpr("org.junit.Assert"), "assertNull");
					exp.addArgument(new StringLiteralExpr(String.format("Expected item %s to be NULL", name)));
					exp.addArgument(name);
					ex = exp;
					break;
				}

				default:
					throw new IllegalArgumentException("Invalid check type passed!" + type);
				}
				if (ex != null) {
					context.getFinalStatements().add(new ExpressionStmt(ex));
				}
			});
		});
		return context;
	}

}
