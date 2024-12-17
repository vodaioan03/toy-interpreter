package model.expressions;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.HeapException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.types.IType;
import model.types.RefType;
import model.values.IValue;
import model.values.RefValue;

public class ReadHeapExpression implements IExpression{

    IExpression expr;

    public ReadHeapExpression(IExpression expr){
        this.expr = expr;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> symTable, MyIHeap heap) throws ADTException, ExpressionException {

        //evaluate the expression
        IValue val = expr.eval(symTable, heap);

        if(!(val instanceof RefValue)){
            throw new ExpressionException("expression is not a reference value");
        }

        //get the address from the reference value and use it to access the value from the heap
        int address = ((RefValue) val).getAddr();

        //check if the address exists in the heap
        IValue heapValue;
        try {
            heapValue = heap.getValue(address);
        }catch (HeapException e){
            throw new ExpressionException(e.getMessage());
        }

        //return the value from the heap
        return heapValue;
    }

    public String toString(){
        return "rH(" + expr + ")";
    }

    @Override
    public IExpression deepCopy() {
        return new ReadHeapExpression(expr.deepCopy());
    }

    @Override
    public IType typeCheck(MyIDictionary<String, IType> typeEnv) throws ExpressionException, ADTException {
        IType type = expr.typeCheck(typeEnv);
        if(type instanceof RefType reft){
            return reft.getInner();
        }
        throw new ExpressionException("the rH argument is not a reference type");
    }
}
