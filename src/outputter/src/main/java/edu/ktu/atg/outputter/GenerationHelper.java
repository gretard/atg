package edu.ktu.atg.outputter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.utils.StringEscapeUtils;

import edu.ktu.atg.common.executables.ExecutableArray;
import edu.ktu.atg.common.executables.ExecutableValue;
import edu.ktu.atg.common.executables.IExecutable;
import edu.ktu.atg.common.executables.ValueType;

public final class GenerationHelper {

    private GenerationHelper() {

    }

    protected static String generateNameIfArray(final IExecutable item) {
        String pName = item.getClassName().replace('$', '.');
        if (item instanceof ExecutableArray) {
            ExecutableArray a = (ExecutableArray) item;
            pName = a.getComponentType().getClassName().replace('$', '.') + StringUtils.repeat("[]", a.getDimension());
        }
        return pName;
    }

    public static final Type generateType(IExecutable item) {
        ClassOrInterfaceType type = new ClassOrInterfaceType(generateNameIfArray(item));
        List<ClassOrInterfaceType> types = new ArrayList<>();

        if (!types.isEmpty()) {
            type.setTypeArguments(types.toArray(new Type[0]));
        }

        return type;
    }

    public static Type valueTypeToExpr(ExecutableValue item) {
        ValueType type = item.getType();
        switch (type) {
        case BOOLEAN:
            return PrimitiveType.booleanType();
        case BYTE:
            return PrimitiveType.byteType();
        case CHAR:
            return PrimitiveType.charType();
        case DOUBLE:
            return PrimitiveType.doubleType();
        case ENUM:
            return generateType(item);
        case FLOAT:
            return PrimitiveType.floatType();
        case INT:
            return PrimitiveType.intType();
        case LONG:
            return PrimitiveType.longType();
        case SHORT:
            return PrimitiveType.shortType();
        default:
            return generateType(item);
        }
    }

    public static Expression executableValueToNode(ExecutableValue item, String value) {
        return castTo(valueTypeToExpr(item), executableValueToNodeWithNoCast(item, value));
    }

    public static Expression executableValueToNodeWithNoCast(ExecutableValue item, String value) {
        final ValueType type = item.getType();
        if (value == null) {
            return new NullLiteralExpr();
        }
        switch (type) {
        case BOOLEAN:
            return new BooleanLiteralExpr(Boolean.parseBoolean(value));
        case DOUBLE:
            return doubleToString("Double", value, "d");
        case FLOAT:
            return doubleToString("Float", value, "f");
        case INT:
            return new IntegerLiteralExpr(value);
        case LONG:
            return new LongLiteralExpr(value + "l");
        case STRING:
            return new StringLiteralExpr(StringEscapeUtils.escapeJava(value));
        case SHORT:
            return castTo(PrimitiveType.shortType(), new IntegerLiteralExpr(value));
        case BYTE:
            return castTo(PrimitiveType.byteType(), new IntegerLiteralExpr(value));
        case CHAR:
            return castTo(PrimitiveType.charType(), new IntegerLiteralExpr(((int) value.charAt(0)) + ""));
        case ENUM:
            return new NameExpr(item.getClassName() + "." + value);
        case NULL:
            return castTo(generateType(item), new NullLiteralExpr());
        default:

            throw new IllegalArgumentException("Invalid type passed.. ");
        }
    }

    public static final Expression castTo(final Type type, final Expression val) {
        return new CastExpr(type, val);
    }

    public static final Expression doubleToString(final String prefix, final String val, final String postFix) {
        String v = val.toString();
        if ("Infinity".equalsIgnoreCase(v)) {
            v = prefix + ".POSITIVE_INFINITY";
            return new DoubleLiteralExpr(v);
        }
        if ("-Infinity".equalsIgnoreCase(v)) {
            v = prefix + ".NEGATIVE_INFINITY";
            return new DoubleLiteralExpr(v);
        }
        if ("NaN".equalsIgnoreCase(v)) {
            v = prefix + ".NaN";
            return new DoubleLiteralExpr(v);
        }
        if (v.startsWith(".")) {
            v = "0" + v;
        }
        return new DoubleLiteralExpr(v + postFix);
    }
}
