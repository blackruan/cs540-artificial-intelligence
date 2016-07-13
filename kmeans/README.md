## K-Means

In this assignment, I built a k-means clustering algorithm with Euclidean distance. My main contribution is the implementation of the **cluster** method. It accepts an array of initial centroids, an array of instances, and a threshold for stopping the iterations of the algorithm.

The format of testing command will like this:
```
java HW1 data_file init_centroid_file threshold output_flag
```
where **data_file** contains the features of all instances, **init_centroid** file gives the initial positions of certains of number of centroids, and **threhold** has same meaning as mentioned. Besides, the **outputFlag** is an integer argument, controlling what the program outputs:

* 1 - The array of centroids
* 2 - The array of assignments
* 3 - The array of distortions

A sample input files is provided, which is **words0.txt** and **initCentroid0.txt**. Here is an example command:
```
java HW1 words0.txt initCentroid0.txt 1e-4 3
```
