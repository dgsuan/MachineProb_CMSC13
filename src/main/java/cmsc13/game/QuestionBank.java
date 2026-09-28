package cmsc13.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Manages the entire question bank for The Paradigm Trials.
 * Note: Update your Question.java class constructor to accept 
 * two additional String parameters: progType and progLanguage.
 */
public class QuestionBank {
    private static final Set<String> VALID_TOPICS = new HashSet<>(Arrays.asList(
        "Introduction to Programming Paradigms",
        "Procedural Programming",
        "Functional Programming",
        "Object-Oriented Programming",
        "Imperative vs Declarative Programming",
        "Event-Driven Programming",
        "Component Mappings between Programming Paradigms",
        "Comprehensive Programming Mastery"
    ));
    
    private List<Question> allQuestions;
    
    public QuestionBank() {
        this.allQuestions = new ArrayList<>();
        initializeQuestions();
    }
    
    private void initializeQuestions() {
        int qId = 1;
        // Trial 1: 10 Theory, 4 Prog (Total 14)
        qId = addTrial1Questions(qId);
        // Trial 2: 10 Theory, 5 Prog (Total 15)
        qId = addTrial2Questions(qId);
        // Trial 3: 10 Theory, 6 Prog (Total 16)
        qId = addTrial3Questions(qId);
        // Trial 4: 10 Theory, 7 Prog (Total 17)
        qId = addTrial4Questions(qId);
        // Trial 5: 10 Theory, 8 Prog (Total 18)
        qId = addTrial5Questions(qId);
        // Trial 6: 10 Theory, 9 Prog (Total 19)
        qId = addTrial6Questions(qId);
        // Trial 7: 10 Theory, 11 Prog (Total 21)
        qId = addTrial7Questions(qId);
        // Trial 8: 0 Theory, 25 Prog (Total 25)
        qId = addTrial8Questions(qId);
    }
    
    private int addTrial1Questions(int qId) {
        String topic = "Introduction to Programming Paradigms";
        String[][] theory = {
            {"What is a programming paradigm?", "A debugging tool", "A style and approach to programming", "A specific language", "An operating system", "1", "A paradigm is a fundamental style of programming."},
            {"Which of the following is NOT a standard programming paradigm?", "Procedural", "Functional", "Alphabetical", "Object-Oriented", "2", "Alphabetical is not a programming paradigm."},
            {"What does imperative programming focus on?", "What the result should be", "How to achieve the result step-by-step", "Mathematical functions", "Object interactions", "1", "Imperative focuses on the exact steps."},
            {"What does declarative programming focus on?", "How to execute loops", "Hardware memory", "What the desired outcome is", "Mutable state", "2", "Declarative describes the outcome, not the steps."},
            {"Which paradigm treats computation as the evaluation of mathematical functions?", "Functional", "Event-Driven", "Procedural", "Object-Oriented", "0", "Functional programming is based on math functions."},
            {"Which paradigm relies on triggers like user clicks?", "Declarative", "Functional", "Event-Driven", "Procedural", "2", "Event-driven reacts to events."},
            {"What is the main benefit of multi-paradigm languages?", "They are immune to bugs", "They use the right tool for different problems", "They run faster", "They don't need compilers", "1", "Different paradigms solve different problems better."},
            {"Which of the following is an imperative paradigm?", "SQL", "Functional", "Procedural", "HTML", "2", "Procedural is a type of imperative programming."},
            {"What encapsulates data and behavior together?", "A pure function", "A procedure", "An object", "A declarative query", "2", "Objects group state and behavior."},
            {"Why is state change a concern in programming paradigms?", "It makes code run too fast", "It can cause side effects and bugs", "It requires more RAM", "It is never allowed", "1", "Managing mutable state is a key difference between paradigms."}
        };
        // 4 Prog: 2 C, 2 Java | 2 A, 1 B, 1 C
        String[][] prog = {
            {"Given:\nint a = 5;\nint b = 3;\nprintf(\"%d\", a + b * 2);", "11", "16", "10", "8", "0", "Standard order of operations.", "Type A", "C"},
            {"Given:\nSystem.out.println(10 / 2 + 5);", "15", "10", "5", "Error", "1", "Division before addition.", "Type A", "Java"},
            {"Output: 20", "int x = 5;\nprintf(\"%d\", x + 10);", "int x = 10;\nprintf(\"%d\", x * 2);", "int x = 20;\nprintf(\"%d\", x / 2);", "int x = 2;\nprintf(\"%d\", x + 2);", "1", "10 * 2 = 20.", "Type B", "C"},
            {"Given:\nint x = 5;\n____;\nSystem.out.println(x); \n// Expected: 6", "x + 1;", "x++;", "x = 5;", "x--;", "1", "x++ increments the value by 1.", "Type C", "Java"}
        };
        qId = addRows(qId, QuestionType.THEORY, topic, 1, theory);
        return addRows(qId, QuestionType.PROGRAMMING, topic, 1, prog);
    }

