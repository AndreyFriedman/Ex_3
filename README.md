# Ex_3

## The Main Idea

EX3 is a project created for an assignment for Object Oriented Programming course in Ariel University. In this project we are going to take several graphs, Java alghoritms(that we took from EX2), Python algorithms(we created them for this project) and NetworkX algorithms and compare which algorithm has better running time for same functions.

The graphs we are going to use to compare between the algorithm are in the folder "Graphs_no_pos"



## Java Algorithms

We took the algorithms that we used in our previous project (Ex2: https://github.com/itay74121/OOP_EX2 ) and added to it 2 functions: connected_component and connecred_components. to campere between the algorithms we are going to use this 2 function and shortestPath and shortestPathDist functions.

## Python Algorithms

We created 3 Classess that we will need. NodeData - this class represents the nodes in the graph. DiGraph - this function represents all the function that we need to create a graph. GraphAlgo - in this class we have all the function that we will compare. you can read more about every class in our Wiki page.

## NetworkX Algorithms

NetworkX is a Python package for the creation, manipulation, and study of the structure, dynamics, and functions of complex networks. https://networkx.org/documentation/stable/index.html
NetworkX doesnt have connected_component and connecred_components functions.

## The functions we are going to compare

connected_component: Determine the scc of a certain node. we are going to compare this function only between Java and Python algotihms because NetworkX doesnt have it.

connected_components: Returning all the connected_components of the graph. we are going to compare this function only between Java and Python algotihms because NetworkX doesnt have it.

shortestPath: returning the shortest distance between two nodes in the graph and a list of nodes that represents the shortest path between this 2 nodes. In Java, not like in Python, you cant return two things from one function so we are going to use two functions: shortestPath - returning the list that represents the shortest path between this 2 nodes, shortestPathDist - returning the shortest distance between two nodes.
