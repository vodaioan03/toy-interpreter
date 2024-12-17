package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adt.MyIDictionary;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.IType;
import model.types.StringType;
import model.values.IValue;
import model.values.StringValue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class OpenRFileStatement implements IStatement{

    IExpression exp;

    public OpenRFileStatement(IExpression exp){
        this.exp = exp;
    }


    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException, ADTException {

        IValue result = exp.eval(state.getSymTable(), state.getHeap());

        //check if the expression evaluates to a string
        if(!result.getType().equals(new StringType())){
            throw new StatementException("File name is not a string");
        }

        //search for the file in the file table
        if(state.getFileTable().contains((StringValue) result)){
            throw new StatementException("File already opened");
        }

        try{

            //open the file using the BufferedReader class
            BufferedReader reader = new BufferedReader(new FileReader(result.toString()));

            //add the file to the file table
            state.getFileTable().insert((StringValue) result, reader);
        }
        catch(FileNotFoundException e){
            throw new StatementException("File not found");
        }

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new OpenRFileStatement(exp.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws StatementException, ADTException, ExpressionException {
        IType expType = exp.typeCheck(typeEnv);
        if(!expType.equals(new StringType())){
            throw new StatementException("File name is not a string");
        }
        return typeEnv;
    }

    public String toString(){
        return "openRFile(" + exp + ")";
    }
}