    private int addTrial2Questions(int qId) {
        String topic = "Procedural Programming";
        String[][] theory = {
            {"What is the fundamental building block of procedural programming?", "Classes", "Procedures/Functions", "Events", "Queries", "1", "Procedural logic is grouped into procedures."},
            {"Which of these relies heavily on mutable state?", "Functional", "Procedural", "Declarative", "Logic", "1", "Procedural uses mutable variables."},
            {"What is 'scope' in procedural programming?", "The speed of the program", "Where a variable is accessible", "The size of the file", "The number of functions", "1", "Scope defines variable visibility."},
            {"Which language is classically procedural?", "C", "Haskell", "SQL", "Prolog", "0", "C is the classic procedural language."},
            {"What does 'pass by value' mean?", "Passing the memory address", "Passing a copy of the data", "Passing a function", "Passing a class", "1", "Pass by value copies the variable."},
            {"What is structured programming?", "Programming with classes", "Avoiding goto by using loops and conditionals", "Using only global variables", "Using CSS", "1", "Structured programming relies on blocks like if/while."},
            {"What is a major risk of global variables?", "They are too slow", "They cannot hold integers", "Any procedure can modify them, causing bugs", "They require too much memory", "2", "Global state is hard to track."},
            {"How do procedures communicate primarily?", "Through global state only", "Through parameters and return values", "Through inheritance", "Through HTML", "1", "Parameters pass data in, return values pass it out."},
            {"What is a side effect in procedural code?", "A comment", "A syntax error", "Modifying state outside the local environment", "A loop", "2", "Functions modifying external state cause side effects."},
            {"Which control structure is NOT typical of procedural code?", "If-else", "While loop", "Switch", "Pattern matching monads", "3", "Monads are functional concepts."}
        };
        // 5 Prog: 2 C, 2 Java, 1 Py | 2 A, 2 B, 1 C
        String[][] prog = {
            {"Given:\nint i = 0;\nwhile (i < 3) {\n    printf(\"%d\", i);\n    i++;\n}", "0123", "012", "123", "12", "1", "Loop stops when i is 3.", "Type A", "C"},
            {"Given:\npublic void run() {\n    System.out.print(\"A\");\n}", "Error", "Nothing", "A", "run", "2", "Prints A.", "Type A", "Java"},
            {"Output: Hello", "printf(\"Hello\");", "printf(Hello);", "print Hello;", "echo Hello;", "0", "Standard C print syntax.", "Type B", "C"},
            {"Output: 5.0", "System.out.print(5);", "System.out.print(5.0);", "System.out.print(\"5\");", "All of the above output visually 5.0", "1", "Prints float/double literal.", "Type B", "Java"},
            {"Given:\nx = 10\n____\nprint(x)  \n# Expected: 20", "x + 10", "x * 2", "x = x * 2", "x = 20 + x", "2", "Reassigns x to 20.", "Type C", "Python"}
        };
        qId = addRows(qId, QuestionType.THEORY, topic, 2, theory);
        return addRows(qId, QuestionType.PROGRAMMING, topic, 2, prog);
    }

