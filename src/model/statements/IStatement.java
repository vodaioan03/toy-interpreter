package model.statements;

import exceptions.StatementException;
import model.state.PrgState;

public interface IStatement {
    PrgState execute(PrgState state) throws StatementException;
    IStatement deepCopy();
}
