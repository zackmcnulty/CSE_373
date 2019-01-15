package calculator.ast;

import calculator.interpreter.Environment;
import calculator.errors.EvaluationError;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;

import java.util.Iterator;
import datastructures.concrete.DoubleLinkedList;
import calculator.gui.ImageDrawer;

/**
 * All of the public static methods in this class are given the exact same parameters for
 * consistency. You can often ignore some of these parameters when implementing your
 * methods.
 *
 * Some of these methods should be recursive. You may want to consider using public-private
 * pairs in some cases.
 */
public class ExpressionManipulators {
    /**
     * Checks to make sure that the given node is an operation AstNode with the expected
     * name and number of children. Throws an EvaluationError otherwise.
     */
    private static void assertNodeMatches(AstNode node, String expectedName, int expectedNumChildren) {
        if (!node.isOperation()
                && !node.getName().equals(expectedName)
                && node.getChildren().size() != expectedNumChildren) {
            throw new EvaluationError("Node is not valid " + expectedName + " node.");
        }
    }

    /**
     * Accepts an 'toDouble(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'toDouble'.
     * - The 'node' parameter has exactly one child: the AstNode to convert into a double.
     *
     * Postconditions:
     *
     * - Returns a number AstNode containing the computed double.
     *
     * For example, if this method receives the AstNode corresponding to
     * 'toDouble(3 + 4)', this method should return the AstNode corresponding
     * to '7'.
     * 
     * This method is required to handle the following binary operations
     *      +, -, *, /, ^
     *  (addition, subtraction, multiplication, division, and exponentiation, respectively) 
     * and the following unary operations
     *      negate, sin, cos
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if any of the expressions uses an unknown operation.
     */
    
    public static AstNode handleToDouble(Environment env, AstNode node) {
        // To help you get started, we've implemented this method for you.
        // You should fill in the locations specified by "your code here"
        // in the 'toDoubleHelper' method.
        //
        // If you're not sure why we have a public method calling a private
        // recursive helper method, review your notes from CSE 143 (or the
        // equivalent class you took) about the 'public-private pair' pattern.

        assertNodeMatches(node, "toDouble", 1); //verifies pre-conditions
        AstNode exprToConvert = node.getChildren().get(0);
        return new AstNode(toDoubleHelper(env.getVariables(), exprToConvert));
    }
    
    // given an expression tree, evaluates the given expression using the known variables
    // and returns a double. If it encounters an unknown variable or operator, throws 
    // an EvaluationError.
    private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {
        // There are three types of nodes, so we have three cases. 
        if (node.isNumber()) { 
            return node.getNumericValue();
        } else if (node.isVariable()) {
            String name = node.getName();
            if (!variables.containsKey(name)) {
                throw new EvaluationError("Undefined variable");
            } 
            return toDoubleHelper(variables, variables.get(name));
        } else {
            // You may assume the expression node has the correct number of children.
            // If you wish to make your code more robust, you can also use the provided
            // "assertNodeMatches" method to verify the input is valid.
            String name = node.getName();
            IList<AstNode> children = node.getChildren();
            if (children.size() == 1) { // uniary operations
                AstNode inside = children.get(0);
                if (name.equals("sin")) {
                    return Math.sin(toDoubleHelper(variables, inside));
                } else if (name.equals("cos")) {
                    return Math.cos(toDoubleHelper(variables, inside));
                } else if (name.equals("negate")) {
                    return ((toDoubleHelper(variables, inside)) * -1);
                } else if (name.equals("ln")) { // needed in extra credit derive method
                    return Math.log(toDoubleHelper(variables, inside));
                }
            } else if (children.size() == 2) { // binary operations
                AstNode first = children.get(0);
                AstNode second = children.get(1);
                if (name.equals("+")) {
                    return toDoubleHelper(variables, first) + toDoubleHelper(variables, second);
                } else if (name.equals("-")) {
                    return toDoubleHelper(variables, first) - toDoubleHelper(variables, second);
                } else if (name.equals("*")) {
                    return toDoubleHelper(variables, first) * toDoubleHelper(variables, second);
                } else if (name.equals("/")) {
                    return toDoubleHelper(variables, first) / toDoubleHelper(variables, second);
                } else if (name.equals("^")) {
                    return (Math.pow(toDoubleHelper(variables, first), toDoubleHelper(variables, second)));
                } else if (name.equals("equals")) { // needed in extra credit solve method
                    return toDoubleHelper(variables, first) == toDoubleHelper(variables, second) ?  1:  0;
                }
            }
            // if we reach the end without returning, node satisfies none of the conditions
            throw new EvaluationError("Undefined Node");
        }
     }

