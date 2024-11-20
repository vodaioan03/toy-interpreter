package model.statements;

import exceptions.AppException;
import exceptions.StatementException;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.IType;
import model.values.IValue;
import model.values.ReferenceValue;

public class WriteHeapStatement implements IStatement{
    IExpression address;
    IExpression expression;

    public WriteHeapStatement(IExpression address, IExpression expression) {
        this.address = address;
        this.expression = expression;
    }


    @Override
    public PrgState execute(PrgState state) throws StatementException {
        IValue addressValue = address.eval(state.getSymTable(),state.getHeap());
        IValue expressionValue = expression.eval(state.getSymTable(),state.getHeap());
        if(!(addressValue.getType() instanceof IType)){
            throw new AppException("Heap should be accessed only using references");
        }

        state.getHeap().write(((ReferenceValue)addressValue).getAddress(), expressionValue);
        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new WriteHeapStatement(address.deepCopy(), expression.deepCopy());
    }

    @Override
    public String toString(){
        return "writeHeap(" + address.toString() + ", " + expression.toString() + ")";
    }
}
