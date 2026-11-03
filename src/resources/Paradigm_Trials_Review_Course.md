# The Paradigm Trials — Review Course

## Purpose

This review course is based directly on the current `QuestionBank.java`. The bank contains **145 questions: 70 theory questions and 75 programming/practical questions**, organized into eight trial topics. Trials 1–7 each contain 10 theory questions, while Trial 8 is programming-only with 25 programming questions.

The goal of this material is not simply to memorize the answers. Each lesson is organized so a player can learn the concept, recognize the wording used by the question bank, trace the relevant code, avoid common traps, and then move forward like a course app.

---

# COURSE STRUCTURE

The review area should have two major tracks:

## Track A — THEORY

1. Introduction to Programming Paradigms
2. Procedural Programming
3. Functional Programming
4. Object-Oriented Programming
5. Imperative vs Declarative Programming
6. Event-Driven Programming
7. Component Mappings between Programming Paradigms
8. Comprehensive Theory Recap

## Track B — PROGRAMMING / PRACTICAL

1. Introduction: Basic Code Reading
2. Procedural Programming Practice
3. Functional Programming Practice
4. Object-Oriented Programming Practice
5. Imperative vs Declarative Code
6. Event-Driven Code
7. Cross-Paradigm Code Mapping
8. Comprehensive Programming Mastery

Each topic should be divided into small **cards/lessons**. A player reads one card, presses **Next**, and proceeds to the next card. A topic ends with a **Topic Review / Quick Check** before the next topic becomes available.

---

# PART I — THEORY TRACK

---

# TOPIC 1 — INTRODUCTION TO PROGRAMMING PARADIGMS

## Card 1.1 — What Is a Programming Paradigm?

A programming paradigm is a general **style, model, or approach to organizing and expressing programs**.

A paradigm is not the same thing as a programming language.

Think of it this way:

- **Programming language** = the language/tools you write with.
- **Programming paradigm** = the way you think about and organize the solution.

For example, C is strongly associated with procedural programming, while functional programming concepts appear in languages such as LISP. A language can also support more than one paradigm.

### Question cues

If a question asks:

> "What is a programming paradigm?"

Look for an answer describing a **style or approach to programming**, not a specific language, operating system, debugger, or tool.

### Key idea

**Paradigm = programming style.**

---

## Card 1.2 — Major Paradigms in This Course

The question bank works with these major ideas:

- Procedural
- Functional
- Object-Oriented
- Imperative
- Declarative
- Event-Driven

These categories can overlap.

For example:

- Procedural programming is generally imperative.
- Object-oriented programming can use imperative techniques.
- Event-driven programs often use objects and callbacks.
- Functional programming emphasizes functions, immutability, and reduced side effects.
- Declarative programming describes desired results rather than explicit steps.

Do not assume that paradigms are mutually exclusive boxes.

---

## Card 1.3 — Imperative Programming

Imperative programming focuses on **how** a computation should happen.

The programmer gives instructions that change program state.

Typical ideas:

- assignments
- loops
- conditionals
- changing variables
- explicit step-by-step control flow

Example:

```text
x = 5
x = x + 1
print(x)
```

The program explicitly tells the computer what operations to perform.

### Memory cue

**Imperative = instructions = HOW**

The question bank compares imperative programming to a **step-by-step recipe**.

---

## Card 1.4 — Declarative Programming

Declarative programming focuses on **what result is wanted** rather than explicitly describing every step used to obtain it.

Examples from the question bank include:

- SQL
- HTML
- CSS
- regular expressions

Example idea:

```sql
SELECT name FROM students WHERE age >= 18;
```

The query describes the desired data rather than manually writing a loop through every record.

### Memory cue

**Declarative = desired result = WHAT**

This is one of the most important distinctions in the entire review.

---

## Card 1.5 — Functional Programming

Functional programming treats computation heavily in terms of **functions**.

Important concepts:

- pure functions
- immutability
- higher-order functions
- recursion
- map
- filter
- reduce/fold
- first-class functions

A pure function produces the same output for the same input and does not produce side effects.

Example:

```text
square(3) -> 9
```

If calling `square(3)` does not modify anything outside the function, it is a good example of a pure function.

### Memory cue

**Functional = functions + predictable transformations + reduced mutable state**

---

## Card 1.6 — Object-Oriented Programming

Object-oriented programming organizes programs around **objects** that combine data/state and behavior.

A class can act as a blueprint.

An object is a particular instance created from that class.

Four major OOP ideas emphasized in the question bank are:

1. Encapsulation
2. Inheritance
3. Polymorphism
4. Abstraction

### Memory cue

**OOP = objects containing state + behavior**

---

## Card 1.7 — Event-Driven Programming

Event-driven programming organizes execution around **events**.

Examples:

- button clicks
- keyboard actions
- sensor output
- messages
- timers

Instead of simply executing a fixed sequence from top to bottom, the program waits for events and runs the corresponding handlers.

Important terms:

- event
- callback
- listener
- event loop
- asynchronous execution
- Promise
- event emitter
- pub/sub

### Memory cue

**Event-driven = something happens → handler reacts**

---

## Card 1.8 — Why Multi-Paradigm Languages Matter

A language may support several programming styles.

This is useful because different problems can be expressed naturally using different approaches.

For example:

- procedural code for straightforward step-by-step operations
- OOP for systems organized around objects
- functional techniques for transformations over collections
- event-driven techniques for interactive applications
- declarative techniques for describing data, structure, or styling