    private int addTrial3Questions(int qId) {
        String topic = "Functional Programming";
        String[][] theory = {
            {"What is a pure function?", "A function with no variables", "A function that always returns the same output for same input with no side effects", "A function that runs fast", "A void function", "1", "Deterministic and side-effect free."},
            {"What is immutability?", "Variables can only be integers", "Once created, data cannot be modified", "Code cannot be refactored", "Functions cannot be deleted", "1", "Immutability means unchanging state."},
            {"What is a higher-order function?", "A very complex function", "A function that takes or returns another function", "A function at the top of the file", "A recursive function", "1", "Functions treating functions as data."},
            {"How are loops typically replaced in pure functional languages?", "With GOTO", "With recursion", "With classes", "With events", "1", "Recursion replaces iterative loops."},
            {"What does the 'map' function generally do?", "Finds a location", "Filters out bad data", "Applies a function to every item in a list", "Reduces a list to a single value", "2", "Map transforms every element."},
            {"What does 'reduce' or 'fold' do?", "Deletes elements", "Combines all elements into a single accumulated value", "Sorts the list", "Copies the list", "1", "Accumulates elements."},
            {"What does 'filter' do?", "Removes items that don't match a condition", "Changes the data type of items", "Sorts items alphabetically", "Adds new items", "0", "Keeps only matching items."},
            {"First-class functions mean:", "Functions get priority CPU time", "Functions can be assigned to variables and passed around", "Functions are error-free", "Functions are public", "1", "Functions act like regular values."},
            {"Which is a benefit of functional programming?", "Easier parallel processing due to immutability", "Less memory usage", "Faster single-thread execution", "Easier UI design", "0", "No shared mutable state makes threading safe."},
            {"Which language heavily influenced functional concepts?", "C", "Assembly", "LISP", "HTML", "2", "LISP is an early functional language."}
        };
        // 6 Prog: 2 C, 2 Java, 1 Py, 1 JS | 2 A, 2 B, 2 C
        String[][] prog = {
            {"Given:\nint f(int x) {\n    return x * x;\n}\n\nprintf(\"%d\", f(3));", "6", "9", "3", "Error", "1", "3 squared is 9.", "Type A", "C"},
            {"Given:\ndef f(x):\n    return x + 1\n\n____\n# Expected: 6", "print(f(5))", "print(f(6))", "print(5)", "print(x + 1)", "0", "Calls f(5) to get 6.", "Type C", "Python"},
            {"Given:\nSystem.out.print(Math.max(2, 5));", "2", "5", "7", "Error", "1", "Returns the maximum value.", "Type A", "Java"},
            {"Output: 10", "System.out.print(5 + 4);", "System.out.print(2 * 5);", "System.out.print(10 / 2);", "System.out.print(10 % 2);", "1", "2 * 5 equals 10.", "Type B", "Java"},
            {"Output: 120 (Factorial of 5)", "def fact(n):\n    return n", "def fact(n):\n    return 120", "def fact(n):\n    return 1 if n == 0 else n * fact(n - 1)\n\nprint(fact(5))", "print(5 * 4)", "2", "Standard recursive factorial.", "Type B", "Python"},
            {"Given:\nconst add = (a, b) => a + b;\nconsole.log(____); \n// Expected 7", "add(3, 4)", "add(7)", "add(3)(4)", "add[3, 4]", "0", "Standard arrow function call.", "Type C", "JavaScript"}
        };
        qId = addRows(qId, QuestionType.THEORY, topic, 3, theory);
        return addRows(qId, QuestionType.PROGRAMMING, topic, 3, prog);
    }

    private int addTrial4Questions(int qId) {
        String topic = "Object-Oriented Programming";
        String[][] theory = {
            {"What is a Class?", "A variable", "A blueprint for creating objects", "A database table", "A function", "1", "Classes define structure and behavior."},
            {"What is Encapsulation?", "Hiding internal state and requiring all interaction to be performed through an object's methods", "Inheriting from a parent", "Multiple methods with same name", "Writing long functions", "0", "Bundles data and restricts access."},
            {"What is Inheritance?", "When a class copies code from a file", "When a subclass derives properties and behaviors from a superclass", "When objects share memory", "When a method calls itself", "1", "Allows class hierarchies."},
            {"What is Polymorphism?", "Objects being immutable", "A single interface representing different underlying forms", "Hiding data", "Multiple inheritance", "1", "Means 'many forms'."},
            {"What does Abstraction do?", "Hides complex implementation details, showing only essential features", "Makes code run slower", "Prevents class creation", "Requires all variables to be public", "0", "Reduces complexity."},
            {"What is a Constructor?", "A method to delete an object", "A special method used to initialize an object", "A type of variable", "An interface", "1", "Sets up initial state."},
            {"What is an instance?", "A function call", "A specific realization of any object created from a class", "A class definition", "A global variable", "1", "An object built from a class."},
            {"What does the 'this' or 'self' keyword refer to?", "The parent class", "The current instance of the object", "A global scope", "A static method", "1", "Refers to the current object context."},
            {"What is method overriding?", "Having two methods with different parameters", "A subclass providing a specific implementation of a parent's method", "Deleting a method", "Calling a method repeatedly", "1", "Replaces inherited behavior."},
            {"Which is an access modifier?", "Static", "Void", "Private", "Class", "2", "Private controls visibility."}
        };
        // 7 Prog: 2 C, 2 Java, 2 Py, 1 JS | 2 A, 2 B, 3 C
        String[][] prog = {
            {"Given:\nstruct Point {\n    int x;\n    int y;\n};\n\nstruct Point p = {1, 2};\nprintf(\"%d\", p.y);", "1", "2", "3", "Error", "1", "Accesses the y member.", "Type A", "C"},
            {"Given:\nstruct A {\n    int val;\n};\n\nstruct A obj;\n____; \n// Expected val=5", "obj.val = 5;", "obj->val = 5;", "A.val = 5;", "val = 5;", "0", "Dot operator sets member.", "Type C", "C"},
            {"Given:\nclass A {\n    int x = 1;\n}\n\nA obj = new A();\nSystem.out.print(obj.x);", "0", "1", "Null", "Error", "1", "Accesses object field.", "Type A", "Java"},
            {"Given:\nclass Dog extends Animal {\n    ____\n} \n// Expected to override speak()", "public void speak() { }", "override speak()", "function speak()", "Dog speak()", "0", "Standard method signature overrides parent.", "Type C", "Java"},
            {"Output: Meow", "class Cat:\n    def sound():\n        return 'Meow'\n\nprint(Cat().sound())", "class Cat:\n    def sound(self):\n        return 'Meow'\n\nprint(Cat().sound())", "print('Bark')", "class Cat:\n    sound = 'Meow'", "1", "Self is required in Python methods.", "Type B", "Python"},
            {"Output: True", "print(isinstance(\"hi\", str))", "print(type(\"hi\") == int)", "print(\"hi\".isString())", "print(typeof(\"hi\"))", "0", "Checks if string is instance of str.", "Type B", "Python"},
            {"Given:\nclass Car {\n    constructor(make) {\n        ____\n    }\n} \n// Expected to set make", "this.make = make;", "Car.make = make;", "make = make;", "self.make = make;", "0", "JS uses 'this' to set properties.", "Type C", "JavaScript"}
        };
        qId = addRows(qId, QuestionType.THEORY, topic, 4, theory);
        return addRows(qId, QuestionType.PROGRAMMING, topic, 4, prog);
    }

