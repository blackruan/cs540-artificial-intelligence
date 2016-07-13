## Naive Bayes

In this assignment, I implemented a naive bayes classifier to identify SPAM from HAM. Laplace smoothing is used for the classifier. Besides, log-probabilities is used to avoid the underflow problem. The data set provided in class consists of 5,580 text messages that have been labelled as either SPAM or HAM and has been split into training set and testing set. The first word per line is the label while the remainder of the line is the text message.

The format of testing command will like this:
```
java HW4 trainingFilename testFilename
```
which trains the classifier from the data in **trainingFilename** and tests it against **testFilename**, outputting the classifiers prediction for the label and the true label for each instance in the testing data, in the same order as it occurs in **testFilename**.

Sample input files are provided, which are **train0.txt** and **test0.txt**. Here is an example command:
```
java HW4 train0.txt test0.txt
```