    /**
     * Accepts a 'simplify(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'simplify'.
     * - The 'node' parameter has exactly one child: the AstNode to simplify
     *
     * Postconditions:
     *
     * - Returns an AstNode containing the simplified inner parameter.
     *
     * For example, if we received the AstNode corresponding to the expression
     * "simplify(3 + 4)", you would return the AstNode corresponding to the
     * number "7".
     *
     * Note: there are many possible simplifications we could implement here,
     * but you are only required to implement a single one: constant folding.
     *
     * That is, whenever you see expressions of the form "NUM + NUM", or
     * "NUM - NUM", or "NUM * NUM", simplify them.
     */
    public static AstNode handleSimplify(Environment env, AstNode node) {
        // Try writing this one on your own!
        // Hint 1: Your code will likely be structured roughly similarly
        //         to your "handleToDouble" method
        // Hint 2: When you're implementing constant folding, you may want
        //         to call your "handleToDouble" method in some way
        // Hint 3: When implementing your private pair, think carefully about
        //         when you should recurse. Do you recurse after simplifying
        //         the current level? Or before?

        assertNodeMatches(node, "simplify", 1); // checks pre-conditions
        
        return simplifyHelper(env.getVariables(), node.getChildren().get(0)); 
    }
    
    // helper method to handleSimplify that returns the simplified expression stored in a new AstNode subtree
    // that has root "node".
    private static AstNode simplifyHelper(IDictionary<String, AstNode> variables, AstNode node) {
        if (node.isNumber()) {
            return new AstNode(node.getNumericValue());
        }else if (node.isVariable()) {
            String var = node.getName();
            if (variables.containsKey(var)) {
                return simplifyHelper(variables, variables.get(var));
            } else {
                return new AstNode(var);
            }
        } else { // node is operation node
            // simplify each child node; then see if they can be combined under current operation
            IList<AstNode> nodeChildren = node.getChildren();
            Iterator<AstNode> iter = nodeChildren.iterator();
            IList<AstNode> simpChildren = new DoubleLinkedList<>();
            while (iter.hasNext()) {
                AstNode next = iter.next();
                AstNode simplifiedNode = simplifyHelper(variables, next);
                simpChildren.add(simplifiedNode);
            }
            
            // check if all simplified nodes are numeric; if they are, then we can combine them
            boolean allNums = true;
            for (AstNode child : simpChildren) {
                if (!child.isNumber()) {
                    allNums = false;
                }
            }
            
            String operType = node.getName();
            // if the operation node is an operation that simplify can reduce, reduce it
            // else just return the node as is with simplified children.
            if (allNums && (operType.equals("+") || operType.equals("-") || operType.equals("*"))) {
                return new AstNode(toDoubleHelper(variables, new AstNode(operType, simpChildren)));
            } else if (operType.equals("derive")) {
                return simplifyHelper(variables, handleDerive(variables, node, simpChildren));
            
            // extra credit: simplifying common operations with 1 and 0.
            } else if (simpChildren.size() == 2) {
                AstNode output = checkCommSimps(variables, simpChildren, operType);
                if (!(output == null)) {
                    return output;
                }
            }
            return new AstNode(operType, simpChildren);
        }
    }
    
    
    // checks for common simplifications involving multiplication/addition/division of
    // 1's and 0's. Returns simplified node if such simplifications exist, else returns
    // null;
    private static AstNode checkCommSimps(IDictionary<String, AstNode> variables, 
                    IList<AstNode> simpChildren, String operType) {
        AstNode left = simpChildren.get(0);
        AstNode right = simpChildren.get(1);
        if (operType.equals("*")) {
            if (left.isNumber() && left.getNumericValue() == 1) {
                   return right;
            } else if (left.isNumber() && left.getNumericValue() == 0) {
                   return new AstNode(0);
            } else if (right.isNumber() && right.getNumericValue() == 1) {
                    return left;
            } else if (right.isNumber() && right.getNumericValue() == 0) {
                    return new AstNode(0);
            }
        } else if (operType.equals("+")) {
            if (right.isNumber() && right.getNumericValue() == 0) {
                return left;
            } else if (left.isNumber() && left.getNumericValue() == 0) {
                return right;
            }
        } else if (operType.equals("/")) {
            if (left.isNumber() && left.getNumericValue() == 0) {
                return new AstNode(0);
            }
        }
        return null;
    }

 