    private int addTrial5Questions(int qId) {
        String topic = "Imperative vs Declarative Programming";
        String[][] theory = {
            {"Imperative programming is analogous to:", "A restaurant order", "A step-by-step recipe", "A mathematical equation", "A spreadsheet", "1", "Imperative gives explicit instructions."},
            {"Declarative programming is analogous to:", "A step-by-step recipe", "Ordering a meal at a restaurant", "Building an engine", "Driving a manual car", "1", "You ask for what you want, not how to make it."},
            {"Which language is primarily declarative?", "C", "Java", "SQL", "Python", "2", "SQL queries what data you want."},
            {"HTML is considered declarative because:", "It uses tags", "It describes structure without control flow steps", "It runs in a browser", "It is old", "1", "HTML dictates structure, not rendering steps."},
            {"State changes are prominent in:", "Declarative", "Functional", "Imperative", "Logic", "2", "Imperative mutates state constantly."},
            {"Regex (Regular Expressions) represents which paradigm?", "Declarative", "Imperative", "Object-Oriented", "Procedural", "0", "Regex describes a pattern to match."},
            {"Which code is more readable for complex filtering?", "Nested for-loops", "Declarative filter/map chains", "While loops with break statements", "Goto statements", "1", "Declarative shows intent clearly."},
            {"CSS describes:", "How to compute pixels", "What the visual styling should be", "How to download fonts", "When to execute scripts", "1", "CSS is declarative styling."},
            {"Under the hood, declarative languages are executed by:", "Magic", "Declarative hardware", "Imperative implementations", "Object-oriented databases", "2", "Compilers/interpreters execute them imperatively."},
            {"Which paradigm focuses on the 'How'?", "Logic", "Functional", "Declarative", "Imperative", "3", "Imperative focuses on the steps (how)."}
        };
        // 8 Prog: 3 C, 2 Java, 2 Py, 1 JS | 3 A, 3 B, 2 C
        String[][] prog = {
            {"Given:\nint a = 0;\nfor (int i = 0; i < 3; i++) {\n    a += i;\n}\nprintf(\"%d\", a);", "0", "3", "6", "2", "1", "0 + 1 + 2 = 3.", "Type A", "C"},
            {"Output: 10", "int x = 5;\nx *= 2;\nprintf(\"%d\", x);", "printf(\"10\");", "int x = 10;\nprintf(\"%d\", x);", "All of the above", "3", "All snippets print 10.", "Type B", "C"},
            {"Given:\nint x = 1;\n____; \n// Expected: x is now 5", "x += 4;", "x = 5;", "Both A and B", "x == 5;", "2", "Both assignment and compound assignment work.", "Type C", "C"},
            {"Given:\nList<String> l = Arrays.asList(\"A\", \"B\");\nSystem.out.print(l.size());", "1", "2", "3", "Error", "1", "List has 2 elements.", "Type A", "Java"},
            {"Output: HELLO", "System.out.print(\"hello\".toUpperCase());", "System.out.print(\"HELLO\");", "Both A and B", "System.out.print(toUpper(\"hello\"));", "2", "Both produce HELLO.", "Type B", "Java"},
            {"Given:\nprint([x * 2 for x in [1, 2, 3]])", "[1, 2, 3]", "[2, 4, 6]", "Error", "[x*2]", "1", "List comprehension is declarative.", "Type A", "Python"},
            {"Given:\nevens = list(filter(lambda x: ____, [1, 2, 3, 4])) \n# Expected: [2, 4]", "x % 2 == 0", "x == 2", "x / 2", "x > 0", "0", "Filters for even numbers.", "Type C", "Python"},
            {"Output: 3", "console.log([1, 2, 3].length);", "console.log(3);", "Both A and B", "console.log(length([1, 2, 3]));", "2", "Both output 3.", "Type B", "JavaScript"}
        };
        qId = addRows(qId, QuestionType.THEORY, topic, 5, theory);
        return addRows(qId, QuestionType.PROGRAMMING, topic, 5, prog);
    }