The question bank describes the benefit as being able to use the **right tool for different problems**.

---

## Card 1.9 — State and Side Effects

**State** is information that can change while a program runs.

Example:

```text
x = 5
x = 6
```

The value of `x` changed, so the program has mutable state.

A **side effect** occurs when an operation changes something beyond simply producing its return value.

Examples:

- changing a global variable
- modifying external data
- printing output
- changing an object
- performing I/O

The question bank particularly emphasizes that mutable state can create side effects and bugs.

### Exam cue

If the question mentions:

- state changing
- external modification
- unpredictable interactions

think **side effects / mutable state**.

---

# TOPIC 2 — PROCEDURAL PROGRAMMING

## Card 2.1 — What Is Procedural Programming?

Procedural programming organizes a program into **procedures/functions** that perform operations.

A procedure usually receives data, performs operations, and may return a result.

Core concepts:

- functions/procedures
- variables
- scope
- parameters
- return values
- loops
- conditionals
- mutable state

### Core cue

**Procedural = procedures/functions + step-by-step operations**

---

## Card 2.2 — Mutable State

Procedural programming commonly changes variables while a program executes.

Example:

```c
int x = 5;
x++;
```

After `x++`, the state changed from 5 to 6.

This is different from an immutable approach where existing data is not directly changed.

---

## Card 2.3 — Scope

**Scope** describes where a variable can be accessed.

A local variable generally exists only within its relevant block/function.

A global variable can be accessible from many parts of a program.

### Why scope matters

If a variable is outside the area where it is defined, the program may not be able to access it.

### Question cue

If the question asks:

> "Where is a variable accessible?"

The answer is **scope**.

---

## Card 2.4 — Pass by Value

Pass by value means a function receives a **copy of a value**.

Conceptually:

```text
original x = 5
function receives 5
function changes its local copy
```

The important exam distinction is:

- pass by value → value/copy
- passing an address/reference → gives access to the referenced object/data depending on the language

The question bank explicitly defines pass by value as passing a copy of the data.

---

## Card 2.5 — Structured Programming

Structured programming emphasizes structured control flow instead of unrestricted jumps.

Common structures:

- `if`
- `if/else`
- `while`
- `for`
- `switch`

The question bank associates structured programming with **avoiding `goto` by using loops and conditionals**.

### Memory cue

**Structured = blocks of understandable control flow**

---

## Card 2.6 — Global Variables

Global variables can be accessed or modified by many procedures.

That can be convenient, but it can also make a program harder to reason about.

The question bank focuses on the risk:

> Any procedure can modify the global state, which can cause bugs.

### Exam cue

If the question describes many functions unexpectedly changing the same variable, think:

**global mutable state**

---

## Card 2.7 — Parameters and Return Values

Procedures communicate primarily through:

- **parameters** → information entering the procedure
- **return values** → information coming back from the procedure

Example:

```text
result = add(2, 3)
```

`2` and `3` are arguments passed to `add`.

The returned result is `5`.

---

## Card 2.8 — Common Procedural Code Patterns

### Loop tracing

For:

```c
int i = 0;
while (i < 3) {
    printf("%d", i);
    i++;
}
```

Trace:

```text
i = 0 → print 0
i = 1 → print 1
i = 2 → print 2
i = 3 → stop
```

Output:

```text
012
```

### Exam technique

Never guess a loop output.

Make a tiny table:

| Step | Variable | Action |
|---|---:|---|
| 1 | 0 | print |
| 2 | 1 | print |
| 3 | 2 | print |
| 4 | 3 | condition fails |

---

# TOPIC 3 — FUNCTIONAL PROGRAMMING

## Card 3.1 — Pure Functions

A pure function has two important properties:

1. Same input → same output.
2. No side effects.

Example:

```text
square(4) = 16
```

Calling it does not modify external state.

### Exam cue

If the question says:

- deterministic
- same output for same input
- no side effects

answer **pure function**.

---

## Card 3.2 — Immutability

Immutable data is data that cannot be modified after it has been created.

Instead of changing an existing value, a new value is produced.

### Contrast

Mutable:

```text
list = [1, 2]
list.add(3)
```

Immutable idea:

```text
newList = oldList + 3
```

The original remains unchanged.

### Memory cue

**Immutable = don't modify the original**

---

## Card 3.3 — First-Class Functions

A language treats functions as first-class values when functions can be handled like other values.

They can be:

- stored in variables
- passed as arguments
- returned from functions

Example:

```text
f = someFunction
```

Then:

```text
f(5)
```

can call the stored function.

---

## Card 3.4 — Higher-Order Functions

A higher-order function is a function that:

- accepts another function as an argument, or
- returns another function.

This is broader than recursion.

A recursive function calls itself.

A higher-order function works with other functions.

### Exam trap

Do not choose "recursive function" just because the question says "function involving functions."

Look specifically for:

**takes or returns another function**

---

## Card 3.5 — Recursion

Recursion is when a function calls itself.

Example:

```python
def fact(n):
    return 1 if n == 0 else n * fact(n - 1)
```

For `fact(5)`:

```text
5 × fact(4)
5 × 4 × fact(3)
5 × 4 × 3 × fact(2)
5 × 4 × 3 × 2 × fact(1)
5 × 4 × 3 × 2 × 1
= 120
```

The question bank presents recursion as an important replacement for loops in pure functional approaches.

---

## Card 3.6 — map

`map` generally applies a function to **every element**.

Example:

```text
[1, 2, 3]
×2
↓
[2, 4, 6]
```

