package cmsc13.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Manages the entire question bank for The Paradigm Trials
 */
public class QuestionBank {
    private static final Set<String> VALID_TOPICS = new HashSet<>(Arrays.asList(
        "Introduction to Programming Paradigms",
        "Procedural Programming",
        "Functional Programming",
        "Object-Oriented Programming",
        "Imperative vs Declarative Programming",
        "Event-Driven Programming",
        "Component Mappings between Programming Paradigms"
    ));
    
    private List<Question> allQuestions;
    
    public QuestionBank() {
        this.allQuestions = new ArrayList<>();
        initializeQuestions();
    }
    
    private void initializeQuestions() {
        // Trial 1 - Introduction to Programming Paradigms (7 theory + 7 practical)
        addTrial1Questions();
        
        // Trial 2 - Procedural Programming (7 theory + 7 practical)
        addTrial2Questions();
        
        // Trial 3 - Object-Oriented Programming Part 1 (7 theory + 7 practical)
        addTrial3Questions();
        
        // Trial 4 - Object-Oriented Programming Part 2 - Inheritance (7 theory + 7 practical)
        addTrial4Questions();
        
        // Trial 5 - Functional Programming (7 theory + 7 practical)
        addTrial5Questions();
        
        // Trial 6 - Imperative vs Declarative (7 theory + 7 practical)
        addTrial6Questions();
        
        // Trial 7 - Paradigm Comparison (7 theory + 7 practical)
        addTrial7Questions();
        
        // Trial 8 - Event-Driven Programming (7 theory + 7 practical)
        addTrial8Questions();
        
        // Trial 9 - Component Mappings (7 theory + 7 practical)
        addTrial9Questions();
        
        // Trial 10 - System Mastery (7 theory + 7 practical)
        addTrial10Questions();
    }
    
    private void addTrial1Questions() {
        int qId = 1;
        
        // Theory Questions
        addQuestion(qId++, QuestionType.THEORY, "Introduction to Programming Paradigms", 1,
            "What is a programming paradigm?",
            Arrays.asList(
                "A set of tools for debugging code",
                "A style and approach to programming with a set of concepts and rules",
                "A programming language",
                "A method for organizing files"
            ), 1, "A paradigm is a style of programming with its own concepts and rules.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Introduction to Programming Paradigms", 1,
            "Which of the following is NOT typically considered a programming paradigm?",
            Arrays.asList(
                "Object-Oriented Programming",
                "Functional Programming",
                "Procedural Programming",
                "Alphabetical Programming"
            ), 3, "Alphabetical Programming is not a recognized paradigm.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Introduction to Programming Paradigms", 1,
            "What does imperative programming emphasize?",
            Arrays.asList(
                "Describing what the result should be",
                "Telling the computer how to do something step-by-step",
                "Using mathematical functions",
                "Object interactions"
            ), 1, "Imperative programming focuses on how to solve problems.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Introduction to Programming Paradigms", 1,
            "Which paradigm treats programs as transformations of data?",
            Arrays.asList(
                "Procedural",
                "Functional",
                "Object-Oriented",
                "Event-Driven"
            ), 1, "Functional programming focuses on functions and data transformations.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Introduction to Programming Paradigms", 1,
            "What is the main benefit of using multiple paradigms?",
            Arrays.asList(
                "It makes the code slower",
                "Different problems are better suited to different approaches",
                "It requires more programming languages",
                "It eliminates the need for testing"
            ), 1, "Different paradigms suit different problems and contexts.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Introduction to Programming Paradigms", 1,
            "Which paradigm focuses on objects that contain both data and behavior?",
            Arrays.asList(
                "Functional",
                "Procedural",
                "Object-Oriented",
                "Declarative"
            ), 2, "Object-Oriented programming is centered around objects.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Introduction to Programming Paradigms", 1,
            "What is a pure function in functional programming?",
            Arrays.asList(
                "A function that takes only one argument",
                "A function with no return value",
                "A function that always returns the same output for the same input",
                "A function that modifies global variables"
            ), 2, "Pure functions have no side effects and are deterministic.",2);
        
        // Practical Questions
        addQuestion(qId++, QuestionType.PROGRAMMING, "Introduction to Programming Paradigms", 1,
            "Given this code: int sum = 0; for(int i=0; i<10; i++) { sum += i; } Which paradigm does this demonstrate?",
            Arrays.asList(
                "Functional",
                "Procedural",
                "Object-Oriented",
                "Declarative"
            ), 1, "This is imperative/procedural code with step-by-step instructions.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Introduction to Programming Paradigms", 1,
            "Which code snippet best represents functional programming?",
            Arrays.asList(
                "for(int i=0; i<n; i++) { process(arr[i]); }",
                "numbers.map(x -> x * 2).filter(x -> x > 5)",
                "obj.method().method2().method3();",
                "if(condition) { doA(); } else { doB(); }"
            ), 1, "Functional code uses map, filter, and function composition.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Introduction to Programming Paradigms", 1,
            "What would an object-oriented approach to modeling a car look like?",
            Arrays.asList(
                "A list of car instructions",
                "A class with properties (color, speed) and methods (accelerate, brake)",
                "A mathematical function",
                "A series of if-statements"
            ), 1, "OOP models things as classes with properties and methods.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Introduction to Programming Paradigms", 1,
            "Functional programming avoids which of the following?",
            Arrays.asList(
                "Function calls",
                "Parameters",
                "Side effects and mutable state",
                "Return statements"
            ), 2, "Functional programming minimizes side effects.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Introduction to Programming Paradigms", 1,
            "Which paradigm is most similar to SQL?",
            Arrays.asList(
                "Procedural",
                "Functional",
                "Declarative",
                "Event-Driven"
            ), 2, "SQL is declarative - you declare what you want, not how.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Introduction to Programming Paradigms", 1,
            "Identify the paradigm: mouse.onClick(() -> handleClick()); ",
            Arrays.asList(
                "Procedural",
                "Functional",
                "Event-Driven",
                "Declarative"
            ), 2, "This is event-driven programming with a callback.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Introduction to Programming Paradigms", 1,
            "What does this declarative code describe? SELECT * FROM users WHERE age > 18",
            Arrays.asList(
                "How to find users step-by-step",
                "The steps to execute a loop",
                "What data you want, not how to get it",
                "An object with properties"
            ), 2, "Declarative code describes what, not how.",2);
    }
    