    private int addTrial6Questions(int qId) {
        String topic = "Event-Driven Programming";
        String[][] theory = {
            {"What triggers execution in event-driven programming?", "A while loop", "Sequential lines of code", "Events like user actions or sensor outputs", "A compiler", "2", "Events drive the flow."},
            {"What is a callback function?", "A function that calls itself", "A function passed as an argument to be executed when an event occurs", "A function that returns an error", "A procedural sequence", "1", "Callbacks handle events."},
            {"What is an event listener?", "Hardware that listens to audio", "An object/function that waits for an event to occur", "A database table", "A CSS style", "1", "Listeners wait for triggers."},
            {"What is the 'Event Loop'?", "A bug causing infinite loops", "A programming mechanism that waits for and dispatches events", "A loop that creates events", "A testing framework", "1", "The core of async environments like Node.js."},
            {"Event-driven architecture is most common in:", "Batch processing", "Scientific computing", "GUI applications", "Compilers", "2", "GUIs rely on user interactions."},
            {"What is synchronous execution?", "Code running at the same time", "Code executing one line at a time, blocking until finished", "Code reacting to events", "Code in multiple files", "1", "Synchronous code blocks."},
            {"What is asynchronous execution?", "Code that executes without blocking the main thread", "Code that fails randomly", "Code that runs on a timer", "Code that cannot be debugged", "0", "Async code allows other tasks to run."},
            {"What is a 'Promise' (e.g., in JS)?", "A comment in code", "An object representing eventual completion or failure of an async operation", "A variable type", "A strict mode feature", "1", "Promises handle async results."},
            {"How are events decoupled from their triggers?", "Through inheritance", "By using event emitters and pub/sub patterns", "By removing variables", "Through recursion", "1", "Pub/sub separates concern."},
            {"What is 'Callback Hell'?", "A compiler error", "Deeply nested callbacks making code hard to read", "When no events fire", "A syntax error", "1", "Nesting makes async code complex."}
        };
        // 9 Prog: 3 C, 3 Java, 2 Py, 1 JS | 3 A, 3 B, 3 C
        String[][] prog = {
            {"Given:\nvoid cb() {\n    printf(\"Hi\");\n}\n\nvoid run(void (*f)()) {\n    f();\n}\n\nint main() {\n    run(cb);\n}", "Error", "cb", "Hi", "Nothing", "2", "Function pointer acts as a callback.", "Type A", "C"},
            {"Output: 1", "printf(\"%d\", 1);", "int x = 1;\nprintf(\"%d\", x);", "Both A and B", "printf(1);", "2", "Both print 1 properly.", "Type B", "C"},
            {"Given:\nint arr[] = {1, 2};\nprintf(\"%d\", ____); \n// Expected: 2", "arr[1]", "arr[2]", "arr[0]", "arr", "0", "Array index 1 holds 2.", "Type C", "C"},
            {"Given:\nJButton b = new JButton();\nb.addActionListener(e -> System.out.print(\"C\")); \n// If clicked?", "Error", "C", "Nothing", "b", "1", "Lambda acts as event handler.", "Type A", "Java"},
            {"Output: Event", "System.out.print(\"Event\");", "String s = \"Event\";\nSystem.out.print(s);", "Both A and B", "print(\"Event\");", "2", "Both print Event.", "Type B", "Java"},
            {"Given:\nThread t = new Thread(() -> ____);\nt.start(); \n// Expected to print Run", "System.out.print(\"Run\")", "print(\"Run\")", "return \"Run\"", "\"Run\"", "0", "Runnable body executes.", "Type C", "Java"},
            {"Given:\ndef handler():\n    print(\"Click\")\n\nbutton_click = handler\nbutton_click()", "Click", "handler", "Error", "Nothing", "0", "Function assigned and called.", "Type A", "Python"},
            {"Output: Async", "print(\"Async\")", "import asyncio\nprint(\"Async\")", "Both A and B", "console.log(\"Async\")", "2", "Both print Async.", "Type B", "Python"},
            {"Given:\ndocument.getElementById('btn').addEventListener('click', ____); \n// Expected to log 'Hi'", "() => console.log('Hi')", "console.log('Hi')", "print('Hi')", "function() {}", "0", "Arrow function acts as callback.", "Type C", "JavaScript"}
        };
        qId = addRows(qId, QuestionType.THEORY, topic, 6, theory);
        return addRows(qId, QuestionType.PROGRAMMING, topic, 6, prog);
    }