Python example:

```python
[x * 2 for x in [1, 2, 3]]
```

### Memory cue

**map = transform every item**

---

## Card 3.7 — filter

`filter` keeps elements that satisfy a condition.

Example:

```text
[1, 2, 3, 4]
condition: even
↓
[2, 4]
```

Python:

```python
filter(lambda x: x % 2 == 0, [1, 2, 3, 4])
```

### Memory cue

**filter = keep what passes**

---

## Card 3.8 — reduce / fold

`reduce` or `fold` combines multiple elements into a single accumulated result.

Example:

```text
[1, 2, 3, 4]
↓
1 + 2 + 3 + 4
↓
10
```

### Memory cue

- map → many outputs
- filter → fewer outputs
- reduce → one accumulated output

---

## Card 3.9 — Functional Programming and Parallelism

The question bank associates immutability with easier parallel processing.

Why?

Shared mutable state can cause multiple operations to interfere with one another.

If data is immutable, one computation cannot unexpectedly change the data another computation is reading.

The important idea for the question bank is:

**less shared mutable state → fewer synchronization/state-change concerns**

---

## Card 3.10 — LISP

The question bank identifies **LISP** as a language that heavily influenced functional programming concepts.

For this review, remember the direct association:

**LISP → early/influential functional programming**

---

# TOPIC 4 — OBJECT-ORIENTED PROGRAMMING

## Card 4.1 — Class vs Object

A **class** is a blueprint.

An **object** is an actual instance created from that blueprint.

Analogy:

```text
Class = blueprint
Object = house built from blueprint
```

Example:

```java
class Dog {
    String name;
}
```

Then:

```java
Dog d = new Dog();
```

`Dog` is the class.

`d` is an object/instance.

---

## Card 4.2 — Encapsulation

Encapsulation bundles data and behavior and controls how internal state is accessed.

A common example is using private fields and methods to control access.

Example:

```java
class Student {
    private int age;

    public int getAge() {
        return age;
    }
}
```

The field is not directly exposed.

### Exam cue

If the question says:

- hide internal state
- controlled access
- bundle data and methods

think **encapsulation**.

---

## Card 4.3 — Inheritance

Inheritance allows a subclass to derive properties and behavior from a superclass.

Example:

```java
class Animal {
    void speak() {}
}

class Dog extends Animal {
}
```

`Dog` inherits from `Animal`.

### Memory cue

**Inheritance = IS-A relationship**

A dog is an animal.

---

## Card 4.4 — Polymorphism

Polymorphism means that a common interface can represent different underlying forms.

Example:

```java
Animal a = new Dog();
```

The variable can use the `Animal` type while the actual object is a `Dog`.

The same method call can behave differently depending on the actual object.

### Memory cue

**Poly = many, morph = forms**

---

## Card 4.5 — Abstraction

Abstraction hides unnecessary implementation details and exposes the essential features.

The goal is to reduce complexity for the user of the component.

Example idea:

```text
car.start()
```

You use the operation without needing to manually perform every engine operation.

### Exam cue

If the question says:

> "Hide complex implementation details and show only essential features"

answer **abstraction**.

---

## Card 4.6 — Constructors

A constructor is used to initialize a newly created object.

Example:

```java
class Person {
    String name;

    Person(String name) {
        this.name = name;
    }
}
```

The constructor initializes the object's starting state.

### Memory cue

**Constructor = setup when object is created**

---

## Card 4.7 — Instance

An instance is a particular object created from a class.

```java
Dog dog1 = new Dog();
Dog dog2 = new Dog();
```

Both are instances of `Dog`.

---

## Card 4.8 — this / self

The current object's instance is commonly referred to using:

- `this` in Java and JavaScript
- `self` in Python

Example Java:

```java
this.name = name;
```

Example Python:

```python
self.name = name
```

### Exam cue

If a question asks what `this` or `self` refers to:

**the current instance**

---

## Card 4.9 — Method Overriding

Overriding occurs when a subclass provides its own implementation of a method inherited from a parent class.

Example:

```java
class Animal {
    void speak() {}
}

class Dog extends Animal {
    public void speak() {
        System.out.println("Bark");
    }
}
```

The subclass gives a specific implementation.

### Do not confuse

**Overriding**:

```text
same method concept in parent/subclass
```

**Overloading**:

```text
same method name, different parameter list
```

The question bank specifically tests overriding.

---

## Card 4.10 — Access Modifiers

An access modifier controls visibility/access.

The question bank uses:

```java
private
```

as the correct example.

Common Java modifiers include:

- `private`
- `public`
- `protected`

Do not confuse `static` with an access modifier. `static` describes class-level behavior/storage, not visibility.

---

# TOPIC 5 — IMPERATIVE VS DECLARATIVE PROGRAMMING

## Card 5.1 — The Core Difference

The single most important distinction:

| Style | Main question |
|---|---|
| Imperative | How do I do it? |
| Declarative | What do I want? |

Imperative code explicitly describes the steps.

Declarative code describes the desired result or rule.

---

## Card 5.2 — Recipe vs Restaurant Order

The question bank uses two useful analogies.

### Imperative

A step-by-step recipe:

```text
1. Heat pan.
2. Add oil.
3. Add egg.
4. Cook.
5. Serve.
```

You describe the process.

### Declarative

Ordering a meal:

```text
"I want fried rice."
```

You describe the desired result.

### Memory cue

**Imperative = recipe**

**Declarative = order**

---