    private void addTrial2Questions() {
        int qId = 15;
        
        // Theory Questions
        addQuestion(qId++, QuestionType.THEORY, "Procedural Programming", 2,
            "What is the fundamental principle of procedural programming?",
            Arrays.asList(
                "Everything is an object",
                "Computation happens through sequences of procedures",
                "Avoid changing state",
                "Respond to events"
            ), 1, "Procedural programming is based on sequences of procedures.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Procedural Programming", 2,
            "Which of these is a characteristic of procedural programming?",
            Arrays.asList(
                "Data and functions are bundled together",
                "State changes through variable modifications",
                "No use of functions",
                "No return values"
            ), 1, "Procedural programming uses mutable state.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Procedural Programming", 2,
            "What is a procedure in procedural programming?",
            Arrays.asList(
                "A data structure",
                "A named sequence of instructions that performs a task",
                "An object method",
                "A mathematical formula"
            ), 1, "A procedure is a sequence of instructions.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Procedural Programming", 2,
            "Which language is primarily procedural?",
            Arrays.asList(
                "Python",
                "C",
                "Haskell",
                "Ruby"
            ), 1, "C is a classic procedural language.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Procedural Programming", 2,
            "What is structured programming?",
            Arrays.asList(
                "Using only objects",
                "Breaking down problems into smaller procedures with control structures",
                "Functional composition",
                "Event handling"
            ), 1, "Structured programming breaks problems into procedures.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Procedural Programming", 2,
            "In procedural programming, what is the primary way to avoid code repetition?",
            Arrays.asList(
                "Using objects",
                "Creating functions/procedures",
                "Global variables",
                "Comments"
            ), 1, "Procedures eliminate code repetition.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Procedural Programming", 2,
            "What is a major limitation of procedural programming?",
            Arrays.asList(
                "Cannot perform calculations",
                "Limited reusability across unrelated code",
                "Cannot handle loops",
                "No function support"
            ), 1, "Procedural code can be hard to reuse.",2);
        
        // Practical Questions
        addQuestion(qId++, QuestionType.PROGRAMMING, "Procedural Programming", 2,
            "Write a procedure to calculate factorial. What would be the first step?",
            Arrays.asList(
                "Create an object",
                "Accept a number as input",
                "Use functional composition",
                "Define multiple classes"
            ), 1, "A procedure needs input.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Procedural Programming", 2,
            "In procedural code, how do you pass data between procedures?",
            Arrays.asList(
                "Through global objects",
                "Parameters and return values",
                "Only through files",
                "Data cannot be shared"
            ), 1, "Procedures communicate through parameters and return values.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Procedural Programming", 2,
            "What does this procedural code do? void swap(int &a, int &b) { int temp = a; a = b; b = temp; }",
            Arrays.asList(
                "Creates two integers",
                "Exchanges the values of two variables",
                "Returns a new variable",
                "Compares two values"
            ), 1, "This swaps two values.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Procedural Programming", 2,
            "Which demonstrates procedural thinking?",
            Arrays.asList(
                "Plan 1: Boil water, Add tea, Add milk, Stir",
                "Treat tea as a blend object with properties",
                "Wait for a button click to make tea",
                "Describe all possible tea combinations"
            ), 0, "Step-by-step instructions are procedural.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Procedural Programming", 2,
            "In procedural programming, global variables are:",
            Arrays.asList(
                "Always avoided",
                "Commonly used but can cause issues",
                "Required",
                "Not supported"
            ), 1, "Global variables are common but problematic in procedural code.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Procedural Programming", 2,
            "What happens when you call a procedure multiple times with the same input?",
            Arrays.asList(
                "Different outputs each time",
                "Same output each time (if deterministic)",
                "The program crashes",
                "The input is lost"
            ), 1, "Deterministic procedures produce consistent results.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Procedural Programming", 2,
            "Procedural code typically uses which control structures?",
            Arrays.asList(
                "Only if statements",
                "If, loops, switches, and function calls",
                "No control structures",
                "Only lambdas"
            ), 1, "Procedural code uses various control structures.",2);
    }
    
