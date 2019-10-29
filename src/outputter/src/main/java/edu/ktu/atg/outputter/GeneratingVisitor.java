package edu.ktu.atg.outputter;

import java.lang.reflect.Modifier;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

import edu.ktu.atg.common.executables.ExecutableAbstractClassz;
import edu.ktu.atg.common.executables.ExecutableAbstractMethod;
import edu.ktu.atg.common.executables.ExecutableArray;
import edu.ktu.atg.common.executables.ExecutableConstructor;
import edu.ktu.atg.common.executables.ExecutableEnum;
import edu.ktu.atg.common.executables.ExecutableFieldObserver;
import edu.ktu.atg.common.executables.ExecutableFieldWriter;
import edu.ktu.atg.common.executables.ExecutableInterface;
import edu.ktu.atg.common.executables.ExecutableMethod;
import edu.ktu.atg.common.executables.ExecutableSequence;
import edu.ktu.atg.common.executables.ExecutableValue;
import edu.ktu.atg.common.executables.ExecutableVoid;
import edu.ktu.atg.common.executables.IBaseVisitor;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.executables.IVisitor;
import edu.ktu.atg.common.execution.SolutionExecutionData;
import edu.ktu.atg.common.execution.models.DefinedValue;
import edu.ktu.atg.common.execution.models.DefinedValue.ValueState;

public class GeneratingVisitor implements IVisitor<Node>, IBaseVisitor<Node> {

    private final OutputContext context;
    private final SolutionExecutionData executionData;

    public GeneratingVisitor(OutputContext context, SolutionExecutionData executionData) {
        this.context = context;
        this.executionData = executionData;

    }

    public Node innerVisit(IExecutable root, IExecutable item) throws Throwable {
        if (!this.context.getStatements().containsKey(item.getId())) {

            DefinedValue value = this.executionData.getDefinedValues().getOrDefault(item.getId(),
                    DefinedValue.createExecutable(item));
            Node nodeValue = null;
            if (value.getState() == ValueState.FIXED && value.getValue() == null) {
                if (item instanceof ExecutableValue) {
                    nodeValue = GenerationHelper.castTo(GenerationHelper.valueTypeToExpr((ExecutableValue) item),
                            new NullLiteralExpr());
                } else {
                    nodeValue = GenerationHelper.castTo(GenerationHelper.generateType(item), new NullLiteralExpr());
                }
            } else {
                if (value.getState() == ValueState.REF) {
                    nodeValue = new NameExpr(context.getNames().get(value.getRef().getId()));
                } else {
                    nodeValue = item.accept(root, this);
                }
            }

            this.context.save(item, nodeValue);
        }
        Node value = context.getNodeValue(item);
        return value;
    }

    public String getValue(IExecutable item) {
        DefinedValue v = executionData.getDefinedValues().getOrDefault(item.getId(), null);
        if (v == null) {
            return null;
        }
        if (v.getValue() == null) {
            return null;
        }
        return v.getValue().toString();
    }

    @Override
    public Node visit(ExecutableValue item, IExecutable root) throws Throwable {
        String value = getValue(item);
        return GenerationHelper.executableValueToNode(item, value);
    }

    @Override
    public Node visit(ExecutableInterface item, IExecutable root) throws Throwable {
        final ObjectCreationExpr exp = new ObjectCreationExpr();
        exp.setType(GenerationHelper.generateNameIfArray(item));
        exp.setAnonymousClassBody(new NodeList<>());
        for (final ExecutableAbstractMethod ex : item.getMethodsToImplement()) {
            Node node = this.innerVisit(item, ex);
            exp.addAnonymousClassBody((BodyDeclaration<?>) node);
        }

        return exp;
    }

    @Override
    public Node visit(ExecutableArray item, IExecutable root) throws Throwable {

        ArrayCreationExpr cr = new ArrayCreationExpr();
        cr.setElementType(GenerationHelper.generateNameIfArray(item));
        ArrayInitializerExpr ex = new ArrayInitializerExpr();
        cr.setInitializer(ex);

        DefinedValue val = this.executionData.getDefinedValues().getOrDefault(item.getId(),
                DefinedValue.createExecutable(item));
        if (val.getState() == ValueState.ARRAY) {
            for (IExecutable child : val.getChildren()) {
                Node main = this.innerVisit(item, child);
                ex.getValues().add((Expression) main);
            }
        }

        return cr;

    }

