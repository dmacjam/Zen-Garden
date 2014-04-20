#This is Zen Garden Puzzle Solver.

It uses evolutionary algorithms for finding solutions. There are
three types of crossover - Uniform, Single, Two-point and also
two types of selections- Roulette wheel selection, Tournament selection
(for 2 and 3 individuals).

Program reads map from file- file must contain informations:
```
<number_of_rows> <number_of_columns>
<stone1_x> <stone1_y>
<stone2_x> <stone2_y>
...
```

Example of input garden file with garden 10x12 and 3 stones:
```
10 12
1 1
2 5
5 3
```