    private void addTrial3Questions() {
        int qId = 29;
        
        // Theory Questions
        addQuestion(qId++, QuestionType.THEORY, "Object-Oriented Programming", 3,
            "What is an object in object-oriented programming?",
            Arrays.asList(
                "A data type",
                "An instance of a class with data and behavior",
                "A mathematical formula",
                "A file"
            ), 1, "Objects are instances of classes.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Object-Oriented Programming", 3,
            "What is the relationship between a class and an object?",
            Arrays.asList(
                "A class is an instance of an object",
                "An object is an instance of a class",
                "They are the same thing",
                "Objects are larger than classes"
            ), 1, "Objects are instances created from class blueprints.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Object-Oriented Programming", 3,
            "What is encapsulation?",
            Arrays.asList(
                "Making all data public",
                "Bundling data and methods together, hiding internal details",
                "Creating multiple copies of data",
                "Using only functions"
            ), 1, "Encapsulation hides internal implementation.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Object-Oriented Programming", 3,
            "What is an attribute in OOP?",
            Arrays.asList(
                "A method name",
                "Data held by an object",
                "A program file",
                "A type of loop"
            ), 1, "Attributes are data members of objects.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Object-Oriented Programming", 3,
            "What is a method in OOP?",
            Arrays.asList(
                "A type of variable",
                "A behavior or function defined in a class",
                "A programming language",
                "A class name"
            ), 1, "Methods are functions within classes.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Object-Oriented Programming", 3,
            "What does 'access control' mean?",
            Arrays.asList(
                "Controlling network access",
                "Controlling who can read/modify attributes and methods",
                "Controlling computer memory",
                "Controlling program speed"
            ), 1, "Access control manages visibility of class members.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Object-Oriented Programming", 3,
            "What is a constructor?",
            Arrays.asList(
                "A method that destroys objects",
                "A special method that initializes an object when created",
                "A loop structure",
                "A variable declaration"
            ), 1, "Constructors initialize objects.",2);
        
        // Practical Questions
        addQuestion(qId++, QuestionType.PROGRAMMING, "Object-Oriented Programming", 3,
            "Which represents a class definition?",
            Arrays.asList(
                "class Dog { String name; void bark() { } }",
                "Dog dog = new Dog();",
                "dog.bark();",
                "import Dog;"
            ), 0, "The first is a class definition.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Object-Oriented Programming", 3,
            "Which creates an object (instance)?",
            Arrays.asList(
                "class Car { }",
                "Car myCar = new Car();",
                "void Car() { }",
                "public Car"
            ), 1, "Creating an instance with new.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Object-Oriented Programming", 3,
            "What is private encapsulation used for?",
            Arrays.asList(
                "Making all data public",
                "Hiding internal implementation details",
                "Allowing everyone to modify data",
                "Removing methods"
            ), 1, "Private hides implementation details.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Object-Oriented Programming", 3,
            "If you have: class Person { public String name; public int age; } What are name and age?",
            Arrays.asList(
                "Methods",
                "Attributes/Properties",
                "Classes",
                "Functions"
            ), 1, "These are attributes.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Object-Oriented Programming", 3,
            "What does this represent? public void setName(String n) { name = n; }",
            Arrays.asList(
                "A constructor",
                "A method (setter)",
                "A variable",
                "A class"
            ), 1, "This is a setter method.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Object-Oriented Programming", 3,
            "In OOP, to access an object's method, you typically use:",
            Arrays.asList(
                "object.methodName()",
                "methodName(object)",
                "call methodName",
                "execute object"
            ), 0, "Dot notation accesses object members.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Object-Oriented Programming", 3,
            "What makes OOP 'modular'?",
            Arrays.asList(
                "Using global variables",
                "Breaking problems into interacting objects",
                "Using only functions",
                "Avoiding data structures"
            ), 1, "Objects are modular units.",2);
    }
    
    private void addTrial4Questions() {
        int qId = 43;
        
        // Theory Questions
        addQuestion(qId++, QuestionType.THEORY, "Object-Oriented Programming", 4,
            "What is inheritance in OOP?",
            Arrays.asList(
                "Sharing money between classes",
                "A mechanism for a class to inherit properties from another class",
                "Copying code between files",
                "Renaming variables"
            ), 1, "Inheritance allows classes to extend others.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Object-Oriented Programming", 4,
            "What is a parent class (superclass)?",
            Arrays.asList(
                "A class that extends another class",
                "A class that is extended by other classes",
                "The same as an interface",
                "Always abstract"
            ), 1, "Parent classes are extended by child classes.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Object-Oriented Programming", 4,
            "What is a child class (subclass)?",
            Arrays.asList(
                "A class that other classes extend",
                "A class that extends another class and inherits its properties",
                "A class that cannot have methods",
                "A temporary class"
            ), 1, "Child classes inherit from parent classes.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Object-Oriented Programming", 4,
            "What is polymorphism?",
            Arrays.asList(
                "Many copies of the same code",
                "Many forms - same interface, different implementations",
                "Multiple files",
                "Multiple loops"
            ), 1, "Polymorphism means many forms.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Object-Oriented Programming", 4,
            "What is method overriding?",
            Arrays.asList(
                "Using a method twice",
                "A subclass providing its own implementation of a parent method",
                "Removing a method",
                "Copying a method"
            ), 1, "Overriding replaces a parent method.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Object-Oriented Programming", 4,
            "What is abstraction?",
            Arrays.asList(
                "Making code longer",
                "Hiding complexity, showing only essential features",
                "Removing variables",
                "Using only basic operations"
            ), 1, "Abstraction hides complexity.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Object-Oriented Programming", 4,
            "What is an interface?",
            Arrays.asList(
                "A GUI element",
                "A contract defining methods a class must implement",
                "A type of loop",
                "The same as a class"
            ), 1, "Interfaces define contracts.",2);
        
        // Practical Questions
        addQuestion(qId++, QuestionType.PROGRAMMING, "Object-Oriented Programming", 4,
            "If Dog extends Animal, Dog is:",
            Arrays.asList(
                "A parent of Animal",
                "A child of Animal",
                "Unrelated to Animal",
                "The same as Animal"
            ), 1, "Dog extends (inherits from) Animal.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Object-Oriented Programming", 4,
            "Which is an example of inheritance?",
            Arrays.asList(
                "class Animal { }",
                "class Dog extends Animal { }",
                "new Animal();",
                "Animal animal;"
            ), 1, "extends shows inheritance.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Object-Oriented Programming", 4,
            "What does @Override indicate?",
            Arrays.asList(
                "The method replaces a parent's method",
                "The method is final",
                "The method is static",
                "The method returns nothing"
            ), 0, "@Override marks overridden methods.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Object-Oriented Programming", 4,
            "If Animal has makeSound() and Dog overrides it, what happens?",
            Arrays.asList(
                "Dog ignores makeSound()",
                "Dog uses Animal's version",
                "Dog uses its own version",
                "An error occurs"
            ), 2, "Overriding replaces the parent method.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Object-Oriented Programming", 4,
            "Is 'Cat extends Animal' an example of polymorphism?",
            Arrays.asList(
                "No, it's just inheritance",
                "Yes, same interface (animal) with different implementations",
                "No, classes cannot extend",
                "Only if methods are abstract"
            ), 1, "Inheritance enables polymorphism.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Object-Oriented Programming", 4,
            "An abstract class is useful for:",
            Arrays.asList(
                "Creating instances directly",
                "Defining common structure for subclasses",
                "Removing inheritance",
                "Avoiding methods"
            ), 1, "Abstract classes define structure.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Object-Oriented Programming", 4,
            "What does implementing an interface require?",
            Arrays.asList(
                "Optional method implementation",
                "Implementing all methods defined in the interface",
                "Inheriting from another class",
                "Nothing special"
            ), 1, "All interface methods must be implemented.",2);
    }
    
