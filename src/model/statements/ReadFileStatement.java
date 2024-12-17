package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adt.MyIDictionary;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.IType;
import model.types.IntType;
import model.types.StringType;
import model.values.IntValue;
import model.values.StringValue;
import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStatement implements IStatement {

    private IExpression expression;
    private String variableName;

    public ReadFileStatement(IExpression expression, String variableName) {
        this.expression = expression;
        this.variableName = variableName;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException, ADTException {

        //evaluate the expression
        var table = state.getSymTable();

        if(!table.contains(variableName)){
            throw new StatementException("Variable not found");
        }

        if(!table.get(variableName).getType().equals(new IntType())){
            throw new StatementException("Variable is not of type int");
        }

        var result = expression.eval(table, state.getHeap());

        if(!result.getType().equals(new StringType())){
            throw new StatementException("Expression is not of type string");
        }

        BufferedReader file = state.getFileTable().get((StringValue) result);

        try {
            String line = file.readLine();
            if(line.isEmpty()){
                line = "0";
            }
            int parser = Integer.parseInt(line);
            table.insert(variableName, new IntValue(parser));
        }
        catch (IOException e){
            throw new StatementException("Error reading from file");
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new ReadFileStatement(expression.deepCopy(), variableName);
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws StatementException, ADTException, ExpressionException {
        IType expType = expression.typeCheck(typeEnv);
        IType varType = typeEnv.get(variableName);

        if(!expType.equals(new StringType())){
            throw new StatementException("Expression is not of type string");
        }
        if(!varType.equals(new IntType())){
            throw new StatementException("Variable is not of type int");
        }

        return typeEnv;
    }

    public String toString(){
        return "readFile(" + expression + ", " + variableName + ")";
    }
}
