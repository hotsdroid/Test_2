package org.ogu.lang.parser.ast.decls;

import org.ogu.lang.parser.ast.OguIdentifier;
import org.ogu.lang.parser.ast.typeusage.TypeArg;

import java.util.ArrayList;
import java.util.List;

/**
 * a def declaration
 * def funcion : T -> T -> T
 * Created by ediaz on 23-01-16.
 */
public class FunctionDeclaration extends FunctionalDeclaration {

    private List<TypeArg> paramTypes;
    private TypeArg returnType;


    public FunctionDeclaration(OguIdentifier id, List<TypeArg> paramTypes, List<Decorator> decorators) {
        super(id, decorators);
        this.paramTypes = new ArrayList<>();
        this.paramTypes.addAll(paramTypes.subList(0, paramTypes.size()-1));
        this.paramTypes.forEach((p) -> p.setParent(this));
        this.returnType = paramTypes.get(paramTypes.size()-1);
        this.returnType.setParent(this);

    }

    @Override
    public String toString() {

        return "FunctionDeclaration{" +
                "id ='" + name + '\'' +
                ", returnType =" + returnType+
                ", paramTypes =" + paramTypes +
                ", decorators = " + decorators +
                '}';
    }


}