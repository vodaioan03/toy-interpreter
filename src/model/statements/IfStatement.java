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


public class IfStatement implements IStatement {

    private IStatement statementThen;
    private IStatement statementElse;
    private IExpression expression;

    public IfStatement(IExpression expression, IStatement statementThen, IStatement statementElse) {
        this.statementThen = statementThen;
        this.statementElse = statementElse;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws StatementException, ExpressionException, ADTException {

        //evaluate the condition
        IValue result = expression.eval(state.getSymTable(), state.getHeap());

        //check if it is a boolean
        if(!result.getType().equals(new BoolType())){
            throw new StatementException("Expression is not boolean");
        }

        //if it is true, push the statementThen on the execution stack
        if(((BoolValue)result).getValue()){
            state.getExecStack().push(statementThen);
        }
        //else push the else branch
        else{
            state.getExecStack().push(statementElse);
        }
        return null;
    }

    @Override
    public IStatement deepCopy() {
        return new IfStatement(expression.deepCopy(), statementThen.deepCopy(), statementElse.deepCopy());
    }

    @Override
    public MyIDictionary<String, IType> typeCheck(MyIDictionary<String, IType> typeEnv) throws StatementException, ADTException, ExpressionException {
        IType exprTyp = expression.typeCheck(typeEnv);
        if(!exprTyp.equals(new BoolType())){
            throw new StatementException("The condition of IF is not a boolean");
        }
        statementThen.typeCheck(typeEnv.deepCopy());
        statementElse.typeCheck(typeEnv.deepCopy());
        return typeEnv;
    }

    public String toString(){
        return "if (" + expression + "){ " + statementThen + "} else { " + statementElse + "}";
    }
}