    private void addTrial5Questions() {
        int qId = 57;
        
        // Theory Questions
        addQuestion(qId++, QuestionType.THEORY, "Functional Programming", 5,
            "What is functional programming?",
            Arrays.asList(
                "Programming with functions only",
                "Programming that treats computation as evaluation of functions avoiding state change",
                "Programming that uses function names",
                "Programming with many features"
            ), 1, "Functional programming focuses on functions and immutability.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Functional Programming", 5,
            "What is a pure function?",
            Arrays.asList(
                "A function in a pure language",
                "Always returns same output for same input with no side effects",
                "A function without parameters",
                "A function that modifies global state"
            ), 1, "Pure functions are deterministic.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Functional Programming", 5,
            "What is immutability?",
            Arrays.asList(
                "Changing state frequently",
                "Once created, data cannot be changed",
                "Using mutable variables",
                "Modifying objects often"
            ), 1, "Immutability prevents state changes.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Functional Programming", 5,
            "What is a side effect?",
            Arrays.asList(
                "A function's output",
                "Modifications to state or I/O operations outside the function",
                "A return value",
                "A parameter"
            ), 1, "Side effects modify external state.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Functional Programming", 5,
            "What is higher-order function?",
            Arrays.asList(
                "A function that runs multiple times",
                "A function that takes functions as arguments or returns functions",
                "A function with many parameters",
                "A function with a complex name"
            ), 1, "Higher-order functions operate on functions.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Functional Programming", 5,
            "What is recursion in functional programming?",
            Arrays.asList(
                "Using global variables",
                "A function calling itself to solve smaller versions of a problem",
                "Using loops",
                "Modifying state"
            ), 1, "Recursion replaces loops in functional code.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Functional Programming", 5,
            "What is function composition?",
            Arrays.asList(
                "Writing a function in multiple files",
                "Combining functions so output of one is input to another",
                "Renaming functions",
                "Using only one function"
            ), 1, "Composition chains functions together.",2);
        
        // Practical Questions
        addQuestion(qId++, QuestionType.PROGRAMMING, "Functional Programming", 5,
            "Which is a pure function?",
            Arrays.asList(
                "int getRandomNumber() { return random(); }",
                "void printHello() { System.out.println(\"Hi\"); }",
                "int add(int a, int b) { return a + b; }",
                "int counter = 0; int increment() { return ++counter; }"
            ), 2, "add() is pure - same input, same output, no side effects.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Functional Programming", 5,
            "What does list.map(x -> x * 2) do?",
            Arrays.asList(
                "Modifies the list in place",
                "Creates new list with each element doubled",
                "Removes elements",
                "Combines elements"
            ), 1, "map transforms each element.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Functional Programming", 5,
            "What does list.filter(x -> x > 5) do?",
            Arrays.asList(
                "Multiplies elements",
                "Returns list with only elements greater than 5",
                "Combines elements",
                "Sorts the list"
            ), 1, "filter selects matching elements.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Functional Programming", 5,
            "Why is immutability important in functional programming?",
            Arrays.asList(
                "It makes code slower",
                "It prevents unexpected state changes and bugs",
                "It requires more memory",
                "It eliminates functions"
            ), 1, "Immutability ensures predictability.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Functional Programming", 5,
            "A function that returns a function is:",
            Arrays.asList(
                "Invalid",
                "A higher-order function",
                "Must have a return type of void",
                "Only in imperative languages"
            ), 1, "Returning functions makes them higher-order.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Functional Programming", 5,
            "Recursion in functional programming replaces:",
            Arrays.asList(
                "Function definitions",
                "Classes",
                "Loops",
                "Variables"
            ), 2, "Recursion replaces loops in functional style.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Functional Programming", 5,
            "What is the advantage of functional programming's immutability?",
            Arrays.asList(
                "Easier to parallelize code",
                "No benefits",
                "Slower execution",
                "More memory usage"
            ), 0, "Immutability enables parallelization.",2);
    }
    
