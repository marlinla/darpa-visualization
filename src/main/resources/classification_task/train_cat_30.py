


with open('../Node2Vec/Data/train.txt', 'w') as trainFile:
	head = open('header.txt', 'r')
	trainFile.write(head.read())
	trainFile.write('\n')
	for i in range(0,31):
		
		f = open('../Node2Vec/Data/trainset/graph_' + str(i), 'r')
		trainFile.write(f.read())
			
		

#for line in f:
#	print(line)
#for i in range(0,31):
#print()
	