## Card 5.3 — SQL

SQL is treated in the question bank as primarily declarative.

Example:

```sql
SELECT name
FROM students
WHERE age >= 18;
```

The programmer describes which records are wanted.

The database system handles the underlying execution strategy.

### Exam cue

**SQL → declarative**

---

## Card 5.4 — HTML

The question bank considers HTML declarative because it describes document structure without explicitly writing the control-flow steps needed to render that structure.

Example:

```html
<h1>Hello</h1>
```

This declares that the text is a heading.

---

## Card 5.5 — CSS

CSS describes desired visual styling.

Example:

```css
color: red;
```

The stylesheet specifies the desired appearance rather than manually drawing each pixel.

### Exam cue

**CSS → declarative styling**

---

## Card 5.6 — Regular Expressions

Regular expressions describe a **pattern** to match.

You specify the pattern rather than writing a procedural algorithm that manually checks every character.

The question bank categorizes regex as declarative.

---

## Card 5.7 — State Changes

Imperative programming commonly emphasizes changing program state.

Example:

```text
x = 1
x = 2
x = 3
```

The variable changes repeatedly.

Declarative approaches tend to emphasize the result or relationship rather than exposing every state transition.

---

## Card 5.8 — Declarative Code Under the Hood

Declarative does not mean "no computation."

A declarative language still has to be executed by a system.

The question bank's intended idea is:

**declarative descriptions are implemented/executed by underlying imperative mechanisms.**

So:

```text
Declarative surface
        ↓
compiler/interpreter/runtime
        ↓
actual execution steps
```

---

## Card 5.9 — Recognizing the "How" Question

If a question asks:

> Which paradigm focuses on "How"?

Answer:

**Imperative**

If it asks:

> Which paradigm focuses on the desired result / "What"?

Answer:

**Declarative**

---

# TOPIC 6 — EVENT-DRIVEN PROGRAMMING

## Card 6.1 — Events

An event is something that happens and can trigger program behavior.

Examples from the question bank:

- user clicks a button
- keyboard action
- sensor output
- asynchronous completion

The event becomes the trigger for a handler.

---

## Card 6.2 — Callback Functions

A callback is a function passed to another component so it can be executed later, often when an event occurs.

Concept:

```text
register callback
       ↓
event happens
       ↓
callback executes
```

JavaScript:

```javascript
button.addEventListener("click", () => {
    console.log("Hi");
});
```

The arrow function is a callback.

---

## Card 6.3 — Event Listener

An event listener waits for a specific event and responds when that event occurs.

Example Java Swing:

```java
button.addActionListener(e -> {
    System.out.print("C");
});
```

When the button is clicked, the handler runs.

### Exam cue

**listener = waits for event**

---

## Card 6.4 — Event Loop

An event loop is a mechanism that waits for and dispatches events/tasks.

This is especially important in asynchronous environments such as JavaScript/Node.js.

Conceptually:

```text
wait
 ↓
event arrives
 ↓
dispatch handler
 ↓
handle event
 ↓
wait again
```

Do not confuse the event loop with an ordinary `while` loop.

---

## Card 6.5 — GUI Applications

Event-driven architecture is especially common in GUI applications.

Why?

A GUI cannot assume exactly when the user will:

- click
- type
- drag
- close a window

Instead, the application waits for events.

---

## Card 6.6 — Synchronous vs Asynchronous

### Synchronous

Operations execute in sequence and one operation can block the next.

```text
A finishes
 ↓
B starts
 ↓
B finishes
 ↓
C starts
```

### Asynchronous

An operation can allow other work to proceed while waiting for completion.

The question bank defines asynchronous execution as code that can execute without blocking the main thread.

### Memory cue

**Sync = wait**

**Async = don't block unnecessarily**

---

## Card 6.7 — Promises

A JavaScript Promise represents the eventual completion or failure of an asynchronous operation.

Conceptually:

```text
pending
   ↓
fulfilled
or
rejected
```

The important question-bank definition is:

**an object representing eventual completion or failure of an async operation**

---

## Card 6.8 — Event Emitters and Pub/Sub

Event-driven systems can separate the producer of an event from the component that handles it.

A simplified pattern:

```text
Emitter
   ↓ event
Subscriber/listener
   ↓
handler
```

This reduces direct coupling between components.

---

## Card 6.9 — Callback Hell

Callback hell refers to deeply nested callbacks that make asynchronous code difficult to read and maintain.

Concept:

```javascript
first(() => {
    second(() => {
        third(() => {
            fourth(() => {
            });
        });
    });
});
```

The problem is not simply "callbacks exist."

The problem is **excessive nesting and complexity**.

---

# TOPIC 7 — COMPONENT MAPPINGS BETWEEN PROGRAMMING PARADIGMS

## Card 7.1 — Why Map Components?

Different paradigms can solve similar problems using different structures.

Mapping helps answer:

> "If I know how to express this idea in one paradigm, what concept performs a similar role in another?"

The purpose in the question bank is to understand how solutions can be translated across architectural styles.

---

## Card 7.2 — OOP Method → Functional Function

OOP:

```text
object.method()
```

Functional style:

```text
function(data)
```

The question bank uses **pure functions** as the functional equivalent for behavior.

---

## Card 7.3 — Loop → Recursion / Higher-Order Functions

Imperative/procedural:

```text
for each item:
    process item
```

Functional:

```text
map/process function over collection
```

or recursion.

The question bank specifically maps loops to:

- recursion
- higher-order functions

---