    private void addTrial6Questions() {
        int qId = 71;
        
        // Theory Questions
        addQuestion(qId++, QuestionType.THEORY, "Imperative vs Declarative Programming", 6,
            "What is imperative programming?",
            Arrays.asList(
                "Describing what you want",
                "Telling the computer how to do something with explicit steps",
                "Using mathematical functions",
                "Responding to events"
            ), 1, "Imperative means giving commands/steps.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Imperative vs Declarative Programming", 6,
            "What is declarative programming?",
            Arrays.asList(
                "Giving step-by-step instructions",
                "Describing what result you want, not how to get it",
                "Using only loops",
                "Using only objects"
            ), 1, "Declarative focuses on 'what', not 'how'.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Imperative vs Declarative Programming", 6,
            "Which is more declarative?",
            Arrays.asList(
                "for(int i=0; i<n; i++) { sum += i; }",
                "SELECT * FROM users WHERE age > 18",
                "if(x > 0) { doA(); } else { doB(); }",
                "int[] array = new int[10];"
            ), 1, "SQL is declarative.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Imperative vs Declarative Programming", 6,
            "Which is more imperative?",
            Arrays.asList(
                "SELECT name FROM table",
                "for(String name : list) { print(name); }",
                "CSS styling rule",
                "HTML markup"
            ), 1, "The loop is imperative.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Imperative vs Declarative Programming", 6,
            "A CSS rule is an example of:",
            Arrays.asList(
                "Imperative programming",
                "A procedural language",
                "Declarative programming",
                "Object-oriented design"
            ), 2, "CSS declares how things should look.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Imperative vs Declarative Programming", 6,
            "Advantages of declarative programming include:",
            Arrays.asList(
                "More control over execution",
                "Simpler to read and maintain",
                "Faster execution",
                "More powerful"
            ), 1, "Declarative is easier to understand.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Imperative vs Declarative Programming", 6,
            "Many modern languages support which programming style?",
            Arrays.asList(
                "Only imperative",
                "Only declarative",
                "Both imperative and declarative elements",
                "Neither"
            ), 2, "Many languages are multi-paradigm.",2);
        
        // Practical Questions
        addQuestion(qId++, QuestionType.PROGRAMMING, "Imperative vs Declarative Programming", 6,
            "Which is imperative code?",
            Arrays.asList(
                "<h1>Hello</h1>",
                "color: blue;",
                "for(int i=0; i<10; i++) { process(i); }",
                "SELECT * FROM data"
            ), 2, "The loop is imperative.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Imperative vs Declarative Programming", 6,
            "How would imperative programming solve 'sum all numbers'?",
            Arrays.asList(
                "sum = numbers",
                "int sum = 0; for(int n : numbers) { sum += n; }",
                "SELECT SUM FROM numbers",
                "{sum}"
            ), 1, "Explicit loop is imperative.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Imperative vs Declarative Programming", 6,
            "How would declarative programming solve 'sum all numbers'?",
            Arrays.asList(
                "int sum = 0; for...",
                "while(...)",
                "numbers.sum() or SELECT SUM",
                "if(sum > 0) {"
            ), 2, "Declarative expresses intent, not steps.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Imperative vs Declarative Programming", 6,
            "What does this declarative code do? [1,2,3].map(x => x*2)",
            Arrays.asList(
                "Iterates and prints",
                "Creates new array with doubled values",
                "Modifies the original array",
                "Sums the elements"
            ), 1, "map is declarative transformation.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Imperative vs Declarative Programming", 6,
            "Regular expressions are:",
            Arrays.asList(
                "Purely imperative",
                "Declarative patterns for matching",
                "Always procedural",
                "Not a real tool"
            ), 1, "Regex is declarative pattern matching.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Imperative vs Declarative Programming", 6,
            "HTML markup is:",
            Arrays.asList(
                "Imperative code",
                "Declarative - describing structure",
                "A programming language",
                "Functional"
            ), 1, "HTML declares structure.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Imperative vs Declarative Programming", 6,
            "Which approach is easier to read?",
            Arrays.asList(
                "Imperative is always easier",
                "Declarative is often easier for high-level intent",
                "They're equally difficult",
                "None are readable"
            ), 1, "Declarative intent is clearer.",2);
    }
    
