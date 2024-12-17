package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.HeapException;
import exceptions.StatementException;
import model.adt.MyIDictionary;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class WriteHeapStatement implements IStatement{

    String var;
    IExpression expression;

    public WriteHeapStatement(String var, IExpression expression){
        this.var = var;
        this.expression = expression;
    }


    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException, ADTException, HeapException {

        //check if the variable is in the symbol table
        if(!state.getSymTable().contains(var)){
            throw new StatementException("Variable " + var + " is not defined");
        }

        //check if the variable is a reference
        IValue val = state.getSymTable().get(var);
        if(!(val instanceof RefValue)){
            throw new StatementException("Variable " + var + " is not a reference");
        }

        //check if the address exists in the heap
        int address = ((RefValue) val).getAddr();
        if(!state.getHeap().contains(address)){
            throw new StatementException("Address " + address + " is not in the heap");
        }

        //evaluate the expression
        IValue expValue = expression.eval(state.getSymTable(), state.getHeap());

        //check if the expression value is of the same type as the reference type
        if(!((RefValue)val).getLocationType().equals(expValue.getType())){
            throw new StatementException("Expression value is not of the same type as the reference type");
        }

        //update the value in the heap
        state.getHeap().set(address, expValue);

        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new WriteHeapStatement(var, expression.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws StatementException, ADTException, ExpressionException {

        IType varType = typeEnv.get(var);
        IType expType = expression.typeCheck(typeEnv);

        if(!varType.equals(new RefType(expType))){
            throw new StatementException("WriteHeap: right hand side and left hand side have different types");
        }

        return typeEnv;
    }

    public String toString(){
        return "wH(" + var + ", " + expression + ")";
    }
}
