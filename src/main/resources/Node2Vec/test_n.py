import sys


def main(i):
    with open('Data/test.txt', 'w') as testFile:
        head = open('Data/header.txt', 'r')
        testFile.write(head.read())
        testFile.write('\n')

        f = open('Data/trainset/graph_' + str(i), 'r')
        testFile.write(f.read())


# for line in f:
#	print(line)
# for i in range(0,31):
# print()
if __name__ == '__main__':
    main(sys.argv[1])