    private void addTrial7Questions() {
        int qId = 85;
        
        // Theory Questions
        addQuestion(qId++, QuestionType.THEORY, "Component Mappings between Programming Paradigms", 7,
            "How do classes in OOP relate to types in functional programming?",
            Arrays.asList(
                "They are unrelated",
                "Both define structures and groups of data",
                "Classes are superior",
                "Functional has no types"
            ), 1, "Both define structured data.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Component Mappings between Programming Paradigms", 7,
            "In procedural programming, what maps to a class method?",
            Arrays.asList(
                "A global variable",
                "A procedure that operates on data",
                "A data type",
                "Nothing equivalent"
            ), 1, "Procedures act like methods without objects.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Component Mappings between Programming Paradigms", 7,
            "What is the functional equivalent of a loop?",
            Arrays.asList(
                "Another loop",
                "An if-statement",
                "Recursion with map/filter/reduce",
                "Classes"
            ), 2, "Recursion and higher-order functions replace loops.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Component Mappings between Programming Paradigms", 7,
            "How do event handlers relate across paradigms?",
            Arrays.asList(
                "Only in OOP",
                "Function callbacks exist in functional programming too",
                "Procedures don't support events",
                "All paradigms handle events identically"
            ), 1, "Callbacks appear in multiple paradigms.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Component Mappings between Programming Paradigms", 7,
            "State management differs between paradigms. What's true?",
            Arrays.asList(
                "All paradigms treat state the same",
                "Imperative changes state often; functional avoids it",
                "Procedural ignores state",
                "OOP has no state"
            ), 1, "State handling is paradigm-specific.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Component Mappings between Programming Paradigms", 7,
            "Can one solve every problem with one paradigm?",
            Arrays.asList(
                "Yes, one paradigm solves everything",
                "No, different paradigms excel at different problems",
                "Paradigms are arbitrary",
                "Mixing paradigms is impossible"
            ), 1, "Different paradigms suit different problems.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Component Mappings between Programming Paradigms", 7,
            "What is the benefit of understanding multiple paradigms?",
            Arrays.asList(
                "Makes programming harder",
                "Expands problem-solving tools and perspectives",
                "Not useful",
                "Confuses developers"
            ), 1, "Multiple paradigms expand thinking.",2);
        
        // Practical Questions
        addQuestion(qId++, QuestionType.PROGRAMMING, "Component Mappings between Programming Paradigms", 7,
            "Translate this imperative code to functional: for(int i=0; i<arr.length; i++) { sum += arr[i]; }",
            Arrays.asList(
                "Use another for loop",
                "arr.stream().reduce(0, (a,b) -> a+b) or similar",
                "Create a class",
                "Add an if-statement"
            ), 1, "Functional uses reduce instead of loop.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Component Mappings between Programming Paradigms", 7,
            "Which paradigm does this function processing exemplify? numbers.filter(n -> n > 5).map(n -> n*2)",
            Arrays.asList(
                "Procedural",
                "Object-Oriented",
                "Functional",
                "Imperative"
            ), 2, "This is functional composition.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Component Mappings between Programming Paradigms", 7,
            "How would OOP represent procedural operations?",
            Arrays.asList(
                "Procedures don't work in OOP",
                "As static methods or instance methods",
                "Can't be done",
                "Only with inheritance"
            ), 1, "Methods represent procedures in OOP.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Component Mappings between Programming Paradigms", 7,
            "What paradigm shift is jQuery/event-driven code?",
            Arrays.asList(
                "Purely procedural",
                "Purely functional",
                "Event-driven (responds to events with callbacks)",
                "Completely OOP"
            ), 2, "jQuery is event-driven with callbacks.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Component Mappings between Programming Paradigms", 7,
            "Comparing SQL vs a loop: SQL is more ___",
            Arrays.asList(
                "imperative",
                "procedural",
                "declarative",
                "complex"
            ), 2, "SQL is declarative.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Component Mappings between Programming Paradigms", 7,
            "Which paradigm would you choose for data transformation?",
            Arrays.asList(
                "Only procedural",
                "Functional (map, filter, reduce)",
                "Only OOP",
                "Event-driven"
            ), 1, "Functional excels at transformations.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Component Mappings between Programming Paradigms", 7,
            "A real-world recommendation system probably uses:",
            Arrays.asList(
                "Single paradigm",
                "Multiple paradigms combined strategically",
                "No programming",
                "Only events"
            ), 1, "Complex systems mix paradigms.",2);
    }
    
    private void addTrial8Questions() {
        int qId = 99;
        
        // Theory Questions
        addQuestion(qId++, QuestionType.THEORY, "Event-Driven Programming", 8,
            "What is event-driven programming?",
            Arrays.asList(
                "Using only events",
                "Program flow determined by user actions or system events",
                "Always using loops",
                "Ignoring user input"
            ), 1, "Events drive program execution.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Event-Driven Programming", 8,
            "What is an event?",
            Arrays.asList(
                "A line of code",
                "Something that happens (click, keypress, timer)",
                "A variable",
                "A function name"
            ), 1, "Events are occurrences.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Event-Driven Programming", 8,
            "What is an event handler?",
            Arrays.asList(
                "A variable",
                "Code that responds to an event",
                "A data structure",
                "A type of loop"
            ), 1, "Handlers respond to events.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Event-Driven Programming", 8,
            "What is a callback?",
            Arrays.asList(
                "Returning from a function",
                "A function passed to execute later when event occurs",
                "A variable assignment",
                "A loop construct"
            ), 1, "Callbacks execute later.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Event-Driven Programming", 8,
            "What is the event loop?",
            Arrays.asList(
                "A traditional for loop",
                "Continuously waits for events and dispatches them",
                "A programming language",
                "A type of data structure"
            ), 1, "Event loops wait for and process events.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Event-Driven Programming", 8,
            "Where is event-driven programming commonly used?",
            Arrays.asList(
                "Data analysis",
                "Graphics rendering",
                "GUI applications and web programming",
                "Mathematical computations"
            ), 2, "Events dominate GUI and web development.",2);
        
        addQuestion(qId++, QuestionType.THEORY, "Event-Driven Programming", 8,
            "What is asynchronous programming?",
            Arrays.asList(
                "Everything runs simultaneously",
                "Code doesn't wait for completion; continues while tasks run",
                "All code runs in order",
                "No programming structure"
            ), 1, "Async programming doesn't block.",2);
        
        // Practical Questions
        addQuestion(qId++, QuestionType.PROGRAMMING, "Event-Driven Programming", 8,
            "Which demonstrates event-driven code?",
            Arrays.asList(
                "for(int i=0; i<10; i++) { }",
                "button.onClick(e -> handleClick(e));",
                "int sum = a + b;",
                "if(x > 0) { }"
            ), 1, "onClick is an event handler.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Event-Driven Programming", 8,
            "What is a typical event in GUI programming?",
            Arrays.asList(
                "A mathematical calculation",
                "Click, keypress, mouse movement",
                "A loop iteration",
                "A variable declaration"
            ), 1, "These are common GUI events.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Event-Driven Programming", 8,
            "Why use callbacks in event-driven programming?",
            Arrays.asList(
                "They slow down programs",
                "To execute code when events occur",
                "They're required everywhere",
                "To replace loops"
            ), 1, "Callbacks execute on events.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Event-Driven Programming", 8,
            "What does JavaScript's addEventListener() do?",
            Arrays.asList(
                "Removes event listeners",
                "Registers a callback for an event",
                "Creates an element",
                "Modifies HTML"
            ), 1, "Adds an event listener with callback.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Event-Driven Programming", 8,
            "In event-driven code, execution is determined by:",
            Arrays.asList(
                "Sequential line order always",
                "User actions and system events",
                "Alphabetical order",
                "Programmer's mood"
            ), 1, "Events determine execution flow.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Event-Driven Programming", 8,
            "Which scenario is NOT suited to event-driven programming?",
            Arrays.asList(
                "Responding to button clicks",
                "Processing real-time data streams",
                "Simple mathematical batch processing",
                "Handling user input"
            ), 2, "Batch processing doesn't need events.",2);
        
        addQuestion(qId++, QuestionType.PROGRAMMING, "Event-Driven Programming", 8,
            "What's a common challenge in event-driven programming?",
            Arrays.asList(
                "Too much structure",
                "Callback nesting and complexity",
                "Not enough power",
                "Too simple"
            ), 1, "Callback chains become complex.",2);
    }
    
