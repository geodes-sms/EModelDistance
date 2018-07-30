# Info
*Author:* Eugene Syriani

*Date:* 25 July 2018


# Description

This project uses [MOMot](http://martin-fleck.github.io/momot/) to search for the sequence of application of model transformation rules that lead M1 to M2.
Here, we use the PacmanGame domain, with the rules specified in [Henshin](https://www.eclipse.org/henshin/).

The search code is under `org.pacman\src\PacmanGame.search`.
To run the search, execute `PacmanSearch.momot`.
`PacmanGameDistanceCalculator` is the distance handwritten specifically for the PacmanGame.

The idea behind this project is to generate the optimizing search problem to be tailored for the domain. In this sense, we customize the fitness function based on a distance metric.
The metrics to **minimize** are:<a name="distance"></a>
* **Move distance**: There is often movement of elements (e.g., pacman moving on the grid, attributes moving around classes). The move distance of a movable object is the length of the shortest path from its position in M1 to its position in M2. Move distance is related to computing the difference with Ecore references.
* **Element distance**: Is the difference in the presence/absence of elements in M1 and M2. This can be computed by type or aggregated.
* **Value distance**: Is the difference in attribute values between M1 and M2. We assume that any attribute type can be encoded as an integer. Then the value distance of attribute x becomes `|M2.x - M1.x|`.
* **Rule application distance**: Is the number of rules to apply to get from M1 to M2.

# Installation requirements

* Java 8
* Eclipse Photon release (4.8.0)
* Eclipse Modeling Tools (4.8.0)
* EMFCompare (3.3.3)
* MOMot (2.0.0)
* Henshin (1.4.0)

# Current implementation

This implementation is just a proof of concept. It has been implemented with the mindset that the distance calculation is generated automatically from analyzing the metamodel and the transformation rules.

Currently, what is implemented is the *Move distance*.
Here, we rely on the [*Floyd-Marshall*](https://en.wikipedia.org/wiki/Floyd-Warshall_algorithm) algorithm that computes the shortest from any position element to any position element in an Ecore model. This code is in `EcoreShortestPaths` and is independent from the domain. Its only external dependency is an interface `IEReferenceNavigator` which provides a function that gives the neighbor(s) of a position element.

`DistanceCalculator` is the abstract class at the root that should be inherited by your distance function. For example `MoveDistance` only relies on the move distance between M1 and M2.
The only code that is specific to the metamodel is the class that implements `DistanceUtil`. This is where you define the functions that provide the following information:
* *The movable objects:* the list of movable objects. An object is movable if, when analyzing the rules, it has a reference to a position object and the rules modify that reference. Note that it could also be that a position object references a movable object.
* *The position objects:* the list of position objects. An object is a position if, when analyzing the rules, it is what movable objects are always linked to. Note that it could also be that a position object references a movable object.
* *The movable types:* class names of the objects that can move in the model.
* *The position types:* class names of the objects that can act as position in the model.
* *The ID of an object:* the value that uniquely identifies an object. This is used to find similar movable and position elements between M1 and M2.
* *Accessing the position:* the attribute used to know the position of a movable object.
* *Accessing the neighbors of a position:* the attribute used to connect position objects.
* *Accessing the root:* used to find the root object of M1 and M2.

For example, `PacmanDistanceUtil` is a utility class that extends this abstract class. A utility class should only be accessed through its factory class to make sure it is a **singleton**.

## Documentation

The automatically generated Java documentation of the search package is accessible at [org.pacman/doc](org.pacman/doc/index.html).

## Testing

To test the current implementation, run `PacmanSearch.momot` on models `models/M1.xmi` (source) and `models/M2.xmi` (target). The search should find one optimal solution (with distance 0.0) where ghost g1 has to move down twice and pacman p1 has to move right once.

# TODO

## Instructions for distance generator

Given a metamodel MM and Henshin rules R, we want to generate the distance calculator that will be used by the MOMot script. The following files can be generated as is: `DistanceCalculator`, `EcoreShortestPaths`, `DistanceUtil`, and `IEReferenceNavigator`.

The `MoveDistance` can be generated easily. It has two dependencies to the metamodel:
1. The package instance, by overriding the `getEPackageInstance()` function
2. The constructor, by instantiating the appropriate `DistanceUtil` singleton object

Only the utility class (e.g., `PacmanDistanceUtil`) must be generated after analyzing MM and R. Following the code in `PacmanDistanceUtil` should guide you to know how to generate the code. Here are special considerations:

1. Your `Utility` class must inherit from `DistanceUtil`. As a name convention, this class should be called `[MM_name]DistanceUtil`.
2. It should import all movable and position classes.
3. It must provide a function used for its singleton instantiation as follows
```java
public static Utility getInstance()
```
4. It should have two attributes one for movable types and one for position types as follows
```java
private final Set<String> movableTypes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("Movable1", "Movable2")));
```

You also need to generate the `DistanceUtilFactory` specific to the metamodel (e.g., `PacmanGameDistanceUtilFactory`). Its only purpose is to make the concrete `DistanceUtil` accessible as a singleton.

## Next steps

### Technical concerns

The current implementation assumes that M1 and M2 have the same position objects (none are deleted or created). This is too restrictive. Therefore M1 and M2 should be preprocessed by merging their position elements and then use the move distance. One possibility is to use EMFCompare to merge M1 and M2 based on the position elements.

The distance calculator should also consider the value distance and the element distance as defined [above](#distance). The rule application distance is already taken into account in MOMot.

Implement the code generator that produces the search package for your metamodel.

### Publication concerns

Have a larger pacman example. Have other domains: class diagram refactoring, stack buckets. Compare results with generic distance metrics, for example EMFCompare difference output (see `EMFCompareDistanceCalculator.java`),  to see if domain-specific distance metrics perform better.

This work can be published at GECCO 2019, in Prague.