    /**
     * Accepts an Environment variable and a 'plot(exprToPlot, var, varMin, varMax, step)'
     * AstNode and generates the corresponding plot on the ImageDrawer attached to the
     * environment. Returns some arbitrary AstNode.
     *
     * Example 1:
     *
     * >>> plot(3 * x, x, 2, 5, 0.5)
     *
     * This method will receive the AstNode corresponding to 'plot(3 * x, x, 2, 5, 0.5)'.
     * Your 'handlePlot' method is then responsible for plotting the equation
     * "3 * x", varying "x" from 2 to 5 in increments of 0.5.
     *
     * In this case, this means you'll be plotting the following points:
     *
     * [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
     *
     * ---
     *
     * Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4"
     * from -10 to 10 in 0.01 increments. In this case, "a" is our "x" variable.
     *
     * >>> c := 4
     * 4
     * >>> step := 0.01
     * 0.01
     * >>> plot(a^2 + c*a + a, a, -10, 10, step)
     *
     * ---
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if varMin > varMax
     * @throws EvaluationError  if 'var' was already defined
     * @throws EvaluationError  if 'step' is zero or negative
     */
    public static AstNode plot(Environment env, AstNode node) {
        assertNodeMatches(node, "plot", 5);
        plotHelper(node, env.getImageDrawer(), env.getVariables());
        // Note: every single function we add MUST return an
        // AST node that your "simplify" function is capable of handling.
        // However, your "simplify" function doesn't really know what to do
        // with "plot" functions (and what is the "plot" function supposed to
        // evaluate to anyways?) so we'll settle for just returning an
        // arbitrary number.
        //
        // When working on this method, you should uncomment the following line:
        //
        return new AstNode(1);
    }
    
    // helper method to plot that generates and plots discrete points specified by the given parameters
    private static void plotHelper(AstNode node, ImageDrawer imgd, IDictionary<String, AstNode> variables) {
        IList<AstNode> parameters = node.getChildren();
        
        AstNode varMax = parameters.get(3);
        double max = toDoubleHelper(variables, varMax);
        AstNode varMin = parameters.get(2);
        double min = toDoubleHelper(variables, varMin);
        // toDouble will throw the evaluation error for undefined node/variable 
        // if varMax or varMin cannot be reduced to a numeric
        
        if (max < min) {
            throw new EvaluationError("Lower bound cannot be greater than upper bound");
        }
        
        AstNode indVar = parameters.get(1);
        if (!indVar.isVariable()) {
            throw new EvaluationError("Given independent variable is not a variable");
        }
        
        String var = indVar.getName();
        if (variables.containsKey(var)) {
            throw new EvaluationError("Variable: " + var + " was already defined");
        }
        
        AstNode step = parameters.get(4);
        // throws evaluation error if step cannot be reduced to a numeric
        double stepVal = toDoubleHelper(variables, step);
        if (stepVal <= 0) {
            throw new EvaluationError("Step value cannot be less than or equal to 0");
        }
        
        IList<Double> yVals = new DoubleLinkedList<Double>();
        IList<Double> xVals = new DoubleLinkedList<Double>();
        double i = min;
        AstNode exprToPlot = parameters.get(0);
        while (i <= max) {
            xVals.add(i);
            variables.put(var, new AstNode(i));
            yVals.add(toDoubleHelper(variables, exprToPlot));
            i += stepVal;
        }
        variables.remove(var); 
        imgd.drawScatterPlot("Plot", "x", "output", xVals, yVals);
    }
    
    
    // attempted extra credit operations: derive and solve methods!
    
    // support for equals() operator added in toDouble above, but not essentially used in the following
    
    
    /**
     * Accepts an expression to solve  and an initial guess: solve(expression, variable, initialGuess)
     *  And uses newton's method to iteratively search for a solution near initial guess.
     *  
     *  example call solve(equals(3*x, x), x, 1)
     *  
     * @throws EvaluationError if given variable is already defined
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError if solver cannot find a solution (expression may not converge iteratively)
     * @throws EvaluationError if given guess is non numeric
     */
   
    public static AstNode handleSolve(Environment env, AstNode node) {
        assertNodeMatches(node, "solve", 3);
        IList<AstNode> children = node.getChildren();
        IDictionary<String, AstNode> variables = env.getVariables();
        AstNode var = children.get(1);
        if (!var.isVariable()) {
            throw new EvaluationError("Variable given is not a variable");
        }
        String varName = var.getName();
        if (variables.containsKey(varName)) {
            throw new EvaluationError("Variable " + varName + " is already defined");
        }
        AstNode equation = children.get(0);
        AstNode expression = new AstNode("-", equation.getChildren().get(0), equation.getChildren().get(1));
        AstNode guess = children.get(2);
        // throws evaluation error if guess is non numeric
        double initialGuess = toDoubleHelper(variables, guess);
        return new AstNode(solveHelper(variables, varName, expression, new AstNode(initialGuess)));
    }
    
