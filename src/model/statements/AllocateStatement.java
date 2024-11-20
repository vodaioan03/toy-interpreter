package model.statements;


import exceptions.StatementException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.expressions.IExpression;
import model.state.PrgState;
import model.statements.IStatement;
import model.types.ReferenceType;
import model.values.IValue;
import model.values.ReferenceValue;

public class AllocateStatement implements IStatement {
    private final String variableName;
    private final IExpression expression;

    public AllocateStatement(String variableName, IExpression expression)
    {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState currentState) throws StatementException {
        MyIDictionary<String, IValue> symbolTable = currentState.getSymTable();
        MyIHeap heapTable = currentState.getHeap();
        if (!symbolTable.contains(variableName))
            throw new StatementException("Variable " + variableName + " is not defined in symbol table!");
        if (!(symbolTable.get(variableName).getType() instanceof ReferenceType))
            throw new StatementException("Variable " + variableName + " is not of reference type!");
        ReferenceValue variableToAllocate = (ReferenceValue) symbolTable.get(variableName);
        IValue expressionValue = expression.eval(symbolTable, heapTable);
        ReferenceType variableType = (ReferenceType) variableToAllocate.getType();
        if (!expressionValue.getType().equals(variableType.getInner()))
            throw new StatementException("Expression does not match location type!");
        int addressOfVariable = heapTable.allocate(expressionValue);
        symbolTable.insert(variableName, new ReferenceValue(addressOfVariable, variableType.getInner()));
        return currentState;
    }

    @Override
    public IStatement deepCopy() {
        return new AllocateStatement(variableName, expression.deepCopy());
    }

    @Override
    public String toString() {
        return "new(" + variableName + ", " + expression.toString() + ")";
    }
}