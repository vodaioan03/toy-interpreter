package model.expressions;

import exceptions.ADTException;
import exceptions.AppException;
import exceptions.ExpressionException;
import model.adt.MyIDictionary;
import model.adt.MyIHeap;
import model.types.ReferenceType;
import model.values.IValue;
import model.values.ReferenceValue;

public class ReadHeapExpression implements IExpression{
    IExpression expression;

    public ReadHeapExpression(IExpression expression) {
        this.expression = expression;
    }


    @Override
    public IValue eval(MyIDictionary<String, IValue> symTable, MyIHeap heap) throws ADTException, ExpressionException {
        IValue value = expression.eval(symTable,heap);
        if(!(value.getType() instanceof ReferenceType)){
            throw new AppException("Heap should only be accessed through references");
        }
        return heap.read(((ReferenceValue)value).getAddress());
    }

    @Override
    public IExpression deepCopy() {
        return new ReadHeapExpression(expression.deepCopy());
    }

    @Override
    public String toString(){
        return "ReadHeap(" + expression.toString() + ")";
    }
}
