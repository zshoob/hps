#!/bin/bash

python generate_map.py # writes population_map.txt
python random_player.py 5 # writes random_player_solution.txt
python evaluate.py 'random_player' 5 # writes <player_name>_results.txt + all three pngs