    @Override
    public Node visit(ExecutableAbstractClassz item, IExecutable root) throws Throwable {
        final ObjectCreationExpr exp = new ObjectCreationExpr();
        exp.setType(GenerationHelper.generateNameIfArray(item));
        exp.setAnonymousClassBody(new NodeList<>());

        for (final IExecutable p : item.getParameters()) {
            final Node value = this.innerVisit(item, p);
            exp.addArgument((Expression) value);
        }
        for (final ExecutableAbstractMethod ex : item.getMethodsToImplement()) {
            Node node = this.innerVisit(item, ex);
            exp.addAnonymousClassBody((BodyDeclaration<?>) node);
        }

        return exp;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Node visit(ExecutableAbstractMethod item, IExecutable root) throws Throwable {
        final MethodDeclaration dec = new MethodDeclaration();
        dec.setName(item.getName());
        dec.setType(new ClassOrInterfaceType(GenerationHelper.generateNameIfArray(item.getReturnValue())));
        if (Modifier.isPublic(item.getModifiers())) {
            dec.getModifiers().add(com.github.javaparser.ast.Modifier.publicModifier());
        }
        if (Modifier.isProtected(item.getModifiers())) {
            dec.getModifiers().add(com.github.javaparser.ast.Modifier.protectedModifier());
        }
        int counter = 0;
        for (final IExecutable param : item.getParameters()) {
            dec.getParameters().add(new Parameter(new ClassOrInterfaceType(GenerationHelper.generateNameIfArray(param)),
                    "p_" + counter++));
        }
        if (!(item.getReturnValue() instanceof ExecutableVoid)) {
            final BlockStmt block = new BlockStmt();
            Node value = this.innerVisit(item, item.getReturnValue());
            block.addStatement(new ReturnStmt((Expression) value));
            dec.setBody(block);
        }

        return dec;

    }

    @Override
    public Node visit(ExecutableMethod item, IExecutable root) throws Throwable {
        Node rootNode = null;
        if (item.isStatic()) {
            rootNode = new NameExpr(item.getClassName());
        } else {
            rootNode = this.innerVisit(null, root);
        }

        MethodCallExpr exp = new MethodCallExpr((Expression) rootNode, item.getName());
        for (IExecutable p : item.getParameters()) {
            final Node value = this.innerVisit(item, p);
            exp.addArgument((Expression) value);
        }
        return exp;
    }

    @Override
    public Node visit(ExecutableConstructor item, IExecutable root) throws Throwable {

        final ObjectCreationExpr exp = new ObjectCreationExpr();
        exp.setType(new ClassOrInterfaceType(GenerationHelper.generateNameIfArray(item)));
        for (final IExecutable p : item.getParameters()) {
            final Node value = this.innerVisit(item, p);
            exp.addArgument((Expression) value);
        }
        return exp;
    }

    @Override
    public Node visit(ExecutableFieldWriter item, IExecutable root) throws Throwable {
        Node rootNode = null;
        if (item.isStatic()) {
            rootNode = new NameExpr(GenerationHelper.generateNameIfArray(item));
        } else {
            rootNode = this.innerVisit(null, root);
        }
        this.innerVisit(item, item.getInputValue());
        return new FieldAccessExpr((Expression) rootNode, item.getName());
    }

    @Override
    public Node visit(ExecutableFieldObserver item, IExecutable root) throws Throwable {
        Node rootNode = null;
        if (item.isStatic()) {
            rootNode = new NameExpr(GenerationHelper.generateNameIfArray(item));
        } else {
            rootNode = this.innerVisit(null, root);
        }
        return new FieldAccessExpr((Expression) rootNode, item.getName());

    }

    @Override
    public Node visit(ExecutableSequence item, IExecutable root) throws Throwable {
        Node rootValue = null;
        if (item.getRoot() != null) {
            rootValue = innerVisit(item.getRoot(), item);
        }
        for (IExecutable p : item.getWriters()) {
            innerVisit(p, item.getRoot());

        }
        for (IExecutable p : item.getObservers()) {
            innerVisit(p, item.getRoot());
        }
        return rootValue;
    }

    @Override
    public Node visit(ExecutableEnum item, IExecutable root) throws Throwable {
        if (!executionData.getDefinedValues().containsKey(item.getId()) || item.getClassz().getEnumConstants().length == 0) {
            return GenerationHelper.castTo(GenerationHelper.generateType(item), new NullLiteralExpr());
        }
        String value = getValue(item);
        if (value == null) {
           value = item.getClassz().getEnumConstants()[0].toString();

        }
        return new NameExpr(GenerationHelper.generateType(item) + "." + value);
    }

}