    private int addTrial7Questions(int qId) {
        String topic = "Component Mappings between Programming Paradigms";
        String[][] theory = {
            {"In OOP, behavior is represented by methods. What is the equivalent in functional programming?", "Global variables", "Pure functions", "Classes", "Interfaces", "1", "Pure functions define behavior without object state."},
            {"What maps to a 'loop' in pure functional programming?", "If-statements", "Classes", "Recursion and higher-order functions", "Switch cases", "2", "Functional uses map/reduce or recursion instead of loops."},
            {"An object (state + behavior) can be simulated in functional programming using:", "Closures", "Integers", "While loops", "Strings", "0", "Closures capture state and expose functions."},
            {"A procedural 'struct' or 'record' maps to what in OOP?", "A method", "An event", "A class with attributes (and possibly methods)", "A generic type", "2", "Structs hold data; classes hold data + behavior."},
            {"What is the declarative equivalent of imperative array iteration?", "SQL queries or Array.map/filter", "Nested loops", "Pointers", "Global arrays", "0", "Declarative uses higher-order collection methods."},
            {"In event-driven code, a callback maps to what OOP concept?", "A primitive integer", "An implementation of a Listener/Observer interface", "A static property", "A constructor", "1", "Listeners handle callbacks in OOP."},
            {"How does functional immutability map to OOP?", "By making all fields public", "By using final/readonly properties and returning new objects", "By using void methods", "By deleting classes", "1", "Immutable objects represent functional state."},
            {"Polymorphism in OOP (interfaces) maps to what in functional programming?", "Variables", "Loops", "Higher-order functions or Type Classes", "Null values", "2", "Type classes/function passing provide polymorphism."},
            {"Why do we map components between paradigms?", "To prove one is better", "To understand how to translate solutions across different architectural styles", "To confuse developers", "To slow down execution", "1", "It helps translate concepts."},
            {"A declarative SQL 'WHERE' clause maps to which functional concept?", "map", "reduce", "filter", "forEach", "2", "Filter removes items that do not match, like WHERE."}
        };
        // 11 Prog: 4 C, 3 Java, 2 Py, 2 JS | 4 A, 4 B, 3 C
        String[][] prog = {
            {"Given:\nint x = 10;\nint *p = &x;\nprintf(\"%d\", *p);", "Address of x", "10", "Pointer error", "0", "1", "Dereferencing gets the value.", "Type A", "C"},
            {"Given:\nchar c = 'A';\nprintf(\"%c\", c + 1);", "A", "B", "65", "66", "1", "Character arithmetic advances to B.", "Type A", "C"},
            {"Output: 5", "printf(\"%d\", 5);", "int x = 5;\nprintf(\"%d\", x);", "Both A and B", "printf(5);", "2", "Both print 5 safely.", "Type B", "C"},
            {"Given:\nint a = 2;\n____;\nprintf(\"%d\", a); \n// Expected: 4", "a = a * 2", "a += 2", "Both A and B", "a = 4", "2", "Both double the value.", "Type C", "C"},
            {"Given:\nString s = \"Hi\";\nSystem.out.print(s.length());", "1", "2", "3", "Error", "1", "Length of 'Hi' is 2.", "Type A", "Java"},
            {"Output: true", "System.out.print(1 == 1);", "System.out.print(\"a\".equals(\"a\"));", "Both A and B", "System.out.print(true);", "2", "Both evaluate to true.", "Type B", "Java"},
            {"Given:\nint[] a = {1, 2};\n____; // Expected a[0] to be 5", "a[0] = 5;", "a[1] = 5;", "a.set(0, 5);", "a[0] == 5;", "0", "Array assignment.", "Type C", "Java"},
            {"Given:\nprint(len([1, 2, 3]))", "1", "2", "3", "Error", "2", "Length is 3.", "Type A", "Python"},
            {"Output: dict", "print(type({}))", "print(type(dict()))", "Both A and B", "print({})", "2", "Both are dictionary types.", "Type B", "Python"},
            {"Output: NaN", "console.log(parseInt('a'));", "console.log(0/0);", "Both A and B", "console.log(NaN);", "2", "Both produce Not-A-Number.", "Type B", "JavaScript"},
            {"Given:\nlet x = 5;\n____;\nconsole.log(x); \n// Expected 10", "x *= 2;", "x = 10;", "Both A and B", "x == 10;", "2", "Both assignments result in 10.", "Type C", "JavaScript"}
        };
        qId = addRows(qId, QuestionType.THEORY, topic, 7, theory);
        return addRows(qId, QuestionType.PROGRAMMING, topic, 7, prog);
    }

