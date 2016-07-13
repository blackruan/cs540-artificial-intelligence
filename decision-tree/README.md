## Decision Tree

In this assignment, I built a classification decision tree for categorical attributes, which includes building a tree from a training dataset and pruning the learned decision tree with a tuning dataset.

The format of testing command will like this:
```
java HW2 <modeFlag> <trainFile> <tuneFile> <testFile>
```
where **trainFile**, **tuneFile**, **testFile** are the names of training, tuning, testing dataset which contain the features of corresponding instances. **modeFlag** is an integer valued from 0 to 4, controlling what the program will output:

* 0: print the information gain for each attribute at the root node
* 1: create a decision tree from a training set, print the tree
* 2: create a decision tree from a training set, print the classifications for instances in a test set
* 3: create a decision tree from a training set then prune, print the tree
* 4: create a decision tree from a training set then prune, print the classifications for instances in a test set

Sample input files are provided, which are **monks-train0.txt**, **monks-tune0.txt** and **monks-testo.txt**. Here is an example command:
```
java HW2 1 monks-train0.txt monks-tune0.txt monks-test0.txt
```
