package myparser;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class SemanticAnalyzer {
    private HashMap<String, String> symbolTable = new HashMap<>();
    private List<String> errors = new ArrayList<>();

    public List<String> analyze(Node programNode) {
        Node declarations = programNode.children.get(1);
        Node statements = programNode.children.get(2);

        visitDeclarations(declarations);
        visitStatements(statements);

        return errors;
    }

    private void visitDeclarations(Node node){
        if (node.children.isEmpty()) return;

        Node declaration = node.children.get(0);
        Node moreDecls = node.children.get(1);

        String declaredType = declaration.type;
        visitVarlist(declaration.children.get(0), declaredType);

        visitDeclarations(moreDecls);
    }

    private void visitVarlist(Node varlist, String declaredType) {
        Node variable = varlist.children.get(0);
        String name = variable.value;

        if (symbolTable.containsKey(name)) {
            errors.add("Duplicate declaration: " + name);
        } else {
            symbolTable.put(name, declaredType);
        }

        if (varlist.children.size() > 1) {
            visitVarlist(varlist.children.get(1), declaredType);
        }
    }

    private void visitStatements(Node node) {
        if (node.children.isEmpty()) return;

        Node statement = node.children.get(0);
        Node rest = node.children.get(1);

        visitStatement(statement);
        visitStatements(rest);
    }

    private void visitStatement(Node node) {
        switch(node.value) {
            case "assignment":
                visitAssignment(node);
                break;
            case "repetition":
                visitRepetition(node);
                break;
            case "conditional":
                visitConditional(node);
                break;
            case "input":
                visitInput(node);
                break;
            case "output":
                visitOutput(node);
                break;
            default:
                errors.add("Invalid Statement");
        }
    }

    private void visitAssignment(Node node){
        Node variable = node.children.get(0);
        Node expression = node.children.get(1);

        String varName = variable.value;
        String declaredType = symbolTable.get(varName);

        if (declaredType == null) {
            errors.add("Undeclared variable: " + varName);
            return;
        }

        String exprType = inferExpressionType(expression);
        if (exprType != null && !exprType.equals(declaredType)) {
            errors.add("Type mismatch in assignment to " + varName +
                    ": expected " + declaredType + ", got " + exprType);
        }
    }

    private String inferExpressionType(Node expression) {
        Node term = expression.children.get(0);
        Node exprPrime = expression.children.get(1);

        String type = inferTermType(term);

        if (!exprPrime.children.isEmpty()) {
            //operator is present, therefore type is num, operands must be num
            if (!type.equals("num") && !type.equals("unknown")) {
                errors.add("Non-numeric operand used in arithmetic expression: " + term.value);
            }
            checkExpressionPrime(exprPrime);
            return "num";
        }
        return type;
    }

    private void checkExpressionPrime(Node exprPrime) {
        Node term = exprPrime.children.get(1);
        String type = inferTermType(term);
        if(!type.equals("num")) {
            errors.add("Non-numeric operand used in arithmetic expression: " + term.value);
        }
        Node next = exprPrime.children.get(2);
        if(!next.children.isEmpty()) checkExpressionPrime(next);
    }

    private String inferTermType(Node term) {
        if (term.type != null && term.type.equals("NUMBER")) return "num";
        String name = term.value;
        if(!symbolTable.containsKey(name)) {
            errors.add("Undeclared variable: " + name);
            return "unknown";
        }
        return symbolTable.get(name);
    }

    private void visitRepetition(Node node) {
        switch(node.type) {
            case "repeat_times":
                String exprType = inferExpressionType(node.children.get(0));
                if (!"num".equals(exprType)) {
                    errors.add("Repeat count must be numeric at line " + node.children.get(0).line);
                }
                visitStatements(node.children.get(1));
                break;

            case "repeat_until":
                visitStatements(node.children.get(0));
                visitCondition(node.children.get(1));
                break;
            case "from_to":
                String fromType = inferExpressionType(node.children.get(0));
                String toType   = inferExpressionType(node.children.get(1));
                if (!"num".equals(fromType)) {
                    errors.add("From bound must be numeric at line " + node.children.get(0).line);
                }
                if (!"num".equals(toType)) {
                    errors.add("To bound must be numeric at line " + node.children.get(1).line);
                }
                visitStatements(node.children.get(2));
                break;
        }
    }

    private void visitConditional(Node node) {
        visitCondition(node.children.get(0));
        visitStatements(node.children.get(1));

        Node elseNode = node.children.get(2);
        if (!elseNode.children.isEmpty()){
            visitStatements(elseNode.children.get(0));
        }
    }

    private void visitCondition(Node node) {
        String leftType  = inferExpressionType(node.children.get(0));
        String rightType = inferExpressionType(node.children.get(2));

        if (leftType != null && rightType != null && !leftType.equals(rightType)) {
            errors.add("Type mismatch in condition at line " + node.line);
        }
    }

    private void visitInput(Node node) {
        Node variable = node.children.get(1);
        if (!symbolTable.containsKey(variable.value)) {
            errors.add("Undeclared variable: " + variable.value + " at line " + variable.line);
        }
    }

    private void visitOutput(Node node) {
        switch (node.type) {
            case "print_string":
                break;
            case "print_expr":
                inferExpressionType(node.children.get(0));
                break;
            case "print_both":
                inferExpressionType(node.children.get(1));
                break;
        }
    }
}
