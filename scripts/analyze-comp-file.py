#!/usr/bin/python

# Analyzes a .comp file generated by decls2comp.py and prints
# out interesting properties about it

# by Philip Guo

import re

pptNameRE = re.compile(':::(ENTER|EXIT).*')

import sys

f = open(sys.argv[1], 'r')
allLines = [line.strip() for line in f.readlines()]

# Key: program point name
# Value: a list where each element is the size of a comparability set
#        for that ppt
pptInfoDict = {}

# The current program point we are analyzing
curPpt = None

# Initialize this to an empty list and append on the size of the
# comparability sets for each line you encounter in the program point
curPptSetSizes = []

# Initialize pptInfoDict
for line in allLines:
    # We hit a program point name, so calculate stuff
    # for the previous program point
    if pptNameRE.search(line):
        curPptSetSizes = []
        
        # Special case for the first program point reached
        if not curPpt:
            curPpt = line

        # Normally, throw all of the data about the previous program
        # point into pptInfoDict and update curPpt
        else:
            pptInfoDict[curPpt] = curPptSetSizes
            curPpt = line


    # (Ignore blank lines) ... we hit a space-delimited list of
    # comparable variables
    elif line:
        # Size of comparability set for that line (mostly gonna be 1)
        curPptSetSizes.append(len(line.split()))


# Now pptInfoDict should be initialized with all numerical data.
# We can now crunch numbers and produce useful-looking results

sumOfAvgs = 0
numPpts = len(pptInfoDict.keys())

for ppt in pptInfoDict:
    compSetSizesAtPpt = pptInfoDict[ppt]
    totalVarsAtPpt = sum(compSetSizesAtPpt)
    squareVarsAtPpt = sum([(i*i) for i in compSetSizesAtPpt])

    avgForPpt = float(squareVarsAtPpt) / float(totalVarsAtPpt)

    sumOfAvgs += avgForPpt

#    print ppt
#    print "Total # vars:     ", totalVarsAtPpt
#    print "Average set size: ", avgForPpt


#print
print ((sumOfAvgs) / float(numPpts))