## Card 7.4 — Object State + Behavior → Closure

An object can contain state and behavior.

A functional technique can simulate a similar relationship using a **closure**.

A closure can capture variables from its surrounding environment and expose functions that use that captured state.

Concept:

```text
captured state
      +
function
      ↓
closure
```

---

## Card 7.5 — Procedural Struct/Record → OOP Class

A procedural `struct` or record primarily groups data.

An OOP class can group:

- attributes/data
- methods/behavior

Therefore the question bank maps a procedural structure to:

**a class with attributes and possibly methods**

---

## Card 7.6 — Imperative Array Iteration → Declarative Collection Operations

Imperative:

```text
for each item:
    do something
```

Declarative/functional-style collection processing:

```text
map(...)
filter(...)
```

The question bank explicitly uses SQL queries and `Array.map/filter` as examples of declarative alternatives.

---

## Card 7.7 — Event Callback → Listener/Observer

In event-driven programming:

```text
callback
```

In an OOP design:

```text
Listener / Observer implementation
```

Both represent behavior that should run when an event occurs.

---

## Card 7.8 — Functional Immutability → OOP final/readonly + New Objects

Functional programming emphasizes immutable state.

In OOP, a similar idea can be represented by:

- `final` fields in Java
- readonly-style properties
- objects whose state is not mutated
- creating new objects instead of changing existing ones

The key is the **behavioral idea**, not that the syntax is identical.

---

## Card 7.9 — OOP Polymorphism → Functional Polymorphism

The question bank maps OOP interface-style polymorphism to functional techniques such as:

- higher-order functions
- type classes

The common idea is that code can work with multiple implementations/forms through a shared abstraction.

---

## Card 7.10 — SQL WHERE → filter

SQL:

```sql
WHERE age >= 18
```

Functional:

```text
filter(age >= 18)
```

Both select only elements satisfying a condition.

### Memory cue

**WHERE = filter**

---

# TOPIC 8 — COMPREHENSIVE THEORY RECAP

This section is a final theory map.

## Card 8.1 — One-Word Cues

| If you see... | Think... |
|---|---|
| style/approach to programming | paradigm |
| procedures/functions + mutable state | procedural |
| same input → same output, no side effects | pure function |
| cannot be modified | immutable |
| function takes/returns function | higher-order |
| applies to every item | map |
| keeps matching items | filter |
| combines into one value | reduce/fold |
| blueprint | class |
| specific object from class | instance |
| hide internal state | encapsulation |
| subclass gets parent behavior | inheritance |
| many forms | polymorphism |
| hide implementation details | abstraction |
| object initialization | constructor |
| current object | this/self |
| subclass replaces parent method implementation | overriding |
| visibility control | access modifier |
| "How?" | imperative |
| "What result?" | declarative |
| SQL | declarative |
| HTML structure | declarative |
| CSS styling | declarative |
| regex pattern | declarative |
| user action triggers code | event-driven |
| passed function executed later | callback |
| waits for event | listener |
| waits and dispatches events | event loop |
| eventual async result | Promise |
| deeply nested callbacks | callback hell |
| SQL WHERE | filter |
| loop alternative | recursion / map/filter |

---

# PART II — PROGRAMMING / PRACTICAL TRACK

The programming questions use C, Java, Python, and JavaScript. Most are not asking for large programs. They are testing whether you can **read a short snippet, trace it, recognize syntax, and predict output**.

---

# PRACTICAL TOPIC 1 — BASIC CODE READING

## Card P1.1 — The Four-Step Code Reading Method

For every code question:

### Step 1 — Identify the language

Look for clues:

- C → `printf`, `int main`, semicolons, pointers
- Java → `System.out.print`, classes, `new`, typed variables
- Python → indentation, `def`, `print`, no required semicolons
- JavaScript → `console.log`, `let`, `const`, arrow functions

### Step 2 — Identify the operation

Ask:

- arithmetic?
- assignment?
- function call?
- loop?
- array/list operation?
- object operation?
- callback?

### Step 3 — Trace values

Write the variable values after each important operation.

### Step 4 — Match the result to the choices

Do not choose an answer until you know the actual output/state.

---

## Card P1.2 — Operator Precedence

Example:

```c
int a = 5;
int b = 3;
printf("%d", a + b * 2);
```

Multiplication happens before addition:

```text
b * 2 = 6
a + 6 = 11
```

Output:

```text
11
```

### Quick rule

When no parentheses are present:

```text
multiplication/division/modulo
        before
addition/subtraction
```

---

## Card P1.3 — Integer Division

In languages such as C and Java, integer division with integer operands produces an integer result.

Example:

```java
10 / 2
```

gives:

```text
5
```

Be careful with:

```java
5 / 2
```

when both operands are integers: the result is integer division.

---

## Card P1.4 — Increment and Decrement

```text
x++
```

increases `x` by 1.

```text
x--
```

decreases `x` by 1.

Also:

```text
x += 4
```

means:

```text
x = x + 4
```

and:

```text
x *= 2
```

means:

```text
x = x * 2
```

---

# PRACTICAL TOPIC 2 — PROCEDURAL PROGRAMMING PRACTICE

## Card P2.1 — C printf

Basic form:

```c
printf("%d", value);
```

`%d` is used for an integer in the question bank.

Example:

```c
printf("%d", 10 + 5);
```

Output:

```text
15
```

---

## Card P2.2 — Java Printing

Java:

```java
System.out.print(5);
```

prints:

```text
5
```

No automatic newline.