    private static double solveHelper(IDictionary<String, AstNode> variables, 
                    String varName, AstNode expression, AstNode guess) {
        double tol = Math.pow(10, -5);
        double error = 1;
        int maxIter = 10000;
        variables.put(varName, guess);
        AstNode fPrime = deriveFn(variables, expression, varName);
        AstNode fOverFprime = new AstNode("/", expression, fPrime);
        AstNode iter = new AstNode("-", new AstNode(varName), fOverFprime);
        int iterCount = 0;
        while (error > tol) {
            double xNew = toDoubleHelper(variables, iter);
            variables.put(varName, new AstNode(xNew));
            error = Math.abs(toDoubleHelper(variables, expression));
            iterCount += 1;
            if (iterCount > maxIter) {
                variables.remove(varName);
                throw new EvaluationError("Solution Did Not Converge");
            }
        }
        double finalValue = variables.get(varName).getNumericValue();
        variables.remove(varName);
        return finalValue;
    }
    
    
    
    /**
     * Returns an AstNode tree representing the derivative of the expression stored within node. Can
     * handle issues involving chain rule, product rule, quotient rule, and such.
     * sample call: derive(3*x + 1, x)
     * ** all variables besides x must have some numeric value, or only be functions of x.
     * 
     * @throws EvaluationError if given variable is already defined
     * @throws EvaluationError if a given variable in expression is not defined, or not a function of x
     * 
     */
    
    public static AstNode handleDerive(IDictionary<String, AstNode> variables, AstNode node, 
                    IList<AstNode> simpChildren) {
        assertNodeMatches(node, "derive", 2);
        AstNode expression = simpChildren.get(0);
        AstNode var = simpChildren.get(1);
        if (!var.isVariable()) {
            throw new EvaluationError("Given variable is not a variable");
        }
        
        return simplifyHelper(variables, deriveFn(variables, expression, var.getName()));
    }
    
    
    // recursively finds the derivative for the given expression stored 
    // within node and the nodes below it. Applies chain rule, product rule, quotient rule, and
    // other basic arithmetic derivatives to find derivatives of all functions defined under the calculator.
    private static AstNode deriveFn(IDictionary<String, AstNode> variables, AstNode node, String var) {
        if (node.isVariable()) {
            String varName = node.getName();
            if (varName.equals(var)) {
                return new AstNode(1);
            } else if (variables.containsKey(varName)) {
                return deriveFn(variables, variables.get(varName), var);
            } else {
                throw new EvaluationError("Variable does not match variable given");
            }
        } else if (node.isNumber()) {
            return new AstNode(0);
        } else { // is operation
            IList<AstNode> children = node.getChildren();
            IList<AstNode> newKids = new DoubleLinkedList<>();
            String operType = node.getName();
            if (children.size() == 1) { // uniary operations
                AstNode inside = children.get(0);
                newKids.add(deriveFn(variables, inside, var));
                
                if (operType.equals("sin")) {
                    AstNode outside =  new AstNode("cos", children); 
                    newKids.add(outside);
                    return new AstNode("*", newKids);
                    
                } else if (operType.equals("cos")) {
                    AstNode outside =  new AstNode("sin", children);
                    newKids.add(outside);
                    IList<AstNode> secondBranch = new DoubleLinkedList<>();
                    secondBranch.add(new AstNode("*", newKids));
                   return new AstNode("negate", secondBranch);
                   
                } else if (operType.equals("negate")) {
                    return new AstNode("negate", newKids);
                    
                } else if (operType.equals("ln")) {
                    newKids.add(new AstNode("/", new AstNode(1), inside));
                    return new AstNode("*", newKids);
                }
            } else if (children.size() == 2) {
                AstNode f = children.get(0);
                AstNode g = children.get(1);

                AstNode fPrime = deriveFn(variables, f, var);
                AstNode gPrime = deriveFn(variables, g, var);

                if (operType.equals("+") || operType.equals("-")) {
                    return new AstNode(operType, fPrime, gPrime);
                } else if (operType.equals("*")) {

                    AstNode left = new AstNode("*", fPrime, g);

                    AstNode right = new AstNode("*", gPrime, f);
                    
                    return new AstNode("+", left, right);
                } else if (operType.equals("/")) {
                    AstNode n1 = new AstNode("*", g, fPrime);
                    
                    AstNode n2 = new AstNode("*", f, gPrime);
                    
                    AstNode n3 = new AstNode("^", g, new AstNode(2));
                    
                    AstNode n4 = new AstNode("-", n1, n2);
                    
                    return new AstNode("/", n4, n3);
                } else if (operType.equals("^")) {
                    AstNode logF = new AstNode("ln", f);
                    
                    AstNode right = new AstNode("*", gPrime, logF);
                    
                    AstNode div = new AstNode("/", g, f);
                    
                    AstNode n3 = new AstNode("*", div, fPrime);
                    
                    AstNode n4 = new AstNode("+", n3, right);
                    
                    return new AstNode("*", node, n4);
                }
            }
        }
        throw new EvaluationError("Unknown Node");
    }
}