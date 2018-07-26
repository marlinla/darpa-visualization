import numpy as np
import sys

#010.020.030.040 172.016.112.050
#edgeList_dir = '../node classification/node_embedding_for_classification/Dynamic_node2Vec/Data/edge_list_w2/'
edgeList_dir = 'Data/edge_list_w2/'

#edgeList_dir = 'C:/Users/ShimaKhoshraftar/Downloads/PhD/dataSetGeneration/Data/edge_list/'

#37 128
#192.168.001.020 -0.259947 0.305190 0.094357 -0.153968 0.085126 -0.307102 0.432591 -0.127193 -0.085630 -0.051247 -0.369449 0.052557 0.125533 0.086138 -0.400520 -0.018403 -0.065622 0.787844 -0.143383 -0.579763 -0.301603 -0.271569 -0.381774 0.023645 0.664731 -0.087928 0.126643 -0.237199 0.174895 -0.336980 -0.511409 -0.087937 0.340804 -0.170231 0.197380 0.115406 -0.066546 -0.394304 -0.171130 -0.651824 0.012866 0.113701 -0.123542 -0.171139 -0.140221 0.018358 0.551776 0.005215 0.121151 0.087256 -0.311801 -0.181232 0.083832 -0.698010 0.571825 -0.364825 -0.108917 0.007429 0.202534 0.682122 -0.097189 0.216851 0.313540 -0.342030 0.199959 -0.703372 -0.157365 -0.096920 -0.226549 -0.347594 0.245002 0.320363 0.492049 -0.080533 -0.005565 0.232392 0.257316 -0.348294 -0.093289 -0.019176 0.193021 0.000291 0.232498 0.028767 0.059740 -0.201656 -0.011228 0.154043 0.313374 0.324794 0.178835 -0.536488 -0.021342 -0.087906 0.115446 0.501248 -0.248394 -0.325436 -0.162187 -0.183115 0.345670 0.031602 -0.106456 -0.465314 0.421059 0.203639 0.492972 0.381479 -0.148850 -0.087379 -0.307281 -0.532916 0.397657 0.247516 0.071258 -0.263661 0.356552 0.199580 -0.139664 0.065739 0.166932 0.453398 -0.227059 -0.346043 -0.557769 0.006743 -0.307496 0.088190
#dir = '../node classification/node_embedding_for_classification/Static_node2Vec/Data/emb-w/'
#dir = '../node classification/node_embedding_for_classification/Dynamic_node2Vec/Data/emb-w/'
#dir = '../node classification/node_embedding_for_classification/Dynamic_node2Vec_e/Data/emb-w/'
#dir = '../node classification/node_embedding_for_classification/normal/Data/emb-w/'
dir = 'Data/emb-w2/'

#edgeEmb_dir = 'Sta-emb/edge_embs/'
#edgeEmb_dir = 'Dyn-emb/edge_embs/'
#edgeEmb_dir = 'Dyn-emb-e/edge_embs/'
edgeEmb_dir = 'Data/edge_embs/'

def main(i):
#for i in range(0, 1):

    #save the edgeList of graph in an array
    edgeList_file = open(edgeList_dir + 'graph_' + str(i), 'r')
    edgeList_array = []
    for line in edgeList_file:
        if line not in edgeList_array:
            edgeList_array.append(line)

    #save the node embeddings in two arrays to find them quickly
    emb_file = open(dir + 'graph_' + str(i), 'r')
    rows, cols = emb_file.readline().rstrip().split(' ')
    print(rows)
    print('******')
    print(cols)
    #this array for node id
    nodes = []
    #this array for node embs
    node_embs = np.zeros((int(rows), int(cols)))
    count = 0
    for line in emb_file:
        line1 = line.rstrip().split(' ')
        nodes.append(line1[0])
        node_embs[count] = [float(j) for j in line1[1:]]
        count += 1

    print(nodes)
    print("****")
    print(node_embs)

    #read from edglist array, find in node emb hash and save edge emb
    fw = open(edgeEmb_dir + 'graph_' + str(i), 'w')
    fw.write(str(len(edgeList_array)) + ' ' + rows + '\n')
    for edge in edgeList_array:
        n1, n2, w = edge.rstrip().split(' ')

        #this is where you define edge emb formula
        #edge_emb = node_embs[nodes.index(n1)] - node_embs[nodes.index(n2)]
        edge_emb = node_embs[nodes.index(n1)] * node_embs[nodes.index(n2)]
        fw.write(n1 +',' + n2 + ':')
        for j in range(0,len(edge_emb)):
            fw.write(str(edge_emb[j]))
            if j!= len(edge_emb)-1:
                fw.write(',')

            else:
                fw.write('\n')

    fw.close()


if __name__ == "__main__":
    main(sys.argv[1])