    private int addTrial8Questions(int qId) {
        String topic = "Comprehensive Programming Mastery";
        // 25 Prog (0 Theory): 7 C, 9 Java, 5 Py, 4 JS | 7 A, 8 B, 10 C
        String[][] prog = {
            // C (7) - 2A, 2B, 3C
            {"Given:\nprintf(\"%d\", 10 % 3);", "0", "1", "3", "10", "1", "Modulo arithmetic.", "Type A", "C"},
            {"Given:\nint a = 5, b = 2;\nprintf(\"%f\", (float)a / b);", "2.0", "2.5", "2", "Error", "1", "Casting ensures float division.", "Type A", "C"},
            {"Output: A", "printf(\"%c\", 65);", "printf(\"A\");", "Both", "None", "2", "ASCII 65 is A.", "Type B", "C"},
            {"Output: 15", "printf(\"%d\", 10 + 5);", "int x = 15;\nprintf(\"%d\", x);", "Both", "None", "2", "Both output 15.", "Type B", "C"},
            {"Given:\nchar str[] = \"Hi\";\n____; // Expected: change 'i' to 'a'", "str[1] = 'a';", "str[0] = 'a';", "str = \"Ha\";", "str[2] = 'a';", "0", "Index 1 modifies the second character.", "Type C", "C"},
            {"Given:\nint x = 10;\nif (____) {\n    printf(\"Yes\");\n} // Expected: Yes", "x == 10", "x > 5", "Both", "x < 5", "2", "Both conditions are true.", "Type C", "C"},
            {"Given:\nint i = 0;\ndo {\n    i++;\n} while (____); // Expected to loop until i becomes 2", "i < 2", "i < 1", "i == 0", "i > 5", "0", "Loop continues while i<2.", "Type C", "C"},
            
            // Java (9) - 3A, 3B, 3C
            {"Given:\nSystem.out.print(\"a\" + \"b\");", "a b", "ab", "Error", "a+b", "1", "String concatenation.", "Type A", "Java"},
            {"Given:\nSystem.out.print(Math.abs(-5));", "-5", "5", "0", "Error", "1", "Absolute value.", "Type A", "Java"},
            {"Given:\nString s = null;\nSystem.out.print(s);", "null", "Error", "Nothing", "s", "0", "Prints the string 'null'.", "Type A", "Java"},
            {"Output: 100", "System.out.print(10 * 10);", "System.out.print(\"100\");", "Both", "None", "2", "Both output 100.", "Type B", "Java"},
            {"Output: false", "System.out.print(1 == 2);", "System.out.print(!true);", "Both", "None", "2", "Both evaluate to false.", "Type B", "Java"},
            {"Output: 0", "System.out.print(0);", "System.out.print(1 % 1);", "Both", "None", "2", "Both result in 0.", "Type B", "Java"},
            {"Given:\nint x = 5;\n____;\nSystem.out.print(x); \n// Expected 4", "x--;", "x -= 1;", "Both", "x == 4;", "2", "Both decrement x by 1.", "Type C", "Java"},
            {"Given:\nString s = \"Hello\";\nchar c = ____; \n// Expected c to be 'e'", "s.charAt(1);", "s.charAt(0);", "s[1];", "s.indexOf('e');", "0", "charAt(1) gets 'e'.", "Type C", "Java"},
            {"Given:\nArrayList<Integer> list = new ArrayList<>();\n____; \n// Expected list to have 1 item", "list.add(1);", "list.push(1);", "list.insert(1);", "list[0] = 1;", "0", "Java ArrayList uses add().", "Type C", "Java"},
            
            // Python (5) - 1A, 2B, 2C
            {"Given:\nprint(\"ab\" * 3)", "ababab", "ab3", "Error", "ab ab ab", "0", "String multiplication repeats.", "Type A", "Python"},
            {"Output: 5", "print(len(\"hello\"))", "print(2 + 3)", "Both", "None", "2", "Both output 5.", "Type B", "Python"},
            {"Output: [1, 2]", "print([1, 2])", "print(list((1, 2)))", "Both", "None", "2", "Both create lists.", "Type B", "Python"},
            {"Given:\nx = [1, 2, 3]\n____\n\n# Expected x is [1, 2, 3, 4]", "x.append(4)", "x.add(4)", "x.push(4)", "x + 4", "0", "Python lists use append().", "Type C", "Python"},
            {"Given:\nd = {'a': 1}\n____\n\n# Expected d['a'] is 2", "d['a'] = 2", "d.a = 2", "d(a) = 2", "d['a'] == 2", "0", "Dictionary assignment.", "Type C", "Python"},
            
            // JS (4) - 1A, 1B, 2C
            {"Given:\nconsole.log(typeof \"123\");", "number", "string", "object", "undefined", "1", "It's a string primitive.", "Type A", "JavaScript"},
            {"Output: undefined", "let x;\nconsole.log(x);", "console.log(undefined);", "Both", "console.log(null);", "2", "Uninitialized variables are undefined.", "Type B", "JavaScript"},
            {"Given:\nlet arr = [1];\n____; \n// Expected arr is [1, 2]", "arr.push(2);", "arr.add(2);", "arr.append(2);", "arr[2] = 2;", "0", "JS arrays use push().", "Type C", "JavaScript"},
            {"Given:\nconst obj = {a: 1};\n____; \n// Expected obj.a is 2", "obj.a = 2;", "obj['a'] = 2;", "Both", "obj == 2;", "2", "Both bracket and dot notation work.", "Type C", "JavaScript"}
        };
        return addRows(qId, QuestionType.PROGRAMMING, topic, 8, prog);
    }

