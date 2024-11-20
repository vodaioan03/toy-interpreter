import controller.Controller;
import java.io.BufferedReader;
import model.adt.MyDictionary;
import model.adt.MyHeap;
import model.adt.MyList;
import model.adt.MyStack;
import model.expressions.*;
import model.state.PrgState;
import model.statements.*;
import model.types.BoolType;
import model.types.IntType;
import model.types.StringType;
import model.values.BoolValue;
import model.values.IValue;
import model.values.IntValue;
import model.values.StringValue;
import model.types.ReferenceType;
import repository.IRepository;
import repository.MyRepository;
import view.TextMenu;
import view.commands.ExitCommand;
import view.commands.RunExampleCommand;

public class Main {
    public static void main(String[] args) {

        IStatement ex1 = new CompoundStatement(new VarDecStatement("v", new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(2))),
                        new PrintStatement(new VariableExpression("v"))));

        IStatement ex2 = new CompoundStatement(new VarDecStatement("a", new IntType()),
                new CompoundStatement(new VarDecStatement("b", new IntType()),
                        new CompoundStatement(new AssignStatement("a",
                                new ArithmeticExpression(new ValueExpression(new IntValue(2)), ArithmeticalOperator.PLUS,
                                        new ArithmeticExpression(new ValueExpression(new IntValue(3)), ArithmeticalOperator.MULTIPLY,
                                                new ValueExpression(new IntValue(5))))), new CompoundStatement(new AssignStatement("b",
                                new ArithmeticExpression(new VariableExpression("a"), ArithmeticalOperator.PLUS,
                                        new ValueExpression(new IntValue(1)))), new PrintStatement(new VariableExpression("b"))))));

        IStatement ex3 = new CompoundStatement(new VarDecStatement("a", new BoolType()),
                new CompoundStatement(new VarDecStatement("v", new IntType()),
                        new CompoundStatement(new AssignStatement("a", new ValueExpression(new BoolValue(true))),
                                new CompoundStatement(new IfStatement(new VariableExpression("a"),
                                        new AssignStatement("v", new ValueExpression(new IntValue(2))),
                                        new AssignStatement("v", new ValueExpression(new IntValue(3)))),
                                        new PrintStatement(new VariableExpression("v"))))));

        IStatement ex4 = new CompoundStatement(new VarDecStatement("varf", new StringType()),
                new CompoundStatement(new AssignStatement("varf", new ValueExpression(new StringValue("test.in"))),
                        new CompoundStatement(new OpenRFileStatement(new VariableExpression("varf")),
                                new CompoundStatement(new VarDecStatement("varc", new IntType()),
                                        new CompoundStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                        new CompoundStatement(new ReadFileStatement(new VariableExpression("varf"), "varc"),
                                                                new CompoundStatement(new PrintStatement(new VariableExpression("varc")),
                                                                        new CloseRFileStatement(new VariableExpression("varf"))))))))));


        IStatement ex5 = new IfStatement(new RelationalExpression(new ValueExpression(new IntValue(2)), ComparisonOperator.LESS,
                new ValueExpression(new IntValue(3))), new PrintStatement(new ValueExpression(new IntValue(4))),
                new PrintStatement(new ValueExpression(new IntValue(5))));

        IRepository repo = new MyRepository("new_list.txt");
        repo.addPrgState(new PrgState(new MyList<String>(), new MyDictionary<String, IValue>(), new MyDictionary<StringValue, BufferedReader>(), new MyStack<IStatement>(), new MyHeap(),ex5));

        IRepository repo2 = new MyRepository("ex4.txt");
        repo2.addPrgState(new PrgState(new MyList<String>(), new MyDictionary<String, IValue>(), new MyDictionary<StringValue, BufferedReader>(), new MyStack<IStatement>(),new MyHeap(), ex4));

        Controller ctrl = new Controller(repo);
        Controller ctrl2 = new Controller(repo2);




        IStatement newEx1 = new CompoundStatement(new VarDecStatement("v",new ReferenceType(new IntType())),
                new CompoundStatement(new AllocateStatement("v", new ValueExpression(new IntValue(20))),
                        new CompoundStatement(new VarDecStatement("a",new ReferenceType(new ReferenceType(new IntType()))),
                                new CompoundStatement(new AllocateStatement("a", new VariableExpression("v")),
                                        new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                                new PrintStatement(new VariableExpression("a")))))));

        IStatement newEx2 =         new CompoundStatement(new VarDecStatement("v",new ReferenceType(new IntType())),
                        new CompoundStatement(new AllocateStatement("v", new ValueExpression(new IntValue(20))),
                                new CompoundStatement(new VarDecStatement("a",new ReferenceType(new ReferenceType(new IntType()))),
                                        new CompoundStatement(new AllocateStatement("a", new VariableExpression("v")),
                                                new CompoundStatement(new PrintStatement(new ReadHeapExpression(
                                                        new VariableExpression("v"))), new PrintStatement(new ArithmeticExpression(
                                                        new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))),
                                                        ArithmeticalOperator.PLUS,new ValueExpression(new IntValue(5)))))))));

        IStatement newEx3 = new CompoundStatement(new VarDecStatement("v",new ReferenceType(new IntType())),
                        new CompoundStatement(new AllocateStatement("v", new ValueExpression(new IntValue(20))),
                                new CompoundStatement(new PrintStatement(new ReadHeapExpression(new VariableExpression("v"))),
                                        new CompoundStatement(new WriteHeapStatement(new VariableExpression("v"),
                                                new ValueExpression(new IntValue(30))), new PrintStatement(new ArithmeticExpression(
                                                new ReadHeapExpression(new VariableExpression("v")),
                                                ArithmeticalOperator.PLUS,new ValueExpression(new IntValue(5))))))));

        IStatement newEx4 = new CompoundStatement(new VarDecStatement("v",new ReferenceType(new IntType())),
                        new CompoundStatement(new AllocateStatement("v", new ValueExpression(new IntValue(20))),
                                new CompoundStatement(new VarDecStatement("a",new ReferenceType(new ReferenceType(new IntType()))),
                                        new CompoundStatement(new AllocateStatement("a", new VariableExpression("v")),
                                                new CompoundStatement(new AllocateStatement("v", new ValueExpression(new IntValue(30))),
                                                        new PrintStatement(new ReadHeapExpression(new ReadHeapExpression
                                                                (new VariableExpression("a")))))))));

        // int v; v=4; (while(v>0) print(v); v=v-1;) print(v);
        IStatement newEx5 = new CompoundStatement(new VarDecStatement("v",new IntType()),
                new CompoundStatement(new AssignStatement("v", new ValueExpression(new IntValue(4))),
                        new CompoundStatement(new WhileStatement(new RelationalExpression(new VariableExpression("v"),
                                ComparisonOperator.GREATER,new ValueExpression(new IntValue(0))),
                                new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                        new AssignStatement("v",new ArithmeticExpression(new VariableExpression("v"),
                                                ArithmeticalOperator.MINUS,new ValueExpression(new IntValue(1)))))),
                                new PrintStatement(new VariableExpression("v")))));


        IRepository whileGarbageRepo = new MyRepository("whileGarbage.txt");
        //whileGarbageRepo.addPrgState(new PrgState(new MyList<String>(), new MyDictionary<String, IValue>(), new MyDictionary<StringValue, BufferedReader>(), new MyStack<IStatement>(),new MyHeap(), newEx1));
        //whileGarbageRepo.addPrgState(new PrgState(new MyList<String>(), new MyDictionary<String, IValue>(), new MyDictionary<StringValue, BufferedReader>(), new MyStack<IStatement>(),new MyHeap(), newEx2));
        //whileGarbageRepo.addPrgState(new PrgState(new MyList<String>(), new MyDictionary<String, IValue>(), new MyDictionary<StringValue, BufferedReader>(), new MyStack<IStatement>(),new MyHeap(), newEx3));
        whileGarbageRepo.addPrgState(new PrgState(new MyList<String>(), new MyDictionary<String, IValue>(), new MyDictionary<StringValue, BufferedReader>(), new MyStack<IStatement>(),new MyHeap(), newEx4));
        //whileGarbageRepo.addPrgState(new PrgState(new MyList<String>(), new MyDictionary<String, IValue>(), new MyDictionary<StringValue, BufferedReader>(), new MyStack<IStatement>(),new MyHeap(), newEx5));
        Controller newController = new Controller(whileGarbageRepo);

        TextMenu menu = new TextMenu();
        menu.addCommand(new ExitCommand("0", "Do you want to exit?"));
        menu.addCommand(new RunExampleCommand("1", "Run all examples.", newController));
        menu.addCommand(new RunExampleCommand("2", "Run example 6 [WHILE].", ctrl2));
        menu.show();

    }


}