package model.statements;

import com.sun.jdi.BooleanType;
import com.sun.jdi.BooleanValue;
import exceptions.StatementException;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.BoolType;
import model.values.BoolValue;
import model.values.IValue;

public class WhileStatement implements IStatement {

    private IStatement statement;
    private IExpression condition;

    public WhileStatement(IExpression condition,IStatement statement) {
        this.condition = condition;
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException {
        IValue value = condition.eval(state.getSymTable(),state.getHeap());

        if(!(value.getType() instanceof BoolType)){
            throw new StatementException("The while condition should evaluate to a Boolean Type");
        }
        if(((BoolValue)value).getValue()){
            state.getExecStack().push(this);
            state.getExecStack().push(statement);
        }

        return state;
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(condition.deepCopy(), statement.deepCopy());
    }

    @Override
    public String toString() {
        return "while ( ) ";
    }
}