`println` prints a newline after the value.

---

## Card P2.3 — Python Printing

Python:

```python
print(5)
```

prints:

```text
5
```

Function calls use parentheses.

Example:

```python
print(f(5))
```

means:

1. call `f(5)`
2. obtain result
3. print result

---

## Card P2.4 — Function Calls

Given:

```c
int f(int x) {
    return x * x;
}

printf("%d", f(3));
```

Trace inside-out:

```text
f(3)
= 3 * 3
= 9
```

Then:

```text
printf("%d", 9)
```

Output:

```text
9
```

---

## Card P2.5 — Loops

For every loop question, identify:

1. starting value
2. condition
3. body
4. update

Example:

```c
for (int i = 0; i < 3; i++) {
    printf("%d", i);
}
```

Trace:

```text
i=0 → print
i=1 → print
i=2 → print
i=3 → stop
```

Output:

```text
012
```

---

# PRACTICAL TOPIC 3 — FUNCTIONAL PROGRAMMING PRACTICE

## Card P3.1 — Recursive Functions

When you see:

```text
function calls itself
```

look for:

- base case
- recursive case

For factorial:

```python
return 1 if n == 0 else n * fact(n - 1)
```

The base case prevents infinite recursion.

---

## Card P3.2 — Function Values

Python:

```python
def handler():
    print("Click")

button_click = handler
button_click()
```

The function is stored in a variable.

Then:

```text
button_click()
```

calls the function.

Output:

```text
Click
```

This demonstrates first-class functions.

---

## Card P3.3 — JavaScript Arrow Functions

Example:

```javascript
const add = (a, b) => a + b;
console.log(add(3, 4));
```

Trace:

```text
add(3, 4)
→ 3 + 4
→ 7
```

Output:

```text
7
```

---

## Card P3.4 — map/filter Recognition

Python list comprehension:

```python
[x * 2 for x in [1, 2, 3]]
```

means transform every element:

```text
1 → 2
2 → 4
3 → 6
```

Result:

```text
[2, 4, 6]
```

For filtering:

```python
filter(lambda x: x % 2 == 0, [1,2,3,4])
```

keep values where:

```text
x % 2 == 0
```

Result:

```text
[2, 4]
```

---

# PRACTICAL TOPIC 4 — OBJECT-ORIENTED PROGRAMMING PRACTICE

## Card P4.1 — C Structs

The question bank uses C structs as grouped data.

Example:

```c
struct Point {
    int x;
    int y;
};

struct Point p = {1, 2};
```

Access a member using:

```c
p.y
```

Result:

```text
2
```

---

## Card P4.2 — Dot vs Arrow

For a struct object:

```c
p.x
```

uses the dot operator.

For a pointer to a struct:

```c
p->x
```

is used.

The question bank specifically tests the distinction between:

```text
obj.member
```

and pointer-member access.

---

## Card P4.3 — Java Objects

Example:

```java
class A {
    int x = 1;
}

A obj = new A();
System.out.print(obj.x);
```

Steps:

1. define class
2. create object
3. `x` starts as `1`
4. access `obj.x`

Output:

```text
1
```

---

## Card P4.4 — Java Method Override

Given:

```java
class Dog extends Animal {
    public void speak() {
    }
}
```

The method has the same method concept as the parent and provides a subclass implementation.

This is overriding.

---

## Card P4.5 — Python self

Python instance methods normally use `self`:

```python
class Cat:
    def sound(self):
        return "Meow"
```

Then:

```python
Cat().sound()
```

returns:

```text
Meow
```

For this question bank, remember:

**Python instance method → `self`**

---

## Card P4.6 — Python isinstance

Example:

```python
isinstance("hi", str)
```

asks whether `"hi"` is an instance of `str`.

Result:

```text
True
```

---

## Card P4.7 — JavaScript this

Inside a JavaScript class constructor:

```javascript
constructor(make) {
    this.make = make;
}
```

`this` refers to the current object.

The assignment stores the constructor argument in the object's property.

---

# PRACTICAL TOPIC 5 — IMPERATIVE VS DECLARATIVE CODE

## Card P5.1 — Recognizing Imperative Code

Look for:

- loops
- assignments
- explicit updates
- step-by-step instructions

Example:

```c
int x = 1;
x = x + 4;
```

The code explicitly changes state.

---

## Card P5.2 — Recognizing Declarative/Functional-Style Code

Look for:

- list comprehensions
- `map`
- `filter`
- SQL queries
- descriptions of desired structure/style

Example:

```python
[x * 2 for x in [1, 2, 3]]
```

This describes a transformation over a collection rather than manually writing a loop.

---

## Card P5.3 — "Both" Answers

Some programming questions deliberately have multiple correct snippets.

Example:

```text
Output: 10
```

Potential snippets:

```c
printf("10");
```

or:

```c
int x = 10;
printf("%d", x);
```

If both produce the required output, the correct choice may be **Both** / **All of the above**, depending on the options.

### Exam technique

Do not stop after finding one correct answer.

If an option says:

```text
Both A and B
```

test A and B individually.

---

# PRACTICAL TOPIC 6 — EVENT-DRIVEN CODE

## Card P6.1 — C Function Pointer Callback

C can pass a function pointer as an argument.

Concept:

```c
void run(void (*f)()) {
    f();
}
```

If `f` points to:

```c
void cb() {
    printf("Hi");
}
```

then:

```c
run(cb);
```

causes:

```text
cb()
→ printf("Hi")
```

Output:

