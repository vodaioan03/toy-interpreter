package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adt.MyIDictionary;
import model.state.PrgState;
import model.types.IType;

public class CompoundStatement implements IStatement {

    private IStatement statement1;
    private IStatement statement2;

    public CompoundStatement(IStatement statement1, IStatement statement2) {
        this.statement1 = statement1;
        this.statement2 = statement2;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException, ADTException {
        //split the statement into its components and push them on the stack
        state.getExecStack().push(statement2);
        state.getExecStack().push(statement1);
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new CompoundStatement(statement1.deepCopy(), statement2.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws StatementException, ADTException, ExpressionException {
        return statement2.typeCheck(statement1.typeCheck(typeEnv));
    }

    public String toString(){
        return statement1 + ";" + statement2;
    }
}