    private void addTrial9Questions() {
        int qId = 113;
        String topic = "Component Mappings between Programming Paradigms";
        String[][] theory = {
            {"What does component mapping between paradigms mean?", "Matching equivalent roles across styles", "Renaming variables", "Using only one language", "Removing functions", "0", "It compares how different paradigms express similar responsibilities."},
            {"A procedure is most commonly mapped to which OOP component?", "A method", "An interface color", "A database row", "A mouse event", "0", "Methods encapsulate operations in an object-oriented design."},
            {"Which functional concept commonly replaces a loop that builds a result?", "reduce/fold", "A constructor", "An event queue", "Inheritance", "0", "A reduction combines a sequence into one result."},
            {"What is a useful reason to map components between paradigms?", "To choose an appropriate representation", "To avoid understanding code", "To make every program identical", "To eliminate testing", "0", "Mappings clarify alternatives when designing or translating code."},
            {"In an event-driven GUI, what corresponds to a procedural call made after a click?", "An event handler callback", "A global constant", "A class name", "A comment", "0", "The handler is called when the event occurs."},
            {"Which mapping best describes encapsulation?", "Data plus related operations are kept together", "Every variable is global", "Functions cannot return values", "Instructions are unordered", "0", "Encapsulation groups state and behavior behind a defined interface."},
            {"When mapping declarative code to imperative code, what usually changes?", "The explicit steps become visible", "The desired result disappears", "All data becomes immutable", "Events stop existing", "0", "Imperative code specifies how to achieve the requested result."}
        };
        String[][] practical = {
            {"Which OOP form maps the procedural function move(player, dx) most directly?", "player.move(dx)", "SELECT move FROM player", "onMove = dx", "const move = 0", "0", "An instance method puts the operation with the object it acts on."},
            {"Which functional expression maps a loop that doubles every number?", "numbers.map(n -> n * 2)", "numbers = 2", "new Number(numbers)", "if(numbers) double", "0", "map applies the same transformation to each item."},
            {"Which event-driven form maps 'run save when the button is clicked'?", "saveButton.setOnAction(e -> save())", "save(); save();", "class Click extends Save", "SELECT save", "0", "The callback defers save until the click event."},
            {"Which declarative expression maps filtering active users?", "SELECT * FROM users WHERE active = true", "for each user, inspect active", "user.active = true", "button.onClick(active)", "0", "SQL describes the desired rows rather than the iteration steps."},
            {"A class Account with deposit() and balance best maps which idea?", "Data with related behavior", "A pure event loop", "A SQL query", "A recursive base case", "0", "The account object owns state and its operations."},
            {"Which rewrite preserves a pure calculation from procedural code?", "int total(int a, int b) { return a + b; }", "sum += input;", "globalTotal++;", "print total repeatedly", "0", "The function returns a result without changing outside state."},
            {"A callback receives a value later from a network request. Which paradigm is central?", "Event-driven programming", "Only procedural programming", "Only declarative programming", "Only inheritance", "0", "The callback runs in response to a later event."}
        };
        qId = addRows(qId, QuestionType.THEORY, topic, 9, theory);
        addRows(qId, QuestionType.PROGRAMMING, topic, 9, practical);
    }
    
    private void addTrial10Questions() {
        int qId = 127;
        String[][] theory = {
            {"Introduction to Programming Paradigms", "Why can a program use more than one paradigm?", "Languages and problems can combine useful approaches", "Paradigms cannot coexist", "Only one paradigm has functions", "Mixing removes all bugs", "0", "Many languages support multiple styles, and each can suit a different task."},
            {"Procedural Programming", "What organizes a procedural program into reusable tasks?", "Procedures or functions", "Only comments", "Only classes", "Only events", "0", "Named procedures group related steps."},
            {"Functional Programming", "What makes a function pure?", "It has no observable side effects", "It must use a loop", "It changes global state", "It has no parameters", "0", "Pure functions depend only on inputs and do not alter outside state."},
            {"Object-Oriented Programming", "What is abstraction in OOP?", "Exposing essential behavior while hiding detail", "Duplicating every field", "Removing all methods", "Making every field public", "0", "Abstraction gives users a useful interface without unnecessary detail."},
            {"Imperative vs Declarative Programming", "Declarative programming primarily states:", "What result is wanted", "Every machine instruction", "Which key was pressed", "The exact object layout", "0", "Declarative code focuses on the goal, not its step-by-step execution."},
            {"Event-Driven Programming", "What determines control flow in an event-driven program?", "Events such as clicks, input, and timers", "Only source-file order", "Only inheritance", "Only SQL queries", "0", "Handlers run when relevant events occur."},
            {"Component Mappings between Programming Paradigms", "What should guide a choice of paradigm?", "The problem's needs and trade-offs", "The longest syntax", "The most global variables", "Avoiding all functions", "0", "Different representations are useful for different responsibilities."}
        };
        String[][] practical = {
            {"Introduction to Programming Paradigms", "Which snippet is most declarative?", "SELECT name FROM students", "for each student, inspect name", "student.name = name", "button.setOnAction(...) ", "0", "The query says which data is desired."},
            {"Procedural Programming", "Which code best shows a procedural decomposition?", "readInput(); validate(); printReport();", "new Report().run() only", "values.map(this::report)", "button.onClick(report)", "0", "A sequence of named procedures makes the steps explicit."},
            {"Functional Programming", "Which expression avoids changing the source list?", "var doubled = numbers.stream().map(n -> n * 2).toList();", "numbers.replaceAll(n -> n * 2);", "for each number, overwrite it", "globalNumbers.add(2);", "0", "It creates a transformed result rather than mutating the original list."},
            {"Object-Oriented Programming", "Which design best encapsulates a player's score?", "Player with addScore() and a private score field", "A public global score changed anywhere", "A SQL table with no access rules", "A loop with no data", "0", "The object controls changes to its internal state."},
            {"Imperative vs Declarative Programming", "Which is imperative?", "for (User u : users) { if (u.active) result.add(u); }", "SELECT * FROM users WHERE active", "A schema describing users", "A CSS selector", "0", "The loop explicitly gives the filtering steps."},
            {"Event-Driven Programming", "Which code reacts to a key press?", "scene.setOnKeyPressed(e -> movePlayer(e));", "movePlayer(); movePlayer();", "class Key extends Player", "SELECT key FROM input", "0", "The handler is invoked in response to the key event."},
            {"Component Mappings between Programming Paradigms", "A loop that computes a total maps most directly to which functional operation?", "reduce", "inherit", "listen", "instantiate", "0", "reduce combines values into one accumulated result."}
        };
        qId = addRows(qId, QuestionType.THEORY, null, 10, theory);
        addRows(qId, QuestionType.PROGRAMMING, null, 10, practical);
    }