```text
Hi
```

---

## Card P6.2 — Java Swing Listener

The question bank uses:

```java
JButton b = new JButton();

b.addActionListener(e -> System.out.print("C"));
```

The lambda is the event handler.

When the button is clicked:

```text
click
 ↓
ActionListener
 ↓
lambda executes
 ↓
print C
```

---

## Card P6.3 — Java Thread Lambda

Example:

```java
Thread t = new Thread(() -> System.out.print("Run"));
t.start();
```

The lambda supplies the runnable work.

When the thread executes, it prints:

```text
Run
```

For the question bank, focus on recognizing the lambda as executable behavior supplied to another component.

---

## Card P6.4 — Python Function as Callback

Example:

```python
def handler():
    print("Click")

button_click = handler
button_click()
```

The function is stored and later called.

This is both:

- first-class function behavior
- callback-style behavior

---

## Card P6.5 — JavaScript addEventListener

Example:

```javascript
document
    .getElementById('btn')
    .addEventListener('click', () => console.log('Hi'));
```

Break it down:

```text
get element
   ↓
register click listener
   ↓
wait for click
   ↓
execute arrow-function callback
   ↓
log Hi
```

---

# PRACTICAL TOPIC 7 — CROSS-PARADIGM CODE MAPPING

## Card P7.1 — Pointer Dereferencing

C:

```c
int x = 10;
int *p = &x;
printf("%d", *p);
```

Important operators:

```text
&x → address of x
*p → value stored at the address in p
```

Therefore:

```text
*p = 10
```

Output:

```text
10
```

---

## Card P7.2 — Character Arithmetic in C

The question bank uses:

```c
char c = 'A';
printf("%c", c + 1);
```

Characters have numeric representations.

Incrementing `'A'` by one reaches the next character:

```text
A → B
```

So the expected output is:

```text
B
```

---

## Card P7.3 — Java Strings

### Length

```java
String s = "Hi";
s.length()
```

Result:

```text
2
```

### Character access

```java
String s = "Hello";
s.charAt(1)
```

Indexing starts at zero:

```text
H → 0
e → 1
l → 2
l → 3
o → 4
```

Therefore:

```text
s.charAt(1) = 'e'
```

---

## Card P7.4 — Java Equality

For the simple values used in the question bank:

```java
1 == 1
```

is `true`.

For strings, the bank uses:

```java
"a".equals("a")
```

to compare string contents.

Remember:

```text
== → primitive equality / reference comparison depending on operands
.equals() → object/content equality according to the class implementation
```

For the question bank's string example, `.equals()` produces `true`.

---

## Card P7.5 — Python Length and Types

```python
len([1, 2, 3])
```

returns:

```text
3
```

The question bank also uses:

```python
type({})
```

and:

```python
type(dict())
```

Both represent a dictionary type.

---

## Card P7.6 — Python Lists

Add an element:

```python
x.append(4)
```

Example:

```python
x = [1, 2, 3]
x.append(4)
```

Result:

```text
[1, 2, 3, 4]
```

The question bank contrasts `append` with methods that do not belong to Python lists.

---

## Card P7.7 — Python Dictionaries

Given:

```python
d = {'a': 1}
```

Update the value:

```python
d['a'] = 2
```

Then:

```text
d['a']
```

is:

```text
2
```

---

## Card P7.8 — JavaScript typeof

Given:

```javascript
typeof "123"
```

the result is:

```text
"string"
```

Even though the characters are digits, `"123"` is inside quotation marks, so it is a string.

---

## Card P7.9 — JavaScript undefined

```javascript
let x;
console.log(x);
```

`x` exists but has not been assigned a value.

The result is:

```text
undefined
```

---

## Card P7.10 — JavaScript Arrays

Add an item:

```javascript
arr.push(2);
```

Example:

```javascript
let arr = [1];
arr.push(2);
```

Result:

```text
[1, 2]
```

The question bank uses `push()` as the standard array-add operation.

---

## Card P7.11 — JavaScript Objects

Given:

```javascript
const obj = {a: 1};
```

Both of these can update the property:

```javascript
obj.a = 2;
```

and:

```javascript
obj['a'] = 2;
```

They access the same property.

---

# PRACTICAL TOPIC 8 — COMPREHENSIVE PROGRAMMING MASTERY

## Card P8.1 — C Quick Reference

Know these patterns from the question bank:

```c
printf("%d", x);
```

integer output

```c
printf("%f", (float)a / b);
```

floating-point division after casting

```c
printf("%c", 65);
```

character output based on the numeric character representation used by the question bank

```c
x % y
```

remainder/modulo

```c
str[1] = 'a';
```

modify the second character of a mutable character array

```c
if (x == 10)
```

comparison

```c
do {
    ...
} while (condition);
```

body executes at least once before the condition is checked.

---

## Card P8.2 — Java Quick Reference

### String concatenation

```java
System.out.print("a" + "b");
```

Output:

```text
ab
```

### Absolute value

```java
Math.abs(-5)
```

Result:

```text
5
```

### Null printing

For:

```java
String s = null;
System.out.print(s);
```

the question bank expects the textual result:

```text
null
```

### Arithmetic

```java
10 * 10
10 % 1
```

produce:

```text
100
0
```

### Decrement

```java
x--;
x -= 1;
```

both reduce `x` by one.

---

## Card P8.3 — Java ArrayList

The question bank uses:

```java
ArrayList<Integer> list = new ArrayList<>();
list.add(1);
```

`add()` inserts an element.

