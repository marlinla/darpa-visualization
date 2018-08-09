#this code just opens embedding files and write them in one file
import os
import re
import numpy as np

#dir = 'Sta-emb/'
#dir = 'Dyn-emb/'
#dir = 'Dyn-emb-e/'
dir = '../Node2Vec/Data/'


f = open(dir + 'anomal_edges_from_list.txt', 'r')
#f = open('../dataSetGeneration/Data/anomal_edges_synthetic', 'r')

fw = open(dir + 'data.txt', 'w')

fnames = os.listdir(dir + 'edge_embs')
ordered_fnames = sorted(fnames, key=lambda x: (int(re.sub('\D', '', x)), x))


for curr_file in ordered_fnames:
#curr_file = 'graph_0'
    #get one line from anomaly list per file
    anomals1 = next(f).rstrip().split(',')[2:]
    anomals = []
    for a in range(0, len(anomals1)-1, 2):
        anomals.append(anomals1[a] +','+anomals1[a+1])
        #a = a + 2

    print(anomals)

    #start reading the file
    with open(dir + 'edge_embs/' + curr_file) as fe:
        next(fe)
        for line in fe:
            emb = []
            label = ''

            line = line.rstrip().split(':')
            id = line[0]
            #print(id)
            emb1 = line[1]
            emb = emb1.split(",")

            if id in anomals:
                label = 'anomal'

            else:
                label = 'normal'

            for i in range(0, len(emb)):
                fw.write(emb[i] + ',')
                if i == len(emb)-1:
                    fw.write(label + '\n')

fw.close()