    /** Adds compact question data while keeping QuestionBank as the replaceable data source. */
    private int addRows(int firstId, QuestionType type, String fixedTopic, int trialId, String[][] rows) {
        int id = firstId;
        for (String[] row : rows) {
            String topic = fixedTopic == null ? row[0] : fixedTopic;
            int offset = fixedTopic == null ? 1 : 0;
            addQuestion(id++, type, topic, trialId, row[offset],
                Arrays.asList(row[offset + 1], row[offset + 2], row[offset + 3], row[offset + 4]),
                Integer.parseInt(row[offset + 5]), row[offset + 6], 3);
        }
        return id;
    }
    
    private void addQuestion(int id, QuestionType type, String topic, int trialId,
                            String questionText, List<String> choices,
                            int correctIndex, String explanation, int difficulty) {
        Question q = new Question(id, type, topic, questionText, choices, correctIndex, explanation, difficulty, trialId);
        allQuestions.add(q);
    }
    
    // Getters
    public List<Question> getAllQuestions() {
        return new ArrayList<>(allQuestions);
    }
    
    public List<Question> getQuestionsForTrial(int trialId) {
        if (trialId == 11) {
            List<Question> finalQuestions = new ArrayList<>();
            for (int sourceTrial = 1; sourceTrial <= 7; sourceTrial++) {
                List<Question> source = getQuestionsForTrial(sourceTrial);
                finalQuestions.add(source.stream().filter(q -> q.getType() == QuestionType.THEORY).findFirst().orElseThrow());
                finalQuestions.add(source.stream().filter(q -> q.getType() == QuestionType.PROGRAMMING).findFirst().orElseThrow());
            }
            return finalQuestions;
        }
        return allQuestions.stream()
            .filter(q -> q.getTrialId() == trialId)
            .collect(Collectors.toList());
    }
    
    public List<Question> getTheoryQuestions() {
        return allQuestions.stream()
            .filter(q -> q.getType() == QuestionType.THEORY)
            .collect(Collectors.toList());
    }
    
    public List<Question> getProgrammingQuestions() {
        return allQuestions.stream()
            .filter(q -> q.getType() == QuestionType.PROGRAMMING)
            .collect(Collectors.toList());
    }
    
    public int getTotalQuestions() {
        return allQuestions.size();
    }

    /** Returns every structural issue so development builds fail loudly instead of silently. */
    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (allQuestions.size() != 140) {
            errors.add("Expected 140 questions, found " + allQuestions.size());
        }
        if (getTheoryQuestions().size() != 70 || getProgrammingQuestions().size() != 70) {
            errors.add("Expected 70 theory and 70 programming questions; found "
                + getTheoryQuestions().size() + " theory and " + getProgrammingQuestions().size() + " programming");
        }
        for (int trialId = 1; trialId <= 10; trialId++) {
            List<Question> trialQuestions = getQuestionsForTrial(trialId);
            long theory = trialQuestions.stream().filter(q -> q.getType() == QuestionType.THEORY).count();
            long programming = trialQuestions.stream().filter(q -> q.getType() == QuestionType.PROGRAMMING).count();
            if (trialQuestions.size() != 14 || theory != 7 || programming != 7) {
                errors.add("Trial " + trialId + " must contain 7 theory and 7 programming questions");
            }
        }
        for (Question question : allQuestions) {
            if (question.getTrialId() < 1 || question.getTrialId() > 10) {
                errors.add("Question " + question.getId() + " has invalid trial ID " + question.getTrialId());
            }
            if (!VALID_TOPICS.contains(question.getTopic())) {
                errors.add("Question " + question.getId() + " has invalid topic: " + question.getTopic());
            }
            if (question.getChoices() == null || question.getChoices().size() != 4) {
                errors.add("Question " + question.getId() + " must have exactly four choices");
            }
            if (question.getCorrectAnswerIndex() < 0 || question.getCorrectAnswerIndex() >= 4) {
                errors.add("Question " + question.getId() + " has an invalid correct-answer index");
            }
        }
        return errors;
    }
}
