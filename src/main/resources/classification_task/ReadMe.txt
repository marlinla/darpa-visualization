1. convert files in edge_embs folder to different format
n1,n2:features --> features,class
class labels are in anomal_edges_from_list file
The code for doing this is similar to create_trainset.py


2. starting predicting class for graph_31, 32,...
first training file contains header and graph_0 to 30 merged in one file
first test file is header and graph_31

second train file is header and graph_0 to 31
second test file is header and graph_32

code for doing this is similar to edge_classification_in_sequence_visual.py

3. step 2 save predictions in a folder, using that folder you previously adjusted visualization colors

