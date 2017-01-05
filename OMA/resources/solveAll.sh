#!/bin/bash
for filename in ./input/*.txt; do
	java -jar ./OMA.jar -i $filename -o ./output/summary.csv
done