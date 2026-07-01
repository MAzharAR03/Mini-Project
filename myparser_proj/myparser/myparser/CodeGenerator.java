package myparser;

public class CodeGenerator {
    private StringBuilder stringBuilder = new StringBuilder();
    private int repeatCounter = 0;


    public String generate(Node programNode) {
        Node declarations = programNode.children.get(1);
        Node statements = programNode.children.get(2);


        stringBuilder.append("import java.util.Scanner;\n");
        stringBuilder.append("public class GeneratedProgram {\n");
        stringBuilder.append("public static void main(String[] args) {\n");
        stringBuilder.append("Scanner scanner = new Scanner(System.in);\n");


        genDeclarations(declarations);
        genStatements(statements);

        stringBuilder.append("    }\n");
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }

    private void genDeclarations(Node node) {
        if (node.children.isEmpty()) return;

        Node declaration = node.children.get(0);
        Node moreDecls = node.children.get(1);

        String javaType;
        if (declaration.type.equals("num")){
            javaType = "int";
        } else {
            javaType = "String";
        }

        genVarList(declaration.children.get(0), javaType);
        genDeclarations(moreDecls);
    }

    private void genVarList(Node varlist, String javaType) {
        Node variable = varlist.children.get(0);
        String defaultVal;
        if (javaType.equals("int")) {
            defaultVal = "0";
        } else {
            defaultVal = "\"\"";
        }

        stringBuilder.append(javaType).append(" ").
                append(variable.value).append(" = ").append(defaultVal).append(";\n");
        if (varlist.children.size() > 1) {
            genVarList(varlist.children.get(1), javaType);
        }
    }

    private void genStatements(Node node) {
        if (node.children.isEmpty()) return;

        Node statement = node.children.get(0);
        Node rest = node.children.get(1);

        genStatement(statement);
        genStatements(rest);
    }

    private void genStatement(Node node) {
        if (node.value.equals("assignment")) {
            genAssignment(node);
        } else if (node.value.equals("repetition")) {
            genRepetition(node);
        } else if (node.value.equals("conditional")) {
            genConditional(node);
        } else if (node.value.equals("input")) {
            genInput(node);
        } else if (node.value.equals("output")) {
            genOutput(node);
        }
    }

    private String genExpression(Node node) {
        Node term = node.children.get(0);
        Node exprPrime = node.children.get(1);

        StringBuilder expr = new StringBuilder(genTerm(term));
        appendExpressionPrime(exprPrime,expr);
        return expr.toString();
    }

    private String genTerm(Node node) {
        return node.value;
    }

    private void appendExpressionPrime(Node node, StringBuilder expr){
        if (node.children.isEmpty()) return;

        Node operation = node.children.get(0);
        Node term = node.children.get(1);
        Node next = node.children.get(2);

        expr.append(" ").append(mapOperator(operation.value)).append(" ").append(genTerm(term));
        appendExpressionPrime(next, expr);
    }

    private String mapOperator(String tokenName) {
        switch(tokenName){
            case "PLUS":
                return "+";
            case "MINUS":
                return "-";
            case "MULTIPLY":
                return "*";
            case "DIVIDE":
                return "/";
            default:
                return "?";
        }
    }


    private void genAssignment(Node node) {
        Node variable = node.children.get(0);
        Node expression = node.children.get(1);

        String exprCode = genExpression(expression);
        stringBuilder.append(variable.value).append(" = ").append(exprCode).append(";\n");
    }

    private void genOutput(Node node){
        if (node.type.equals("print_string")) {
            Node stringLiteral = node.children.get(0);
            stringBuilder.append("System.out.println(\"").append(stringLiteral.value).append("\");\n");
        } else if (node.type.equals("print_expr")) {
            Node expression = node.children.get(0);
            String exprCode = genExpression(expression);
            stringBuilder.append("System.out.println(" ).append(exprCode).append(");\n");
        } else if (node.type.equals("print_both")) {
            Node stringLiteral = node.children.get(0);
            Node expression = node.children.get(1);
            String exprCode = genExpression(expression);
            stringBuilder.append("System.out.println(\"").append(stringLiteral.value)
                    .append("\" + ").append(exprCode).append(");\n");
        }
    }

    private void genInput(Node node) {
        Node stringLiteral = node.children.get(0);
        Node variable = node.children.get(1);

        stringBuilder.append("System.out.print(\"").append(stringLiteral.value).append("\");\n");

        if (variable.type.equals("NUMVAL")) {
            stringBuilder.append(variable.value).append(" = scanner.nextInt();\n");
        } else {
            stringBuilder.append(variable.value).append(" = scanner.next();\n");
        }
    }

    private String genCondition(Node node) {
        Node left = node.children.get(0);
        Node logical = node.children.get(1);
        Node right = node.children.get(2);

        String leftCode = genExpression(left);
        String rightCode = genExpression(right);
        String op = mapLogical(logical.value);

        return leftCode + " " + op + " " + rightCode;
    }

    private String mapLogical(String tokenName) {
        if (tokenName.equals("LESS_THAN")) {
            return "<";
        } else if (tokenName.equals("GREATER_THAN")) {
            return ">";
        } else if (tokenName.equals("EQUALS")) {
            return "==";
        } else if (tokenName.equals("NOT_EQUALS")) {
            return "!=";
        } else {
            return "?";
        }
    }

    private void genRepetition(Node node) {
        if (node.type.equals("repeat_times")) {
            Node expression = node.children.get(0);
            Node statements = node.children.get(1);

            String exprCode = genExpression(expression);
            String loopVar = "i" + repeatCounter;
            repeatCounter++;

            stringBuilder.append("for (int ").append(loopVar).append(" = 0; ")
                    .append(loopVar).append(" < ").append(exprCode).append("; ")
                    .append(loopVar).append("++) {\n");
            genStatements(statements);
            stringBuilder.append("}\n");

        } else if (node.type.equals("repeat_until")) {
            Node statements = node.children.get(0);
            Node condition = node.children.get(1);

            String condCode = genCondition(condition);

            stringBuilder.append("do {\n");
            genStatements(statements);
            stringBuilder.append("} while (!(").append(condCode).append("));\n");

        } else if (node.type.equals("from_to")) {
            Node expr1 = node.children.get(0);
            Node expr2 = node.children.get(1);
            Node statements = node.children.get(2);

            String fromCode = genExpression(expr1);
            String toCode = genExpression(expr2);
            String loopVar = "i" + repeatCounter;
            repeatCounter++;

            stringBuilder.append("for (int ").append(loopVar).append(" = ").append(fromCode).append("; ")
                    .append(loopVar).append(" <= ").append(toCode).append("; ")
                    .append(loopVar).append("++) {\n");
            genStatements(statements);
            stringBuilder.append("}\n");
        }
    }

    private void genConditional(Node node) {
        Node condition = node.children.get(0);
        Node statements = node.children.get(1);
        Node elseNode = node.children.get(2);

        String condCode = genCondition(condition);

        stringBuilder.append("if (").append(condCode).append(") {\n");
        genStatements(statements);
        stringBuilder.append("}\n");

        if (!elseNode.children.isEmpty()) {
            stringBuilder.append("else {\n");
            genStatements(elseNode.children.get(0));
            stringBuilder.append("}\n");
        }
    }
}
