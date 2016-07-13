## Minimax algorithm

In this assignment, I implemented Minimax algorithm to solve a game problem, the Factor- Multiples game, of which the details can be seen in the HW6.pdf. The algorithm has two functions: the Max-value function and the Min-value function.

The format of testing command will like this:
```
java HW6 <n> <modeFlag>
```
where **n** is the value of n in the Factor-Multiples game, and **modeFlag** is an integer ranging from 1 to 3, controlling which types of players will play the game. Your program should give the moves of the players and the winner as output: Also we will not test for n > 30.

When modeFlag=1, both players are computers and you will see a game played between them. When modeFlag=2, the first player is human and the second player is the computer. When modeFlag=3, the first player is the computer and the second player is human. For the latter two modes you can play against my program, which is on my behalf. And see that if you can win!!!

Here is an example command:
```
java HW6 7 1
```