The expected result is a list containing one item.

### Memory cue

**Java ArrayList → add()**

---

## Card P8.4 — Python Quick Reference

### Repeat a string

```python
"ab" * 3
```

Result:

```text
ababab
```

### Length

```python
len("hello")
```

Result:

```text
5
```

### List

```python
[1, 2]
```

is a list.

### Append

```python
x.append(4)
```

adds an element to the list.

### Dictionary

```python
d['a'] = 2
```

updates a dictionary entry.

---

## Card P8.5 — JavaScript Quick Reference

### typeof

```javascript
typeof "123"
```

returns:

```text
"string"
```

### undefined

```javascript
let x;
```

leaves `x` as:

```text
undefined
```

### Array push

```javascript
arr.push(2);
```

adds an item.

### Object properties

Both work:

```javascript
obj.a = 2;
obj["a"] = 2;
```

---

## Card P8.6 — NaN

The question bank uses:

```javascript
parseInt('a')
```

and:

```javascript
0 / 0
```

as examples that can produce `NaN`.

`NaN` means **Not-a-Number**.

It represents an invalid/non-numeric numerical result rather than ordinary numeric zero.

---

## Card P8.7 — Output Questions

A large portion of Trial 8 can be solved using one procedure.

### Output-solving algorithm

```text
1. Identify language.
2. Identify starting values.
3. Execute statements in order.
4. Apply operators/functions.
5. Track changed variables.
6. Evaluate the final print/log statement.
7. Compare every answer choice.
```

Do not mentally execute the entire program at once.

Break it into tiny transformations.

---

# FINAL EXAM / GAME STRATEGY

## Card F1 — Theory Questions

When you see a theory question:

1. Find the keyword.
2. Identify which paradigm/concept owns that keyword.
3. Eliminate answers from unrelated paradigms.
4. Check the definition.
5. Watch for NOT questions.

### Important trigger words

```text
style/approach → paradigm
procedures → procedural
same input/same output → pure function
cannot change → immutable
function as data → first-class function
takes/returns function → higher-order
every element → map
matching elements → filter
single accumulated result → reduce/fold
blueprint → class
instance → object
hide data → encapsulation
subclass → inheritance
many forms → polymorphism
hide complexity → abstraction
initialize object → constructor
current object → this/self
subclass implementation → overriding
visibility → access modifier
HOW → imperative
WHAT → declarative
click/trigger → event-driven
passed function → callback
waits for event → listener
async dispatch → event loop
eventual async result → Promise
nested callbacks → callback hell
```

---

## Card F2 — Programming Questions

For code questions:

```text
LANGUAGE
   ↓
SYNTAX
   ↓
VALUES
   ↓
OPERATION
   ↓
OUTPUT
```

Never jump directly from syntax to an answer without tracing the value.

---

## Card F3 — Handling "Expected Output" Questions

If the question says:

```text
Expected: 20
```

your task is usually to find the statement that changes the program into exactly that state.

For example:

```text
x = 10
```

To make `x = 20`:

```text
x = x * 2
```

or:

```text
x = 20
```

may both be valid depending on the answer choices.

Always test each candidate.

---

## Card F4 — Indexing

For arrays/strings, check whether indexing starts at zero.

In the languages used in the bank:

```text
first item → index 0
second item → index 1
third item → index 2
```

So:

```java
"Hello".charAt(1)
```

is:

```text
e
```

and:

```c
arr[1]
```

is the second array element.

---

## Card F5 — Assignment vs Comparison

This is a common trap.

Assignment:

```text
x = 5
```

means put 5 into `x`.

Comparison:

```text
x == 5
```

asks whether `x` equals 5.

In the question bank, an option such as:

```text
x == 5;
```

does not change `x`.

An assignment does.

---

## Card F6 — Final Mental Checklist

Before answering:

### Theory

```text
What concept is being defined?
What keyword gives it away?
Is the question asking NOT?
Are two answers actually related?
```

### Code

```text
What language?
What are the initial values?
What changes?
What is the final operation?
What exactly gets printed?
```

### Multiple-choice

```text
Could more than one option work?
Is there a "Both" or "All" option?
Does the question ask for an error, output, or state?
```

---

# COURSE COMPLETION STRUCTURE

Each topic should follow this progression:

```text
TOPIC INTRO
    ↓
CARD 1 — Core concept
    ↓
CARD 2 — Definition
    ↓
CARD 3 — Example
    ↓
CARD 4 — Recognition cues
    ↓
CARD 5 — Common traps
    ↓
CARD 6 — Question-solving method
    ↓
TOPIC QUICK CHECK
    ↓
TOPIC COMPLETE
```

Programming topics should additionally include:

```text
syntax card
    ↓
trace example
    ↓
language-specific example
    ↓
output prediction
    ↓
fill-the-blank/code completion
    ↓
common mistake
    ↓
quick check
```

---

# SOURCE-ALIGNMENT NOTE

This review was designed around the concepts, terminology, languages, code patterns, and question styles actually present in `QuestionBank.java`.

The current question bank identifies eight topics and separates questions into `THEORY` and `PROGRAMMING` types. It also uses C, Java, Python, and JavaScript in the programming questions. The bank contains 145 questions in total: 70 theory and 75 programming questions.

The review intentionally teaches the concepts needed to solve those questions instead of simply reproducing their answers.

Some code snippets in the question bank are simplified quiz snippets rather than complete standalone programs. The review therefore focuses on the intended concept and the local behavior being tested.
