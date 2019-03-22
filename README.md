# Info
*Author:* Eugene Syriani
*Contributors:* Manuel Wimmer, RObert Bill

*Date:* 25 July 2018


# Description

This project uses [MOMot](http://martin-fleck.github.io/momot/) to search for the sequence of application of model transformation rules that lead an input model M1 to a target model M2.
Here, we use the PacmanGame domain, with the rules specified in [Henshin](https://www.eclipse.org/henshin/).

The search code is under `org.pacman/src/PacmanGame.search`.
To run the search, execute `PacmanSearch.momot`.
`PacmanGameDistanceCalculator` is the distance handwritten specifically for the PacmanGame.

The idea behind this project is to generate the optimizing search problem to be tailored for the domain. In this sense, we customize the fitness function based on a distance metric.
The metrics to **minimize** are:<a name="distance"></a>
* **Move distance**: There is often movement of elements (e.g., pacman moving on the grid, attributes moving around classes). The move distance of a movable object is the length of the shortest path from its position in M1 to its position in M2. Move distance is related to computing the difference with Ecore references.
* **Element distance**: Is the difference in the presence/absence of elements in M1 and M2. The element distance is the ratio, between 0 and 1, of the number of differences with respect to the total number of objects in M1 and M2.
* **Value distance**: <a name="value"></a>Is the difference in attribute values between M1 and M2. We assume that any attribute type can be encoded as numbers. Then the value distance of attribute x is its *margin of error*: `|M2.x - M1.x| / M2.x`. In this case, M2 acts as the expected target.
* **Rule application distance**: Is the number of rules to apply to get from M1 to M2. This distance is already taken into account in MOMot and returns a positive integer.

# Installation requirements

* Java 8
* Eclipse Photon release (4.8.0)
* Eclipse Modeling Tools (4.8.0)
* EMFCompare (3.3.3)
* MOMot (2.0.0)
* Henshin (1.4.0)

# Current implementation

This implementation is just a proof of concept. It has been implemented with the mindset that the distance calculation is generated automatically from analyzing the metamodel and the transformation rules.

The *Move distance* relies on the [*Floyd-Marshall*](https://en.wikipedia.org/wiki/Floyd-Warshall_algorithm) algorithm that computes the shortest from any position element to any position element in an Ecore model. This code is in `EcoreShortestPaths` and is independent from the domain. Its only external dependency is an interface `IEReferenceNavigator` which provides a function that gives the neighbor(s) of a position element. `DistanceUtil` provides all the necessary methods the move distance requires.

The *Value distance* is an average of all attribute distances. We only consider attributes of objects present in both M1 and M2 because the element distance takes care of absence and presence of elements. We assume that any attribute value can be represented as a unique number. `DistanceUtil` provides the `toDouble` method to perform that conversion. Currently, it only supports number values encoded as `Number` or `String` data types. For each attribute, we compute its [margin of error](#value).

The *Element distance* looks for the objects present in M1 but not M2 and M2 but not M1. This is then divided by the size of M1 and M2. We only consider objects instances of metamodel classes. So references and attributes are not taken into account in this measure. This distance relies on the unique ID of each object as returned by the [getId method](#id).

`DistanceCalculator` is the abstract class at the root that should be inherited by your distance function. For example `MoveDistance` only relies on the move distance between M1 and M2.

The only code that is specific to the metamodel is the class that implements `DistanceUtil`. This is where you define the functions that provide the following information:
* *The movable objects:* An object is movable if, when analyzing the rules, it has a reference to a position object and the rules modify that reference. Note that it could also be that a position object references a movable object.
* *The position objects:* An object is a position if, when analyzing the rules, it is what movable objects are always linked to. Note that it could also be that a position object references a movable object.
* *The modifiable objects:* An object is modifiable if, when analyzing the rules, one of its attributes changes value.
* *The other objects:* Any object that is not movable, position or modifiable.
* *The ID of an object:* the value that uniquely identifies an object. This is used to find similar elements between M1 and M2.
* *The modifiable attributes:* All attribute values subject to modification for a given object.
* *Accessing the position:* the attribute used to know the position of a movable object.
* *Accessing the neighbors of a position:* the attribute used to connect position objects.
* *Accessing the root:* used to find the root object of M1 and M2.

For example, `PacmanDistanceUtil` is a utility class that extends this abstract class. A utility class should only be accessed through its **factory class** to make sure it is a *singleton*. The factory of the utility class should have the following content:
```java
public final class MM_nameDistanceUtilFactory {
  private static MM_nameDistanceUtil INSTANCE;
  public synchronized static MM_nameDistanceUtil getInstance() {
    if(INSTANCE == null)
      synchronized (MM_nameDistanceUtil.class) {
        if(INSTANCE == null)
          INSTANCE = new MM_nameDistanceUtil();
      }
    return INSTANCE;
  }
}
```

## Documentation

The automatically generated Java documentation of the search package is accessible at [org.geodes.sms.emfmodeldistance/doc](org.geodes.sms.emfmodeldistance/doc/index.html).

## Testing

Here are some test cases to verify the implementation.

### Test 1 (overall)
This tests all distance measures on a simple 3x3 connected grid with 1 ghost, 1 pacman, and 1 food.
Run `PacmanSearch.momot` on models `models/input.xmi` (input) and `models/target.xmi` (target) in the `org.pacman` project. The search should find one optimal solution (with move, value and element distance 0.0) where ghost g1 has to move down twice, pacman p1 has to move right once and ate the food on that grid node. This can be done in 4 rule applications.

### Test 2 (element)
This test is similar to Test 1, but now pacman p1 is deleted. This means that the element distance gears the rules towards a new solution.
Run `PacmanSearch.momot` on models `models/input.xmi` (input) and `models/targetNoPac.xmi` (target) in the `org.pacman` project. The search should find one optimal solution (with move, value and element distance 0.0) where ghost g1 has to move down twice, pacman p1 has to move right twice, ate the food along the way, and g1 kills p1. This can be done in 6 rule applications.

### Test 3 (disconnect move)
In this test, the 3x3 grid is missing the grid node south of the ghost in the input model, forcing it to take a different path then in Test 1. Here, we test how the move distance behaves when the position elements are not identically connected.
Run `PacmanSearch.momot` on models `models/input12missing.xmi` (input) and `models/target.xmi` (target) in the `org.pacman` project. The search should find one optimal solution (with move and value distance 0.0) where ghost g1 has to move left then down twice then right, pacman p1 has to move right once and ate the food on that grid node. This can be done in 6 rule applications.

### Test 4 (value)
This tests the value distance measure specifically, by counting the number of tokens in the 3 places. Initially, only p1 has 10 tokens.
In the `org.petrinets` project, run `PetrinetSearch.momot` on models `models/input10.0.0.xmi` (input) and `models/target1.6.3.xmi` (target). The search should find one optimal solution (with value distance 0.0) where p1 has 1 token, p2 has 6 tokens and p3 has 3 tokens, applied in 12 rule applications.

# Next steps

## Technical concerns

The current implementation assumes that M1 and M2 have the same position objects (none are deleted or created). This is too restrictive. Therefore M1 and M2 should be preprocessed by merging their position elements and then use the move distance. One possibility is to use EMFCompare to merge M1 and M2 based on the position elements.

Interestingly, when you run the Pacman test with `models/input12missing.xmi` and `models/targetNoPac.xmi` in one signle run, you don't necessarily get always the same sequence of rule applications. That is because p1 can be killed anywhere along the path of g1. Also, on this test, you **sometimes** get an exception *"Comparison method violates its general contract!"* at the *org.moeaframework.core.Population.sort(Population.java:283)*. Something to fix in MOMot?

Implement the code generator that produces the search package for your metamodel.

### Instructions for domain-specific distance generator

Given a metamodel MM and Henshin rules R, we want to generate the distance calculator that will be used by the MOMot script. All the files in this package `org.geodes.sms.emfmodeldistance` can be reused as is because they do not have any dependency to a specific metamodel.

The domain-specific distance classes (e.g., the move, element, value distances) can be easily generated. They have two dependencies to the metamodel:
1. The package instance, by overriding the `getEPackageInstance()` function
2. The constructor, by instantiating the appropriate `DistanceUtil` singleton object specific to the metamodel

Only the utility class (e.g., `PacmanDistanceUtil`) must be generated after analyzing MM and R. Following the code in `PacmanDistanceUtil` should guide you to know how to generate the code. Here are special considerations:

1. Your `Utility` class must inherit from `DistanceUtil`. As a name convention, this class should be called `[MM_name]DistanceUtil`.
2. It should import all movable, position, modifiable, and other classes.
3. It must provide a function used for its singleton instantiation as follows
```java
public static Utility getInstance()
```
4. It should have 4 attributes for movable, position,  modifiable, and all other types as follows
```java
private final Set<String> movableTypes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("Movable1", "Movable2")));
```
5. It should override all abstract methods from `DistanceUtil`.
    * <a name="id"></a> The tricky part is the generation of the `Object getId(EObject object)` method. If each object has attribute with setID(true) you can rely on super. Otherwise, you have to make up one unique on your own. For example, if you know for sure that an attribute is unique, then you can rely on it (e.g., Places and Transitions in the Petrinet example). If there is only one possible instance of this element, then return `true` (e.g., Scoreboard in the Pacman example). It can also rely on an object it references or that references it (e.g., Food in the Pacman example).

You also need to generate the `DistanceUtilFactory` specific to the metamodel (e.g., `PacmanGameDistanceUtilFactory`). Its only purpose is to make the concrete `DistanceUtil` accessible as a singleton.