    /** Helper method to add multiple questions efficiently. */
    private int addRows(int firstId, QuestionType type, String topic, int trialId, String[][] rows) {
        int id = firstId;
        for (String[] row : rows) {
            if (type == QuestionType.THEORY) {
                addQuestion(id++, type, topic, trialId, row[0],
                    Arrays.asList(row[1], row[2], row[3], row[4]),
                    Integer.parseInt(row[5]), row[6], 2, null, null);
            } else {
                addQuestion(id++, type, topic, trialId, row[0],
                    Arrays.asList(row[1], row[2], row[3], row[4]),
                    Integer.parseInt(row[5]), row[6], 3, row[7], row[8]);
            }
        }
        return id;
    }
    
    /** 
     * Adds a question to the bank. 
     */
    private void addQuestion(int id, QuestionType type, String topic, int trialId,
                            String questionText, List<String> choices,
                            int correctIndex, String explanation, int difficulty,
                            String progType, String progLang) {

        Question q = new Question(id, type, topic, questionText, choices, correctIndex, explanation, difficulty, trialId, progType, progLang);
        allQuestions.add(q);
    }
    
    // Getters
    public List<Question> getAllQuestions() {
        return new ArrayList<>(allQuestions);
    }
    
    public List<Question> getQuestionsForTrial(int trialId) {
        return allQuestions.stream()
            .filter(q -> q.getTrialId() == trialId)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /** Number of trials represented by the loaded question data. */
    public int getTrialCount() {
        return allQuestions.stream().mapToInt(Question::getTrialId).max().orElse(0);
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

    /** Validation checks ensuring distribution meets all requirements. */
    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (allQuestions.size() != 145) {
            errors.add("Expected 145 questions total, found " + allQuestions.size());
        }
        if (getTheoryQuestions().size() != 70 || getProgrammingQuestions().size() != 75) {
            errors.add("Expected 70 theory and 75 programming questions; found "
                + getTheoryQuestions().size() + " theory and " + getProgrammingQuestions().size() + " programming");
        }
        
        // Validate theory distribution (Trials 1-7 must have exactly 10 theory each)
        for (int trialId = 1; trialId <= 7; trialId++) {
            final int tId = trialId;
            long theory = getTheoryQuestions().stream().filter(q -> q.getTrialId() == tId).count();
            if (theory != 10) {
                errors.add("Trial " + trialId + " must contain exactly 10 theory questions");
            }
        }
        
        // Add additional standard integrity checks
        for (Question question : allQuestions) {
            if (question.getTrialId() < 1 || question.getTrialId() > 8) {
                errors.add("Question " + question.getId() + " has invalid trial ID " + question.getTrialId());
            }
            if (!VALID_TOPICS.contains(question.getTopic())) {
                errors.add("Question " + question.getId() + " has invalid topic: " + question.getTopic());
            }
            if (question.getChoices() == null || question.getChoices().size() != 4) {
                errors.add("Question " + question.getId() + " must have exactly four choices");
            }
        }
        return errors;
    }
}