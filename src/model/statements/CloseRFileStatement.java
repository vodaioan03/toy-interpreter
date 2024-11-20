package model.statements;

import exceptions.StatementException;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseRFileStatement implements IStatement{

    IExpression exp;

    public CloseRFileStatement(IExpression exp){
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {

        IValue result = exp.eval(state.getSymTable(),state.getHeap());

        
        if(!result.getType().equals(new StringType())){
            throw new StatementException("File name is not a string");
        }

        
        if(!state.getFileTable().contains((StringValue) result)){
            throw new StatementException("File not opened");
        }

        //close the reader
        try{
            BufferedReader reader = state.getFileTable().get((StringValue) result);
            reader.close();
        }
        catch (IOException e){
            throw new StatementException("Error closing file");
        }

        
        state.getFileTable().remove((StringValue) result);

        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new CloseRFileStatement(exp.deepCopy());
    }

    public String toString(){
        return "closeRFile(" + exp.toString() + ")";
    }
}
