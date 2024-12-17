package model.statements;

import exceptions.ADTException;
import exceptions.ExpressionException;
import exceptions.StatementException;
import model.adt.MyIDictionary;
import model.expressions.IExpression;
import model.state.PrgState;
import model.types.BoolType;
import model.types.IType;
import model.values.BoolValue;
import model.values.IValue;

public class WhileStatement implements IStatement{

    IExpression expression;
    IStatement statement;

    public WhileStatement(IExpression expression, IStatement statement){
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException, ADTException {

        //evaluate the expression
        IValue val = expression.eval(state.getSymTable(), state.getHeap());

        //check if the expression is a boolean
        if(!(val instanceof BoolValue)){
            throw new StatementException("Expression is not a boolean");
        }

        //check if the expression is true
        if(((BoolValue)val).getValue()){
            //push the statement on the execution stack and then the while statement
            state.getExecStack().push(this);
            state.getExecStack().push(statement);
        }
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new WhileStatement(expression.deepCopy(), statement.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws StatementException, ADTException, ExpressionException {
        IType typeExpr = expression.typeCheck(typeEnv);
        if(typeExpr.equals(new BoolType())) {
            statement.typeCheck(typeEnv.deepCopy());
            return typeEnv;
        }
        else{
            throw new StatementException("The condition of while is not a boolean");
        }
    }

    public String toString(){
        return "while(" + expression + "){" + statement + "}";
    }
}
